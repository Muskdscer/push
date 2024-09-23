package com.push.service.schedule.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.push.common.annotation.TransactionalWithRollback;
import com.push.common.constants.BillInfoTypeConstant;
import com.push.common.enums.BillInfoStatusEnum;
import com.push.common.enums.PlatformBillStatusEnum;
import com.push.common.utils.DateUtil;
import com.push.dao.mapper.platform.PlatformBalanceMapper;
import com.push.dao.mapper.shop.ShopUserMapper;
import com.push.entity.agent.*;
import com.push.entity.platform.CpPlatformBillInfo;
import com.push.entity.platform.PhoneOrderRecord;
import com.push.entity.platform.PlatformBalance;
import com.push.entity.platform.PlatformBillInfo;
import com.push.entity.shop.CpShopBillInfo;
import com.push.entity.shop.ShopBillInfo;
import com.push.entity.shop.ShopUserInfo;
import com.push.entity.upstream.CpUpstreamBillInfo;
import com.push.entity.upstream.UpstreamBalance;
import com.push.entity.upstream.UpstreamBillInfo;
import com.push.service.agent.*;
import com.push.service.platform.CpPlatformBillInfoService;
import com.push.service.platform.PhoneOrderRecordService;
import com.push.service.platform.PlatformBillInfoService;
import com.push.service.schedule.BalanceService;
import com.push.service.shop.CpShopBillInfoService;
import com.push.service.shop.ShopBillInfoService;
import com.push.service.upstream.CpUpstreamBillInfoService;
import com.push.service.upstream.UpstreamAgentUserBalanceService;
import com.push.service.upstream.UpstreamBalanceService;
import com.push.service.upstream.UpstreamBillInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Description:
 * Create DateTime: 2020/3/31 18:47
 *
 *

 */
@Service
@Slf4j
public class BalanceServiceImpl implements BalanceService {

    @Resource
    private BalanceService balanceService;

    @Resource
    private CpUpstreamBillInfoService cpUpstreamBillInfoService;

    @Resource
    private UpstreamBillInfoService upstreamBillInfoService;

    @Resource
    private CpShopBillInfoService cpShopBillInfoService;

    @Resource
    private ShopBillInfoService shopBillInfoService;

    @Resource
    private ShopUserMapper shopUserMapper;

    @Resource
    private CpPlatformBillInfoService cpPlatformBillInfoService;

    @Resource
    private PlatformBillInfoService platformBillInfoService;

    @Resource
    private PlatformBalanceMapper platformBalanceMapper;

    @Resource
    private PhoneOrderRecordService phoneOrderRecordService;

    @Resource
    private CpShopAgentBillInfoService cpShopAgentBillInfoService;

    @Resource
    private ShopAgentBillInfoService shopAgentBillInfoService;

    @Resource
    private ShopAgentUserInfoService shopAgentUserInfoService;

    @Resource
    private CpUpstreamAgentBillInfoService cpUpstreamAgentBillInfoService;

    @Resource
    private UpstreamAgentBillInfoService upstreamAgentBillInfoService;

    @Resource
    private UpstreamBalanceService upstreamBalanceService;


    @Resource
    private UpstreamAgentUserBalanceService upstreamAgentUserBalanceService;


    //上游记账操作
    @Override
    public synchronized void handleCpUpstreamRecord() {
        Page<CpUpstreamBillInfo> dataList = cpUpstreamBillInfoService.findAllByState(BillInfoStatusEnum.TODO.getCode());
        List<CpUpstreamBillInfo> records = dataList.getRecords();
        if (CollectionUtils.isNotEmpty(records)) {
            for (CpUpstreamBillInfo cpUpstreamBillInfo : records) {
//                balanceService.handleCpUpstreamRecordHook(cpUpstreamBillInfo);
                balanceService.newhandleCpUpstreamRecordHook(cpUpstreamBillInfo);
            }
        }
    }


