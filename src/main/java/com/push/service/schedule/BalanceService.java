package com.push.service.schedule;

import com.push.entity.agent.CpShopAgentBillInfo;
import com.push.entity.agent.CpUpstreamAgentBillInfo;
import com.push.entity.platform.CpPlatformBillInfo;
import com.push.entity.shop.CpShopBillInfo;
import com.push.entity.upstream.CpUpstreamBillInfo;

/**
 * Description:
 * Create DateTime: 2020/3/31 18:47
 *
 * 

 */
public interface BalanceService {

    //上游记账操作
    void handleCpUpstreamRecord();

    /**==========0818墨子废弃，handleCpUpstreamRecordHook， 使用newhandleCpUpstreamRecordHook=======================*/
//    void handleCpUpstreamRecordHook(CpUpstreamBillInfo cpUpstreamBillInfo);
    // 记账处理方式
    void newhandleCpUpstreamRecordHook(CpUpstreamBillInfo cpUpstreamBillInfo);

    //商户记账操作
    void handleCpShopRecord();

    void handleCpShopRecordHook(CpShopBillInfo cpShopBillInfo);

    //平台记账操作
    void handleCpPlatformRecord();

    void handleOneCpPlatformRecordHook(CpPlatformBillInfo cpPlatformBillInfo);

    //商户代理商记账操作
    void handleCpShopAgentRecord();

    void handleOneCpShopAgentRecordHook(CpShopAgentBillInfo cpShopAgentBillInfo);

    void handleCpUpstreamAgentRecord();

    /**==========0818墨子废弃，handleOneCpUpstreamAgentRecordHook， 使用newhandleOneCpUpstreamAgentRecordHook=======================*/
//    void handleOneCpUpstreamAgentRecordHook(CpUpstreamAgentBillInfo cpUpstreamAgentBillInfo);
    void newhandleOneCpUpstreamAgentRecordHook(CpUpstreamAgentBillInfo cpUpstreamAgentBillInfo);
}
