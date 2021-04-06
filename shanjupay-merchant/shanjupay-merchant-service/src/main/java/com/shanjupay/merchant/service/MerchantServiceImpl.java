package com.shanjupay.merchant.service;

import com.shanjupay.common.domain.BusinessException;
import com.shanjupay.common.domain.CommonErrorCode;
import com.shanjupay.merchant.api.MerchantService;
import com.shanjupay.merchant.api.dto.MerchantDTO;
import com.shanjupay.merchant.convert.MerchantCovert;
import com.shanjupay.merchant.entity.Merchant;
import com.shanjupay.merchant.mapper.MerchantMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class MerchantServiceImpl implements MerchantService {

    @Autowired
    MerchantMapper merchantMapper;

    @Override
    @Transactional
    public void applyMerchant(Long merchantId, MerchantDTO merchantDTO) {
        //接收资质申请信息，更新到商户表
        Merchant merchant = merchantMapper.selectById(merchantId);
        if (merchant == null) {
            throw new BusinessException(CommonErrorCode.E_200002);
        }
        Merchant entity = MerchantCovert.INSTANCE.dto2entity(merchantDTO);
        entity.setId(merchant.getId());
        entity.setMobile(merchant.getMobile());//因为资质申请的时候手机号不让改，还使用数据库中原来的手机号
        entity.setAuditStatus("1");
        entity.setTenantId(merchant.getTenantId());
        merchantMapper.updateById(entity);
    }

    @Override
    @Transactional
    public MerchantDTO createMerchant(MerchantDTO merchantDTO) {
        //将dto转成entity
        Merchant entity = MerchantCovert.INSTANCE.dto2entity(merchantDTO);
        //设置审核状态0-未申请,1-已申请待审核,2-审核通过,3-审核拒绝
        entity.setAuditStatus("0");
        //保存商户
        merchantMapper.insert(entity);
        //将entity转成dto
        MerchantDTO merchantDTONew = MerchantCovert.INSTANCE.entity2dto(entity);
        return merchantDTONew;
    }

    @Override
    public MerchantDTO queryMerchantById(Long merchantId) {
        Merchant merchant = merchantMapper.selectById(merchantId);
        MerchantDTO merchantDTO = new MerchantDTO();

        BeanUtils.copyProperties(merchant, merchantDTO);

        return merchantDTO;
    }
}
