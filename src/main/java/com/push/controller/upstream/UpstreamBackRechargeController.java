package com.push.controller.upstream;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.push.common.annotation.Operation;
import com.push.common.controller.BaseController;
import com.push.common.controller.ResultMapInfo;
import com.push.common.enums.OperationTypeEnum;
import com.push.common.enums.RechargeStatusEnum;
import com.push.common.enums.UserTypeCodeEnum;
import com.push.common.utils.BeanUtils;
import com.push.common.utils.TokenUtil;
import com.push.common.webfacade.bo.platform.UpstreamRechargeBO;
import com.push.common.webfacade.bo.upstream.UpstreamBackUserRechargeBO;
import com.push.common.webfacade.vo.platform.CheckRechargeVO;
import com.push.common.webfacade.vo.platform.GetRechargeVO;
import com.push.entity.upstream.UpstreamUserRecharge;
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
@RequestMapping("back/upstreamRecharge")
public class UpstreamBackRechargeController extends BaseController {

    @Resource
    private UpstreamUserRechargeService upstreamUserRechargeService;

    //充值
    @Operation(userType = UserTypeCodeEnum.UPSTREAM_USER, operationType = OperationTypeEnum.UPSTREAM_RECHARGE, operationDescribe = "渠道发起充值")
    @RequestMapping(value = "/recharge", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> recharge(@RequestBody UpstreamBackUserRechargeBO bo) {
        bo.validate();
        bo.setUpstreamUserId(getUserId());
        UpstreamUserRecharge upstreamUserRecharge = BeanUtils.copyPropertiesChaining(bo, UpstreamUserRecharge::new);
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
        bo.setUpstreamUserId(getUserId());
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
        bo.setUpstreamUserId(getUserId());
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
        bo.setUpstreamUserId(getUserId());
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
}
