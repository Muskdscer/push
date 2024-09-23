package com.push.service.upstream;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.push.common.webfacade.bo.platform.OperateBO;
import com.push.common.webfacade.vo.platform.OperateVO;
import com.push.entity.upstream.UpstreamLogOperate;

/**
 * <p>
 * 上游操作日志表 服务类
 * </p>
 *
 *

 * @since 2020-03-27
 */
public interface UpstreamLogOperateService extends IService<UpstreamLogOperate> {

    IPage<OperateVO> listShopOperateLog(OperateBO bo);
}
