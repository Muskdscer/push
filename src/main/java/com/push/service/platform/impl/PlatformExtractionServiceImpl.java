package com.push.service.platform.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.push.common.annotation.TransactionalWithRollback;
import com.push.common.enums.BillInfoStatusEnum;
import com.push.common.enums.BillInfoTypeEnum;
import com.push.common.utils.BeanUtils;
import com.push.common.webfacade.bo.platform.PlatformExtractionBO;
import com.push.common.webfacade.error.CommonException;
import com.push.common.webfacade.error.ErrorEnum;
import com.push.common.webfacade.vo.platform.PlatformExtractionVO;
import com.push.dao.mapper.platform.CpPlatformBillInfoMapper;
import com.push.dao.mapper.platform.PlatformBalanceMapper;
import com.push.dao.mapper.platform.PlatformBillInfoMapper;
import com.push.dao.mapper.platform.PlatformExtractionMapper;
import com.push.entity.platform.CpPlatformBillInfo;
import com.push.entity.platform.PlatformBalance;
import com.push.entity.platform.PlatformBillInfo;
import com.push.entity.platform.PlatformExtraction;
import com.push.service.platform.PlatformExtractionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * <p>
 * 上游用户提现表 服务实现类
 * </p>
 *
 *

 * @since 2020-03-30
 */
@Service
public class PlatformExtractionServiceImpl extends ServiceImpl<PlatformExtractionMapper, PlatformExtraction> implements PlatformExtractionService {

    @Resource
    private PlatformExtractionMapper platformExtractionMapper;

    @Resource
    private PlatformBillInfoMapper platformBillInfoMapper;

    @Resource
    private CpPlatformBillInfoMapper cpPlatformBillInfoMapper;

    @Resource
    private PlatformBalanceMapper platformBalanceMapper;


    @Override
    public Page<PlatformExtractionVO> getPlatformExtractionToDoList(Integer code, PlatformExtractionBO bo) {
        Page<PlatformExtractionVO> page = new Page<>(bo.getPageNo(), bo.getPageSize());
        return platformExtractionMapper.getPlatformExtractionToDoList(page, code, bo.getOperatorPerson(), bo.getUserMobile(), bo.getStartTime(), bo.getEndTime());
    }

    @Override
    @TransactionalWithRollback
    public Boolean operatorSuccess(PlatformExtraction platformExtraction, PlatformBalance platformBalance) {
        //更新平台提现表提现状态
        int result1 = platformExtractionMapper.updateById(platformExtraction);
        if (result1 != 1) {
            throw new CommonException(ErrorEnum.UNKNOWN);
        }
        //增加账单事务表记录
        CpPlatformBillInfo cpPlatformBillInfo = new CpPlatformBillInfo();
        cpPlatformBillInfo.setTraderUserId(platformExtraction.getPlatformUserId());
        cpPlatformBillInfo.setMoney(platformExtraction.getExtractionMoney());
        cpPlatformBillInfo.setTradeNo(platformExtraction.getExtractionNumber());
        cpPlatformBillInfo.setType(BillInfoTypeEnum.EXTRACTION.getCode());
        cpPlatformBillInfo.setBalance(platformBalance.getPlatformBalance());
        cpPlatformBillInfo.setLastBalance(platformBalance.getPlatformBalance().subtract(platformExtraction.getExtractionMoney()));
        cpPlatformBillInfo.setLastTime(new Date());
        int result2 = cpPlatformBillInfoMapper.insert(cpPlatformBillInfo);
        if (result2 != 1) {
            throw new CommonException(ErrorEnum.UNKNOWN);
        }
        //增加账单表
        PlatformBillInfo platformBillInfo = new PlatformBillInfo();
        BeanUtils.copyProperties(cpPlatformBillInfo, platformBillInfo);
        int result3 = platformBillInfoMapper.insert(platformBillInfo);
        if (result3 != 1) {
            throw new CommonException(ErrorEnum.UNKNOWN);
        }


        return true;
    }

    @Override
    @TransactionalWithRollback
    public boolean failPlatformUserExtraction(PlatformExtraction platformExtraction) {
        int result1 = platformExtractionMapper.updateById(platformExtraction);
        //增加账单表
        /*PlatformBillInfo platformBillInfo = new PlatformBillInfo();
        platformBillInfo.setTraderUserId(platformExtraction.getPlatformUserId());
        platformBillInfo.setTradeNo(platformExtraction.getExtractionNumber());
        platformBillInfo.setType(BillInfoTypeEnum.EXTRACTION.getCode());
        platformBillInfo.setMoney(platformExtraction.getExtractionMoney());
        platformBillInfo.setStatus(BillInfoStatusEnum.SUCCESSS.getCode());
        int result2 = platformBillInfoMapper.insert(platformBillInfo);*/
        //扣减冻结金额
        PlatformBalance platformBalance = platformBalanceMapper.selectList(null).get(0);
        platformBalance.setFrozenMoney(platformBalance.getFrozenMoney().subtract(platformExtraction.getExtractionMoney()));
        int result3 = platformBalanceMapper.updateById(platformBalance);
        //if (result1 == 0 || result2 == 0 || result3 == 0) {
        if (result1 == 0 || result3 == 0) {
            return false;
        }
        return true;
    }

    @Override
    @Transactional
    public boolean saveExtractionAndUpdateUserInfo(PlatformExtraction extraction, PlatformBalance platformBalance) {
        int insert = platformExtractionMapper.insert(extraction);
        int update = platformBalanceMapper.updateById(platformBalance);
        return insert == 1 && update == 1;
    }
}
