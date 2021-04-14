package com.shanjupay.paymentagent.message;

import com.alibaba.fastjson.JSON;
import com.shanjupay.paymentagent.api.PayChannelAgentService;
import com.shanjupay.paymentagent.api.conf.AliConfigParam;
import com.shanjupay.paymentagent.api.conf.WXConfigParam;
import com.shanjupay.paymentagent.api.dto.PaymentResponseDTO;
import com.shanjupay.paymentagent.api.dto.TradeStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;


/**
 * @author: WeiQiuKe
 * @create: 2021-04-13
 **/
@Slf4j
@Service
@RocketMQMessageListener(topic = "TP_PAYMENT_ORDER", consumerGroup = "CID_PAYMENT_CONSUMER")
public class PayConsumer implements RocketMQListener<MessageExt> {

    @Resource
    private PayChannelAgentService payAgentService;
    @Autowired
    private PayProducer payProducer;

    @Override
    public void onMessage(MessageExt message) {
        log.info("开始消费支付结果查询消息:{}", message);
        //取出消息内容
        String body = new String(message.getBody(), StandardCharsets.UTF_8);
        PaymentResponseDTO response = JSON.parseObject(body, PaymentResponseDTO.class);
        String outTradeNo = response.getOutTradeNo();
        String msg = response.getMsg();
        String param = String.valueOf(response.getContent());
        AliConfigParam aliConfigParam = JSON.parseObject(param, AliConfigParam.class);
        //判断是支付宝还是微信
        PaymentResponseDTO result = new PaymentResponseDTO();
        if ("ALIPAY_WAP".equals(msg)) {
            //查询支付宝支付结果
            result = payAgentService.queryPayOrderByAli(aliConfigParam, outTradeNo);
        } else if ("WX_JSAPI".equals(msg)) {
            //查询微信支付结果
            WXConfigParam wxConfigParam = JSON.parseObject(param, WXConfigParam.class);
            result = payAgentService.queryPayOrderByWeChat(wxConfigParam, outTradeNo);
        }

        //返回查询获得的支付状态
        if (TradeStatus.UNKNOWN.equals(result.getTradeState()) || TradeStatus.USERPAYING.equals(result.getTradeState())) {
            //在支付状态未知或支付中，抛出异常会重新消费此小时
            log.info("支付代理---支付状态未知，等待重试");
            throw new RuntimeException("支付状态未知，等待重试");
        }

        //不管支付成功还是失败都需要发送支付结果消息
        log.info("交易中心处理支付结果通知，支付代理发送消息:{}", result);
        payProducer.payResultNotice(result);
    }
}
