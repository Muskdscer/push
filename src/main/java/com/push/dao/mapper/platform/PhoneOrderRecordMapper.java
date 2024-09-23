package com.push.dao.mapper.platform;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.push.common.webfacade.vo.platform.*;
import com.push.common.webfacade.vo.platform.statistics.StatisticUpstreamCallbackFailVO;
import com.push.entity.platform.PhoneOrderRecord;
import com.push.entity.upstream.UpstreamUserInfo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 可用订单表 Mapper 接口
 * </p>
 *
 *

 * @since 2020-03-27
 */
public interface PhoneOrderRecordMapper extends BaseMapper<PhoneOrderRecord> {

    void updateBatchByOrderNum(@Param("orderNums") List<String> orderNums, @Param("id") Long id, @Param("status") Integer status);

    /**
     * 拿到成功的队列后设置推送状态和平台订单状态
     *
     * @param phoneOrderNos  平台订单号
     * @param pushNum        推送次数
     * @param platformStatus 平台订单状态
     * @param pushStatus     推送状态
     */
    Integer updateStatusBatchByOrderNum(@Param("phoneOrderNos") List<String> phoneOrderNos, @Param("platformStatus") Integer platformStatus, @Param("pushStatus") Integer pushStatus, @Param("pushNum") Integer pushNum);

    List<StatisticShopCallbackVO> getStatisticShop();

    List<StatisticUpstreamCallbackVO> getStatisticUpstream();

    BigDecimal getDealMoney(@Param("begin") DateTime todayBegin, @Param("end") DateTime todayEnd, @Param("platformOrderStatus") Integer platformOrderStatus);

    BigDecimal getDealFailMoney(@Param("begin") DateTime todayBegin, @Param("end") DateTime todayEnd,
                                @Param("platformOrderStatus3") Integer code, @Param("platformOrderStatus4") Integer code1);

    IPage<PhoneOrderRecordVO> listByShopIds(Page<PhoneOrderRecordVO> page, @Param("phoneNum") String phoneNum, @Param("phoneOperator") String phoneOperator, @Param("platformOrderStatus") Integer platformOrderStatus, @Param("upstreamOrderNo") String upstreamOrderNo, @Param("shopOrderNo") String shopOrderNo, @Param("platformOrderNo") String platformOrderNo, @Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("ids") List<Long> ids);

    IPage<PhoneOrderRecordVO> listByUpstreamIds(Page<PhoneOrderRecordVO> page, @Param("phoneNum") String phoneNum, @Param("phoneOperator") String phoneOperator, @Param("platformOrderStatus") Integer platformOrderStatus, @Param("upstreamOrderNo") String upstreamOrderNo, @Param("shopOrderNo") String shopOrderNo, @Param("platformOrderNo") String platformOrderNo, @Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("ids") List<Long> ids);

    List<StatisticUpstreamCallbackFailVO> getStatisticUpstreamCallBackFail();
    IPage<PhoneOrderRecordVO> listByUpstreamIds(Page<PhoneOrderRecordVO> page, @Param("upstreamOrderNo") String upstreamOrderNo, @Param("shopOrderNo") String shopOrderNo, @Param("platformOrderNo") String platformOrderNo, @Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("ids") List<Long> ids);

    UpstreamUserInfo selectUpstreamUserInfoByPlatformOrderNo(@Param("platformOrderNo") String platformOrderNo);

    void updateByOrderNo(@Param("orderNoList") List<String> orderNoList);

    void updateBatchByOrderNo(@Param("orderVOs") List<TpShopUpdateOrderVO> orderVOs);

    List<PhoneOrderRecord> getOrderBatchByOrderNo(@Param("platformOrderNos") List<String> platformOrderNos);

    List<PhoneOrderRecord> getBatchByPlatformOrderNo(@Param("platformOrderNos") List<String> platformOrderNos);

    void updateBatchByPlatformOrderNo(@Param("records") List<PhoneOrderRecord> records);

    List<UpOrderCountGroupVO> getSumOrderCount(@Param("upstreamUserId") Long upstreamUserId, @Param("rate") Double rate);

    List<DownOrderCountGroupVO> getSumOrderCountToShop(@Param("startTime") Date startTime, @Param("endTime") DateTime endTime);

    int batchUpdateShopOrderNoByPlatformOrderNo(@Param("list") List<PhoneOrderRecord> phoneOrderRecordList);

    int updateBatchOrderFailById(@Param("records") List<PhoneOrderRecord> records);
}
