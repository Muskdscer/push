package com.push.service.platform.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.push.common.webfacade.bo.platform.OperateBO;
import com.push.common.webfacade.vo.platform.OperateVO;
import com.push.dao.mapper.platform.PlatformLogOperateMapper;
import com.push.entity.platform.PlatformLogOperate;
import com.push.service.platform.PlatformLogOperateService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 平台用户操作日志表 服务实现类
 * </p>
 *
 * 

 * @since 2020-03-27
 */
@Service
public class PlatformLogOperateServiceImpl extends ServiceImpl<PlatformLogOperateMapper, PlatformLogOperate> implements PlatformLogOperateService {

    @Resource
    private PlatformLogOperateMapper platformLogOperateMapper;

    @Override
    public IPage<OperateVO> listPlatformOperateLog(OperateBO bo) {
        IPage<OperateVO> page = new Page<>(bo.getPageNo(), bo.getPageSize());
        if (bo.getEndTime() != null) {
            bo.setEndTime(DateUtil.offsetDay(bo.getEndTime(),1));
        }
        return platformLogOperateMapper.listPlatformOperateLog(page, bo.getUsername(), bo.getStartTime(), bo.getEndTime());
    }
}
