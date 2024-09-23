package com.push.dao.mapper.agent;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.push.common.webfacade.vo.platform.LoginLogVO;
import com.push.entity.agent.UpstreamAgentLogLogin;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * <p>
 * 上游代理商登录日志表 Mapper 接口
 * </p>
 *
 *

 * @since 2020-04-26
 */
public interface UpstreamAgentLogLoginMapper extends BaseMapper<UpstreamAgentLogLogin> {

    Page<LoginLogVO> getUpstreamAgentLoginLog(Page<LoginLogVO> page,
                                              @Param("userName") String userName,
                                              @Param("loginIp") String loginIP,
                                              @Param("startTime") Date startTime,
                                              @Param("endTime") Date endTime);
}
