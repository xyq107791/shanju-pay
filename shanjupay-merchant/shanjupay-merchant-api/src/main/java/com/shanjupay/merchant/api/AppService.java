package com.shanjupay.merchant.api;

import com.shanjupay.merchant.api.dto.AppDTO;

import java.util.List;

public interface AppService {

    /**
     * 根据业务id查询应用
     * @param id
     * @return
     */
    AppDTO getAppById(String id);

    /**
     * 查询商户下的应用列表
     * @param merchantId
     * @return
     */
    List<AppDTO> queryAppByMerchant(Long merchantId);

    /**
     * 商户下创建应用
     * @return
     */
    AppDTO createApp(Long merchantId, AppDTO app);
}
