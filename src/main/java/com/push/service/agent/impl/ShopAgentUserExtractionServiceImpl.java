package com.push.service.agent.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.push.common.annotation.TransactionalWithRollback;
import com.push.common.constants.ParamConstants;
import com.push.common.enums.BillInfoTypeEnum;
import com.push.common.utils.BeanUtils;
import com.push.common.utils.DateUtil;
import com.push.common.utils.TokenUtil;
import com.push.common.webfacade.bo.platform.ShopAgentExtractionRecordBO;
import com.push.common.webfacade.error.CommonException;
import com.push.common.webfacade.error.ErrorEnum;
import com.push.common.webfacade.vo.platform.statistics.ShopAgentCheckExtractionVO;
import com.push.config.system.SystemConfig;
import com.push.dao.mapper.agent.CpShopAgentBillInfoMapper;
import com.push.dao.mapper.agent.ShopAgentBillInfoMapper;
import com.push.dao.mapper.agent.ShopAgentUserExtractionMapper;
import com.push.dao.mapper.agent.ShopAgentUserInfoMapper;
import com.push.dao.mapper.platform.CpPlatformBillInfoMapper;
import com.push.dao.mapper.platform.PlatformBalanceMapper;
import com.push.dao.mapper.platform.PlatformBillInfoMapper;
import com.push.entity.agent.CpShopAgentBillInfo;
import com.push.entity.agent.ShopAgentBillInfo;
import com.push.entity.agent.ShopAgentUserExtraction;
import com.push.entity.agent.ShopAgentUserInfo;
import com.push.entity.platform.CpPlatformBillInfo;
import com.push.entity.platform.PlatformBalance;
import com.push.entity.platform.PlatformBillInfo;
import com.push.service.agent.ShopAgentUserExtractionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

/**
 * <p>
 * 商户代理商提现表 服务实现类
 * </p>
 *
 * 

 * @since 2020-04-26
 */
@Service
@Slf4j
public class ShopAgentUserExtractionServiceImpl extends ServiceImpl<ShopAgentUserExtractionMapper, ShopAgentUserExtraction> implements ShopAgentUserExtractionService {

    @Resource
    private ShopAgentUserInfoMapper shopAgentUserInfoMapper;

    @Resource
    private ShopAgentUserExtractionMapper shopAgentUserExtractionMapper;

    @Resource
    private CpShopAgentBillInfoMapper cpShopAgentBillInfoMapper;

    @Resource
    private ShopAgentBillInfoMapper shopAgentBillInfoMapper;

    @Resource
    private PlatformBillInfoMapper platformBillInfoMapper;

    @Resource
    private CpPlatformBillInfoMapper cpPlatformBillInfoMapper;

    @Resource
    private PlatformBalanceMapper platformBalanceMapper;

    @Override
    @Transactional
    public boolean saveExtractionAndUpdateUserInfo(ShopAgentUserInfo shopAgentUserInfo, ShopAgentUserExtraction shopAgentUserExtraction) {
        int insert = shopAgentUserExtractionMapper.insert(shopAgentUserExtraction);
        int update = shopAgentUserInfoMapper.updateById(shopAgentUserInfo);
        return insert == 1 && update == 1;
    }

    @Override
    public Page<ShopAgentCheckExtractionVO> getShopAgentExtractionToDoList(Integer code, ShopAgentExtractionRecordBO bo) {
        return shopAgentUserExtractionMapper.getShopAgentExtractionToDoList(new Page<>(bo.getPageNo(), bo.getPageSize()),
                code, bo.getShopAgentUserId(), bo.getShopAgentName(), bo.getUserMobile(), bo.getStartTime(), bo.getEndTime());
    }

    @Override
    @TransactionalWithRollback
    public Boolean operatorSuccess(ShopAgentUserExtraction shopAgentUserExtraction, ShopAgentUserInfo shopAgentUserInfo) {
        //更新商户代理商提现表状态
        int result1 = shopAgentUserExtractionMapper.updateById(shopAgentUserExtraction);
        if (result1 != 1) {
            log.error("更新商户代理商体现表状态异常，【出错时间】{}", DateUtil.dateToString(new Date(), "yyyy-MM-dd HH-mm-ss"));
            throw new CommonException(ErrorEnum.UNKNOWN);
        }
        BigDecimal extractionServiceCharge = new BigDecimal(Objects.requireNonNull(SystemConfig.getParam(ParamConstants.EXTRACTION_SERVICE_CHARGE)));
        //增加账单事务表记录
        CpShopAgentBillInfo cpShopAgentBillInfo = new CpShopAgentBillInfo();
        cpShopAgentBillInfo.setTraderUserId(shopAgentUserExtraction.getShopAgentUserId())
                .setMoney(shopAgentUserExtraction.getExtractionMoney().subtract(extractionServiceCharge))
                .setTradeNo(shopAgentUserExtraction.getExtractionNumber())
                .setType(BillInfoTypeEnum.EXTRACTION.getCode())
                .setBalance(shopAgentUserInfo.getBalance())
                .setLastBalance(shopAgentUserInfo.getBalance().subtract(shopAgentUserExtraction.getExtractionMoney()))
                .setLastTime(new Date());
        int result2 = cpShopAgentBillInfoMapper.insert(cpShopAgentBillInfo);
        if (result2 != 1) {
            log.error("增加商户代理商账单事务表记录异常，【出错时间】{}", DateUtil.dateToString(new Date(), "yyyy-MM-dd HH-mm-ss"));
            throw new CommonException(ErrorEnum.UNKNOWN);
        }
        //增加账单表
        ShopAgentBillInfo shopAgentBillInfo = BeanUtils.copyPropertiesChaining(cpShopAgentBillInfo, ShopAgentBillInfo::new);
        int result3 = shopAgentBillInfoMapper.insert(shopAgentBillInfo);
        if (result3 != 1) {
            log.error("增加商户代理商账单表异常，【出错时间】{}", DateUtil.dateToString(new Date(), "yyyy-MM-dd HH-mm-ss"));
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
    public boolean failShopAgentUserExtraction(ShopAgentUserExtraction shopAgentUserExtraction) {
        int result1 = shopAgentUserExtractionMapper.updateById(shopAgentUserExtraction);
        //增加账单表
        /*ShopAgentBillInfo shopAgentBillInfo = new ShopAgentBillInfo();
        shopAgentBillInfo.setTraderUserId(shopAgentUserExtraction.getShopAgentUserId())
                .setTradeNo(shopAgentUserExtraction.getExtractionNumber())
                .setType(BillInfoTypeEnum.EXTRACTION.getCode())
                .setMoney(shopAgentUserExtraction.getExtractionMoney())
                .setStatus(BillInfoStatusEnum.SUCCESSS.getCode());
        int result2 = shopAgentBillInfoMapper.insert(shopAgentBillInfo);*/
        //扣减冻结金额
        ShopAgentUserInfo shopAgentUserInfo = shopAgentUserInfoMapper.selectById(shopAgentUserExtraction.getShopAgentUserId());
        ShopAgentUserInfo record = new ShopAgentUserInfo();
        record.setId(shopAgentUserInfo.getId());
        record.setFrozenMoney(shopAgentUserInfo.getFrozenMoney().subtract(shopAgentUserExtraction.getExtractionMoney()));
        int result3 = shopAgentUserInfoMapper.updateById(record);
        //if (result1 == 0 || result2 == 0 || result3 == 0) {
        return result1 != 0 && result3 != 0;
    }
}
