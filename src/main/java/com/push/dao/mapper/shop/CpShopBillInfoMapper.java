package com.push.dao.mapper.shop;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.push.entity.shop.CpShopBillInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 商户账单事务表 Mapper 接口
 * </p>
 *
 * 

 * @since 2020-03-27
 */
public interface CpShopBillInfoMapper extends BaseMapper<CpShopBillInfo> {

    void saveBatch(@Param("cpShopBillInfos") List<CpShopBillInfo> cpShopBillInfos);
}
