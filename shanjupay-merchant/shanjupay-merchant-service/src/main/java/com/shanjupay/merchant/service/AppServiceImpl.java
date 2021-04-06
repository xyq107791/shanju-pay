package com.shanjupay.merchant.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shanjupay.common.domain.BusinessException;
import com.shanjupay.common.domain.CommonErrorCode;
import com.shanjupay.common.util.RandomUuidUtil;
import com.shanjupay.merchant.api.AppService;
import com.shanjupay.merchant.api.dto.AppDTO;
import com.shanjupay.merchant.convert.AppCovert;
import com.shanjupay.merchant.entity.App;
import com.shanjupay.merchant.entity.Merchant;
import com.shanjupay.merchant.mapper.AppMapper;
import com.shanjupay.merchant.mapper.MerchantMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author: WeiQiuKe
 * @create: 2021-04-06
 **/

@org.apache.dubbo.config.annotation.Service
public class AppServiceImpl implements AppService {

    @Autowired
    private AppMapper appMapper;

    @Autowired
    private MerchantMapper merchantMapper;

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
