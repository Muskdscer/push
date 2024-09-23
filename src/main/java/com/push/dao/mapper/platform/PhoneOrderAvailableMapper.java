package com.push.dao.mapper.platform;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.push.entity.platform.PhoneOrderAvailable;
import com.push.entity.platform.PhoneOrderUnavailable;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 可用订单表 Mapper 接口
 * </p>
 *
 *

 * @since 2020-03-27
 */
public interface PhoneOrderAvailableMapper extends BaseMapper<PhoneOrderAvailable> {

    void updateBatchByOrderNum(@Param("orderNums") List<String> orderNums, @Param("id") Long id, @Param("status") Integer status);

    /**
     * 拿到成功的队列后设置推送状态和平台订单状态
     *
     * @param phoneOrderNos  平台订单号
     * @param pushNum        推送次数
     * @param platformStatus 平台订单状态
     * @param pushStatus     推送状态
     */
    void updateStatusBatchByOrderNum(@Param("phoneOrderNos") List<String> phoneOrderNos, @Param("platformStatus") Integer platformStatus, @Param("pushStatus") Integer pushStatus, @Param("pushNum") Integer pushNum);

    Integer deleteBatchOrderNos(@Param("orderNums") List<String> orderNums);

    void deleteBatchByOrderNo(@Param("platformOrderNos") List<String> platformOrderNo);

    List<PhoneOrderAvailable> getBatchByPlatformOrderNo(@Param("platformOrderNos") List<String> platformOrderNos);

}
