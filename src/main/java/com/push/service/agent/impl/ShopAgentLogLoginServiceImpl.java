package com.push.service.agent.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.push.common.webfacade.bo.LoginLogBO;
import com.push.common.webfacade.vo.platform.LoginLogVO;
import com.push.dao.mapper.agent.ShopAgentLogLoginMapper;
import com.push.entity.agent.ShopAgentLogLogin;
import com.push.service.agent.ShopAgentLogLoginService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 商户代理商登录日志表 服务实现类
 * </p>
 *
 * 

 * @since 2020-04-26
 */
@Service
public class ShopAgentLogLoginServiceImpl extends ServiceImpl<ShopAgentLogLoginMapper, ShopAgentLogLogin> implements ShopAgentLogLoginService {

    @Resource
    private ShopAgentLogLoginMapper shopAgentLogLoginMapper;

    @Override
    public Page<LoginLogVO> getShopLoginLog(LoginLogBO bo) {
        Page<LoginLogVO> page = new Page<>(bo.getPageNo(), bo.getPageSize());
        return shopAgentLogLoginMapper.getShopLoginLog(page, bo.getUserName(), bo.getLoginIp(), bo.getStartTime(), bo.getEndTime());
    }
}
