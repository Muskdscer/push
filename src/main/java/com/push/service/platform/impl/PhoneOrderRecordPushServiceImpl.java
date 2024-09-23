package com.push.service.platform.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.push.common.constants.RedisConstant;
import com.push.common.enums.SignTypeEnum;
import com.push.common.utils.SignUtils;
import com.push.common.webfacade.bo.platform.PushOrderCallbackUpstreamBO;
import com.push.common.webfacade.vo.out.RedisCallbackOrderStatusToUpstreamVO;
import com.push.dao.mapper.platform.PhoneOrderRecordMapper;
import com.push.dao.mapper.platform.TpPhoneOrderCallBackMapper;
import com.push.entity.platform.PhoneOrderRecord;
import com.push.entity.platform.TpPhoneOrderCallBack;
import com.push.entity.upstream.UpstreamUserInfo;
import com.push.service.platform.PhoneOrderRecordPushService;
import com.push.service.platform.PhoneOrderRecordService;
import com.push.service.upstream.UpstreamCallBackService;
import com.push.service.upstream.UpstreamUserInfoService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Description:
 * Create DateTime: 2020/5/21 10:38
 *
 * 

 */
@Component
public class PhoneOrderRecordPushServiceImpl implements PhoneOrderRecordPushService {

    @Resource
    private PhoneOrderRecordMapper phoneOrderRecordMapper;

    @Resource
    private PhoneOrderRecordService phoneOrderRecordService;

    @Resource
    private UpstreamUserInfoService upstreamUserInfoService;

    @Resource
    private TpPhoneOrderCallBackMapper tpPhoneOrderCallBackMapper;

    @Resource
    private UpstreamCallBackService upstreamCallBackService;

    @Override
    public void pushOrderCallbackUpstream(PushOrderCallbackUpstreamBO bo) {
        List<PhoneOrderRecord> records = phoneOrderRecordService.listByIds(bo.getOrderIdList());
        records.forEach(phoneOrderRecord -> {
            UpstreamUserInfo upstreamUserInfo = upstreamUserInfoService.getOne(Wrappers.lambdaQuery(UpstreamUserInfo.class)
                    .eq(UpstreamUserInfo::getId, phoneOrderRecord.getUpstreamUserId()));
            TpPhoneOrderCallBack tpPhoneOrderCallBack = tpPhoneOrderCallBackMapper.selectOne(Wrappers.lambdaQuery(TpPhoneOrderCallBack.class)
                    .eq(TpPhoneOrderCallBack::getPlatformOrderNo, phoneOrderRecord.getPlatformOrderNo()));


            upstreamCallBackService.callbackUpstream(upstreamUserInfo, phoneOrderRecord,
                    tpPhoneOrderCallBack.getOrderSn(), tpPhoneOrderCallBack.getCertificate(), tpPhoneOrderCallBack.getStatus());
        });
    }
}
