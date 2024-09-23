package com.push.dao.mapper.shop;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.push.common.webfacade.vo.platform.LoginLogVO;
import com.push.entity.shop.ShopLogLogin;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * <p>
 * 商户登录日志表 Mapper 接口
 * </p>
 *
 * 

 * @since 2020-03-27
 */
public interface ShopLogLoginMapper extends BaseMapper<ShopLogLogin> {

    Page<LoginLogVO> getShopLoginLog(Page<LoginLogVO> page, @Param("userName") String userName,
                                     @Param("loginIp") String loginIp,
                                     @Param("startTime") Date startTime,
                                     @Param("endTime") Date endTime);
}
