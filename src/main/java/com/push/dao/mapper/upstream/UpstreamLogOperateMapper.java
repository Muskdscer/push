package com.push.dao.mapper.upstream;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.push.common.webfacade.vo.platform.OperateVO;
import com.push.entity.upstream.UpstreamLogOperate;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * <p>
 * 上游操作日志表 Mapper 接口
 * </p>
 *
 * 

 * @since 2020-03-27
 */
public interface UpstreamLogOperateMapper extends BaseMapper<UpstreamLogOperate> {

    IPage<OperateVO> listPlatformOperateLog(@Param("page") IPage<OperateVO> page, @Param("username") String username, @Param("startTime") Date startTime, @Param("endTime") Date endTime);
}
