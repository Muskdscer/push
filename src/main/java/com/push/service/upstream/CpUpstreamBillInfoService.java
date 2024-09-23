package com.push.service.upstream;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.push.entity.upstream.CpUpstreamBillInfo;

/**
 * <p>
 * 上游账单事务表 服务类
 * </p>
 *
 * 

 * @since 2020-03-27
 */
public interface CpUpstreamBillInfoService extends IService<CpUpstreamBillInfo> {

    Page<CpUpstreamBillInfo> findAllByState(Integer code);
}
