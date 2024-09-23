package com.push.service.shop.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.push.common.annotation.TransactionalWithRollback;
import com.push.common.constants.ParamConstants;
import com.push.common.enums.BillInfoStatusEnum;
import com.push.common.enums.BillInfoTypeEnum;
import com.push.common.utils.BeanUtils;
import com.push.common.utils.TokenUtil;
import com.push.common.webfacade.bo.platform.ShopExtractionRecordBO;
import com.push.common.webfacade.error.CommonException;
import com.push.common.webfacade.error.ErrorEnum;
import com.push.common.webfacade.vo.platform.ShopCheckExtractionVO;
import com.push.config.system.SystemConfig;
import com.push.dao.mapper.platform.CpPlatformBillInfoMapper;
import com.push.dao.mapper.platform.PlatformBalanceMapper;
import com.push.dao.mapper.platform.PlatformBillInfoMapper;
import com.push.dao.mapper.shop.CpShopBillInfoMapper;
import com.push.dao.mapper.shop.ShopBillInfoMapper;
import com.push.dao.mapper.shop.ShopUserExtractionMapper;
import com.push.dao.mapper.shop.ShopUserMapper;
import com.push.entity.platform.CpPlatformBillInfo;
import com.push.entity.platform.PlatformBalance;
import com.push.entity.platform.PlatformBillInfo;
import com.push.entity.shop.CpShopBillInfo;
import com.push.entity.shop.ShopBillInfo;
import com.push.entity.shop.ShopUserExtraction;
import com.push.entity.shop.ShopUserInfo;
import com.push.service.shop.ShopUserExtractionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

/**
 * <p>
 * 商户提现表 服务实现类
 * </p>
 *
 * 

 * @since 2020-03-27
 */
@Service
public class ShopUserExtractionServiceImpl extends ServiceImpl<ShopUserExtractionMapper, ShopUserExtraction> implements ShopUserExtractionService {
    @Resource
    ShopUserExtractionMapper shopUserExtractionMapper;
    @Resource
    ShopBillInfoMapper shopBillInfoMapper;
    @Resource
    CpShopBillInfoMapper cpShopBillInfoMapper;
    @Resource
    ShopUserMapper shopUserMapper;
    @Resource
    private PlatformBillInfoMapper platformBillInfoMapper;
    @Resource
    private CpPlatformBillInfoMapper cpPlatformBillInfoMapper;
    @Resource
    private PlatformBalanceMapper platformBalanceMapper;

    @Override
    public Page<ShopCheckExtractionVO> getShopExtractionToDoList(Integer code, ShopExtractionRecordBO bo) {
        return shopUserExtractionMapper.getShopExtractionToDoList(new Page<>(bo.getPageNo(), bo.getPageSize()),
                code, bo.getShopUserId(), bo.getShopName(), bo.getUserMobile(), bo.getStartTime(), bo.getEndTime());
    }

    @Override
    @TransactionalWithRollback
    public Boolean operatorSuccess(ShopUserExtraction shopUserExtraction, ShopUserInfo shopUserInfo) {
        //更新商户提现表提现状态
        int result1 = shopUserExtractionMapper.updateById(shopUserExtraction);
        if (result1 != 1) {
            throw new CommonException(ErrorEnum.UNKNOWN);
        }
        BigDecimal extractionServiceCharge = new BigDecimal(Objects.requireNonNull(SystemConfig.getParam(ParamConstants.EXTRACTION_SERVICE_CHARGE)));
        //增加账单事务表记录
        CpShopBillInfo cpShopBillInfo = new CpShopBillInfo();
        cpShopBillInfo.setTraderUserId(shopUserExtraction.getShopUserId());
        cpShopBillInfo.setMoney(shopUserExtraction.getExtractionMoney().subtract(extractionServiceCharge));
        cpShopBillInfo.setTradeNo(shopUserExtraction.getExtractionNumber());
        cpShopBillInfo.setType(BillInfoTypeEnum.EXTRACTION.getCode());
        cpShopBillInfo.setBalance(shopUserInfo.getBalance());
        cpShopBillInfo.setLastBalance(shopUserInfo.getBalance().subtract(shopUserExtraction.getExtractionMoney()));
        cpShopBillInfo.setLastTime(new Date());
        int result2 = cpShopBillInfoMapper.insert(cpShopBillInfo);
        if (result2 != 1) {
            throw new CommonException(ErrorEnum.UNKNOWN);
        }
        //增加账单表
        ShopBillInfo shopBillInfo = new ShopBillInfo();
        BeanUtils.copyProperties(cpShopBillInfo, shopBillInfo);
        int result3 = shopBillInfoMapper.insert(shopBillInfo);
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
    public boolean failShopUserExtraction(ShopUserExtraction shopUserExtraction) {
        int result1 = shopUserExtractionMapper.updateById(shopUserExtraction);
        //增加账单表
        /*ShopBillInfo shopBillInfo = new ShopBillInfo();
        shopBillInfo.setTraderUserId(shopUserExtraction.getShopUserId());
        shopBillInfo.setTradeNo(shopUserExtraction.getExtractionNumber());
        shopBillInfo.setType(BillInfoTypeEnum.EXTRACTION.getCode());
        shopBillInfo.setMoney(shopUserExtraction.getExtractionMoney());
        shopBillInfo.setStatus(BillInfoStatusEnum.SUCCESSS.getCode());
        int result2 = shopBillInfoMapper.insert(shopBillInfo);*/
        //扣减冻结金额
        ShopUserInfo shopUserInfo = shopUserMapper.selectById(shopUserExtraction.getShopUserId());
        ShopUserInfo record = new ShopUserInfo();
        record.setId(shopUserInfo.getId());
        record.setFrozenMoney(shopUserInfo.getFrozenMoney().subtract(shopUserExtraction.getExtractionMoney()));
        int result3 = shopUserMapper.updateById(record);
        //if (result1 == 0 || result2 == 0 || result3 == 0) {
        if (result1 == 0 || result3 == 0) {
            return false;
        }
        return true;
    }

    @Override
    @Transactional
    public boolean saveExtractionAndUpdateUserInfo(ShopUserExtraction shopUserExtraction, ShopUserInfo shopUserInfo) {
        int insert = shopUserExtractionMapper.insert(shopUserExtraction);
        int update = shopUserMapper.updateById(shopUserInfo);
        return insert == 1 && update == 1;
    }
}
