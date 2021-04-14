package com.shanjupay.paymentagent.api;

import com.shanjupay.common.domain.BusinessException;
import com.shanjupay.paymentagent.api.conf.AliConfigParam;
import com.shanjupay.paymentagent.api.conf.WXConfigParam;
import com.shanjupay.paymentagent.api.dto.AlipayBean;
import com.shanjupay.paymentagent.api.dto.PaymentResponseDTO;
import com.shanjupay.paymentagent.api.dto.WeChatBean;

import java.util.Map;

/**
 * 与第三支付渠道进行交互
 * Created by Administrator.
 */
public interface PayChannelAgentService {

    /**
     * 查询微信支付结果
     * @param wxConfigParam
     * @param outTradeNo
     * @return
     */
    PaymentResponseDTO queryPayOrderByWeChat(WXConfigParam wxConfigParam, String outTradeNo);

    /**
     * 微信jsapi下单接口请求
     * @param wxConfigParam
     * @param weChatBean
     * @return h5页面锁需要的数据
     */
    Map<String, String> createPayOrderByWeChatJSAPI(WXConfigParam wxConfigParam, WeChatBean weChatBean);

    /**
     * 支付宝交易状态查询
     * @param aliConfigParam 支付渠道参数
     * @param outTradeNo 闪聚平台订单号
     * @return
     */
    PaymentResponseDTO queryPayOrderByAli(AliConfigParam aliConfigParam, String outTradeNo);

    /**
     * 调用支付宝的下单接口
     * @param aliConfigParam 支付渠道配置的参数（配置的支付宝的必要参数）
     * @param alipayBean 业务参数（商户订单号，订单标题，订单描述,,）
     * @return 统一返回PaymentResponseDTO
     */
    PaymentResponseDTO createPayOrderByAliWAP(AliConfigParam aliConfigParam, AlipayBean alipayBean) throws BusinessException;
}
