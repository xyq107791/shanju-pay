package com.shanjupay.transaction.controller;

import com.alibaba.fastjson.JSON;
import com.shanjupay.common.domain.BusinessException;
import com.shanjupay.common.domain.CommonErrorCode;
import com.shanjupay.common.util.AmountUtil;
import com.shanjupay.common.util.EncryptUtil;
import com.shanjupay.common.util.IPUtil;
import com.shanjupay.common.util.ParseURLPairUtil;
import com.shanjupay.merchant.api.AppService;
import com.shanjupay.merchant.api.dto.AppDTO;
import com.shanjupay.paymentagent.api.dto.PaymentResponseDTO;
import com.shanjupay.transaction.api.TransactionService;
import com.shanjupay.transaction.api.dto.PayOrderDTO;
import com.shanjupay.transaction.convert.PayOrderConvert;
import com.shanjupay.transaction.vo.OrderConfirmVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: WeiQiuKe
 * @create: 2021-04-12
 **/
@Slf4j
@Controller
public class PayController {

    @org.apache.dubbo.config.annotation.Reference
    AppService appService;
    @Autowired
    TransactionService transactionService;

    @ApiOperation("支付宝门店下单付款")
    @PostMapping("/createAliPayOrder")
    public void createAlipayOrderForStore(OrderConfirmVO orderConfirmVO, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (StringUtils.isBlank(orderConfirmVO.getAppId())) {
            throw new BusinessException(CommonErrorCode.E_300003);
        }
        PayOrderDTO payOrderDTO = PayOrderConvert.INSTANCE.vo2dto(orderConfirmVO);
        payOrderDTO.setTotalAmount(Integer.valueOf(AmountUtil.changeY2F(orderConfirmVO.getTotalAmount())));
        payOrderDTO.setClientIp(IPUtil.getIpAddr(request));
        //获取下单应用信息
        AppDTO app = appService.getAppById(payOrderDTO.getAppId());
        //设置所属商户
        payOrderDTO.setMerchantId(app.getMerchantId());
        PaymentResponseDTO payOrderResult = transactionService.submitOrderByAli(payOrderDTO);

        String content = String.valueOf(payOrderResult.getContent());
        log.info("支付宝H5支付响应的结果：" + content);
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write(content); //直接将完整的表单html输出到页面
        response.getWriter().flush();
        response.getWriter().close();
    }

    @RequestMapping(value = "/pay-entry/{ticket}")
    public String payEntry(@PathVariable("ticket") String ticket, HttpServletRequest request) {
        try {

            //将ticket的base64还原
            String ticketStr = EncryptUtil.decodeUTF8StringBase64(ticket);
            //将ticket(json)转成对象
            PayOrderDTO order = JSON.parseObject(ticketStr, PayOrderDTO.class);
            //将对象转成url格式
//        String params = ParseURLPairUtil.parseURLPair(order);

            BrowserType browserType = BrowserType.valueOfUserAgent(request.getHeader("user-agent"));

            switch (browserType) {
                case ALIPAY: //直接跳转收银台pay.html
                    return "forward:/pay-page?" + ParseURLPairUtil.parseURLPair(order);
                case WECHAT:
                    return "forward:/pay-page?" + ParseURLPairUtil.parseURLPair(order);
                default:
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage(), e);
        }

        return "forward:/pay-page-error";
    }
}
