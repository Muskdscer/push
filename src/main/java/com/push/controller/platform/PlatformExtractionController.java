package com.push.controller.platform;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.push.common.annotation.Operation;
import com.push.common.controller.BaseController;
import com.push.common.controller.ResultMapInfo;
import com.push.common.enums.ExtractionStatusEnum;
import com.push.common.enums.OperationTypeEnum;
import com.push.common.enums.UserTypeCodeEnum;
import com.push.common.utils.BeanUtils;
import com.push.common.utils.DateUtil;
import com.push.common.utils.TokenUtil;
import com.push.common.webfacade.bo.platform.IdBO;
import com.push.common.webfacade.bo.platform.PlatformExtractionBO;
import com.push.common.webfacade.bo.platform.PlatformUserExtractionBO;
import com.push.common.webfacade.error.CommonException;
import com.push.common.webfacade.error.ErrorEnum;
import com.push.common.webfacade.vo.platform.GetExtractionVO;
import com.push.common.webfacade.vo.platform.PlatformExtractionVO;
import com.push.entity.platform.PlatformBalance;
import com.push.entity.platform.PlatformExtraction;
import com.push.service.platform.PlatformBalanceService;
import com.push.service.platform.PlatformExtractionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * Description:
 * Create DateTime: 2020/3/30 18:42
 *
 *

 */
@RestController
@Slf4j
@RequestMapping("platformExtraction")
public class PlatformExtractionController extends BaseController {
    @Resource
    PlatformExtractionService platformExtractionService;

    @Resource
    PlatformBalanceService platformBalanceService;


    //待处理提现记录
    @RequestMapping(value = "waitCheck", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> getPlatformExtractionToDoList(@RequestBody PlatformExtractionBO bo) {
        Page<PlatformExtractionVO> page = platformExtractionService.getPlatformExtractionToDoList(ExtractionStatusEnum.TODO.getCode(), bo);
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
    @RequestMapping(value = "listCheckSuccess", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> getPlatformExtractionSuccessList(@RequestBody PlatformExtractionBO bo) {
        Page<PlatformExtractionVO> page = platformExtractionService.getPlatformExtractionToDoList(ExtractionStatusEnum.SUCCESSS.getCode(), bo);
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
    @RequestMapping(value = "listCheckFailed", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> getPlatformExtractionFailList(@RequestBody PlatformExtractionBO bo) {
        Page<PlatformExtractionVO> page = platformExtractionService.getPlatformExtractionToDoList(ExtractionStatusEnum.FAIL.getCode(), bo);
        if (page != null) {
            GetExtractionVO getExtractionVO = new GetExtractionVO();
            getExtractionVO.setData(page.getRecords());
            getExtractionVO.setTotal(page.getTotal());
            return returnResultMap(ResultMapInfo.GETSUCCESS, getExtractionVO);
        } else {
            return returnResultMap(ResultMapInfo.GETFAIL);
        }
    }

    //审核成功
    @Operation(userType = UserTypeCodeEnum.PLATFORM_USER, operationType = OperationTypeEnum.PLATFORM_EXTRACTION, operationDescribe = "平台提现审核成功")
    @RequestMapping(value = "passCheck", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> successPlatformUserExtraction(@RequestBody IdBO bo) {
        PlatformExtraction platformExtraction = platformExtractionService.getById(bo.getId());
        if (null == platformExtraction) {
            throw new CommonException(ErrorEnum.UNKNOWN);
        }
        //平台余额表只有一条余额记录
        PlatformBalance platformBalance = platformBalanceService.list().get(0);
        if (null == platformBalance) {
            throw new CommonException(ErrorEnum.UNKNOWN);
        }
        //判断余额是否充足
        if (platformBalance.getPlatformBalance().compareTo(platformBalance.getFrozenMoney()) == -1) {
            //余额不足的情况
            return returnResultMap(ResultMapInfo.BALANCEDEFICIENCY);
        } else {
            //更改提现提现订单状态，增加账单事务表记录,增加账单表
            platformExtraction.setStatus(ExtractionStatusEnum.SUCCESSS.getCode());
            platformExtractionService.operatorSuccess(platformExtraction, platformBalance);
            return returnResultMap(ResultMapInfo.ADDSUCCESS);
        }
    }

    //审核失败
    @Operation(userType = UserTypeCodeEnum.PLATFORM_USER, operationType = OperationTypeEnum.PLATFORM_EXTRACTION, operationDescribe = "平台提现审核失败")
    @RequestMapping(value = "noPassCheck", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> failPlatformUserExtraction(@RequestBody IdBO bo) {
        PlatformExtraction platformExtraction = platformExtractionService.getById(bo.getId());
        if (platformExtraction == null) {
            throw new CommonException(ErrorEnum.UNKNOWN);
        }
        platformExtraction.setStatus(ExtractionStatusEnum.FAIL.getCode());
        boolean result = platformExtractionService.failPlatformUserExtraction(platformExtraction);
        if (!result) {
            log.error("平台审核失败{}", DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
            throw new CommonException(ErrorEnum.UNKNOWN);
        }
        return returnResultMap(ResultMapInfo.ADDSUCCESS);
    }

    @Operation(userType = UserTypeCodeEnum.PLATFORM_USER, operationType = OperationTypeEnum.PLATFORM_EXTRACTION, operationDescribe = "平台发起提现申请")
    @RequestMapping(value = "addPlatformExtraction", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> addPlatformExtraction(@RequestBody PlatformUserExtractionBO bo) {
        bo.validate();
        if (bo.getExtractionMoney().compareTo(new BigDecimal(0)) <= 0) {
            return returnResultMap(ResultMapInfo.MINEXTRACTIONFAIL);
        }
        PlatformExtraction extraction = BeanUtils.copyPropertiesChaining(bo, PlatformExtraction::new);
        extraction.setExtractionNumber(TokenUtil.generateOrderNo());
        extraction.setPlatformUserId(super.getUserId());
        //判断余额是否充足
        PlatformBalance platformBalance = platformBalanceService.list().get(0);
        if (null == platformBalance)
            throw new CommonException(ErrorEnum.UNKNOWN);
        //判断余额是否充足
        if (extraction.getExtractionMoney().compareTo(platformBalance.getPlatformBalance().subtract(platformBalance.getFrozenMoney())) == 1) {
            //余额不足的情况
            return returnResultMap(ResultMapInfo.BALANCEDEFICIENCY);
        }
        //更改平台用户表冻结金额
        platformBalance.setFrozenMoney(platformBalance.getFrozenMoney().add(extraction.getExtractionMoney()));
        boolean save = platformExtractionService.saveExtractionAndUpdateUserInfo(extraction, platformBalance);
        if (save) {
            return returnResultMap(ResultMapInfo.ADDSUCCESS);
        }
        return returnResultMap(ResultMapInfo.ADDFAIL);
    }

}
