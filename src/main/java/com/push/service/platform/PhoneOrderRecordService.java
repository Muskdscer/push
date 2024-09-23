package com.push.service.platform;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.push.common.webfacade.bo.agent.AgentPhoneOrderRecordBO;
import com.push.common.webfacade.vo.platform.*;
import com.push.common.webfacade.vo.platform.statistics.StatisticUpstreamCallbackFailVO;
import com.push.entity.platform.PhoneOrderRecord;
import com.push.entity.upstream.UpstreamUserInfo;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 可用订单表 服务类
 * </p>
 *
 *

 * @since 2020-03-27
 */
public interface PhoneOrderRecordService extends IService<PhoneOrderRecord> {

    /**
     * 根据平台订单号list查询订单记录
     *
     * @param platformOrderNos 平台订单号list
     * @return 订单记录
     */
    List<PhoneOrderRecord> getPhoneOrderRecordByPlatformOrderNos(List<String> platformOrderNos);

    List<StatisticShopCallbackVO> getStatisticShop();

    List<StatisticUpstreamCallbackVO> getStatisticUpstream();

    List<StatisticUpstreamCallbackFailVO> getStatisticUpstreamCallBackFail();

    IPage<PhoneOrderRecordVO> listByShopIds(AgentPhoneOrderRecordBO bo, List<Long> ids);

    IPage<PhoneOrderRecordVO> listByUpstreamIds(AgentPhoneOrderRecordBO bo, List<Long> ids);

    UpstreamUserInfo getUpstreamUserInfoByPlatformOrderNo(String platformOrderNo);

    void operatorSuccess(PhoneOrderRecord phoneOrderRecord);

    List<UpOrderCountGroupVO> getSumOrderCount(Long upstreamUserId, Double rate);

    List<DownOrderCountGroupVO> getSumOrderCountToShop(Date startTime, DateTime endTime);

    /**
     * 订单置失败
     *
     * @param platformOrderNoList 订单号集合
     */
    void changeOrderFailed(List<String> platformOrderNoList);

    /**
     * 根据平台订单号更新商户订单号
     *
     * @param phoneOrderRecordList 订单记录集合
     */
    void batchUpdateShopOrderNoByPlatformOrderNo(List<PhoneOrderRecord> phoneOrderRecordList);
}
