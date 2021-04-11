package com.shanjupay.merchant.api;

import com.shanjupay.common.domain.BusinessException;
import com.shanjupay.merchant.api.dto.MerchantDTO;
import com.shanjupay.merchant.api.dto.StaffDTO;
import com.shanjupay.merchant.api.dto.StoreDTO;

public interface MerchantService {

    /**
     * 商户新增员工
     * @param staffDTO
     * @return
     */
    StaffDTO createStaff(StaffDTO staffDTO);

    /**
     * 为门店设置管理员
     * @param storeId
     * @param staffId
     */
    void bindStaffToStore(Long storeId, Long staffId);

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

    /**
     * 查询租户下的商户
     * @param tenantId
     * @return
     */
    MerchantDTO queryMerchantByTenantId(Long tenantId);
}
