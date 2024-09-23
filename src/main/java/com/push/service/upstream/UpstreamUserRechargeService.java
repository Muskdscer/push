package com.push.service.upstream;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.push.common.webfacade.bo.platform.UpstreamRechargeBO;
import com.push.common.webfacade.vo.platform.CheckRechargeVO;
import com.push.entity.upstream.UpstreamUserInfo;
import com.push.entity.upstream.UpstreamUserRecharge;

/**
 * <p>
 * 上游用户充值表 服务类
 * </p>
 *
 * 

 * @since 2020-03-27
 */
public interface UpstreamUserRechargeService extends IService<UpstreamUserRecharge> {

    Page<CheckRechargeVO> getUpstreamRechargeToDoList(Integer code, UpstreamRechargeBO bo);

    Boolean operatorSuccess(UpstreamUserRecharge upstreamUserRecharge, UpstreamUserInfo upstreamUserInfo);

    boolean failUpstreamUserRecharge(UpstreamUserRecharge upstreamUserRecharge);
}
