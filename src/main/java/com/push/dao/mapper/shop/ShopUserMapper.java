package com.push.dao.mapper.shop;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.push.common.webfacade.vo.platform.ShopUserInfoVO;
import com.push.entity.shop.ShopUserInfo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * Description:
 * Create DateTime: 2020-03-26 11:33
 *
 *

 */
public interface ShopUserMapper extends BaseMapper<ShopUserInfo> {

    ShopUserInfoVO selectByIdWithMatch(@Param("id") Long id, @Param("code") int code);

    IPage<ShopUserInfoVO> pageWithMatch(@Param("page") IPage<ShopUserInfoVO> page, @Param("userName") String userName, @Param("shopName") String shopName, @Param("phoneNumber") String phoneNumber, @Param("endTime") Date endTime, @Param("startTime") Date startTime);
}
