package com.push.service.platform;

import com.baomidou.mybatisplus.extension.service.IService;
import com.push.entity.platform.TpPhoneOrderCallBack;

import java.util.List;

/**
 * <p>
 * 回调订单临时表 服务类
 * </p>
 *
 *

 * @since 2020-04-01
 */
public interface TpPhoneOrderCallBackService extends IService<TpPhoneOrderCallBack> {

    List<TpPhoneOrderCallBack> getTpPhoneOrderCallBackByPlatformNos(List<String> platformOrderNoList);

    boolean saveOrUpdateByPlatformOrderNo(TpPhoneOrderCallBack tpPhoneOrderCallBack);

}
