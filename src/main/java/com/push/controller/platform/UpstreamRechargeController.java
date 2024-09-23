package com.push.controller.platform;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.push.common.annotation.Operation;
import com.push.common.controller.BaseController;
import com.push.common.controller.ResultMapInfo;
import com.push.common.enums.*;
import com.push.common.utils.BeanUtils;
import com.push.common.utils.TokenUtil;
import com.push.common.webfacade.bo.platform.IdBO;
import com.push.common.webfacade.bo.platform.UpstreamRechargeBO;
import com.push.common.webfacade.bo.platform.UpstreamUserRechargeBO;
import com.push.common.webfacade.error.CommonException;
import com.push.common.webfacade.error.ErrorEnum;
import com.push.common.webfacade.vo.platform.CheckRechargeVO;
import com.push.common.webfacade.vo.platform.GetRechargeVO;
import com.push.entity.upstream.UpstreamUserInfo;
import com.push.entity.upstream.UpstreamUserRecharge;
import com.push.service.upstream.UpstreamUserInfoService;
import com.push.service.upstream.UpstreamUserRechargeService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Description: 渠道充值Controller
 * Create DateTime: 2020-03-30 17:16
 *
 *

 */
@RestController
@RequestMapping(value = "upstreamRecharge")
public class UpstreamRechargeController extends BaseController {

    @Resource
    private UpstreamUserRechargeService upstreamUserRechargeService;

    @Resource
    private UpstreamUserInfoService upstreamUserInfoService;

    //充值
    @Operation(userType = UserTypeCodeEnum.PLATFORM_USER, operationType = OperationTypeEnum.UPSTREAM_RECHARGE, operationDescribe = "平台发起渠道充值")
    @RequestMapping(value = "/recharge", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> recharge(@RequestBody UpstreamUserRechargeBO bo) {
        bo.validate();
        UpstreamUserRecharge upstreamUserRecharge = BeanUtils.copyPropertiesChaining(bo, UpstreamUserRecharge::new);
        UpstreamUserInfo upstreamUserInfo = upstreamUserInfoService.getById(bo.getUpstreamUserId());
        if (upstreamUserInfo.getStatus().equals(UserStatusEnum.DISABLE.getValue())) {
            return returnResultMap(ResultMapInfo.ACCOUNT_BAN);
        }
        upstreamUserRecharge.setOrderNo(TokenUtil.generateOrderNo());
        boolean save = upstreamUserRechargeService.save(upstreamUserRecharge);
        if (save) {
            return returnResultMap(ResultMapInfo.ADDSUCCESS);
        }
        return returnResultMap(ResultMapInfo.ADDFAIL);
    }

    //待处理提现记录
    @RequestMapping(value = "waitCheck", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> getUpstreamRechargeToDoList(@RequestBody UpstreamRechargeBO bo) {
        Page<CheckRechargeVO> page = upstreamUserRechargeService.getUpstreamRechargeToDoList(RechargeStatusEnum.TODO.getCode(), bo);
        if (page != null) {
            GetRechargeVO getRechargeVO = new GetRechargeVO();
            getRechargeVO.setData(page.getRecords());
            getRechargeVO.setTotal(page.getTotal());
            return returnResultMap(ResultMapInfo.GETSUCCESS, getRechargeVO);
        } else {
            return returnResultMap(ResultMapInfo.GETFAIL);
        }
    }

    //充值成功记录
    @RequestMapping(value = "listRechargeSuccess", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> getUpstreamRechargeSuccessList(@RequestBody UpstreamRechargeBO bo) {
        Page<CheckRechargeVO> page = upstreamUserRechargeService.getUpstreamRechargeToDoList(RechargeStatusEnum.SUCCESSS.getCode(), bo);
        if (page != null) {
            GetRechargeVO getRechargeVO = new GetRechargeVO();
            getRechargeVO.setData(page.getRecords());
            getRechargeVO.setTotal(page.getTotal());
            return returnResultMap(ResultMapInfo.GETSUCCESS, getRechargeVO);
        } else {
            return returnResultMap(ResultMapInfo.GETFAIL);
        }
    }

    //充值失败记录
    @RequestMapping(value = "listRechargeFailed", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> getUpstreamRechargeFailList(@RequestBody UpstreamRechargeBO bo) {
        Page<CheckRechargeVO> page = upstreamUserRechargeService.getUpstreamRechargeToDoList(RechargeStatusEnum.FAIL.getCode(), bo);
        if (page != null) {
            GetRechargeVO getRechargeVO = new GetRechargeVO();
            getRechargeVO.setData(page.getRecords());
            getRechargeVO.setTotal(page.getTotal());
            return returnResultMap(ResultMapInfo.GETSUCCESS, getRechargeVO);
        } else {
            return returnResultMap(ResultMapInfo.GETFAIL);
        }
    }

    //审核成功
    @Operation(userType = UserTypeCodeEnum.PLATFORM_USER, operationType = OperationTypeEnum.UPSTREAM_RECHARGE, operationDescribe = "渠道充值审核成功")
    @RequestMapping(value = "passCheck", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> successUpStreamUserRecharge(@RequestBody IdBO bo) {
        UpstreamUserRecharge upstreamUserRecharge = upstreamUserRechargeService.getById(bo.getId());
        if (null == upstreamUserRecharge) {
            throw new CommonException(ErrorEnum.UNKNOWN);
        }
        UpstreamUserInfo upstreamUserInfo = upstreamUserInfoService.getById(upstreamUserRecharge.getUpstreamUserId());
        if (null == upstreamUserInfo) {
            return returnResultMap(ResultMapInfo.UPSTREAM_NOT_EXIST);
        }
        if (upstreamUserInfo.getStatus().equals(UserStatusEnum.DISABLE.getValue())) {
            return returnResultMap(ResultMapInfo.ACCOUNT_BAN);
        }
        //更改提现提现订单状态，增加账单事务表记录,增加账单表
        upstreamUserRecharge.setStatus(ExtractionStatusEnum.SUCCESSS.getCode());
        upstreamUserRechargeService.operatorSuccess(upstreamUserRecharge, upstreamUserInfo);
        return returnResultMap(ResultMapInfo.ADDSUCCESS);

    }

    //审核失败
    @Operation(userType = UserTypeCodeEnum.PLATFORM_USER, operationType = OperationTypeEnum.UPSTREAM_RECHARGE, operationDescribe = "渠道充值审核失败")
    @RequestMapping(value = "noPassCheck", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> failUpstreamUserRecharge(@RequestBody IdBO bo) {
        UpstreamUserRecharge upstreamUserRecharge = upstreamUserRechargeService.getById(bo.getId());
        if (upstreamUserRecharge == null) {
            throw new CommonException(ErrorEnum.UNKNOWN);
        }
        UpstreamUserInfo upstreamUserInfo = upstreamUserInfoService.getById(upstreamUserRecharge.getUpstreamUserId());
        if (upstreamUserInfo.getStatus().equals(UserStatusEnum.DISABLE.getValue())) {
            return returnResultMap(ResultMapInfo.ACCOUNT_BAN);
        }
        upstreamUserRecharge.setStatus(ExtractionStatusEnum.FAIL.getCode());
        boolean result = upstreamUserRechargeService.failUpstreamUserRecharge(upstreamUserRecharge);
        if (!result) {
            throw new CommonException(ErrorEnum.UNKNOWN);
        }
        return returnResultMap(ResultMapInfo.ADDSUCCESS);
    }
}
