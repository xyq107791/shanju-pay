package com.shanjupay.merchant.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shanjupay.common.domain.BusinessException;
import com.shanjupay.common.domain.CommonErrorCode;
import com.shanjupay.common.domain.PageVO;
import com.shanjupay.common.util.PhoneUtil;
import com.shanjupay.merchant.api.MerchantService;
import com.shanjupay.merchant.api.dto.MerchantDTO;
import com.shanjupay.merchant.api.dto.StaffDTO;
import com.shanjupay.merchant.api.dto.StoreDTO;
import com.shanjupay.merchant.convert.MerchantCovert;
import com.shanjupay.merchant.convert.StaffConvert;
import com.shanjupay.merchant.convert.StoreConvert;
import com.shanjupay.merchant.entity.Merchant;
import com.shanjupay.merchant.entity.Staff;
import com.shanjupay.merchant.entity.Store;
import com.shanjupay.merchant.entity.StoreStaff;
import com.shanjupay.merchant.mapper.MerchantMapper;
import com.shanjupay.merchant.mapper.StaffMapper;
import com.shanjupay.merchant.mapper.StoreMapper;
import com.shanjupay.merchant.mapper.StoreStaffMapper;
import com.shanjupay.user.api.TenantService;
import com.shanjupay.user.api.dto.tenant.CreateTenantRequestDTO;
import com.shanjupay.user.api.dto.tenant.TenantDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@org.apache.dubbo.config.annotation.Service
@Slf4j
public class MerchantServiceImpl implements MerchantService {

    @Autowired
    MerchantMapper merchantMapper;
    @Autowired
    StoreMapper storeMapper;
    @Autowired
    StoreStaffMapper storeStaffMapper;
    @org.apache.dubbo.config.annotation.Reference
    TenantService tenantService;
    @Autowired
    StaffMapper staffMapper;

    @Override
    public Boolean queryStoreInMerchant(Long storeId, Long merchantId) {
        Integer count = storeMapper.selectCount(new LambdaQueryWrapper<Store>().eq(Store::getId, storeId).eq(Store::getMerchantId, merchantId));
        return count > 0;
    }

    @Override
    public PageVO<StoreDTO> queryStoreByPage(StoreDTO storeDTO, Integer pageNo, Integer pageSize) {
        // ????????????
        Page<Store> page = new Page<>(pageNo, pageSize);
        // ??????????????????
        QueryWrapper<Store> qw = new QueryWrapper<>();
        if (storeDTO != null && storeDTO.getMerchantId() != null) {
            qw.lambda().eq(Store::getMerchantId, storeDTO.getMerchantId());
        }
        // ????????????
        IPage<Store> storeIPage = storeMapper.selectPage(page, qw);
        // entity List???DTO List
        List<StoreDTO> storeList = StoreConvert.INSTANCE.listentity2dto(storeIPage.getRecords());
        return new PageVO<>(storeList, storeIPage.getTotal(), pageNo, pageSize);
    }

    @Override
    public StaffDTO createStaff(StaffDTO staffDTO) {
        //1.????????????????????????????????????
        String mobile = staffDTO.getMobile();

        if (StringUtils.isBlank(mobile)) {
            throw new BusinessException(CommonErrorCode.E_100112);
        }
        //????????????id???????????????????????????
        if (isExistStaffByMobile(mobile, staffDTO.getMerchantId())) {
            throw new BusinessException(CommonErrorCode.E_100113);
        }
        //2.????????????????????????
        String username = staffDTO.getUsername();
        if (StringUtils.isBlank(username)) {
            throw new BusinessException(CommonErrorCode.E_100110);
        }
        //????????????id????????????????????????
        if (isExistStaffByUserName(username, staffDTO.getMerchantId())) {
            throw new BusinessException(CommonErrorCode.E_100114);
        }
        Staff entity = StaffConvert.INSTANCE.dto2entity(staffDTO);
        log.info("?????????????????????");
        staffMapper.insert(entity);

        return StaffConvert.INSTANCE.entity2dto(entity);
    }

