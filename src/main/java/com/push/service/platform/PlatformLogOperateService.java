package com.push.service.platform;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.push.common.webfacade.bo.platform.OperateBO;
import com.push.common.webfacade.vo.platform.OperateVO;
import com.push.entity.platform.PlatformLogOperate;

/**
 * <p>
 * 平台用户操作日志表 服务类
 * </p>
 *
 * 

 * @since 2020-03-27
 */
public interface PlatformLogOperateService extends IService<PlatformLogOperate> {

    IPage<OperateVO> listPlatformOperateLog(OperateBO bo);
}
