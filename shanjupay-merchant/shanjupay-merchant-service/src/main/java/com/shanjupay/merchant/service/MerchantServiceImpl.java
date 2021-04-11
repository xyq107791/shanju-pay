package com.shanjupay.merchant.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shanjupay.common.domain.BusinessException;
import com.shanjupay.common.domain.CommonErrorCode;
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
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Service
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
    public StaffDTO createStaff(StaffDTO staffDTO) {
        //1.校验手机号格式及是否存在
        String mobile = staffDTO.getMobile();

        if (StringUtils.isBlank(mobile)) {
            throw new BusinessException(CommonErrorCode.E_100112);
        }
        //根据商户id和手机号校验唯一性
        if (isExistStaffByMobile(mobile, staffDTO.getMerchantId())) {
            throw new BusinessException(CommonErrorCode.E_100113);
        }
        //2.校验用户是否为空
        String username = staffDTO.getUsername();
        if (StringUtils.isBlank(username)) {
            throw new BusinessException(CommonErrorCode.E_100110);
        }
        //根据商户id和账号校验唯一性
        if (isExistStaffByUserName(username, staffDTO.getMerchantId())) {
            throw new BusinessException(CommonErrorCode.E_100114);
        }
        Staff entity = StaffConvert.INSTANCE.dto2entity(staffDTO);
        log.info("商户下新增员工");
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
        log.info("商户下新增门店" + JSON.toJSONString(store));
        storeMapper.insert(store);
        return StoreConvert.INSTANCE.entity2dto(store);
    }

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
        // 1.校验
        if (merchantDTO == null) {
            throw new BusinessException(CommonErrorCode.E_100108);
        }
        //手机号非空校验
        if (StringUtils.isBlank(merchantDTO.getMobile())) {
            throw new BusinessException(CommonErrorCode.E_100112);
        }
        //校验手机号合法性
        if (!PhoneUtil.isMatches(merchantDTO.getMobile())) {
            throw new BusinessException(CommonErrorCode.E_100109);
        }
        //联系人非空校验
        if (StringUtils.isBlank(merchantDTO.getUsername())) {
            throw new BusinessException(CommonErrorCode.E_100110);
        }
        // 密码非空校验
        if (StringUtils.isBlank(merchantDTO.getPassword())) {
            throw new BusinessException(CommonErrorCode.E_100111);
        }

        //校验商户手机号的唯一性，根据商户的手机号查询商户表，如果存在记录则说明已有相同的手机号重复
        LambdaQueryWrapper<Merchant> lambdaQryWrapper = new LambdaQueryWrapper<Merchant>()
                .eq(Merchant::getMobile, merchantDTO.getMobile());
        Integer count = merchantMapper.selectCount(lambdaQryWrapper);
        if (count > 0) {
            throw new BusinessException(CommonErrorCode.E_100113);
        }
        //2.添加租户和账号并绑定关系
        CreateTenantRequestDTO createTenantRequest = new CreateTenantRequestDTO();
        createTenantRequest.setMobile(merchantDTO.getMobile());
        //表示该租户类型是商户
        createTenantRequest.setTenantTypeCode("shanju-merchant");
        //设置租户套餐为初始化套餐
        createTenantRequest.setBundleCode("shanju-merchant");
        //租户的账号信息
        createTenantRequest.setUsername(merchantDTO.getUsername());
        createTenantRequest.setPassword(merchantDTO.getPassword());
        //新增租户并设置为管理员
        createTenantRequest.setName(merchantDTO.getUsername());
        log.info("商户中心调用统一账号服务，新增租户和账号");
        TenantDTO tenantDTO = tenantService.createTenantAndAccount(createTenantRequest);
        if (tenantDTO == null || tenantDTO.getId() == null) {
            throw new BusinessException(CommonErrorCode.E_200012);
        }

        // 判断租户下是否已经注册过商户
        Merchant merchant = merchantMapper.selectOne(new LambdaQueryWrapper<Merchant>()
                .eq(Merchant::getTenantId, tenantDTO.getId()));
        if (merchant != null && merchant.getId() != null) {
            throw new BusinessException(CommonErrorCode.E_200017);
        }

        //3.设置商户所属租户
        merchantDTO.setTenantId(tenantDTO.getId());
        //设置审核状态，注册默认为"0"
        merchantDTO.setAuditStatus("0"); //审核状态 0-未申请，1-已申请待审核，2-审核通过，3-审核拒绝
        Merchant entity = MerchantCovert.INSTANCE.dto2entity(merchantDTO);
        //保存商户
        log.info("保存商户注册信息");
        merchantMapper.insert(entity);

        //4.新增门店，创建根门店
        StoreDTO storeDTO = new StoreDTO();
        storeDTO.setMerchantId(entity.getId());
        storeDTO.setStoreName("根门店");
        storeDTO = createStore(storeDTO);
        log.info("门店信息:{}" + JSON.toJSONString(storeDTO));

        //5.新增员工，并设置归属门店
        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setMerchantId(entity.getId());
        staffDTO.setMobile(entity.getMobile());
        staffDTO.setUsername(merchantDTO.getUsername());
        // 为员工选择归属门店，此处为根门店
        staffDTO.setStoreId(storeDTO.getId());
        staffDTO = createStaff(staffDTO);

        //6.为门店设置管理员
        bindStaffToStore(storeDTO.getId(), staffDTO.getId());

        //返回商户注册信息
        return MerchantCovert.INSTANCE.entity2dto(entity);
    }

    @Override
    public MerchantDTO queryMerchantById(Long merchantId) {
        Merchant merchant = merchantMapper.selectById(merchantId);
        MerchantDTO merchantDTO = new MerchantDTO();

        BeanUtils.copyProperties(merchant, merchantDTO);

        return merchantDTO;
    }
}
