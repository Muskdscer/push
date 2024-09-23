package com.push.dao.mapper.agent;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.push.common.webfacade.vo.platform.OperateVO;
import com.push.entity.agent.UpstreamAgentLogOperate;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * <p>
 * 上游代理商操作日志表 Mapper 接口
 * </p>
 *
 * 

 * @since 2020-04-26
 */
public interface UpstreamAgentLogOperateMapper extends BaseMapper<UpstreamAgentLogOperate> {

    IPage<OperateVO> listShopAgentOperateLog(Page<Object> page,
                                             @Param("username") String username,
                                             @Param("startTime") Date startTime,
                                             @Param("endTime") Date endTime);
}
