package com.push.dao.mapper.platform;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.push.common.webfacade.vo.platform.OperateVO;
import com.push.entity.platform.PlatformLogOperate;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * <p>
 * 平台用户操作日志表 Mapper 接口
 * </p>
 *
 * 

 * @since 2020-03-27
 */
public interface PlatformLogOperateMapper extends BaseMapper<PlatformLogOperate> {

    IPage<OperateVO> listPlatformOperateLog(@Param("page") IPage<OperateVO> page, @Param("username") String username, @Param("startTime") Date startTime, @Param("endTime") Date endTime);
}
