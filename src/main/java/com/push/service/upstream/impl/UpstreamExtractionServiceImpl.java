package com.push.service.upstream.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.push.common.annotation.TransactionalWithRollback;
import com.push.common.constants.ParamConstants;
import com.push.common.enums.BillInfoStatusEnum;
import com.push.common.enums.BillInfoTypeEnum;
import com.push.common.utils.BeanUtils;
import com.push.common.utils.TokenUtil;
import com.push.common.webfacade.bo.platform.UpstreamExtractionBO;
import com.push.common.webfacade.error.CommonException;
import com.push.common.webfacade.error.ErrorEnum;
import com.push.common.webfacade.vo.platform.UpstreamCheckExtractionVO;
import com.push.config.system.SystemConfig;
import com.push.dao.mapper.platform.CpPlatformBillInfoMapper;
import com.push.dao.mapper.platform.PlatformBalanceMapper;
import com.push.dao.mapper.platform.PlatformBillInfoMapper;
import com.push.dao.mapper.upstream.CpUpstreamBillInfoMapper;
import com.push.dao.mapper.upstream.UpstreamBillInfoMapper;
import com.push.dao.mapper.upstream.UpstreamExtractionMapper;
import com.push.dao.mapper.upstream.UpstreamUserMapper;
import com.push.entity.platform.CpPlatformBillInfo;
import com.push.entity.platform.PlatformBalance;
import com.push.entity.platform.PlatformBillInfo;
import com.push.entity.upstream.CpUpstreamBillInfo;
import com.push.entity.upstream.UpstreamBillInfo;
import com.push.entity.upstream.UpstreamExtraction;
import com.push.entity.upstream.UpstreamUserInfo;
import com.push.service.upstream.UpstreamExtractionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

/**
 * <p>
 * 上游用户提现表 服务实现类
 * </p>
 *
 *

 * @since 2020-03-27
 */
@Service
public class UpstreamExtractionServiceImpl extends ServiceImpl<UpstreamExtractionMapper, UpstreamExtraction> implements UpstreamExtractionService {

    @Resource
    UpstreamExtractionMapper upstreamExtractionMapper;
    @Resource
    UpstreamBillInfoMapper upstreamBillInfoMapper;
    @Resource
    CpUpstreamBillInfoMapper cpUpstreamBillInfoMapper;
    @Resource
    UpstreamUserMapper upstreamUserMapper;
    @Resource
    private PlatformBillInfoMapper platformBillInfoMapper;
    @Resource
    private CpPlatformBillInfoMapper cpPlatformBillInfoMapper;
    @Resource
    private PlatformBalanceMapper platformBalanceMapper;

    @Override
    public Page<UpstreamCheckExtractionVO> getUpstreamExtractionToDoList(Integer code, UpstreamExtractionBO bo) {
        return upstreamExtractionMapper.getUpstreamExtractionToDoList(new Page<>(bo.getPageNo(), bo.getPageSize()),
                code, bo.getUpstreamUserId(), bo.getUpstreamName(), bo.getUserMobile(), bo.getStartTime(), bo.getEndTime());
    }

