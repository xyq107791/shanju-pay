package com.shanjupay.merchant.controller;

import com.shanjupay.common.domain.PageVO;
import com.shanjupay.merchant.api.MerchantService;
import com.shanjupay.merchant.api.dto.StoreDTO;
import com.shanjupay.merchant.common.util.SecurityUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: WeiQiuKe
 * @create: 2021-04-12
 **/
@Api(value = "商户平台‐门店管理", tags = "商户平台‐门店管理", description = "商户平台‐门店的增删改 查")
@RestController
public class StoreController {

    @org.apache.dubbo.config.annotation.Reference
    MerchantService merchantService;

    @ApiOperation("分页条件查询商户下门店")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "页码", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页记录数", required = true, dataType = "int", paramType = "query")
    })
    @PostMapping("/my/stores/merchants/page")
    public PageVO<StoreDTO> queryStoreByPage(@RequestParam Integer pageNo, @RequestParam Integer pageSize) {
        //获取商户id
        Long merchantId = SecurityUtil.getMerchantId();
        StoreDTO storeDTO = new StoreDTO();
        storeDTO.setMerchantId(merchantId);
        //分页查询
        return merchantService.queryStoreByPage(storeDTO, pageNo, pageSize);
    }
}
