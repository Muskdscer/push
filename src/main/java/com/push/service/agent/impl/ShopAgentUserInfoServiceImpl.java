package com.push.service.agent.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.push.common.webfacade.vo.platform.ShopAgentMapVO;
import com.push.dao.mapper.agent.ShopAgentMapInfoMapper;
import com.push.dao.mapper.agent.ShopAgentUserInfoMapper;
import com.push.dao.mapper.shop.ShopUserMapper;
import com.push.entity.agent.ShopAgentUserInfo;
import com.push.service.agent.ShopAgentUserInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 商户代理商信息表 服务实现类
 * </p>
 *
 * 

 * @since 2020-04-26
 */
@Service
public class ShopAgentUserInfoServiceImpl extends ServiceImpl<ShopAgentUserInfoMapper, ShopAgentUserInfo> implements ShopAgentUserInfoService {

    @Resource
    private ShopAgentUserInfoMapper shopAgentUserInfoMapper;

    @Resource
    private ShopAgentMapInfoMapper shopAgentMapInfoMapper;

    @Resource
    private ShopUserMapper shopUserMapper;

    @Override
    public Page<ShopAgentMapVO> getShopAgentMap(Page<ShopAgentMapVO> page, Long id) {
        return shopAgentMapInfoMapper.getShopAgentMap(page,id);
    }
}
