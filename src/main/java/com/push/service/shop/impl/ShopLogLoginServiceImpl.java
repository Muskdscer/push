package com.push.service.shop.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.push.common.webfacade.bo.LoginLogBO;
import com.push.common.webfacade.vo.platform.LoginLogVO;
import com.push.dao.mapper.shop.ShopLogLoginMapper;
import com.push.entity.shop.ShopLogLogin;
import com.push.service.shop.ShopLogLoginService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 商户登录日志表 服务实现类
 * </p>
 *
 * 

 * @since 2020-03-27
 */
@Service
public class ShopLogLoginServiceImpl extends ServiceImpl<ShopLogLoginMapper, ShopLogLogin> implements ShopLogLoginService {
    @Resource
    private ShopLogLoginMapper shopLogLoginMapper;

    @Override
    public Page<LoginLogVO> getShopLoginLog(LoginLogBO bo) {
        Page<LoginLogVO> page = new Page<>(bo.getPageNo(), bo.getPageSize());
        return shopLogLoginMapper.getShopLoginLog(page, bo.getUserName(), bo.getLoginIp(), bo.getStartTime(), bo.getEndTime());
    }
}
