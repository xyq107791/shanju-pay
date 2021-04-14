package com.shanjupay.transaction.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.shanjupay.common.domain.BusinessException;
import com.shanjupay.common.domain.CommonErrorCode;
import com.shanjupay.common.util.AmountUtil;
import com.shanjupay.common.util.EncryptUtil;
import com.shanjupay.common.util.PaymentUtil;
import com.shanjupay.merchant.api.AppService;
import com.shanjupay.merchant.api.MerchantService;
import com.shanjupay.paymentagent.api.PayChannelAgentService;
import com.shanjupay.paymentagent.api.conf.AliConfigParam;
import com.shanjupay.paymentagent.api.conf.WXConfigParam;
import com.shanjupay.paymentagent.api.dto.AlipayBean;
import com.shanjupay.paymentagent.api.dto.PaymentResponseDTO;
import com.shanjupay.paymentagent.api.dto.WeChatBean;
import com.shanjupay.transaction.api.PayChannelService;
import com.shanjupay.transaction.api.TransactionService;
import com.shanjupay.transaction.api.dto.PayChannelParamDTO;
import com.shanjupay.transaction.api.dto.PayOrderDTO;
import com.shanjupay.transaction.api.dto.QRCodeDTO;
import com.shanjupay.transaction.convert.PayOrderConvert;
import com.shanjupay.transaction.entity.PayOrder;
import com.shanjupay.transaction.mapper.PayOrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

/**
 * @author: WeiQiuKe
 * @create: 2021-04-12
 **/
