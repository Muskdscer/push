package com.push.dao.mapper.agent;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.push.common.webfacade.vo.platform.OperateVO;
import com.push.entity.agent.ShopAgentLogOperate;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * <p>
 * 操作日志表 Mapper 接口
 * </p>
 *
 *

 * @since 2020-04-26
 */
public interface ShopAgentLogOperateMapper extends BaseMapper<ShopAgentLogOperate> {

    IPage<OperateVO> listShopAgentOperateLog(IPage<OperateVO> page,
                                             @Param("username") String username,
                                             @Param("startTime") Date startTime,
                                             @Param("endTime") Date endTime);

}
