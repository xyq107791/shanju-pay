package com.shanjupay.merchant.api;

import com.shanjupay.merchant.api.dto.MerchantDTO;

public interface MerchantService {

    /*** 商户注册 * @return */
    MerchantDTO createMerchant(MerchantDTO merchantDTO);

    /**
     * 根据ID查询详细信息
     */
    MerchantDTO queryMerchantById(Long merchantId);
}