    @Override
    @TransactionalWithRollback
    public void newhandleCpUpstreamRecordHook(CpUpstreamBillInfo cpUpstreamBillInfo) {
        //更新UpstreambillInfo 表
        UpstreamBillInfo upstreamBillInfo = upstreamBillInfoService.queryBillInfoByTradeNo(cpUpstreamBillInfo.getTradeNo());
        if (null != upstreamBillInfo) {
            upstreamBillInfo.setStatus(BillInfoStatusEnum.SUCCESSS.getCode());
            upstreamBillInfoService.updateById(upstreamBillInfo);
        } else {
            log.error("handelOneCpUpstreamRecord 更新UpstreambillInfo表失败:{},上游账单号:{}", DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"), cpUpstreamBillInfo.getTradeNo());
            PhoneOrderRecord record = phoneOrderRecordService.getOne(Wrappers.lambdaQuery(PhoneOrderRecord.class).eq(PhoneOrderRecord::getPlatformOrderNo, cpUpstreamBillInfo.getTradeNo()));
            if (PlatformBillStatusEnum.UNBILL.getCode().equals(record.getPlatformBillStatus())) {
                cpUpstreamBillInfoService.removeById(cpUpstreamBillInfo);
            }
            return;
            //throw new CommonException(ErrorEnum.UNKNOWN);
        }
        // 更新UpstreamBalance余额
        // 0818 墨子更新，使用渠道余额表进行记录
        UpstreamBalance upstreamBalance = upstreamBalanceService.getOne(Wrappers.lambdaQuery(UpstreamBalance.class)
                .eq(UpstreamBalance::getUpstreamUserId,cpUpstreamBillInfo.getTraderUserId())
                .eq(UpstreamBalance::getAisleId, cpUpstreamBillInfo.getAisleId())
        );
        if (null == upstreamBalance){
            log.error("==========【渠道记账】============渠道余额账户不存在,账单表信息为==={}", cpUpstreamBillInfo);
            throw new RuntimeException();
        }

        switch (cpUpstreamBillInfo.getType()) {
            //充值
            case BillInfoTypeConstant.BILLINFO_TYPE_RECHAGE:
                //充值后余额
                BigDecimal addBalance = upstreamBalance.getBalance().add(cpUpstreamBillInfo.getMoney());
                upstreamBalance.setBalance(addBalance);
                break;
            //提现
            case BillInfoTypeConstant.BILLINFO_TYPE_EXTRACTION:
                //提现后余额
                BigDecimal extractionBalance = upstreamBalance.getBalance().subtract(cpUpstreamBillInfo.getMoney());
                //提现后冻结金额
                BigDecimal extractionFrozenBalance = upstreamBalance.getFrozenMoney().subtract(cpUpstreamBillInfo.getMoney());
                upstreamBalance.setBalance(extractionBalance);
                upstreamBalance.setFrozenMoney(extractionFrozenBalance);
                break;
            //积攒
            case BillInfoTypeConstant.BILLINFO_TYPE_SAVE:
                //积攒后余额
                BigDecimal saveBalance = upstreamBalance.getBalance().add(cpUpstreamBillInfo.getMoney());
                upstreamBalance.setBalance(saveBalance);
                break;
            //消费
            case BillInfoTypeConstant.BILLINFO_TYPE_REDUCE:
                //消费后余额
                BigDecimal reduceBalance = upstreamBalance.getBalance().subtract(cpUpstreamBillInfo.getMoney());
                upstreamBalance.setBalance(reduceBalance);
                break;
            //扣除手续费
            case BillInfoTypeConstant.EXTRACTION_SERVICE_CHARGE:
                //扣除后余额
                BigDecimal serviceBalance = upstreamBalance.getBalance().subtract(cpUpstreamBillInfo.getMoney());
                upstreamBalance.setBalance(serviceBalance);
                break;
        }
        upstreamBalanceService.updateById(upstreamBalance);
        //删除账单事务表记录
        boolean isDelete = cpUpstreamBillInfoService.removeById(cpUpstreamBillInfo.getId());
        if (!isDelete) {
            throw new RuntimeException();
        }

    }

    /**==========0818墨子废弃，handleCpUpstreamRecordHook， 使用newhandleCpUpstreamRecordHook=======================*/
//    @Override
//    @TransactionalWithRollback
//    public void handleCpUpstreamRecordHook(CpUpstreamBillInfo cpUpstreamBillInfo) {
//        //更新UpstreambillInfo 表
//        UpstreamBillInfo upstreamBillInfo = upstreamBillInfoService.queryBillInfoByTradeNo(cpUpstreamBillInfo.getTradeNo());
//        if (null != upstreamBillInfo) {
//            upstreamBillInfo.setStatus(BillInfoStatusEnum.SUCCESSS.getCode());
//            upstreamBillInfoService.updateById(upstreamBillInfo);
//        } else {
//            log.error("handelOneCpUpstreamRecord 更新UpstreambillInfo表失败:{},上游账单号:{}", DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"), cpUpstreamBillInfo.getTradeNo());
//            PhoneOrderRecord record = phoneOrderRecordService.getOne(Wrappers.lambdaQuery(PhoneOrderRecord.class).eq(PhoneOrderRecord::getPlatformOrderNo, cpUpstreamBillInfo.getTradeNo()));
//            if (PlatformBillStatusEnum.UNBILL.getCode() == record.getPlatformBillStatus()) {
//                cpUpstreamBillInfoService.removeById(cpUpstreamBillInfo);
//            }
//            return;
//            //throw new CommonException(ErrorEnum.UNKNOWN);
//        }
//        // 更新Upstream余额
//        UpstreamUserInfo upstreamUserInfo = upstreamUserMapper.selectById(cpUpstreamBillInfo.getTraderUserId());
//        switch (cpUpstreamBillInfo.getType()) {
//            //充值
//            case BillInfoTypeConstant.BILLINFO_TYPE_RECHAGE:
//                //充值后余额
//                BigDecimal addBalance = upstreamUserInfo.getBalance().add(cpUpstreamBillInfo.getMoney());
//                upstreamUserInfo.setBalance(addBalance);
//                break;
//            //提现
//            case BillInfoTypeConstant.BILLINFO_TYPE_EXTRACTION:
//                //提现后余额
//                BigDecimal extractionBalance = upstreamUserInfo.getBalance().subtract(cpUpstreamBillInfo.getMoney());
//                //提现后冻结金额
//                BigDecimal extractionFrozenBalance = upstreamUserInfo.getFrozenMoney().subtract(cpUpstreamBillInfo.getMoney());
//                upstreamUserInfo.setBalance(extractionBalance);
//                upstreamUserInfo.setFrozenMoney(extractionFrozenBalance);
//                break;
//            //积攒
//            case BillInfoTypeConstant.BILLINFO_TYPE_SAVE:
//                //积攒后余额
//                BigDecimal saveBalance = upstreamUserInfo.getBalance().add(cpUpstreamBillInfo.getMoney());
//                upstreamUserInfo.setBalance(saveBalance);
//                break;
//            //消费
//            case BillInfoTypeConstant.BILLINFO_TYPE_REDUCE:
//                //消费后余额
//                BigDecimal reduceBalance = upstreamUserInfo.getBalance().subtract(cpUpstreamBillInfo.getMoney());
//                upstreamUserInfo.setBalance(reduceBalance);
//                break;
//            //扣除手续费
//            case BillInfoTypeConstant.EXTRACTION_SERVICE_CHARGE:
//                //扣除后余额
//                BigDecimal serviceBalance = upstreamUserInfo.getBalance().subtract(cpUpstreamBillInfo.getMoney());
//                upstreamUserInfo.setBalance(serviceBalance);
//                break;
//        }
//        upstreamUserMapper.updateById(upstreamUserInfo);
//        //删除账单事务表记录
//        boolean isDelete = cpUpstreamBillInfoService.removeById(cpUpstreamBillInfo.getId());
//        if (!isDelete) {
//            throw new RuntimeException();
//        }
//
//    }

    //商户记账操作
    @Override
    public synchronized void handleCpShopRecord() {
        List<CpShopBillInfo> dataList = cpShopBillInfoService.findAllByState(BillInfoStatusEnum.TODO.getCode());
        if (CollectionUtils.isNotEmpty(dataList)) {
            for (CpShopBillInfo cpShopBillInfo : dataList) {
                balanceService.handleCpShopRecordHook(cpShopBillInfo);
            }
        }
    }

    @Override
    @TransactionalWithRollback
    public void handleCpShopRecordHook(CpShopBillInfo cpShopBillInfo) {
        //更新shopbillInfo 表
        ShopBillInfo shopBillInfo = shopBillInfoService.queryBillInfoByTradeNo(cpShopBillInfo.getTradeNo());
        if (null != shopBillInfo) {
            shopBillInfo.setLastTime(new Date());
            shopBillInfo.setStatus(BillInfoStatusEnum.SUCCESSS.getCode());
            shopBillInfoService.updateById(shopBillInfo);
        } else {
            log.error("handelOneCpShopRecord  更新shopbillInfo表失败:{}，商户账单号:{}", DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"), cpShopBillInfo.getTradeNo());
            PhoneOrderRecord record = phoneOrderRecordService.getOne(Wrappers.lambdaQuery(PhoneOrderRecord.class).eq(PhoneOrderRecord::getPlatformOrderNo, cpShopBillInfo.getTradeNo()));
            if (PlatformBillStatusEnum.UNBILL.getCode().equals(record.getPlatformBillStatus())) {
                cpShopBillInfoService.removeById(cpShopBillInfo);
            }
            return;
            //throw new CommonException(ErrorEnum.UNKNOWN);
        }
        // 更新shop余额
        ShopUserInfo shopUserInfo = shopUserMapper.selectById(cpShopBillInfo.getTraderUserId());
        switch (cpShopBillInfo.getType()) {
            //商户无充值
            case BillInfoTypeConstant.BILLINFO_TYPE_RECHAGE:
                //提现
            case BillInfoTypeConstant.BILLINFO_TYPE_EXTRACTION:
                //提现后余额
                BigDecimal extractionBalance = shopUserInfo.getBalance().subtract(cpShopBillInfo.getMoney());
                //提现后冻结金额
                BigDecimal extractionFrozenBalance = shopUserInfo.getFrozenMoney().subtract(cpShopBillInfo.getMoney());
                shopUserInfo.setBalance(extractionBalance);
                shopUserInfo.setFrozenMoney(extractionFrozenBalance);
                break;
            //积攒
            case BillInfoTypeConstant.BILLINFO_TYPE_SAVE:
                //积攒后余额
                BigDecimal saveBalance = shopUserInfo.getBalance().add(cpShopBillInfo.getMoney());
                shopUserInfo.setBalance(saveBalance);
                break;
            //消费
            case BillInfoTypeConstant.BILLINFO_TYPE_REDUCE:
                //消费后余额
                BigDecimal reduceBalance = shopUserInfo.getBalance().subtract(cpShopBillInfo.getMoney());
                shopUserInfo.setBalance(reduceBalance);
                break;
            //扣除手续费
            case BillInfoTypeConstant.EXTRACTION_SERVICE_CHARGE:
                //扣除后余额
                BigDecimal serviceBalance = shopUserInfo.getBalance().subtract(cpShopBillInfo.getMoney());
                shopUserInfo.setBalance(serviceBalance);
                break;
        }
        shopUserMapper.updateById(shopUserInfo);
        //删除账单事务表记录
        boolean isDelete = cpShopBillInfoService.removeById(cpShopBillInfo.getId());
        if (!isDelete) {
            throw new RuntimeException();
        }

    }

    //平台记账操作
    @Override
    public synchronized void handleCpPlatformRecord() {
        List<CpPlatformBillInfo> dataList = cpPlatformBillInfoService.findAllByState(BillInfoStatusEnum.TODO.getCode());
        if (CollectionUtils.isNotEmpty(dataList)) {
            for (CpPlatformBillInfo cpPlatformBillInfo : dataList) {
                balanceService.handleOneCpPlatformRecordHook(cpPlatformBillInfo);
            }
        }
    }

    @Override
    @TransactionalWithRollback
    public void handleOneCpPlatformRecordHook(CpPlatformBillInfo cpPlatformBillInfo) {
        //更新PlatformbillInfo 表
        PlatformBillInfo platformBillInfo = platformBillInfoService.queryBillInfoByTradeNo(cpPlatformBillInfo.getTradeNo());
        if (null != platformBillInfo) {
            platformBillInfo.setStatus(BillInfoStatusEnum.SUCCESSS.getCode());
            platformBillInfoService.updateById(platformBillInfo);
        } else {
            log.error("handelOneCpPlatformRecord  更新PlatformBillInfo表失败:{}平台账单流水号:{}", DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"), cpPlatformBillInfo.getTradeNo());
            PhoneOrderRecord record = phoneOrderRecordService.getOne(Wrappers.lambdaQuery(PhoneOrderRecord.class).eq(PhoneOrderRecord::getPlatformOrderNo, cpPlatformBillInfo.getTradeNo()));
            if (PlatformBillStatusEnum.UNBILL.getCode().equals(record.getPlatformBillStatus())) {
                cpPlatformBillInfoService.removeById(cpPlatformBillInfo);
            }
            return;
            //throw new CommonException(ErrorEnum.UNKNOWN);
        }
        // 更新PlatformBalance余额 平台余额表只有一条记录
        PlatformBalance platformBalance = platformBalanceMapper.selectList(null).get(0);
        switch (cpPlatformBillInfo.getType()) {
            //平台无充值
            case BillInfoTypeConstant.BILLINFO_TYPE_RECHAGE:
                //提现
            case BillInfoTypeConstant.BILLINFO_TYPE_EXTRACTION:
                //提现后余额
                BigDecimal extractionBalance = platformBalance.getPlatformBalance().subtract(cpPlatformBillInfo.getMoney());
                //提现后冻结金额
                BigDecimal extractionFrozenBalance = platformBalance.getFrozenMoney().subtract(cpPlatformBillInfo.getMoney());
                platformBalance.setPlatformBalance(extractionBalance);
                platformBalance.setFrozenMoney(extractionFrozenBalance);
                break;
            //积攒
            case BillInfoTypeConstant.BILLINFO_TYPE_SAVE:
                //积攒后余额
                BigDecimal saveBalance = platformBalance.getPlatformBalance().add(cpPlatformBillInfo.getMoney());
                platformBalance.setPlatformBalance(saveBalance);
                break;
            //消费
            case BillInfoTypeConstant.BILLINFO_TYPE_REDUCE:
                //消费后余额
                BigDecimal reduceBalance = platformBalance.getPlatformBalance().subtract(cpPlatformBillInfo.getMoney());
                platformBalance.setPlatformBalance(reduceBalance);
                break;
            //提现手续费收入
            case BillInfoTypeConstant.BILLINFO_TYPE_SERVICE_CHARGE:
                //消费后余额
                BigDecimal serviceChargeBalance = platformBalance.getPlatformBalance().add(cpPlatformBillInfo.getMoney());
                platformBalance.setPlatformBalance(serviceChargeBalance);
                break;
        }
        platformBalanceMapper.updateById(platformBalance);
        //删除账单事务表记录
        boolean isDelete = cpPlatformBillInfoService.removeById(cpPlatformBillInfo.getId());
        if (!isDelete) {
            throw new RuntimeException();
        }
    }


    //商户代理商记账操作
    @Override
    public void handleCpShopAgentRecord() {
        List<CpShopAgentBillInfo> dataList = cpShopAgentBillInfoService.findAllByState(BillInfoStatusEnum.TODO.getCode());
        if (CollectionUtils.isNotEmpty(dataList)) {
            for (CpShopAgentBillInfo cpShopAgentBillInfo : dataList) {
                balanceService.handleOneCpShopAgentRecordHook(cpShopAgentBillInfo);
            }
        }
    }

    @Override
    @TransactionalWithRollback
    public void handleOneCpShopAgentRecordHook(CpShopAgentBillInfo cpShopAgentBillInfo) {
        //更新ShopAgentBillInfo 表
        ShopAgentBillInfo shopAgentBillInfo = shopAgentBillInfoService.queryBillInfoByTradeNo(cpShopAgentBillInfo.getTradeNo());
        if (null != shopAgentBillInfo) {
            shopAgentBillInfo.setStatus(BillInfoStatusEnum.SUCCESSS.getCode());
            shopAgentBillInfoService.updateById(shopAgentBillInfo);
        } else {
            log.error("handleCpShopAgentRecord  更新ShopAgentBillInfo表失败:{}平台账单流水号:{}", DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"), cpShopAgentBillInfo.getTradeNo());
            PhoneOrderRecord record = phoneOrderRecordService.getOne(Wrappers.lambdaQuery(PhoneOrderRecord.class).eq(PhoneOrderRecord::getPlatformOrderNo, cpShopAgentBillInfo.getTradeNo()));
            /*if (PlatformBillStatusEnum.UNBILL.getCode() == record.getPlatformBillStatus()) {
                cpShopAgentBillInfoService.removeById(cpShopAgentBillInfo);
            }*/
            return;
        }
        // 更新shopAgent余额
        ShopAgentUserInfo shopAgentUserInfo = shopAgentUserInfoService.getById(cpShopAgentBillInfo.getTraderUserId());
        switch (cpShopAgentBillInfo.getType()) {
            //商户代理商无充值
            case BillInfoTypeConstant.BILLINFO_TYPE_RECHAGE:
                //提现
            case BillInfoTypeConstant.BILLINFO_TYPE_EXTRACTION:
                //提现后余额
                BigDecimal extractionBalance = shopAgentUserInfo.getBalance().subtract(cpShopAgentBillInfo.getMoney());
                //提现后冻结金额
                BigDecimal extractionFrozenBalance = shopAgentUserInfo.getFrozenMoney().subtract(cpShopAgentBillInfo.getMoney());
                shopAgentUserInfo.setBalance(extractionBalance);
                shopAgentUserInfo.setFrozenMoney(extractionFrozenBalance);
                break;
            //积攒
            case BillInfoTypeConstant.BILLINFO_TYPE_SAVE:
                //积攒后余额
                BigDecimal saveBalance = shopAgentUserInfo.getBalance().add(cpShopAgentBillInfo.getMoney());
                shopAgentUserInfo.setBalance(saveBalance);
                break;
            //商户代理商无消费
            case BillInfoTypeConstant.BILLINFO_TYPE_REDUCE:
                break;
            //扣除手续费
            case BillInfoTypeConstant.EXTRACTION_SERVICE_CHARGE:
                //扣除后余额
                BigDecimal serviceBalance = shopAgentUserInfo.getBalance().subtract(cpShopAgentBillInfo.getMoney());
                shopAgentUserInfo.setBalance(serviceBalance);
                break;
        }
        shopAgentUserInfoService.updateById(shopAgentUserInfo);
        //删除账单事务表记录
        boolean isDelete = cpShopAgentBillInfoService.removeById(cpShopAgentBillInfo.getId());
        if (!isDelete) {
            throw new RuntimeException();
        }
    }

    @Override
    public void handleCpUpstreamAgentRecord() {
        List<CpUpstreamAgentBillInfo> dataList = cpUpstreamAgentBillInfoService.findAllByState(BillInfoStatusEnum.TODO.getCode());
        if (CollectionUtils.isNotEmpty(dataList)) {
            for (CpUpstreamAgentBillInfo cpUpstreamAgentBillInfo : dataList) {
//                balanceService.handleOneCpUpstreamAgentRecordHook(cpUpstreamAgentBillInfo);
                // 0818 墨子修改，使用新的记账方法，账户记到余额记录里面
                balanceService.newhandleOneCpUpstreamAgentRecordHook(cpUpstreamAgentBillInfo);
            }
        }
    }

    @Override
    @TransactionalWithRollback
    public void newhandleOneCpUpstreamAgentRecordHook(CpUpstreamAgentBillInfo cpUpstreamAgentBillInfo) {
        //更新UpstreamAgentBillInfo 表
        UpstreamAgentBillInfo upstreamAgentBillInfo = upstreamAgentBillInfoService.queryBillInfoByTradeNo(cpUpstreamAgentBillInfo.getTradeNo());
        if (null != upstreamAgentBillInfo) {
            upstreamAgentBillInfo.setStatus(BillInfoStatusEnum.SUCCESSS.getCode());
            upstreamAgentBillInfoService.updateById(upstreamAgentBillInfo);
        } else {
            log.error("handleCpUpstreamAgentRecord  更新UpstreamAgentBillInfo表失败:{}平台账单流水号:{}", DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"), cpUpstreamAgentBillInfo.getTradeNo());
            PhoneOrderRecord record = phoneOrderRecordService.getOne(Wrappers.lambdaQuery(PhoneOrderRecord.class).eq(PhoneOrderRecord::getPlatformOrderNo, cpUpstreamAgentBillInfo.getTradeNo()));
            /*if (PlatformBillStatusEnum.UNBILL.getCode() == record.getPlatformBillStatus()) {
                cpShopAgentBillInfoService.removeById(cpShopAgentBillInfo);
            }*/
            return;
        }
        // 更新shopAgent余额
        //log.error("【上游代理商记账】");
//        UpstreamAgentUserInfo upstreamAgentUserInfo = upstreamAgentUserInfoService.getById(cpUpstreamAgentBillInfo.getTraderUserId());
        //log.error("【上游代理商记账】upstreamAgentUserInfo:{},时间:{}",upstreamAgentUserInfo.toString(),DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));

        UpstreamAgentUserBalance upstreamAgentUserBalance = upstreamAgentUserBalanceService.getOne(Wrappers.lambdaQuery(UpstreamAgentUserBalance.class)
                .eq(UpstreamAgentUserBalance::getUpstreamAgentUserId, upstreamAgentBillInfo.getTraderUserId())
                .eq(UpstreamAgentUserBalance::getAisleId, upstreamAgentBillInfo.getAisleId()));
        if (null == upstreamAgentUserBalance){
            log.error("==========【渠道记账】============渠道余额账户不存在,账单表信息为==={}", upstreamAgentBillInfo);
            throw new RuntimeException();
        }
        switch (cpUpstreamAgentBillInfo.getType()) {
            //渠道代理商无充值
            case BillInfoTypeConstant.BILLINFO_TYPE_RECHAGE:
                //提现
            case BillInfoTypeConstant.BILLINFO_TYPE_EXTRACTION:
                //提现后余额
                BigDecimal extractionBalance = upstreamAgentUserBalance.getBalance().subtract(cpUpstreamAgentBillInfo.getMoney());
                //提现后冻结金额
                BigDecimal extractionFrozenBalance = upstreamAgentUserBalance.getFrozenMoney().subtract(cpUpstreamAgentBillInfo.getMoney());
                upstreamAgentUserBalance.setBalance(extractionBalance);
                upstreamAgentUserBalance.setFrozenMoney(extractionFrozenBalance);
                break;
            //积攒
            case BillInfoTypeConstant.BILLINFO_TYPE_SAVE:
                //积攒后余额
                BigDecimal saveBalance = upstreamAgentUserBalance.getBalance().add(cpUpstreamAgentBillInfo.getMoney());
                upstreamAgentUserBalance.setBalance(saveBalance);
                break;
            //渠道代理商无消费
            case BillInfoTypeConstant.BILLINFO_TYPE_REDUCE:
                break;
            //扣除手续费
            case BillInfoTypeConstant.EXTRACTION_SERVICE_CHARGE:
                //扣除后余额
                BigDecimal serviceBalance = upstreamAgentUserBalance.getBalance().subtract(cpUpstreamAgentBillInfo.getMoney());
                upstreamAgentUserBalance.setBalance(serviceBalance);
                break;
        }
        upstreamAgentUserBalanceService.updateById(upstreamAgentUserBalance);
        //删除账单事务表记录
        boolean isDelete = cpUpstreamAgentBillInfoService.removeById(cpUpstreamAgentBillInfo.getId());
        if (!isDelete) {
            throw new RuntimeException();
        }
    }
//    @Override
//    @TransactionalWithRollback
//    public void handleOneCpUpstreamAgentRecordHook(CpUpstreamAgentBillInfo cpUpstreamAgentBillInfo) {
//        //更新UpstreamAgentBillInfo 表
//        UpstreamAgentBillInfo upstreamAgentBillInfo = upstreamAgentBillInfoService.queryBillInfoByTradeNo(cpUpstreamAgentBillInfo.getTradeNo());
//        if (null != upstreamAgentBillInfo) {
//            upstreamAgentBillInfo.setStatus(BillInfoStatusEnum.SUCCESSS.getCode());
//            upstreamAgentBillInfoService.updateById(upstreamAgentBillInfo);
//        } else {
//            log.error("handleCpUpstreamAgentRecord  更新UpstreamAgentBillInfo表失败:{}平台账单流水号:{}", DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"), cpUpstreamAgentBillInfo.getTradeNo());
//            PhoneOrderRecord record = phoneOrderRecordService.getOne(Wrappers.lambdaQuery(PhoneOrderRecord.class).eq(PhoneOrderRecord::getPlatformOrderNo, cpUpstreamAgentBillInfo.getTradeNo()));
//            /*if (PlatformBillStatusEnum.UNBILL.getCode() == record.getPlatformBillStatus()) {
//                cpShopAgentBillInfoService.removeById(cpShopAgentBillInfo);
//            }*/
//            return;
//        }
//        // 更新shopAgent余额
//        //log.error("【上游代理商记账】");
//        UpstreamAgentUserInfo upstreamAgentUserInfo = upstreamAgentUserInfoService.getById(cpUpstreamAgentBillInfo.getTraderUserId());
//        //log.error("【上游代理商记账】upstreamAgentUserInfo:{},时间:{}",upstreamAgentUserInfo.toString(),DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
//        switch (cpUpstreamAgentBillInfo.getType()) {
//            //渠道代理商无充值
//            case BillInfoTypeConstant.BILLINFO_TYPE_RECHAGE:
//                //提现
//            case BillInfoTypeConstant.BILLINFO_TYPE_EXTRACTION:
//                //提现后余额
//                BigDecimal extractionBalance = upstreamAgentUserInfo.getBalance().subtract(cpUpstreamAgentBillInfo.getMoney());
//                //提现后冻结金额
//                BigDecimal extractionFrozenBalance = upstreamAgentUserInfo.getFrozenMoney().subtract(cpUpstreamAgentBillInfo.getMoney());
//                upstreamAgentUserInfo.setBalance(extractionBalance);
//                upstreamAgentUserInfo.setFrozenMoney(extractionFrozenBalance);
//                break;
//            //积攒
//            case BillInfoTypeConstant.BILLINFO_TYPE_SAVE:
//                //积攒后余额
//                BigDecimal saveBalance = upstreamAgentUserInfo.getBalance().add(cpUpstreamAgentBillInfo.getMoney());
//                upstreamAgentUserInfo.setBalance(saveBalance);
//                break;
//            //渠道代理商无消费
//            case BillInfoTypeConstant.BILLINFO_TYPE_REDUCE:
//                break;
//            //扣除手续费
//            case BillInfoTypeConstant.EXTRACTION_SERVICE_CHARGE:
//                //扣除后余额
//                BigDecimal serviceBalance = upstreamAgentUserInfo.getBalance().subtract(cpUpstreamAgentBillInfo.getMoney());
//                upstreamAgentUserInfo.setBalance(serviceBalance);
//                break;
//        }
//        upstreamAgentUserInfoService.updateById(upstreamAgentUserInfo);
//        //删除账单事务表记录
//        boolean isDelete = cpUpstreamAgentBillInfoService.removeById(cpUpstreamAgentBillInfo.getId());
//        if (!isDelete) {
//            throw new RuntimeException();
//        }
//    }


}
