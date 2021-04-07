package com.shanjupay.merchant.controller;

import com.shanjupay.transaction.api.PayChannelService;
import com.shanjupay.transaction.api.dto.PayChannelDTO;
import com.shanjupay.transaction.api.dto.PlatformChannelDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: WeiQiuKe
 * @create: 2021-04-06
 **/
@Api(value = "商户平台-渠道和支付参数相关", tags = "商户平台-渠道和支付参数", description = "商户平台-渠道和支付参数相关")
@Slf4j
@RestController
public class PlatformParamController {

    @Reference
    private PayChannelService payChannelService;

    @ApiOperation("根据平台服务类型获取支付渠道列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "platformChannelCode", value = "服务类型编码", required = true,
                    dataType = "String", paramType = "path")
    })
    @GetMapping(value = "/my/pay-channels/platform-channel/{platformChannelCode}")
    public List<PayChannelDTO> queryPayChannelByPlatformChannel(@PathVariable String platformChannelCode) {
        return payChannelService.queryPayChannelByPlatformChannel(platformChannelCode);
    }

    @ApiOperation("获取平台服务类型")
    @GetMapping(value = "/my/platform-channels")
    public List<PlatformChannelDTO> queryPlatformChannel() {
        return payChannelService.queryPlatformChannel();
    }
}