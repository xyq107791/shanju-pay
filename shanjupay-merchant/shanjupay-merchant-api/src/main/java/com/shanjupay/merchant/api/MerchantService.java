package com.shanjupay.merchant.api;

import com.shanjupay.common.domain.BusinessException;
import com.shanjupay.merchant.api.dto.MerchantDTO;
import com.shanjupay.merchant.api.dto.StoreDTO;

public interface MerchantService {
    /**
     * 商户下新增门店
     * @param storeDTO
     * @return
     */
    StoreDTO createStore(StoreDTO storeDTO);

    /**
     * 资质申请接口
     * @param merchantId 商户id
     * @param merchantDTO 资质申请的信息
     * @throws BusinessException
     */
    void applyMerchant(Long merchantId,MerchantDTO merchantDTO);

    /*** 商户注册 * @return */
    MerchantDTO createMerchant(MerchantDTO merchantDTO);

    /**
     * 根据ID查询详细信息
     */
    MerchantDTO queryMerchantById(Long merchantId);
}
