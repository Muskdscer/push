package com.push.service.upstream.impl;

import com.alibaba.fastjson.JSON;
import com.push.common.constants.RedisConstant;
import com.push.common.enums.SignTypeEnum;
import com.push.common.utils.SignUtils;
import com.push.common.webfacade.vo.out.RedisCallbackOrderStatusToUpstreamVO;
import com.push.entity.platform.PhoneOrderRecord;
import com.push.entity.upstream.UpstreamUserInfo;
import com.push.service.upstream.UpstreamCallBackService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @since 2020-06-13
 */
@Service
public class UpstreamCallBackServiceImpl implements UpstreamCallBackService {

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public void callbackUpstream(UpstreamUserInfo upstreamUserInfo, PhoneOrderRecord phoneOrderRecord,
                                 String orderSn, String certificate, Integer status) {
        //向Redis中发送数据
        RedisCallbackOrderStatusToUpstreamVO vo = new RedisCallbackOrderStatusToUpstreamVO();
        vo.setAppId(upstreamUserInfo.getAppId());
        TreeMap<String, String> data = new TreeMap<>();
        data.put("a", upstreamUserInfo.getAppKey());
        data.put("b", upstreamUserInfo.getAppId());
        data.put("c", phoneOrderRecord.getPlatformOrderNo());
        data.put("d", phoneOrderRecord.getPhoneNum());
        data.put("e", phoneOrderRecord.getOrderPrice().toPlainString());
        data.put("f", phoneOrderRecord.getPhoneOperator());
        data.put("g", upstreamUserInfo.getAppKey());
        String sign = SignUtils.generateSignature(data, SignTypeEnum.SHA256);
        vo.setSign(sign);
        vo.setPhoneOperator(phoneOrderRecord.getPhoneOperator());
        vo.setPhoneNum(phoneOrderRecord.getPhoneNum());
        vo.setOrderNo(phoneOrderRecord.getUpstreamOrderNo());
        vo.setBizOrderNo(phoneOrderRecord.getPlatformOrderNo());
        vo.setOrderPrice(phoneOrderRecord.getOrderPrice());
        vo.setType(phoneOrderRecord.getType());
        vo.setCity(phoneOrderRecord.getCity());
        vo.setProvince(phoneOrderRecord.getProvince());
        vo.setOrderSn(orderSn);
        vo.setStatus(status);
        vo.setCertificate(certificate);
        Map<String, Object> map = new HashMap<>(2);
        map.put("data", JSON.toJSONString(vo));
        map.put("url", upstreamUserInfo.getPushSite());

        if (upstreamUserInfo.getPushSite().contains("127.0.0.1") || StringUtils.isBlank(upstreamUserInfo.getPushSite())) {
            redisTemplate.opsForList().leftPush(RedisConstant.PLATFORM_IMPORT_ORDER_SHARE_DATA, JSON.toJSONString(vo));
        } else {
            redisTemplate.opsForList().leftPush(RedisConstant.PUSH_ORDER_UP_STREAM_WAIT_CALL_BACK, JSON.toJSONString(map));
        }
    }
}