@org.apache.dubbo.config.annotation.Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {
    /**
     * 支付入口url
     */
    @Value("${shanjupay.payurl}")
    private String payurl;
    @Value("${weixin.oauth2RequestUrl}")
    private String wxOAuth2RequestUrl;
    @Value("${weixin.oauth2Token}")
    String oauth2Token;
    @Value("${weixin.oauth2CodeReturnUrl}")
    private String wxOAuth2CodeReturnUrl;
    @org.apache.dubbo.config.annotation.Reference
    MerchantService merchantService;
    @org.apache.dubbo.config.annotation.Reference
    AppService appService;
    @Autowired
    PayOrderMapper payOrderMapper;
    @Autowired
    PayChannelService payChannelService;
    @Reference
    PayChannelAgentService payChannelAgentService;
    @Autowired
    private RestTemplate restTemplate;

    /**
     * 微信确认支付
     * @param payOrderDTO
     * @return 微信下单接口的响应数据
     */
    @Override
    public Map<String, String> submitOrderByWechat(PayOrderDTO payOrderDTO) {
        //微信openid
        String openId = payOrderDTO.getOpenId();
        payOrderDTO.setPayChannel("WX_JSAPI");
        //保存订单
        PayOrderDTO save = save(payOrderDTO);
        String tradeNo = save.getTradeNo();
        //微信统一下单
        return weChatJsapi(openId, tradeNo);
    }

    /**
     * 微信jsapi 调用支付渠道代理
     * @param openId
     * @param tradeNo
     * @return
     */
    private Map<String, String> weChatJsapi(String openId, String tradeNo) {

        //根据订单号查询订单详情
        PayOrderDTO payOrderDTO = queryPayOrder(tradeNo);
        if (payOrderDTO == null) {
            throw new BusinessException(CommonErrorCode.E_400002);
        }
        //构造微信订单参数实体
        WeChatBean weChatBean = new WeChatBean();
        weChatBean.setOpenId(openId); //openid
        weChatBean.setSpbillCreateIp(payOrderDTO.getClientIp());//
        weChatBean.setTotalFee(payOrderDTO.getTotalAmount());//金额
        weChatBean.setBody(payOrderDTO.getBody());//订单描述
        weChatBean.setOutTradeNo(payOrderDTO.getOutTradeNo());//使用聚合平台的订单号 tradeNo
        weChatBean.setNotifyUrl("none"); //异步接收微信通知支付结果的地址(暂时不用)

        //根据应用、服务类型、支付渠道查询支付渠道参数
        PayChannelParamDTO payChannelParamDTO = payChannelService.queryParamByAppPlatformAndPayChannel(payOrderDTO.getAppId(),
                "shanju_c2b", "WX_JSAPI");
        if (payChannelParamDTO == null) {
            throw new BusinessException(CommonErrorCode.E_300007);
        }
        //支付宝渠道参数
        WXConfigParam wxConfigParam = JSON.parseObject(payChannelParamDTO.getParam(), WXConfigParam.class);
        return payChannelAgentService.createPayOrderByWeChatJSAPI(wxConfigParam, weChatBean);
    }

    @Override
    public String getWXOAuthOpenId(String code, String appId) {
        //获取微信支付渠道参数，根据应用、服务类型、支付渠道查询支付渠道参数
        PayChannelParamDTO payChannelParamDTO = payChannelService.queryParamByAppPlatformAndPayChannel(appId, "shanju_c2b",
                "WX_JSAPI");
        if (payChannelParamDTO == null) {
            throw new BusinessException(CommonErrorCode.E_300007);
        }
        //支付渠道参数
        String payParam = payChannelParamDTO.getParam();
        WXConfigParam wxConfigParam = JSON.parseObject(payParam, WXConfigParam.class);
        //密钥
        String appSecret = wxConfigParam.getAppSecret();
        //获取openid地址
        String url = String.format("%s?appid=%s&secret=%s&code=%s&grant_type=authorization_code", oauth2Token, wxConfigParam.getAppId(),
                wxConfigParam.getAppSecret(), code);
        ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        String response = exchange.getBody();
        return JSONObject.parseObject(response).getString("openid");
    }

    @Override
    public String getWXOAuth2Code(PayOrderDTO order) {
        //将订单信息封装到state参数中
        String state = EncryptUtil.encodeUTF8StringBase64(JSON.toJSONString(order));
        //应用id
        String appId = order.getAppId();
        //服务类型
        String channel = order.getChannel();
        //获取微信支付渠道参数，根据应用、服务类型、支付渠道查询支付渠道参数
        PayChannelParamDTO payChannelParamDTO = payChannelService.queryParamByAppPlatformAndPayChannel(appId, channel,
                "WX_JSAPI");
        if (payChannelParamDTO == null) {
            throw new BusinessException(CommonErrorCode.E_300007);
        }
        //支付渠道参数
        String payParam = payChannelParamDTO.getParam();
        WXConfigParam wxConfigParam = JSON.parseObject(payParam, WXConfigParam.class);

        try {
            String url = String.format("%s?appid=%s&scope=snsapi_base&state=%s&redirect_uri=%s",
                    wxOAuth2RequestUrl, wxConfigParam.getAppId(), state, URLEncoder.encode(wxOAuth2CodeReturnUrl, "utf-8"));
            log.info("微信生成授权码url:{}", url);
            return "redirect:" + url;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "forward:/pay-page-error"; //生成获取授权码连接失败
        }
    }

    @Override
    public void updateOrderTradeNoAndTradeState(String tradeNo, String payChannelTradeNo, String state) {
        LambdaUpdateWrapper<PayOrder> lambda = new LambdaUpdateWrapper<>();
        lambda.eq(PayOrder::getTradeNo, tradeNo).set(PayOrder::getPayChannelTradeNo, payChannelTradeNo)
                .set(PayOrder::getTradeState, state);
        if (StringUtils.isBlank(state) && "2".equals(state)) {
            lambda.set(PayOrder::getPaySuccessTime, LocalDateTime.now());
        }
        payOrderMapper.update(null, lambda);
    }

    @Override
    public PayOrderDTO queryPayOrder(String tradeNo) {
        PayOrder payOrder = payOrderMapper.selectOne(new LambdaQueryWrapper<PayOrder>().eq(PayOrder::getTradeNo, tradeNo));
        return PayOrderConvert.INSTANCE.entity2dto(payOrder);
    }

    @Override
    public PaymentResponseDTO submitOrderByAli(PayOrderDTO payOrderDTO) {
        //保存订单
        payOrderDTO.setPayChannel("ALIPAY_WAP");
        //保存订单
        payOrderDTO = save(payOrderDTO);
        //调用支付代理服务请求第三方支付系统
        //..

        return alipayH5(payOrderDTO.getTradeNo());
    }

    //调用支付宝下单接口
    private PaymentResponseDTO alipayH5(String tradeNo) {
        //构建支付实体
        AlipayBean alipayBean = new AlipayBean();

        //根据订单号查询订单详情
        PayOrderDTO payOrderDTO = queryPayOrder(tradeNo);
        alipayBean.setOutTradeNo(tradeNo);
        alipayBean.setSubject(payOrderDTO.getSubject());
        String totalAmount = null; //支付宝那边入参是元

        try {
            AmountUtil.changeF2Y(payOrderDTO.getTotalAmount().toString());
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(CommonErrorCode.E_300006);
        }
        alipayBean.setTotalAmount(totalAmount);
        alipayBean.setBody(payOrderDTO.getBody());
        alipayBean.setStoreId(payOrderDTO.getStoreId());
        alipayBean.setExpireTime("30m");

        //根据应用、服务类型、支付渠道查询支付渠道参数
        PayChannelParamDTO payChannelParamDTO = payChannelService.queryParamByAppPlatformAndPayChannel(payOrderDTO.getAppId(),
                payOrderDTO.getChannel(), "ALIPAY_WAP");
        if (payChannelParamDTO == null) {
            throw new BusinessException(CommonErrorCode.E_300007);
        }
        //支付宝渠道参数
        AliConfigParam aliConfigParam = JSON.parseObject(payChannelParamDTO.getParam(), AliConfigParam.class);
        //字符编码
        aliConfigParam.setCharest("utf-8");
        PaymentResponseDTO payOrderResponse = payChannelAgentService.createPayOrderByAliWAP(aliConfigParam, alipayBean);
        log.info("支付宝H5支付相应content:" + payOrderResponse.getContent());

        return payOrderResponse;
    }

    private PayOrderDTO save(PayOrderDTO payOrderDTO) {
        PayOrder entity = PayOrderConvert.INSTANCE.dto2entity(payOrderDTO);
        entity.setTradeNo(PaymentUtil.genUniquePayOrderNo());
        //订单创建时间
        entity.setCreateTime(LocalDateTime.now());
        //设置过期时间，30分钟
        entity.setExpireTime(LocalDateTime.now().plus(30, ChronoUnit.MINUTES));
        entity.setCurrency("CNY");
        entity.setTradeState("0");

        int insert = payOrderMapper.insert(entity);
        return PayOrderConvert.INSTANCE.entity2dto(entity);
    }

    @Override
    public String createStoreQRCode(QRCodeDTO qrCodeDTO) {
        //校验应用和门店
        verifyAppAndStore(qrCodeDTO.getMerchantId(), qrCodeDTO.getAppId(), qrCodeDTO.getStoreId());
        //1.生成支付信息
        PayOrderDTO payOrderDTO = new PayOrderDTO();
        payOrderDTO.setMerchantId(qrCodeDTO.getMerchantId());
        payOrderDTO.setAppId(qrCodeDTO.getAppId());
        payOrderDTO.setStoreId(qrCodeDTO.getStoreId());
        payOrderDTO.setSubject(qrCodeDTO.getSubject()); //显示订单标题
        payOrderDTO.setChannel("shanju_c2b"); //服务类型
        payOrderDTO.setBody(qrCodeDTO.getBody());//订单内容
        String jsonString = JSON.toJSONString(payOrderDTO);

        log.info("transaction service createStoreQRCode,JsonString is {}", jsonString);

        //将支付信息保存到票据中
        String ticket = EncryptUtil.encodeUTF8StringBase64(jsonString);
        //支付入口
        String payEntryUrl = payurl + ticket;
        log.info("transaction service createStoreQRCode,pay-entry is {}", payEntryUrl);
        return payEntryUrl;
    }

    /**
     * 校验应用和门店是否属于当前登录商户
     *
     * @param merchantId
     * @param appId
     * @param storeId
     */
    private void verifyAppAndStore(Long merchantId, String appId, Long storeId) {
        Boolean contains = appService.queryAppInMerchant(appId, merchantId);
        if (!contains) {
            throw new BusinessException(CommonErrorCode.E_200005);
        }
        //判断门店是否属于当前商户
        Boolean containsStore = merchantService.queryStoreInMerchant(storeId, merchantId);
        if (!containsStore) {
            throw new BusinessException(CommonErrorCode.E_200006);
        }
    }
}
