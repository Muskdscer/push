package com.push.schedule;

import com.push.service.schedule.BalanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Calendar;

/**
 * Description: 余额定时任务
 * Create DateTime: 2020/3/31 18:46
 *
 *

 */
@Slf4j
@Component
public class BalanceTask {

    @Resource
    private BalanceService balanceService;

    /**
     * 处理渠道账单
     * <p>
     * 每5秒执行一次
     */
    @Scheduled(fixedDelay = 500)
    public void upstreamBalanceTask() {
//        log.info("【渠道记账】记账操作开始:{}", DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
        balanceService.handleCpUpstreamRecord();
//        log.info("【渠道记账】记账操作结束:{}", DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 处理商户账单
     * <p>
     * 每5秒执行一次
     */
    @Scheduled(fixedDelay = 500)
    public void shopBalanceTask() {
//        log.info("【商户记账】记账操作开始:{}", DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
        balanceService.handleCpShopRecord();
//        log.info("【商户记账】记账操作结束:{}", DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 处理平台账单
     * <p>
     * 每5秒执行一次
     */
    @Scheduled(fixedDelay = 500)
    public void platformBalanceTask() {
//        log.info("【平台记账】记账操作开始:{}", DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
        balanceService.handleCpPlatformRecord();
//        log.info("【平台记账】记账操作开始:{}", DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 处理商户代理商账单
     * <p>
     * 每5秒执行一次
     */
    @Scheduled(fixedDelay = 500)
    public void shopAgentBalanceTask() {
//        log.info("【平台记账】记账操作开始:{}", DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
        balanceService.handleCpShopAgentRecord();
//        log.info("【平台记账】记账操作开始:{}", DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
    }
    /**
     * 处理渠道代理商账单
     * <p>
     * 每5秒执行一次
     */
    @Scheduled(fixedDelay = 500)
    public void upstreamAgentBalanceTask() {
//        log.info("【平台记账】记账操作开始:{}", DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
        balanceService.handleCpUpstreamAgentRecord();
//        log.info("【平台记账】记账操作开始:{}", DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
    }
}
