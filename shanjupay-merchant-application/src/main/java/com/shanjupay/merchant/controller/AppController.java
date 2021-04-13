package com.shanjupay.merchant.controller;

import com.shanjupay.common.domain.BusinessException;
import com.shanjupay.common.domain.CommonErrorCode;
import com.shanjupay.common.util.QRCodeUtil;
import com.shanjupay.merchant.api.AppService;
import com.shanjupay.merchant.api.MerchantService;
import com.shanjupay.merchant.api.dto.AppDTO;
import com.shanjupay.merchant.api.dto.MerchantDTO;
import com.shanjupay.merchant.common.util.SecurityUtil;
import com.shanjupay.transaction.api.PayChannelService;
import com.shanjupay.transaction.api.TransactionService;
import com.shanjupay.transaction.api.dto.QRCodeDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * @author: WeiQiuKe
 * @create: 2021-04-06
 **/
@Api(value = "商户平台‐应用管理", tags = "商户平台‐应用相关", description = "商户平台‐应用相关")
@RestController
@Slf4j
public class AppController {

    @org.apache.dubbo.config.annotation.Reference
    private AppService appService;
    @org.apache.dubbo.config.annotation.Reference
    private PayChannelService payChannelService;
    @org.apache.dubbo.config.annotation.Reference
    private MerchantService merchantService;
    @org.apache.dubbo.config.annotation.Reference
    private TransactionService transactionService;

    /**
     * 门店二维码订单标题
     */
    @Value("${shanjupay.c2b.subject}")
    private String subject;
    @Value("${shanjupay.c2b.body}")
    private String body;

    @ApiOperation("生成商户应用门店二维码")
    @ApiImplicitParams({@ApiImplicitParam(name = "appId", value = "商户应用id", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "storeId", value = "商户门店id", required = true, dataType = "String", paramType = "path"),})
    @GetMapping(value = "/my/apps/{appId}/stores/{storeId}/app-store-qrcode")
    public String createCScanBStoreQRCode(@PathVariable String appId, @PathVariable Long storeId) {
        //商户id
        Long merchantId = SecurityUtil.getMerchantId();
        //生成二维码链接
        QRCodeDTO qrCodeDTO = new QRCodeDTO();
        qrCodeDTO.setMerchantId(merchantId);
        qrCodeDTO.setAppId(appId);
        qrCodeDTO.setStoreId(storeId);
        //标题
        MerchantDTO merchantDTO = merchantService.queryMerchantById(merchantId);
        // "%s 商品"
        qrCodeDTO.setSubject(String.format(subject, merchantDTO.getMerchantName()));
        //内容，格式："向%s 付款"
        qrCodeDTO.setBody(String.format(body, merchantDTO.getMerchantName()));

        String storeQRCodeUrl = transactionService.createStoreQRCode(qrCodeDTO);
        log.info("[merchantId:{},appId:{},storeId:{}] createCScanBStoreQRCode is {}", merchantId, appId, storeId, storeQRCodeUrl);

        try {
            //根据返回url，调用生成二维码工具类，生成二维码base64返回
            return QRCodeUtil.createQRCode(storeQRCodeUrl, 200, 200);
        } catch (IOException e) {
            throw new BusinessException(CommonErrorCode.E_200007);
        }
    }


    @ApiOperation("查询应用是否绑定了某个服务类型")
    @ApiImplicitParams({@ApiImplicitParam(name = "appId", value = "应用appId", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "platformChannel", value = "服务类型", required = true, dataType = "String", paramType = "query")})
    @GetMapping("/my/merchants/apps/platformchannels")
    public int queryAppBindPlatformChannel(@RequestParam String appId, @RequestParam String platformChannel) {
        return payChannelService.queryAppBindPlatformChannel(appId, platformChannel);
    }

    @ApiOperation("绑定服务类型")
    @PostMapping(value = "/my/apps/{appId}/platform‐channels")
    @ApiImplicitParams({@ApiImplicitParam(value = "应用id", name = "appId", dataType = "string", paramType = "path"),
            @ApiImplicitParam(value = "服务类型code", name = "platformChannelCodes", dataType = "string", paramType = "query")})
    public void bindPlatformForApp(@PathVariable("appId") String appId, @RequestParam("platformChannelCodes") String platformChannelCodes) {
        payChannelService.bindPlatformChannelForApp(appId, platformChannelCodes);
    }

    @ApiOperation("查询商户下的应用列表")
    @GetMapping(value = "/my/apps")
    public List<AppDTO> queryMyApps() {
        //商户id
        Long merchantId = SecurityUtil.getMerchantId();
        return appService.queryAppByMerchant(merchantId);
    }

    @ApiOperation("根据应用id查询应用信息")
    @ApiImplicitParam(value = "应用id", name = "appId", dataType = "String", paramType = "path")
    @GetMapping(value = "/my/apps/{appId}")
    public AppDTO getApp(@PathVariable("appId") String appId) {
        return appService.getAppById(appId);
    }

    @ApiOperation("商户创建应用")
    @ApiImplicitParams({@ApiImplicitParam(name = "app", value = "应用信息", required = true, dataType = "AppDTO", paramType = "body")})
    @PostMapping(value = "/my/apps")
    public AppDTO createApp(@RequestBody AppDTO app) {
        Long merchantId = SecurityUtil.getMerchantId();
        return appService.createApp(merchantId, app);
    }
}
