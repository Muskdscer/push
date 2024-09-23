package com.push.dao.mapper.platform;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.push.common.webfacade.vo.platform.PhoneOrderWaitCheck;
import com.push.entity.platform.TpPhoneOrderCallBack;
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
public interface TpPhoneOrderCallBackMapper extends BaseMapper<TpPhoneOrderCallBack> {

    void saveOrUpdateByPlatformOrderNo(@Param("order") TpPhoneOrderCallBack tpPhoneOrderCallBack);

    void updateBatch(@Param("ids") List<Long> ids);

    void saveBatch(@Param("tpList") List<TpPhoneOrderCallBack> tpList);

    void saveBatchShopOrderNoIsNull(@Param("tpListShopOrderNoIsNull") List<TpPhoneOrderCallBack> tpListShopOrderNoIsNull);

    List<String> getBatch(@Param("orderWaitCheckNotNulls") List<PhoneOrderWaitCheck> orderWaitCheckNotNulls);
}