    @Override
    @TransactionalWithRollback
    public boolean operatorSuccess(UpstreamExtraction upstreamExtraction, UpstreamUserInfo upstreamUserInfo) {
        //更新上游提现表提现状态
        int result1 = upstreamExtractionMapper.updateById(upstreamExtraction);
        if (result1 != 1) {
            throw new CommonException(ErrorEnum.UNKNOWN);
        }
        BigDecimal extractionServiceCharge = new BigDecimal(Objects.requireNonNull(SystemConfig.getParam(ParamConstants.EXTRACTION_SERVICE_CHARGE)));
        //增加账单事务表记录
        CpUpstreamBillInfo cpUpstreamBillInfo = new CpUpstreamBillInfo();
        cpUpstreamBillInfo.setTraderUserId(upstreamExtraction.getUpstreamUserId());
        cpUpstreamBillInfo.setMoney(upstreamExtraction.getExtractionMoney().subtract(extractionServiceCharge));
        cpUpstreamBillInfo.setTradeNo(upstreamExtraction.getExtractionNumber());
        cpUpstreamBillInfo.setType(BillInfoTypeEnum.EXTRACTION.getCode());
        cpUpstreamBillInfo.setBalance(upstreamUserInfo.getBalance());
        cpUpstreamBillInfo.setLastBalance(upstreamUserInfo.getBalance().subtract(upstreamExtraction.getExtractionMoney()));
        cpUpstreamBillInfo.setLastTime(new Date());
        int result2 = cpUpstreamBillInfoMapper.insert(cpUpstreamBillInfo);
        if (result2 != 1) {
            throw new CommonException(ErrorEnum.UNKNOWN);
        }
        //增加账单表
        UpstreamBillInfo upstreamBillInfo = new UpstreamBillInfo();
        BeanUtils.copyProperties(cpUpstreamBillInfo, upstreamBillInfo);
        int result3 = upstreamBillInfoMapper.insert(upstreamBillInfo);
        if (result3 != 1) {
            throw new CommonException(ErrorEnum.UNKNOWN);
        }

        PlatformBalance platformBalance = platformBalanceMapper.selectList(null).get(0);
        CpPlatformBillInfo cpPlatformBillInfo = new CpPlatformBillInfo();
        cpPlatformBillInfo.setMoney(extractionServiceCharge).setTradeNo(TokenUtil.generateOrderNo())
                .setType(BillInfoTypeEnum.SERVICE_CHARGE.getCode())
                .setBalance(platformBalance.getPlatformBalance())
                .setLastBalance(platformBalance.getPlatformBalance().add(cpPlatformBillInfo.getMoney()))
                .setLastTime(new Date());
        cpPlatformBillInfoMapper.insert(cpPlatformBillInfo);
        PlatformBillInfo platformBillInfo = BeanUtils.copyPropertiesChaining(cpPlatformBillInfo, PlatformBillInfo::new);
        platformBillInfoMapper.insert(platformBillInfo);

        return true;
    }

    @Override
    @TransactionalWithRollback
    public boolean failUpstreamUserExtraction(UpstreamExtraction upstreamExtraction) {
        int result1 = upstreamExtractionMapper.updateById(upstreamExtraction);
        //增加账单表
        /*UpstreamBillInfo upstreamBillInfo = new UpstreamBillInfo();
        upstreamBillInfo.setTraderUserId(upstreamExtraction.getUpstreamUserId());
        upstreamBillInfo.setTradeNo(upstreamExtraction.getExtractionNumber());
        upstreamBillInfo.setType(BillInfoTypeEnum.EXTRACTION.getCode());
        upstreamBillInfo.setMoney(upstreamExtraction.getExtractionMoney());
        upstreamBillInfo.setStatus(BillInfoStatusEnum.SUCCESSS.getCode());
        int result2 = upstreamBillInfoMapper.insert(upstreamBillInfo);*/
        UpstreamUserInfo upstreamUserInfo = upstreamUserMapper.selectById(upstreamExtraction.getUpstreamUserId());
        UpstreamUserInfo record = new UpstreamUserInfo();
        record.setId(upstreamUserInfo.getId());
        record.setFrozenMoney(upstreamUserInfo.getFrozenMoney().subtract(upstreamExtraction.getExtractionMoney()));
        int result3 = upstreamUserMapper.updateById(record);
        //if (result1 == 0 || result2 == 0 || result3 == 0) {
        if (result1 == 0 || result3 == 0) {
            return false;
        }
        return true;
    }

    @Override
    @Transactional
    public boolean saveExtractionAndUpdateUserInfo(UpstreamExtraction extraction, UpstreamUserInfo upstreamUserInfo) {
        int insert = upstreamExtractionMapper.insert(extraction);
        int save = upstreamUserMapper.updateById(upstreamUserInfo);
        return insert == 1 && save == 1;
    }


}
