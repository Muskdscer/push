package com.push.service.shop.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.push.common.webfacade.bo.platform.OperateBO;
import com.push.common.webfacade.vo.platform.OperateVO;
import com.push.dao.mapper.shop.ShopLogOperateMapper;
import com.push.entity.shop.ShopLogOperate;
import com.push.service.shop.ShopLogOperateService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 操作日志表 服务实现类
 * </p>
 *
 * 

 * @since 2020-03-27
 */
@Service
public class ShopLogOperateServiceImpl extends ServiceImpl<ShopLogOperateMapper, ShopLogOperate> implements ShopLogOperateService {

    @Resource
    private ShopLogOperateMapper shopLogOperateMapper;

    @Override
    public IPage<OperateVO> listShopOperateLog(OperateBO bo) {
        IPage<OperateVO> page = new Page<>(bo.getPageNo(), bo.getPageSize());
        if (bo.getEndTime() != null) {
            bo.setEndTime(DateUtil.offsetDay(bo.getEndTime(),1));
        }
        return shopLogOperateMapper.listPlatformOperateLog(page, bo.getUsername(), bo.getStartTime(), bo.getEndTime());
    }
}
