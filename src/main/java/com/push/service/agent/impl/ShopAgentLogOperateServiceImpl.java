package com.push.service.agent.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.push.common.webfacade.bo.platform.AgentOperateBO;
import com.push.common.webfacade.vo.platform.OperateVO;
import com.push.dao.mapper.agent.ShopAgentLogOperateMapper;
import com.push.entity.agent.ShopAgentLogOperate;
import com.push.service.agent.ShopAgentLogOperateService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 操作日志表 服务实现类
 * </p>
 *
 * 

 * @since 2020-04-26
 */
@Service
public class ShopAgentLogOperateServiceImpl extends ServiceImpl<ShopAgentLogOperateMapper, ShopAgentLogOperate> implements ShopAgentLogOperateService {

    @Resource
    private ShopAgentLogOperateMapper shopAgentLogOperateMapper;

    @Override
    public IPage<OperateVO> listShopAgentOperateLog(AgentOperateBO bo) {
        IPage<OperateVO> page = new Page<>(bo.getPageNo(), bo.getPageSize());
        return shopAgentLogOperateMapper.listShopAgentOperateLog(page, bo.getUsername(), bo.getStartTime(), bo.getEndTime());
    }
}
