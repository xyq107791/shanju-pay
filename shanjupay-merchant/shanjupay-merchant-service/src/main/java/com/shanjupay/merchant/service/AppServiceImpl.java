package com.shanjupay.merchant.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shanjupay.common.domain.BusinessException;
import com.shanjupay.common.domain.CommonErrorCode;
import com.shanjupay.common.util.RandomUuidUtil;
import com.shanjupay.merchant.api.AppService;
import com.shanjupay.merchant.api.dto.AppDTO;
import com.shanjupay.merchant.api.dto.StaffDTO;
import com.shanjupay.merchant.convert.AppCovert;
import com.shanjupay.merchant.convert.StaffConvert;
import com.shanjupay.merchant.entity.App;
import com.shanjupay.merchant.entity.Merchant;
import com.shanjupay.merchant.entity.Staff;
import com.shanjupay.merchant.mapper.AppMapper;
import com.shanjupay.merchant.mapper.MerchantMapper;
import com.shanjupay.merchant.mapper.StaffMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author: WeiQiuKe
 * @create: 2021-04-06
 **/

@org.apache.dubbo.config.annotation.Service
@Slf4j
public class AppServiceImpl implements AppService {

    @Autowired
    private AppMapper appMapper;
    @Autowired
    private MerchantMapper merchantMapper;
    @Autowired
    private StaffMapper staffMapper;

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
        //2.校验用户名是否为空
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

    /**
     * 根据账号判断员工是否已在指定商户存在
     * @param username
     * @param merchantId
     * @return
     */
    private boolean isExistStaffByUserName(String username, Long merchantId) {
        Staff staff = staffMapper.selectOne(new LambdaQueryWrapper<Staff>().eq(Staff::getMerchantId, merchantId)
                .eq(Staff::getUsername, username));
        return staff != null;
    }

    /**
     * 根据手机号判断员工是否已在指定商户存在
     * @param mobile 手机号
     * @param merchantId
     * @return
     */
    private boolean isExistStaffByMobile(String mobile, Long merchantId) {
        Staff staff = staffMapper.selectOne(new LambdaQueryWrapper<Staff>().eq(Staff::getMerchantId, merchantId)
                .eq(Staff::getMobile, mobile));
        return staff != null;
    }

    @Override
    public AppDTO getAppById(String id) {
        App app = appMapper.selectOne(new LambdaQueryWrapper<App>().eq(App::getAppId, id));
        return AppCovert.INSTANCE.entity2dto(app);
    }

    @Override
    public List<AppDTO> queryAppByMerchant(Long merchantId) {
        List<App> apps = appMapper.selectList(new LambdaQueryWrapper<App>()
                .eq(App::getMerchantId, merchantId));
        List<AppDTO> appDTOS = AppCovert.INSTANCE.listentity2dto(apps);
        return appDTOS;
    }

    @Override
    public AppDTO createApp(Long merchantId, AppDTO app) {
        //校验商户是否用过资质审核
        Merchant merchant = merchantMapper.selectById(merchantId);
        if (merchant == null) {
            throw new BusinessException(CommonErrorCode.E_200002);
        }
        if (!"2".equals(merchant.getAuditStatus())) {
            throw new BusinessException(CommonErrorCode.E_200004);
        }

        //保存应用信息
        app.setAppId(RandomUuidUtil.getUUID());
        app.setMerchantId(merchant.getId());
        App entity = AppCovert.INSTANCE.dto2entity(app);
        appMapper.insert(entity);

        return AppCovert.INSTANCE.entity2dto(entity);
    }

    public Boolean isExistAppName(String appName) {
        Integer count = appMapper.selectCount(new LambdaQueryWrapper<App>()
                .eq(App::getAppName, appName));
        return count > 0;
    }
}
