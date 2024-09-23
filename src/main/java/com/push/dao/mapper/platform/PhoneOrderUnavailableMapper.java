package com.push.dao.mapper.platform;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
public interface PhoneOrderUnavailableMapper extends BaseMapper<PhoneOrderUnavailable> {

    void saveBatch(@Param("phoneOrderUnavailableList") List<PhoneOrderUnavailable> phoneOrderUnavailableList);
}
