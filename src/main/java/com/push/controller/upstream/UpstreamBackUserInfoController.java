package com.push.controller.upstream;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.push.common.annotation.Operation;
import com.push.common.controller.BaseController;
import com.push.common.controller.ResultMapInfo;
import com.push.common.enums.DeleteFlagEnum;
import com.push.common.enums.OperationTypeEnum;
import com.push.common.enums.UserTypeCodeEnum;
import com.push.common.utils.BeanUtils;
import com.push.common.webfacade.bo.upstream.ModifyUpstreamBackPasswordBO;
import com.push.common.webfacade.bo.upstream.ModifyUpstreamBackUserInfoBO;
import com.push.common.webfacade.vo.platform.UpstreamUserInfoVO;
import com.push.entity.upstream.UpstreamUserInfo;
import com.push.service.upstream.UpstreamUserInfoService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Description:
 * Create DateTime: 2020/3/27 18:47
 *
 * 

 */
@RestController
@RequestMapping("back/upstream")
public class UpstreamBackUserInfoController extends BaseController {

    @Resource
    private UpstreamUserInfoService upstreamUserInfoService;

    //修改上游用户
    @Operation(userType = UserTypeCodeEnum.UPSTREAM_USER, operationType = OperationTypeEnum.UPSTREAM_USER_INFO, operationDescribe = "修改渠道用户")
    @RequestMapping(value = "modifyUpstream", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> modifyUpstream(@RequestBody ModifyUpstreamBackUserInfoBO bo) {
        bo.validate();
        UpstreamUserInfo upstreamUser = BeanUtils.copyPropertiesChaining(bo, UpstreamUserInfo::new);
        upstreamUser.setId(getUserId());
        boolean update = upstreamUserInfoService.updateById(upstreamUser);
        if (update) {
            return returnResultMap(ResultMapInfo.ADDSUCCESS);
        }
        return returnResultMap(ResultMapInfo.ADDFAIL);
    }

    //查询详情
    @RequestMapping(value = "detailUpstream", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> detailUpstream() {
        Long userId = getUserId();
        LambdaQueryWrapper<UpstreamUserInfo> queryWrapper = new LambdaQueryWrapper<>(UpstreamUserInfo.class);
        queryWrapper.eq(UpstreamUserInfo::getId, userId).eq(UpstreamUserInfo::getDeleteFlag, DeleteFlagEnum.NOT_DELETE.getCode());
        UpstreamUserInfo upstreamUser = upstreamUserInfoService.getOne(queryWrapper);
        if (upstreamUser != null) {
            return returnResultMap(ResultMapInfo.GETSUCCESS, BeanUtils.copyPropertiesChaining(upstreamUser, UpstreamUserInfoVO::new));
        }
        return returnResultMap(ResultMapInfo.GETFAIL);
    }

    @Operation(userType = UserTypeCodeEnum.UPSTREAM_USER, operationType = OperationTypeEnum.UPSTREAM_USER_INFO, operationDescribe = "修改渠道密码")
    @RequestMapping(value = "modifyPwd", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> modifyPwd(@RequestBody ModifyUpstreamBackPasswordBO bo) {
        UpstreamUserInfo upstreamUserInfo = new UpstreamUserInfo();
        upstreamUserInfo.setId(getUserId());
        upstreamUserInfo.setPassword(bo.getPassword());
        boolean update = upstreamUserInfoService.updateById(upstreamUserInfo);
        if (update) {
            return returnResultMap(ResultMapInfo.ADDSUCCESS);
        }
        return returnResultMap(ResultMapInfo.ADDFAIL);
    }
}
