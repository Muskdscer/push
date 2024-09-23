package com.push.controller.platform;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.push.common.annotation.Operation;
import com.push.common.controller.BaseController;
import com.push.common.controller.ResultMapInfo;
import com.push.common.enums.DeleteFlagEnum;
import com.push.common.enums.OperationTypeEnum;
import com.push.common.enums.UserTypeCodeEnum;
import com.push.common.utils.BeanUtils;
import com.push.common.webfacade.bo.platform.IdBO;
import com.push.common.webfacade.bo.platform.ShopMatchClassifyBO;
import com.push.common.webfacade.error.CommonException;
import com.push.common.webfacade.error.ErrorEnum;
import com.push.common.webfacade.vo.platform.ShopMatchClassifyVO;
import com.push.entity.BasePageBO;
import com.push.entity.platform.ShopMatchClassify;
import com.push.service.platform.ShopMatchClassifyService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Description:
 * Create DateTime: 2020/4/20 16:22
 *
 * 

 */
@RestController
@RequestMapping("shopMatchClassify")
public class ShopMatchClassifyController extends BaseController {

    @Resource
    private ShopMatchClassifyService shopMatchClassifyService;

    @Operation(userType = UserTypeCodeEnum.PLATFORM_USER, operationType = OperationTypeEnum.PLATFORM_MATCH_CLASSIFY, operationDescribe = "添加配比分类")
    @RequestMapping(value = "addShopMatchClassify", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> addShopMatchClassify(@RequestBody ShopMatchClassifyBO bo) {
        bo.validate();
        boolean save = shopMatchClassifyService.save(BeanUtils.copyPropertiesChaining(bo, ShopMatchClassify::new));
        return save ? returnResultMap(ResultMapInfo.ADDSUCCESS) : returnResultMap(ResultMapInfo.ADDFAIL);
    }

    @Operation(userType = UserTypeCodeEnum.PLATFORM_USER, operationType = OperationTypeEnum.PLATFORM_MATCH_CLASSIFY, operationDescribe = "删除配比分类")
    @RequestMapping(value = "deleteShopMatchClassify", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> deleteShopMatchClassify(@RequestBody IdBO bo) {
        bo.validate();
        ShopMatchClassify shopMatchClassify = new ShopMatchClassify();
        shopMatchClassify.setId(bo.getId());
        shopMatchClassify.setDeleteFlag(DeleteFlagEnum.DELETE.getCode());
        boolean update = shopMatchClassifyService.updateById(shopMatchClassify);
        return update ? returnResultMap(ResultMapInfo.ADDSUCCESS) : returnResultMap(ResultMapInfo.ADDFAIL);
    }

    @Operation(userType = UserTypeCodeEnum.PLATFORM_USER, operationType = OperationTypeEnum.PLATFORM_MATCH_CLASSIFY, operationDescribe = "修改配比分类")
    @RequestMapping(value = "updateShopMatchClassify", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> updateShopMatchClassify(@RequestBody ShopMatchClassifyBO bo) {
        bo.validate();
        if (bo.getId() == null) throw new CommonException(ErrorEnum.REQUIRED_PARAM_EMPTY);
        boolean update = shopMatchClassifyService.updateById(BeanUtils.copyPropertiesChaining(bo, ShopMatchClassify::new));
        return update ? returnResultMap(ResultMapInfo.ADDSUCCESS) : returnResultMap(ResultMapInfo.ADDFAIL);
    }

    @RequestMapping(value = "getShopMatchClassify", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> getShopMatchClassify(@RequestBody IdBO bo) {
        bo.validate();
        ShopMatchClassify shopMatchClassify = shopMatchClassifyService.getOne(Wrappers.lambdaQuery(ShopMatchClassify.class).eq(ShopMatchClassify::getId, bo.getId()).eq(ShopMatchClassify::getDeleteFlag, DeleteFlagEnum.NOT_DELETE.getCode()));
        if (shopMatchClassify == null) return returnResultMap(ResultMapInfo.GETFAIL);
        ShopMatchClassify matchClassify = BeanUtils.copyPropertiesChaining(shopMatchClassify, ShopMatchClassify::new);
        return returnResultMap(ResultMapInfo.GETSUCCESS, matchClassify);
    }

    @RequestMapping(value = "listShopMatchClassify", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> listShopMatchClassify(@RequestBody BasePageBO bo) {
        bo.validate();
        Page<ShopMatchClassify> shopMatchClassifyPage = shopMatchClassifyService.page(new Page<>(bo.getPageNo(), bo.getPageSize()),
                Wrappers.lambdaQuery(ShopMatchClassify.class)
                        .eq(ShopMatchClassify::getDeleteFlag, DeleteFlagEnum.NOT_DELETE.getCode())
                        .orderByDesc(ShopMatchClassify::getCreateTime));
        List<ShopMatchClassifyVO> collect = shopMatchClassifyPage
                .getRecords()
                .stream()
                .map(s -> BeanUtils.copyPropertiesChaining(s, ShopMatchClassifyVO::new))
                .collect(Collectors.toList());
        Map<String, Object> map = new HashMap<>();
        map.put("total", shopMatchClassifyPage.getTotal());
        map.put("list", collect);
        return returnResultMap(ResultMapInfo.GETSUCCESS, map);
    }

    @RequestMapping(value = "resultShopMatchClassify", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> resultShopMatchClassify() {
        List<ShopMatchClassify> list = shopMatchClassifyService.list(Wrappers.lambdaQuery(ShopMatchClassify.class).eq(ShopMatchClassify::getDeleteFlag, DeleteFlagEnum.NOT_DELETE.getCode()));
        return returnResultMap(ResultMapInfo.GETSUCCESS, list);
    }
}
