package com.push.service.platform;

import com.baomidou.mybatisplus.extension.service.IService;
import com.push.entity.platform.CpPlatformBillInfo;

import java.util.List;

/**
 * <p>
 * 平台账单事务表 服务类
 * </p>
 *
 * 

 * @since 2020-03-27
 */
public interface CpPlatformBillInfoService extends IService<CpPlatformBillInfo> {

    List<CpPlatformBillInfo> findAllByState(Integer code);
}
