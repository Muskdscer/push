package com.push.dao.mapper.platform;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.push.entity.platform.CpTpPhoneOrderCallBack;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 回调订单临时表 Mapper 接口
 * </p>
 *
 *

 * @since 2020-04-01
 */
public interface CpTpPhoneOrderCallBackMapper extends BaseMapper<CpTpPhoneOrderCallBack> {

    void deleteBatchByOrderNo(@Param("ids") List<Long> ids);

    void saveBatch(@Param("cpTpList") List<CpTpPhoneOrderCallBack> cpTpList);

    void saveBatchShopOrderNoIsNull(@Param("cpTpListShopOrderNoIsNull") List<CpTpPhoneOrderCallBack> cpTpListShopOrderNoIsNull);

    void deleteBatchOrder(@Param("finalStatusCpTpOrderNo") List<String> finalStatusCpTpOrderNo);
}
