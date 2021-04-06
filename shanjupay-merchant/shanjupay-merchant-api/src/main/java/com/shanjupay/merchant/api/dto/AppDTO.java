package com.shanjupay.merchant.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "AppDTO", description = "应用信息")
public class AppDTO implements Serializable {

    @ApiModelProperty("应用id，新增时无需传入")
    private String appId;

    @ApiModelProperty(value = "应用名称")
    private String appName;

    @ApiModelProperty(value = "商户id")
    private Long merchantId;

    @ApiModelProperty(value = "应用公钥")
    private String publicKey;

    @ApiModelProperty(value = "支付回调应用的url，创建时可不填")
    private String notifyUrl;


}
