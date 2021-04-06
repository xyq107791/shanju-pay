package com.shanjupay.transaction.api;

import com.shanjupay.transaction.api.dto.PlatformChannelDTO;

import java.util.List;

/**
 * 支付渠道服务 管理平台支付渠道，原始支付渠道，以及相关配置
 */
public interface PayChannelService {

    /**
     * 应用是否已经绑定了某个服务类型
     * @param appId
     * @param platformChannel
     * @return 已绑定返回1，否则 返回0
     */
    int queryAppBindPlatformChannel(String appId,String platformChannel);
    /**
     * 为app绑定平台服务类型
     * @param appId 应用id
     * @param platformChannelCodes 平台服务类型列表
     */
    void bindPlatformChannelForApp(String appId, String platformChannelCodes);
    /**
     *  获取平台服务类型
     *  @return
     */
    List<PlatformChannelDTO> queryPlatformChannel();
}