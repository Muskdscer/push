package com.push.dao.mapper.agent;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.push.entity.agent.CpShopAgentBillInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 商户代理商账单事务表 Mapper 接口
 * </p>
 *
 * 

 * @since 2020-04-26
 */
public interface CpShopAgentBillInfoMapper extends BaseMapper<CpShopAgentBillInfo> {

    void saveBatch(@Param("cpShopAgentBillInfos") List<CpShopAgentBillInfo> cpShopAgentBillInfos);
}
