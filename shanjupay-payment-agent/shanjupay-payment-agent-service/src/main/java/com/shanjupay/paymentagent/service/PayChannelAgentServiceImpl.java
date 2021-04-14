package com.shanjupay.paymentagent.service;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradePayModel;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeWapPayResponse;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import com.shanjupay.common.domain.BusinessException;
import com.shanjupay.common.domain.CommonErrorCode;
import com.shanjupay.paymentagent.api.PayChannelAgentService;
import com.shanjupay.paymentagent.api.conf.AliConfigParam;
import com.shanjupay.paymentagent.api.conf.WXConfigParam;
import com.shanjupay.paymentagent.api.dto.AlipayBean;
import com.shanjupay.paymentagent.api.dto.PaymentResponseDTO;
import com.shanjupay.paymentagent.api.dto.TradeStatus;
import com.shanjupay.paymentagent.api.dto.WeChatBean;
import com.shanjupay.paymentagent.common.constant.AliCodeConstants;
import com.shanjupay.paymentagent.config.WXSDKConfig;
import com.shanjupay.paymentagent.message.PayProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Administrator
 * @version 1.0
 **/
@Service
@Slf4j
public class PayChannelAgentServiceImpl implements PayChannelAgentService {

    @Autowired
    PayProducer payProducer;

    @Override
    public PaymentResponseDTO queryPayOrderByWeChat(WXConfigParam wxConfigParam, String outTradeNo) {
        WXSDKConfig config = new WXSDKConfig(wxConfigParam);
        Map<String, String> resp = null;

        try {
            WXPay wxPay = new WXPay(config);
            Map<String, String> data = new HashMap<>();
            data.put("out_trade_no", outTradeNo);
            resp = wxPay.orderQuery(data);
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
            return PaymentResponseDTO.fail("调用微信查询订单异常", outTradeNo, TradeStatus.UNKNOWN);
        }
        String returnCode = resp.get("return_code");
        String resultCode = resp.get("result_code");
        String tradeState = resp.get("trade_state");
        String transactionId = resp.get("transaction_id");
        String returnMsg = resp.get("return_msg");

        if("SUCCESS".equals(returnCode) && "SUCCESS".equals(resultCode)) {
            if ("SUCCESS".equals(tradeState)) { //交易成功
                return PaymentResponseDTO.success(transactionId, outTradeNo, TradeStatus.SUCCESS, "");
            } else if ("USERPAYING".equals(tradeState)) { //等待用户支付
                return PaymentResponseDTO.success(transactionId, outTradeNo, TradeStatus.USERPAYING, "");
            } else if("PAYERROR".equals(tradeState)) {
                return PaymentResponseDTO.success(transactionId, outTradeNo, TradeStatus.FAILED, returnMsg);
            } else if ("CLOSED".equals(tradeState)) {
                return PaymentResponseDTO.success(transactionId, outTradeNo, TradeStatus.REVOKED, returnMsg);
            }
        }

        return PaymentResponseDTO.success("暂不支持其他状态", transactionId, outTradeNo, TradeStatus.UNKNOWN);
    }

    /**
     * 微信jsapi下单接口请求
     * @param wxConfigParam
     * @param weChatBean
     * @return
     */
    @Override
    public Map<String, String> createPayOrderByWeChatJSAPI(WXConfigParam wxConfigParam, WeChatBean weChatBean) {
        WXSDKConfig config = new WXSDKConfig(wxConfigParam); //通过实际支付参数匹配

        try {
            WXPay wxPay = new WXPay(config);

            //按照微信统一下单接口要求构造请求参数
            HashMap<String, String> requestParam = new HashMap<>();
            requestParam.put("body", weChatBean.getBody());
            requestParam.put("out_trade_no", weChatBean.getOutTradeNo());
            requestParam.put("fee_type", "CNY");
            requestParam.put("total_fee", String.valueOf(weChatBean.getTotalFee()));
            requestParam.put("spbill_create_ip", weChatBean.getSpbillCreateIp());
            requestParam.put("notify_url", weChatBean.getNotifyUrl());
            requestParam.put("trade_type", "JSAPI");
            requestParam.put("openId", weChatBean.getOpenId());

            //调用微信统一下单API
            Map<String, String> resp = wxPay.unifiedOrder(requestParam);

            //=====向mq写入订单查询的消息======
            PaymentResponseDTO<WXConfigParam> notice = new PaymentResponseDTO<>();
            //订单号
            notice.setOutTradeNo(weChatBean.getOutTradeNo());
            //支付渠道参数
            notice.setContent(wxConfigParam);
            //msg
            notice.setMsg("WX_JSAPI");
            payProducer.payOrderNotice(notice);


            //返回h5网页需要的数据
            String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
            String key = wxConfigParam.getKey();
            Map<String, String> jsapiPayParam = new HashMap<>();
            jsapiPayParam.put("appId", resp.get("appId"));
            jsapiPayParam.put("package", "prepay_id=" + resp.get("prepay_id"));
            jsapiPayParam.put("timeStamp", timestamp);
            jsapiPayParam.put("noceStr", UUID.randomUUID().toString());
            jsapiPayParam.put("signType", "HMAC-SHA256");
            jsapiPayParam.put("paySign", WXPayUtil.generateSignature(jsapiPayParam, key, WXPayConstants.SignType.HMACSHA256));
            log.info("微信JSAPI支付响应内容:"+ jsapiPayParam);
            return jsapiPayParam;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(CommonErrorCode.E_400001);
        }
    }

