package com.shanjupay.transaction.message;

import com.alibaba.fastjson.JSON;
import com.shanjupay.paymentagent.api.dto.PaymentResponseDTO;
import com.shanjupay.paymentagent.api.dto.TradeStatus;
import com.shanjupay.transaction.api.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

/**
 * @author: WeiQiuKe
 * @create: 2021-04-14
 **/
@Slf4j
@Component
@RocketMQMessageListener(topic = "TP_PAYMENT_RESULT", consumerGroup = "CID_ORDER_CONSUMER")
public class TransactionPayConsumer implements RocketMQListener<MessageExt> {
    @Resource
    private TransactionService transactionService;

    @Override
    public void onMessage(MessageExt messageExt) {
        String body = new String(messageExt.getBody(), StandardCharsets.UTF_8);
        PaymentResponseDTO res = JSON.parseObject(body, PaymentResponseDTO.class);
        log.info("交易中心消费方接受支付结果消息:{}", body);
        final TradeStatus tradeState = res.getTradeState();
        String tradeNo = res.getTradeNo();
        String outTradeNo = res.getOutTradeNo();
        switch (tradeState) {
            case SUCCESS:
                //String tradeNo, String payChannelTradeNo, String state
                //成功
                transactionService.updateOrderTradeNoAndTradeState(outTradeNo, tradeNo, "2");
                return;
            case REVOKED:
                //关闭
                transactionService.updateOrderTradeNoAndTradeState(outTradeNo, tradeNo, "4");
                return;
            case FAILED:
                //失败
                transactionService.updateOrderTradeNoAndTradeState(outTradeNo, tradeNo, "5");
                return;
            default:
                throw new RuntimeException(String.format("无法解析支付结果:%s", body));
        }
    }
}
