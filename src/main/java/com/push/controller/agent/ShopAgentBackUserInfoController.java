package com.push.controller.agent;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.push.common.annotation.Operation;
import com.push.common.controller.BaseController;
import com.push.common.controller.ResultMapInfo;
import com.push.common.enums.DeleteFlagEnum;
import com.push.common.enums.OperationTypeEnum;
import com.push.common.enums.UserTypeCodeEnum;
import com.push.common.utils.BeanUtils;
import com.push.common.webfacade.bo.platform.EditShopAgentUserInfoBO;
import com.push.common.webfacade.bo.shop.ModifyShopBackPasswordBO;
import com.push.common.webfacade.vo.platform.ShopAgentMapVO;
import com.push.common.webfacade.vo.platform.ShopAgentUserDetailInfoVO;
import com.push.entity.BasePageBO;
import com.push.entity.agent.ShopAgentUserInfo;
import com.push.service.agent.ShopAgentUserInfoService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * <p>
 * 商户代理商信息表 前端控制器
 * </p>
 *
 *

 * @since 2020-04-26
 */
@RestController
@RequestMapping("shopAgentBack")
public class ShopAgentBackUserInfoController extends BaseController {

    @Resource
    private ShopAgentUserInfoService shopAgentUserInfoService;


    //修改商户代理商
    @Operation(userType = UserTypeCodeEnum.SHOP_AGENT_USER, operationType = OperationTypeEnum.SHOP_AGENT_USER_INFO, operationDescribe = "修改商户代理商")
    @RequestMapping(value = "/modifyShopAgent", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> modifyShopAgent(@RequestBody EditShopAgentUserInfoBO bo) {
        bo.validate();
        bo.setId(getUserId());
        ShopAgentUserInfo shopAgentUserInfo = BeanUtils.copyPropertiesChaining(bo, ShopAgentUserInfo::new);
        boolean update = shopAgentUserInfoService.updateById(shopAgentUserInfo);
        if (update) {
            return returnResultMap(ResultMapInfo.ADDSUCCESS);
        }
        return returnResultMap(ResultMapInfo.ADDFAIL);
    }

    //查询被代理商户
    @RequestMapping(value = "/shopAgentMap", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> shopAgentMap(@RequestBody BasePageBO bo) {
        bo.validate();
        Page<ShopAgentMapVO> page = new Page<>(bo.getPageNo(),bo.getPageSize());
        //被代理商户列表
        Page<ShopAgentMapVO> shopAgentMapVoPage=shopAgentUserInfoService.getShopAgentMap(page,getUserId());
        return returnResultMap(ResultMapInfo.GETSUCCESS, shopAgentMapVoPage);
    }

    //商户代理商详情
    @PostMapping("/detailShopAgent")
    public Map<String, Object> detailUpstream() {
        ShopAgentUserInfo userInfo = shopAgentUserInfoService.getById(getUserId());
        if (userInfo == null || DeleteFlagEnum.DELETE.getCode() == userInfo.getDeleteFlag()) {
            return returnResultMap(ResultMapInfo.GETFAIL);
        }
        ShopAgentUserDetailInfoVO shopAgentUserDetailInfoVO = BeanUtils.copyPropertiesChaining(userInfo, ShopAgentUserDetailInfoVO::new);
        return returnResultMap(ResultMapInfo.GETSUCCESS, shopAgentUserDetailInfoVO);
    }

    @Operation(userType = UserTypeCodeEnum.SHOP_AGENT_USER, operationType = OperationTypeEnum.SHOP_AGENT_USER_INFO, operationDescribe = "修改商户代理商密码")
    @PostMapping("modifyPwd")
    public Map<String, Object> modifyPwd(@RequestBody ModifyShopBackPasswordBO bo) {
        ShopAgentUserInfo userInfo = new ShopAgentUserInfo();
        userInfo.setId(getUserId());
        userInfo.setPassword(bo.getNewPwd());
        boolean update = shopAgentUserInfoService.updateById(userInfo);
        if (update) {
            return returnResultMap(ResultMapInfo.ADDSUCCESS);
        }
        return returnResultMap(ResultMapInfo.ADDFAIL);
    }

}
