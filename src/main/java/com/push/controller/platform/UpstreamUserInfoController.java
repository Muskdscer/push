package com.push.controller.platform;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.push.common.annotation.Operation;
import com.push.common.controller.BaseController;
import com.push.common.controller.ResultMapInfo;
import com.push.common.enums.DeleteFlagEnum;
import com.push.common.enums.OperationTypeEnum;
import com.push.common.enums.UserTypeCodeEnum;
import com.push.common.utils.BeanUtils;
import com.push.common.webfacade.bo.platform.IdBO;
import com.push.common.webfacade.bo.platform.ModifyUpstreamUserInfoBO;
import com.push.common.webfacade.bo.platform.SaveUpstreamUserInfoBO;
import com.push.common.webfacade.bo.platform.UpstreamUserPageBO;
import com.push.common.webfacade.error.CommonException;
import com.push.common.webfacade.error.ErrorEnum;
import com.push.common.webfacade.vo.platform.ListUpstreamUserInfoVO;
import com.push.common.webfacade.vo.platform.UpstreamUserInfoVO;
import com.push.entity.upstream.UpstreamUserInfo;
import com.push.service.upstream.UpstreamUserInfoService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * Create DateTime: 2020/3/27 18:47
 *
 * 

 */
@RestController
@RequestMapping("upstream")
public class UpstreamUserInfoController extends BaseController {

    @Resource
    private UpstreamUserInfoService upstreamUserInfoService;

    //添加上游用户
    @Operation(userType = UserTypeCodeEnum.PLATFORM_USER, operationType = OperationTypeEnum.UPSTREAM_USER_INFO, operationDescribe = "添加渠道用户")
    @RequestMapping(value = "/addUpstream", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> addUpstream(@RequestBody SaveUpstreamUserInfoBO bo) {
        bo.validate();
        UpstreamUserInfo userInfo = upstreamUserInfoService.getOne(Wrappers.lambdaQuery(UpstreamUserInfo.class).eq(UpstreamUserInfo::getUserName, bo.getUserName()));
        if (userInfo != null) {
            return returnResultMap(ResultMapInfo.USERNAME_HAD_EXIST);
        }
        UpstreamUserInfo upstreamUser = BeanUtils.copyPropertiesChaining(bo, UpstreamUserInfo::new);
        upstreamUser.setAppId(RandomUtil.randomString(32));
        upstreamUser.setAppKey(RandomUtil.randomString(32));
        boolean saveUser = upstreamUserInfoService.save(upstreamUser);
        if (saveUser) {
            return returnResultMap(ResultMapInfo.ADDSUCCESS);
        }
        return returnResultMap(ResultMapInfo.ADDFAIL);
    }

    //修改上游用户
    @Operation(userType = UserTypeCodeEnum.PLATFORM_USER, operationType = OperationTypeEnum.UPSTREAM_USER_INFO, operationDescribe = "修改渠道用户")
    @RequestMapping(value = "/modifyUpstream", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> modifyUpstream(@RequestBody ModifyUpstreamUserInfoBO bo) {
        bo.validate();
        if (bo.getId() == null) {
            throw new CommonException(ErrorEnum.REQUIRED_PARAM_EMPTY);
        }

        //根据appId查询用户，如果查询出来的用户id不等于此用户id则此appId已存在，且如果appId如果未作修改便不会拦截
        /*UpstreamUserInfo userInfo = upstreamUserInfoService.getOne(Wrappers.lambdaQuery(UpstreamUserInfo.class)
                .eq(UpstreamUserInfo::getAppId, bo.getAppId())
                .eq(UpstreamUserInfo::getDeleteFlag,DeleteFlagEnum.NOT_DELETE.getCode()));*/

        /*if (userInfo != null && !userInfo.getId().equals(bo.getId())) {
            return returnResultMap(ResultMapInfo.APPID_HAD_EXIST);
        }*/
        UpstreamUserInfo upstreamUser = BeanUtils.copyPropertiesChaining(bo, UpstreamUserInfo::new);
        boolean update = upstreamUserInfoService.updateById(upstreamUser);
        if (update) {
            return returnResultMap(ResultMapInfo.ADDSUCCESS);
        }
        return returnResultMap(ResultMapInfo.ADDFAIL);
    }

    //删除上游用户
    @Operation(userType = UserTypeCodeEnum.PLATFORM_USER, operationType = OperationTypeEnum.UPSTREAM_USER_INFO, operationDescribe = "删除渠道用户")
    @RequestMapping(value = "/removeUpstream", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> removeUpstream(@RequestBody IdBO bo) {
        bo.validate();
        UpstreamUserInfo info = new UpstreamUserInfo();
        info.setId(bo.getId());
        info.setDeleteFlag(DeleteFlagEnum.DELETE.getCode());
        boolean delete = upstreamUserInfoService.updateById(info);
        if (delete) {
            return returnResultMap(ResultMapInfo.ADDSUCCESS);
        }
        return returnResultMap(ResultMapInfo.ADDFAIL);
    }

    //查询详情
    @RequestMapping(value = "/detailUpstream", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> detailUpstream(@RequestBody IdBO bo) {
        bo.validate();
        UpstreamUserInfoVO upstreamUser = upstreamUserInfoService.getOneWithMatchClassify(bo.getId(), DeleteFlagEnum.NOT_DELETE.getCode());
        if (upstreamUser != null) {
            return returnResultMap(ResultMapInfo.GETSUCCESS, BeanUtils.copyPropertiesChaining(upstreamUser, UpstreamUserInfoVO::new));
        }
        return returnResultMap(ResultMapInfo.GETFAIL);
    }

    //查询加分页
    @RequestMapping(value = "/listUpstream", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> listUpstream(@RequestBody UpstreamUserPageBO bo) {
        bo.validate();
        IPage<UpstreamUserInfoVO> vos = upstreamUserInfoService.listUpstream(bo);
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("list", vos.getRecords());
        returnMap.put("total", vos.getTotal());
        return returnResultMap(ResultMapInfo.GETSUCCESS, returnMap);
    }

    //查询所有
    @RequestMapping(value = "/listAllUpstream", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> listAllUpstream() {
        List<ListUpstreamUserInfoVO> list = upstreamUserInfoService.listAllUpstream(DeleteFlagEnum.NOT_DELETE.getCode());
        return returnResultMap(ResultMapInfo.GETSUCCESS, list);
    }

}
