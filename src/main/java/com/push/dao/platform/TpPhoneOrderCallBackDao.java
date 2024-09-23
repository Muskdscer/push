package com.push.dao.platform;

import com.push.entity.platform.TpPhoneOrderCallBack;

import java.util.List;

/**
 * Description:
 * Create DateTime: 2020-04-10 13:15
 *
 * 

 */
public interface TpPhoneOrderCallBackDao {


    List<TpPhoneOrderCallBack> selectTpPhoneOrderCallBackByPlatformNos(List<String> platformOrderNoList);
}
