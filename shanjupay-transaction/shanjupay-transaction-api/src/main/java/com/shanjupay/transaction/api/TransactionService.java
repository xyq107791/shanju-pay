package com.shanjupay.transaction.api;

import com.shanjupay.paymentagent.api.dto.PaymentResponseDTO;
import com.shanjupay.transaction.api.dto.PayOrderDTO;
import com.shanjupay.transaction.api.dto.QRCodeDTO;
import java.util.Map;

/**
 * 交易订单相关服务接口
 */
public interface TransactionService {

    /**
     * 微信确认支付
     * @param payOrderDTO
     * @return 微信下单接口的响应数据
     */
    Map<String, String> submitOrderByWechat(PayOrderDTO payOrderDTO);

    /**
     * 获取微信openId
     *
     * @param code 授权id
     * @param appId 应用id，用于获取微信支付的参数
     * @return
     */
    String getWXOAuthOpenId(String code, String appId);

    /**
     * 申请微信授权码
     * @param payOrderDTO
     * @return 申请授权码的地址
     */
    String getWXOAuth2Code(PayOrderDTO payOrderDTO);

    /**
     * 更新订单支付状态
     * @param tradeNo 闪聚平台订单号
     * @param payChannelTradeNo 支付宝或微信的交易流水号
     * @param state 订单状态  交易状态支付状态,0‐订单生成,1‐支付中(目前未使用),2‐支付成 功,4‐关闭 5‐‐失败
     */
    void updateOrderTradeNoAndTradeState(String tradeNo, String payChannelTradeNo, String state);

    /**
     * 根据订单号查询订单号
     * @param tradeNo
     * @return
     */
    public PayOrderDTO queryPayOrder(String tradeNo);

    PaymentResponseDTO submitOrderByAli(PayOrderDTO payOrderDTO);

    /**
     * 生成门店二维码
     * @param qrCodeDTO  qrCodeDto，传入merchantId,appId、storeid、channel、subject、body
     * @return 支付入口URL，将二维码的参数组成json并用base64编码
     */
    String createStoreQRCode(QRCodeDTO qrCodeDTO);
}
