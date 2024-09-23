package com.push.dao.mapper.platform;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.push.entity.platform.PhoneOrderRecord;
import com.push.entity.platform.PhoneOrderTimeOut;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Description:
 * Create DateTime: 2020/6/10 16:50
 *
 *

 */
public interface PhoneOrderTimeOutMapper extends BaseMapper<PhoneOrderTimeOut> {

    /**
     * 根据订单状态删除超时订单
     */
    int deleteByOrderStatus();

    /**
     * 分页查询超时订单列表
     *
     * @param page            分页对象
     * @param platformOrderNo 平台订单号
     * @param upstreamOrderNo 渠道订单号
     * @param phoneNum        手机号
     * @param startTime       开始时间
     * @param endTime         结束时间
     * @return 超时订单列表
     */
    Page<PhoneOrderRecord> selectOrderTimeOutList(Page<Object> page,
                                                  @Param("platformOrderNo") String platformOrderNo,
                                                  @Param("upstreamOrderNo") String upstreamOrderNo,
                                                  @Param("phoneNum") String phoneNum,
                                                  @Param("shopName") String shopName,
                                                  @Param("upstreamName") String upstreamName,
                                                  @Param("pushStatus") String pushStatus,
                                                  @Param("startTime") Date startTime,
                                                  @Param("endTime") Date endTime);
}