    /**
     * 支付宝交易状态查询
     *
     * @param aliConfigParam 支付渠道参数
     * @param outTradeNo     闪聚平台订单号
     * @return
     */
    @Override
    public PaymentResponseDTO queryPayOrderByAli(AliConfigParam aliConfigParam, String outTradeNo) {
        String gateway = aliConfigParam.getUrl();//支付接口网关地址
        String appId = aliConfigParam.getAppId();
        String rsaPrivateKey = aliConfigParam.getRsaPrivateKey(); //私钥
        String format = aliConfigParam.getFormat(); //json格式
        String charest = aliConfigParam.getCharest(); //编码 utf-8
        String alipayPublicKey = aliConfigParam.getAlipayPublicKey(); //公钥
        String signtype = aliConfigParam.getSigntype();//签名算法类型
        log.info("C扫B请求支付宝查询订单,参数:{}", JSON.toJSONString(aliConfigParam));

        //构建sdk客户端
        DefaultAlipayClient client = new DefaultAlipayClient(gateway, appId, rsaPrivateKey, format, charest,
                alipayPublicKey, signtype);
        AlipayTradeQueryRequest queryRequest = new AlipayTradeQueryRequest();
        AlipayTradePayModel model = new AlipayTradePayModel();
        //闪聚平台订单号
        model.setOutTradeNo(outTradeNo);
        //封装请求参数
        queryRequest.setBizModel(model);

        PaymentResponseDTO dto;

        try {
            //请求支付宝接口
            AlipayTradeQueryResponse qr = client.execute(queryRequest);
            //接口调用成功
            if (AliCodeConstants.SUCCESSCODE.equals(qr.getCode())) {
                TradeStatus tradeStatus = coverAliTradeStatusToShanjuCode(qr.getTradeStatus());
                dto = PaymentResponseDTO.success(qr.getTradeNo(), qr.getOutTradeNo(), tradeStatus, qr.getMsg() + " " + qr.getSubMsg());
                log.info("----查询支付宝H5支付结果" + JSON.toJSONString(dto));
                return dto;
            }
        } catch (AlipayApiException e) {
            log.warn(e.getMessage(), e);
        }
        dto = PaymentResponseDTO.fail("查询支付宝支付结果异常", outTradeNo, TradeStatus.UNKNOWN);
        return dto;
    }

    private TradeStatus coverAliTradeStatusToShanjuCode(String aliTradeStatus) {
        switch (aliTradeStatus) {
            case AliCodeConstants.WAIT_BUYER_PAY:
                return TradeStatus.USERPAYING;
            case AliCodeConstants.TRADE_SUCCESS:
            case AliCodeConstants.TRADE_FINISHED:
                return TradeStatus.SUCCESS;
            default:
                return TradeStatus.FAILED;
        }
    }

    /**
     * 调用支付宝的下单接口
     *
     * @param aliConfigParam 支付渠道配置的参数（配置的支付宝的必要参数）
     * @param alipayBean     业务参数（商户订单号，订单标题，订单描述,,）
     * @return 统一返回PaymentResponseDTO
     */
    @Override
    public PaymentResponseDTO createPayOrderByAliWAP(AliConfigParam aliConfigParam, AlipayBean alipayBean) throws BusinessException {

        String url = aliConfigParam.getUrl();//支付宝接口网关地址
        String appId = aliConfigParam.getAppId();//支付宝应用id
        String rsaPrivateKey = aliConfigParam.getRsaPrivateKey();//应用私钥
        String format = aliConfigParam.getFormat();//json格式
        String charest = aliConfigParam.getCharest();//编码
        String alipayPublicKey = aliConfigParam.getAlipayPublicKey();//支付宝公钥
        String signtype = aliConfigParam.getSigntype();//签名算法
        String returnUrl = aliConfigParam.getReturnUrl();//支付成功跳转的url
        String notifyUrl = aliConfigParam.getNotifyUrl();//支付结果异步通知的url

        //构造sdk的客户端对象
        AlipayClient alipayClient = new DefaultAlipayClient(url, appId, rsaPrivateKey, format, charest, alipayPublicKey, signtype); //获得初始化的AlipayClient
        AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();//创建API对应的request
        AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
        model.setOutTradeNo(alipayBean.getOutTradeNo());//商户的订单，就是闪聚平台的订单
        model.setTotalAmount(alipayBean.getTotalAmount());//订单金额（元）
        model.setSubject(alipayBean.getSubject());
        model.setBody(alipayBean.getBody());
        model.setProductCode("QUICK_WAP_PAY");//产品代码，固定QUICK_WAP_PAY
        model.setTimeoutExpress(alipayBean.getExpireTime());//订单过期时间
        alipayRequest.setBizModel(model);

        alipayRequest.setReturnUrl(returnUrl);
        alipayRequest.setNotifyUrl(notifyUrl);
        try {
            //请求支付宝下单接口,发起http请求
            AlipayTradeWapPayResponse response = alipayClient.pageExecute(alipayRequest);
            PaymentResponseDTO paymentResponseDTO = new PaymentResponseDTO();
            log.info("调用支付宝下单接口，响应内容:{}", response.getBody());
            paymentResponseDTO.setContent(response.getBody());//支付宝的响应结果

            //向MQ发一条延迟消息,支付结果查询
            PaymentResponseDTO<AliConfigParam> notic = new PaymentResponseDTO<>();
            notic.setOutTradeNo(alipayBean.getOutTradeNo()); //闪聚平台的订单
            notic.setContent(aliConfigParam);
            notic.setMsg("ALIPAY_WAP");
            //发送消息
            payProducer.payOrderNotice(notic);

            return paymentResponseDTO;
        } catch (AlipayApiException e) {
            e.printStackTrace();
            throw new BusinessException(CommonErrorCode.E_400002);
        }

    }
}
