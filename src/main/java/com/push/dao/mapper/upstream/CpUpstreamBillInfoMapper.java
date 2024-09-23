package com.push.dao.mapper.upstream;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.push.entity.upstream.CpUpstreamBillInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 上游账单事务表 Mapper 接口
 * </p>
 *
 * 

 * @since 2020-03-27
 */
public interface CpUpstreamBillInfoMapper extends BaseMapper<CpUpstreamBillInfo> {

    void saveBatch(@Param("cpUpstreamBillInfos") List<CpUpstreamBillInfo> cpUpstreamBillInfos);

    List<CpUpstreamBillInfo> getBatch(@Param("platformOrderNos") List<String> platformOrderNos);
}
