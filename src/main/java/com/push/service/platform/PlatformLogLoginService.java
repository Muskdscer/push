package com.push.service.platform;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.push.common.webfacade.bo.LoginLogBO;
import com.push.common.webfacade.vo.platform.LoginLogVO;
import com.push.entity.platform.PlatformLogLogin;

/**
 * <p>
 * 登录日志表 服务类
 * </p>
 *
 * 

 * @since 2020-03-27
 */
public interface PlatformLogLoginService extends IService<PlatformLogLogin> {

    Page<LoginLogVO> getPlatformLoginLog(LoginLogBO bo);
}
