package com.push.controller.upstream;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.push.common.annotation.Operation;
import com.push.common.controller.BaseController;
import com.push.common.controller.ResultMapInfo;
import com.push.common.enums.ExtractionStatusEnum;
import com.push.common.enums.OperationTypeEnum;
import com.push.common.enums.UserTypeCodeEnum;
import com.push.common.utils.BeanUtils;
import com.push.common.utils.TokenUtil;
import com.push.common.webfacade.bo.platform.UpstreamExtractionBO;
import com.push.common.webfacade.bo.upstream.UpstreamBackUserExtractionBO;
import com.push.common.webfacade.vo.platform.GetExtractionVO;
import com.push.common.webfacade.vo.platform.UpstreamCheckExtractionVO;
import com.push.entity.upstream.UpstreamExtraction;
import com.push.entity.upstream.UpstreamUserInfo;
import com.push.service.upstream.UpstreamExtractionService;
import com.push.service.upstream.UpstreamUserInfoService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Description:
 * Create DateTime: 2020-04-07 16:18
 *
 *

 */
@RestController
@RequestMapping("back/upstreamExtraction")
public class UpstreamBackExtractionController extends BaseController {

    @Resource
    private UpstreamExtractionService upstreamExtractionService;

    @Resource
    private UpstreamUserInfoService upstreamUserInfoService;

    //提现
    @Operation(userType = UserTypeCodeEnum.UPSTREAM_USER, operationType = OperationTypeEnum.UPSTREAM_EXTRACTION, operationDescribe = "平台发起渠道提现")
    @RequestMapping(value = "/extraction", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> extraction(@RequestBody UpstreamBackUserExtractionBO bo) {
        bo.validate();
        bo.setUpstreamUserId(getUserId());
        UpstreamExtraction extraction = BeanUtils.copyPropertiesChaining(bo, UpstreamExtraction::new);
        extraction.setExtractionNumber(TokenUtil.generateOrderNo());
        //判断余额是否充足
        UpstreamUserInfo upstreamUserInfo = upstreamUserInfoService.getById(bo.getUpstreamUserId());
        if (extraction.getExtractionMoney().compareTo(upstreamUserInfo.getBalance().subtract(upstreamUserInfo.getFrozenMoney())) == 1) {
            //余额不足的情况
            return returnResultMap(ResultMapInfo.BALANCEDEFICIENCY);
        }
        extraction.setUserMobile(upstreamUserInfo.getPhoneNumber());
        //更改上游用户表冻结金额
        upstreamUserInfo.setFrozenMoney(upstreamUserInfo.getFrozenMoney().add(extraction.getExtractionMoney()));
        extraction.setUserMobile(upstreamUserInfo.getPhoneNumber());
        //向提现表插入记录，并更新用户表冻结金额
        boolean save = upstreamExtractionService.saveExtractionAndUpdateUserInfo(extraction, upstreamUserInfo);
        if (save) {
            return returnResultMap(ResultMapInfo.ADDSUCCESS);
        }
        return returnResultMap(ResultMapInfo.ADDFAIL);
    }

    //待处理提现记录
    @RequestMapping(value = "waitCheck", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> getUpstreamExtractionToDoList(@RequestBody UpstreamExtractionBO bo) {
        bo.setUpstreamUserId(getUserId());
        Page<UpstreamCheckExtractionVO> page = upstreamExtractionService.getUpstreamExtractionToDoList(ExtractionStatusEnum.TODO.getCode(), bo);
        if (page != null) {
            GetExtractionVO getExtractionVO = new GetExtractionVO();
            getExtractionVO.setData(page.getRecords());
            getExtractionVO.setTotal(page.getTotal());
            return returnResultMap(ResultMapInfo.GETSUCCESS, getExtractionVO);
        } else {
            return returnResultMap(ResultMapInfo.GETFAIL);
        }
    }

    //提现成功记录
    @RequestMapping(value = "listExtractionSuccess", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> getUpstreamExtractionSuccessList(@RequestBody UpstreamExtractionBO bo) {
        bo.setUpstreamUserId(getUserId());
        Page<UpstreamCheckExtractionVO> page = upstreamExtractionService.getUpstreamExtractionToDoList(ExtractionStatusEnum.SUCCESSS.getCode(), bo);
        if (page != null) {
            GetExtractionVO getExtractionVO = new GetExtractionVO();
            getExtractionVO.setData(page.getRecords());
            getExtractionVO.setTotal(page.getTotal());
            return returnResultMap(ResultMapInfo.GETSUCCESS, getExtractionVO);
        } else {
            return returnResultMap(ResultMapInfo.GETFAIL);
        }
    }

    //提现失败记录
    @RequestMapping(value = "listExtractionFailed", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> getUpstreamExtractionFailList(@RequestBody UpstreamExtractionBO bo) {
        bo.setUpstreamUserId(getUserId());
        Page<UpstreamCheckExtractionVO> page = upstreamExtractionService.getUpstreamExtractionToDoList(ExtractionStatusEnum.FAIL.getCode(), bo);
        if (page != null) {
            GetExtractionVO getExtractionVO = new GetExtractionVO();
            getExtractionVO.setData(page.getRecords());
            getExtractionVO.setTotal(page.getTotal());
            return returnResultMap(ResultMapInfo.GETSUCCESS, getExtractionVO);
        } else {
            return returnResultMap(ResultMapInfo.GETFAIL);
        }
    }

}
