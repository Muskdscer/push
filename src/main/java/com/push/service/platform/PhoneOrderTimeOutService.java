package com.push.service.platform;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.push.entity.platform.PhoneOrderRecord;
import com.push.entity.platform.PhoneOrderTimeOut;

import java.util.Date;
import java.util.List;

/**
 * Description:
 * Create DateTime: 2020/6/10 16:49
 *
 *

 */
public interface PhoneOrderTimeOutService extends IService<PhoneOrderTimeOut> {

    /**
     * 根据订单状态移除超时订单
     */
    void removeByOrderStatus();

    /**
     * 分页查询超时订单列表
     *
     * @param platformOrderNo 平台订单号
     * @param upstreamOrderNo 渠道订单号
     * @param phoneNum        手机号
     * @param pushStatus      推送状态
     * @param startTime       开始时间
     * @param endTime         结束时间
     * @return 超时订单列表
     */
    Page<PhoneOrderRecord> queryOrderTimeOutList(String platformOrderNo, String upstreamOrderNo, String phoneNum, String pushStatus,
                                                 String shopName, String upstreamName,
                                                 Date startTime, Date endTime, Long pageNo, Long pageSize);

}
