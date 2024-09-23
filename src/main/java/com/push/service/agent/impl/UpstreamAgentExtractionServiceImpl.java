package com.push.service.agent.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.push.common.annotation.TransactionalWithRollback;
import com.push.common.constants.ParamConstants;
import com.push.common.enums.BillInfoTypeEnum;
import com.push.common.utils.BeanUtils;
import com.push.common.utils.CheckUtils;
import com.push.common.utils.TokenUtil;
import com.push.common.webfacade.bo.platform.AgentPassCheckExtractionBO;
import com.push.common.webfacade.bo.platform.UpstreamAgentExtractionBO;
import com.push.common.webfacade.error.CommonException;
import com.push.common.webfacade.error.ErrorEnum;
import com.push.common.webfacade.vo.platform.AgentPassCheckExtractionVO;
import com.push.common.webfacade.vo.platform.AgentSuAndFaPassCheckExtractionVO;
import com.push.config.system.SystemConfig;
import com.push.dao.mapper.agent.CpUpstreamAgentBillInfoMapper;
import com.push.dao.mapper.agent.UpstreamAgentBillInfoMapper;
import com.push.dao.mapper.agent.UpstreamAgentExtractionMapper;
import com.push.dao.mapper.agent.UpstreamAgentUserInfoMapper;
import com.push.dao.mapper.platform.CpPlatformBillInfoMapper;
import com.push.dao.mapper.platform.PlatformBalanceMapper;
import com.push.dao.mapper.platform.PlatformBillInfoMapper;
import com.push.entity.agent.CpUpstreamAgentBillInfo;
import com.push.entity.agent.UpstreamAgentBillInfo;
import com.push.entity.agent.UpstreamAgentExtraction;
import com.push.entity.agent.UpstreamAgentUserInfo;
import com.push.entity.platform.CpPlatformBillInfo;
import com.push.entity.platform.PlatformBalance;
import com.push.entity.platform.PlatformBillInfo;
import com.push.service.agent.UpstreamAgentExtractionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

/**
 * <p>
 * 上游代理商用户提现表 服务实现类
 * </p>
 *
 *

 * @since 2020-04-26
 */
@Service
public class UpstreamAgentExtractionServiceImpl extends ServiceImpl<UpstreamAgentExtractionMapper, UpstreamAgentExtraction> implements UpstreamAgentExtractionService {

    @Resource
    private UpstreamAgentUserInfoMapper upstreamAgentUserInfoMapper;

    @Resource
    private UpstreamAgentExtractionMapper upstreamAgentExtractionMapper;

    @Resource
    private UpstreamAgentBillInfoMapper upstreamAgentBillInfoMapper;

    @Resource
    private CpUpstreamAgentBillInfoMapper cpUpstreamAgentBillInfoMapper;

    @Resource
    private PlatformBillInfoMapper platformBillInfoMapper;

    @Resource
    private CpPlatformBillInfoMapper cpPlatformBillInfoMapper;

    @Resource
    private PlatformBalanceMapper platformBalanceMapper;

    @Override
    @TransactionalWithRollback
    public boolean saveExtractionAndUpdateUser(UpstreamAgentUserInfo userInfo, UpstreamAgentExtractionBO bo) {
        int update = upstreamAgentUserInfoMapper.updateById(userInfo.setFrozenMoney(userInfo.getFrozenMoney().add(bo.getExtractionMoney())));
        UpstreamAgentExtraction extraction = BeanUtils.copyPropertiesChaining(bo, UpstreamAgentExtraction::new);
        extraction.setExtractionNumber(TokenUtil.generateOrderNo());
        int insert = upstreamAgentExtractionMapper.insert(extraction);
        return update == 1 && insert == 1;
    }

    @Override
    @TransactionalWithRollback
    public boolean updateByIdAndSaveBill(UpstreamAgentExtraction extraction) {
        int update = upstreamAgentExtractionMapper.updateById(extraction);
        if (update != 1){
            throw new CommonException(ErrorEnum.UNKNOWN);
        }
        BigDecimal extractionServiceCharge = new BigDecimal(Objects.requireNonNull(SystemConfig.getParam(ParamConstants.EXTRACTION_SERVICE_CHARGE)));
        Long userId = extraction.getUpstreamAgentUserId();
        UpstreamAgentUserInfo userInfo = upstreamAgentUserInfoMapper.selectById(userId);
        CheckUtils.checkNull(userInfo,"代理用户不存在");
        CpUpstreamAgentBillInfo cpInfo = new CpUpstreamAgentBillInfo();
        cpInfo.setTraderUserId(userId);
        cpInfo.setMoney(extraction.getExtractionMoney().subtract(extractionServiceCharge));
        cpInfo.setTradeNo(extraction.getExtractionNumber());
        cpInfo.setType(BillInfoTypeEnum.EXTRACTION.getCode());
        cpInfo.setBalance(userInfo.getBalance());
        cpInfo.setLastBalance(userInfo.getBalance().subtract(extraction.getExtractionMoney()));
        cpInfo.setLastTime(new Date());
        int insertCp = cpUpstreamAgentBillInfoMapper.insert(cpInfo);
        if (insertCp != 1){
            throw new CommonException(ErrorEnum.UNKNOWN);
        }
        UpstreamAgentBillInfo info = BeanUtils.copyPropertiesChaining(cpInfo, UpstreamAgentBillInfo::new);
        int insert = upstreamAgentBillInfoMapper.insert(info);
        if (insert != 1){
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
    public boolean updateByIdAndUpdateUser(UpstreamAgentExtraction extraction) {
        int update = upstreamAgentExtractionMapper.updateById(extraction);
        if (update != 1){
            throw new CommonException(ErrorEnum.UNKNOWN);
        }
        UpstreamAgentUserInfo userInfo = upstreamAgentUserInfoMapper.selectById(extraction.getUpstreamAgentUserId());
        CheckUtils.checkNull(userInfo,"代理用户不存在");
        userInfo.setFrozenMoney(userInfo.getFrozenMoney().subtract(extraction.getExtractionMoney()));
        int updateById = upstreamAgentUserInfoMapper.updateById(userInfo);
        return updateById == 1;
    }

    @Override
    public IPage<AgentPassCheckExtractionVO> passCheckList(AgentPassCheckExtractionBO bo, Integer code, Long userId) {
        Page<AgentPassCheckExtractionVO> page = new Page<>(bo.getPageNo(),bo.getPageSize());
        return upstreamAgentExtractionMapper.passCheckList(page,bo.getUpstreamName(),bo.getPhoneNumber(),bo.getStartTime(),bo.getEndTime(),code,userId);
    }

    @Override
    public IPage<AgentSuAndFaPassCheckExtractionVO> passSuAndFaCheckList(AgentPassCheckExtractionBO bo, Integer code, Long userId) {
        Page<AgentPassCheckExtractionVO> page = new Page<>(bo.getPageNo(), bo.getPageSize());
        return upstreamAgentExtractionMapper.passSuAndFaCheckList(page, bo.getUpstreamName(), bo.getPhoneNumber(), bo.getStartTime(), bo.getEndTime(), code,userId);
    }
}
