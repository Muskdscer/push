package com.push.service.upstream;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.push.common.webfacade.bo.LoginLogBO;
import com.push.common.webfacade.vo.platform.LoginLogVO;
import com.push.entity.upstream.UpstreamLogLogin;

/**
 * <p>
 * 上游登录日志表 服务类
 * </p>
 *
 *

 * @since 2020-03-27
 */
public interface UpstreamLogLoginService extends IService<UpstreamLogLogin> {

    Page<LoginLogVO> getUpstreamLoginLog(LoginLogBO bo);
}