    private boolean isExistStaffByUserName(String username, Long merchantId) {
        LambdaQueryWrapper<Staff> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Staff::getUsername, username).eq(Staff::getMerchantId, merchantId);
        Integer i = staffMapper.selectCount(lambdaQueryWrapper);
        return i > 0;
    }

    private boolean isExistStaffByMobile(String mobile, Long merchantId) {
        LambdaQueryWrapper<Staff> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Staff::getMobile, mobile).eq(Staff::getMerchantId, merchantId);
        Integer i = staffMapper.selectCount(lambdaQueryWrapper);
        return i > 0;
    }

    @Override
    public void bindStaffToStore(Long storeId, Long staffId) {
        StoreStaff storeStaff = new StoreStaff();
        storeStaff.setStoreId(storeId);
        storeStaff.setStaffId(staffId);
        storeStaffMapper.insert(storeStaff);
    }

    @Override
    public StoreDTO createStore(StoreDTO storeDTO) {
        Store store = StoreConvert.INSTANCE.dto2entity(storeDTO);
        log.info("?????????????????????" + JSON.toJSONString(store));
        storeMapper.insert(store);
        return StoreConvert.INSTANCE.entity2dto(store);
    }

    @Override
    @Transactional
    public void applyMerchant(Long merchantId, MerchantDTO merchantDTO) {
        //?????????????????????????????????????????????
        Merchant merchant = merchantMapper.selectById(merchantId);
        if (merchant == null) {
            throw new BusinessException(CommonErrorCode.E_200002);
        }
        Merchant entity = MerchantCovert.INSTANCE.dto2entity(merchantDTO);
        entity.setId(merchant.getId());
        entity.setMobile(merchant.getMobile());//???????????????????????????????????????????????????????????????????????????????????????
        entity.setAuditStatus("1");
        entity.setTenantId(merchant.getTenantId());
        merchantMapper.updateById(entity);
    }

    @Override
    @Transactional
    public MerchantDTO createMerchant(MerchantDTO merchantDTO) {
        // 1.??????
        if (merchantDTO == null) {
            throw new BusinessException(CommonErrorCode.E_100108);
        }
        //?????????????????????
        if (StringUtils.isBlank(merchantDTO.getMobile())) {
            throw new BusinessException(CommonErrorCode.E_100112);
        }
        //????????????????????????
        if (!PhoneUtil.isMatches(merchantDTO.getMobile())) {
            throw new BusinessException(CommonErrorCode.E_100109);
        }
        //?????????????????????
        if (StringUtils.isBlank(merchantDTO.getUsername())) {
            throw new BusinessException(CommonErrorCode.E_100110);
        }
        // ??????????????????
        if (StringUtils.isBlank(merchantDTO.getPassword())) {
            throw new BusinessException(CommonErrorCode.E_100111);
        }

        //???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        LambdaQueryWrapper<Merchant> lambdaQryWrapper = new LambdaQueryWrapper<Merchant>()
                .eq(Merchant::getMobile, merchantDTO.getMobile());
        Integer count = merchantMapper.selectCount(lambdaQryWrapper);
        if (count > 0) {
            throw new BusinessException(CommonErrorCode.E_100113);
        }
        //2.????????????????????????????????????
        CreateTenantRequestDTO createTenantRequest = new CreateTenantRequestDTO();
        createTenantRequest.setMobile(merchantDTO.getMobile());
        //??????????????????????????????
        createTenantRequest.setTenantTypeCode("shanju-merchant");
        //????????????????????????????????????
        createTenantRequest.setBundleCode("shanju-merchant");
        //?????????????????????
        createTenantRequest.setUsername(merchantDTO.getUsername());
        createTenantRequest.setPassword(merchantDTO.getPassword());
        //?????????????????????????????????
        createTenantRequest.setName(merchantDTO.getUsername());
        log.info("????????????????????????????????????????????????????????????");
        TenantDTO tenantDTO = tenantService.createTenantAndAccount(createTenantRequest);
        if (tenantDTO == null || tenantDTO.getId() == null) {
            throw new BusinessException(CommonErrorCode.E_200012);
        }

        // ??????????????????????????????????????????
        Merchant merchant = merchantMapper.selectOne(new LambdaQueryWrapper<Merchant>()
                .eq(Merchant::getTenantId, tenantDTO.getId()));
        if (merchant != null && merchant.getId() != null) {
            throw new BusinessException(CommonErrorCode.E_200017);
        }

        //3.????????????????????????
        merchantDTO.setTenantId(tenantDTO.getId());
        //????????????????????????????????????"0"
        merchantDTO.setAuditStatus("0"); //???????????? 0-????????????1-?????????????????????2-???????????????3-????????????
        Merchant entity = MerchantCovert.INSTANCE.dto2entity(merchantDTO);
        //????????????
        log.info("????????????????????????");
        merchantMapper.insert(entity);

        //4.??????????????????????????????
        StoreDTO storeDTO = new StoreDTO();
        storeDTO.setMerchantId(entity.getId());
        storeDTO.setStoreName("?????????");
        storeDTO = createStore(storeDTO);
        log.info("????????????:{}" + JSON.toJSONString(storeDTO));

        //5.????????????????????????????????????
        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setMerchantId(entity.getId());
        staffDTO.setMobile(entity.getMobile());
        staffDTO.setUsername(merchantDTO.getUsername());
        // ????????????????????????????????????????????????
        staffDTO.setStoreId(storeDTO.getId());
        staffDTO = createStaff(staffDTO);

        //6.????????????????????????
        bindStaffToStore(storeDTO.getId(), staffDTO.getId());

        //????????????????????????
        return MerchantCovert.INSTANCE.entity2dto(entity);
    }

    @Override
    public MerchantDTO queryMerchantById(Long merchantId) {
        Merchant merchant = merchantMapper.selectById(merchantId);
        MerchantDTO merchantDTO = MerchantCovert.INSTANCE.entity2dto(merchant);
        return merchantDTO;
    }

    @Override
    public MerchantDTO queryMerchantByTenantId(Long tenantId) {
        Merchant merchant = merchantMapper.selectOne(new LambdaQueryWrapper<Merchant>()
                .eq(Merchant::getTenantId, tenantId));
        return MerchantCovert.INSTANCE.entity2dto(merchant);
    }
}
