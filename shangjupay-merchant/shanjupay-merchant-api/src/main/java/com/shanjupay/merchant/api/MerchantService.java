package com.shanjupay.merchant.api;

import com.shanjupay.merchant.api.dto.MerchantDTO;

public interface MerchantService {

    /**
     * 根据ID查询详细信息
     */
    MerchantDTO queryMerchantById(Long merchantId);
}
