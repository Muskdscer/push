package com.push.dao.mapper.platform;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.push.entity.platform.CpPlatformBillInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 平台账单事务表 Mapper 接口
 * </p>
 *
 * 

 * @since 2020-03-27
 */
public interface CpPlatformBillInfoMapper extends BaseMapper<CpPlatformBillInfo> {

    void saveBatch(@Param("cpPlatformBillInfos") List<CpPlatformBillInfo> cpPlatformBillInfos);
}
