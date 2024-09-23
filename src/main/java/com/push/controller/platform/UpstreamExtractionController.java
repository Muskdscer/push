package com.push.controller.platform;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.push.common.annotation.Operation;
import com.push.common.constants.ParamConstants;
import com.push.common.controller.BaseController;
import com.push.common.controller.ResultMapInfo;
import com.push.common.enums.ExtractionStatusEnum;
import com.push.common.enums.OperationTypeEnum;
import com.push.common.enums.UserStatusEnum;
import com.push.common.enums.UserTypeCodeEnum;
import com.push.common.utils.BeanUtils;
import com.push.common.utils.BigDecimalUtil;
import com.push.common.utils.DateUtil;
import com.push.common.utils.TokenUtil;
import com.push.common.webfacade.bo.platform.IdBO;
import com.push.common.webfacade.bo.platform.UpstreamExtractionBO;
import com.push.common.webfacade.bo.platform.UpstreamUserExtractionBO;
import com.push.common.webfacade.error.CommonException;
import com.push.common.webfacade.error.ErrorEnum;
import com.push.common.webfacade.vo.platform.GetExtractionVO;
import com.push.common.webfacade.vo.platform.UpstreamCheckExtractionVO;
import com.push.config.system.SystemConfig;
import com.push.entity.upstream.UpstreamExtraction;
import com.push.entity.upstream.UpstreamUserInfo;
import com.push.service.upstream.UpstreamExtractionService;
import com.push.service.upstream.UpstreamUserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * Description:
 * Create DateTime: 2020/3/30 9:05
 *
 *

 */
@RestController
@Slf4j
@RequestMapping(value = "upstreamExtraction")
public class UpstreamExtractionController extends BaseController {

    @Resource
    UpstreamExtractionService upstreamExtractionService;

    @Resource
    private UpstreamUserInfoService upstreamUserInfoService;

    //待处理提现记录
    @RequestMapping(value = "waitCheck", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> getUpstreamExtractionToDoList(@RequestBody UpstreamExtractionBO bo) {
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

    //提现
    @Operation(userType = UserTypeCodeEnum.PLATFORM_USER, operationType = OperationTypeEnum.UPSTREAM_EXTRACTION, operationDescribe = "平台发起渠道提现")
    @RequestMapping(value = "/extraction", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> extraction(@RequestBody UpstreamUserExtractionBO bo) {
        bo.validate();
        if (bo.getExtractionMoney().compareTo(new BigDecimal(0)) <= 0) {
            return returnResultMap(ResultMapInfo.MINEXTRACTIONFAIL);
        }
        UpstreamExtraction extraction = BeanUtils.copyPropertiesChaining(bo, UpstreamExtraction::new);
        extraction.setExtractionNumber(TokenUtil.generateOrderNo());
        //判断余额是否充足
        UpstreamUserInfo upstreamUserInfo = upstreamUserInfoService.getById(bo.getUpstreamUserId());
        if (upstreamUserInfo.getStatus().equals(UserStatusEnum.DISABLE.getValue())) {
            return returnResultMap(ResultMapInfo.ACCOUNT_BAN);
        }
        if (extraction.getExtractionMoney().compareTo(upstreamUserInfo.getBalance().subtract(upstreamUserInfo.getFrozenMoney())) > 0) {
            //余额不足的情况
            return returnResultMap(ResultMapInfo.BALANCEDEFICIENCY);
        }
        BigDecimal extractionServiceCharge = new BigDecimal(Objects.requireNonNull(SystemConfig
                .getParam(ParamConstants.EXTRACTION_SERVICE_CHARGE)));
        if (BigDecimalUtil.isMax(extractionServiceCharge, upstreamUserInfo.getFrozenMoney())) {
            //提现金额小于冻结金额
            return returnResultMap(ResultMapInfo.EXTRACTIONMONEYLESSSERVICE);
        }
        //更改上游用户表冻结金额
        upstreamUserInfo.setFrozenMoney(upstreamUserInfo.getFrozenMoney().add(extraction.getExtractionMoney()));
        //向提现表插入记录，并更新用户表冻结金额
        boolean save = upstreamExtractionService.saveExtractionAndUpdateUserInfo(extraction, upstreamUserInfo);
        if (save) {
            return returnResultMap(ResultMapInfo.ADDSUCCESS);
        }
        return returnResultMap(ResultMapInfo.ADDFAIL);
    }

    //审核成功
    @Operation(userType = UserTypeCodeEnum.PLATFORM_USER, operationType = OperationTypeEnum.UPSTREAM_EXTRACTION, operationDescribe = "渠道提现审核成功")
    @RequestMapping(value = "passCheck", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> successUpStreamUserExtraction(@RequestBody IdBO bo) {
        UpstreamExtraction upstreamExtraction = upstreamExtractionService.getById(bo.getId());
        if (null == upstreamExtraction) {
            throw new CommonException(ErrorEnum.UNKNOWN);
        }
        UpstreamUserInfo upstreamUserInfo = upstreamUserInfoService.getById(upstreamExtraction.getUpstreamUserId());
        if (null == upstreamUserInfo) {
            return returnResultMap(ResultMapInfo.UPSTREAM_NOT_EXIST);
        }
        if (upstreamUserInfo.getStatus().equals(UserStatusEnum.DISABLE.getValue())) {
            return returnResultMap(ResultMapInfo.ACCOUNT_BAN);
        }
        //判断余额是否充足
        if (upstreamUserInfo.getBalance().compareTo(upstreamUserInfo.getFrozenMoney()) < 0) {
            //余额不足的情况
            return returnResultMap(ResultMapInfo.BALANCEDEFICIENCY);
        } else {
            //更改提现提现订单状态，增加账单事务表记录,增加账单表
            upstreamExtraction.setStatus(ExtractionStatusEnum.SUCCESSS.getCode());
            upstreamExtractionService.operatorSuccess(upstreamExtraction, upstreamUserInfo);
            return returnResultMap(ResultMapInfo.ADDSUCCESS);
        }
    }

    //审核失败
    @Operation(userType = UserTypeCodeEnum.PLATFORM_USER, operationType = OperationTypeEnum.UPSTREAM_EXTRACTION, operationDescribe = "渠道提现审核失败")
    @RequestMapping(value = "noPassCheck", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> failUpstreamUserExtraction(@RequestBody IdBO bo) {
        UpstreamExtraction upstreamExtraction = upstreamExtractionService.getById(bo.getId());
        if (upstreamExtraction == null) {
            throw new CommonException(ErrorEnum.UNKNOWN);
        }
        UpstreamUserInfo upstreamUserInfo = upstreamUserInfoService.getById(upstreamExtraction.getUpstreamUserId());
        if (upstreamUserInfo.getStatus().equals(UserStatusEnum.DISABLE.getValue())) {
            return returnResultMap(ResultMapInfo.ACCOUNT_BAN);
        }
        upstreamExtraction.setStatus(ExtractionStatusEnum.FAIL.getCode());
        boolean result = upstreamExtractionService.failUpstreamUserExtraction(upstreamExtraction);
        if (!result) {
            log.error("渠道审核失败{}", DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
            throw new CommonException(ErrorEnum.UNKNOWN);
        }
        return returnResultMap(ResultMapInfo.ADDSUCCESS);
    }
}










