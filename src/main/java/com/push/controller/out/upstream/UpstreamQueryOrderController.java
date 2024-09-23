package com.push.controller.out.upstream;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.push.common.controller.BaseController;
import com.push.common.controller.ResultMapInfo;
import com.push.common.enums.DeleteFlagEnum;
import com.push.common.enums.OrderStatusCodeEnum;
import com.push.common.enums.SignTypeEnum;
import com.push.common.enums.UpstreamCallBackStatusEnum;
import com.push.common.utils.SignUtils;
import com.push.common.webfacade.bo.out.UpstreamCheckOrderBO;
import com.push.common.webfacade.vo.out.UpstreamCheckOrderVO;
import com.push.entity.platform.PhoneOrderRecord;
import com.push.entity.platform.TpPhoneOrderCallBack;
import com.push.entity.platform.TpPhoneOrderUpstreamQueryLog;
import com.push.entity.upstream.UpstreamUserInfo;
import com.push.service.platform.PhoneOrderRecordService;
import com.push.service.platform.TpPhoneOrderCallBackService;
import com.push.service.platform.TpPhoneOrderUpstreamQueryLogService;
import com.push.service.upstream.UpstreamUserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;
import java.util.TreeMap;

/**
 * Description: 上游用户查询订单Controller
 * Create DateTime: 2020/4/28 14:31
 *
 * 

 */
@RestController
@Slf4j
@RequestMapping("out")
public class UpstreamQueryOrderController extends BaseController {

    @Resource
    private PhoneOrderRecordService phoneOrderRecordService;
    @Resource
    private UpstreamUserInfoService upstreamUserInfoService;
    @Resource
    private TpPhoneOrderUpstreamQueryLogService tpPhoneOrderUpstreamQueryLogService;
    @Resource
    private TpPhoneOrderCallBackService tpPhoneOrderCallBackService;

    @PostMapping("queryOrder")
    public Map<String, Object> acquireOrder(@RequestBody UpstreamCheckOrderBO bo) {
        bo.validate();
        //判断用户是否存在,如果不存在直接返回
        UpstreamUserInfo userInfo = upstreamUserInfoService.getOne(Wrappers.lambdaQuery(UpstreamUserInfo.class)
                .eq(UpstreamUserInfo::getAppId, bo.getAppId())
                .eq(UpstreamUserInfo::getDeleteFlag, DeleteFlagEnum.NOT_DELETE.getCode()));

        if (userInfo == null) {
            TpPhoneOrderUpstreamQueryLog tpPhoneOrderUpstreamQueryLog = new TpPhoneOrderUpstreamQueryLog();
            tpPhoneOrderUpstreamQueryLog.setAppId(bo.getAppId()).setRemark("渠道不存在");
            tpPhoneOrderUpstreamQueryLogService.save(tpPhoneOrderUpstreamQueryLog);
            log.error("【渠道发送订单】用户为空，appId为：{}", bo.getAppId());
            return returnResultMap(ResultMapInfo.UPSTREAM_NOT_EXIST);
        }
        //验签,不通过直接返回验签失败
        TreeMap<String, String> data = new TreeMap<>();
        data.put("a", userInfo.getAppKey());
        data.put("b", userInfo.getAppId());
        data.put("c", bo.getOrderNo());
        data.put("d", userInfo.getAppKey());
        String sign = SignUtils.generateSignature(data, SignTypeEnum.SHA256);
        if (!StringUtils.equals(sign, bo.getSign())) {
            TpPhoneOrderUpstreamQueryLog tpPhoneOrderUpstreamQueryLog = new TpPhoneOrderUpstreamQueryLog();
            tpPhoneOrderUpstreamQueryLog.setAppId(bo.getAppId()).setRemark("验签未通过");
            tpPhoneOrderUpstreamQueryLogService.save(tpPhoneOrderUpstreamQueryLog);
            return returnResultMap(ResultMapInfo.FAILESIGN);
        }
        //判断订单号是否存在,如果不存在直接返回
        PhoneOrderRecord phoneOrderRecord = phoneOrderRecordService.getOne(Wrappers.lambdaQuery(PhoneOrderRecord.class)
                .eq(PhoneOrderRecord::getUpstreamOrderNo, bo.getOrderNo())
                .eq(PhoneOrderRecord::getUpstreamUserId, userInfo.getId()));
        if (phoneOrderRecord == null) {
            TpPhoneOrderUpstreamQueryLog tpPhoneOrderUpstreamQueryLog = new TpPhoneOrderUpstreamQueryLog();
            tpPhoneOrderUpstreamQueryLog.setAppId(bo.getAppId()).setRemark("订单号不存在");
            tpPhoneOrderUpstreamQueryLogService.save(tpPhoneOrderUpstreamQueryLog);
            return returnResultMap(ResultMapInfo.NOTFINDORDER);
        }
        //查询成功操作，如果订单是亏损状态，将平台订单状态改为消费完成，上游回调状态改为成功，上游记账状态改为已记账,向tpPhoneOrderUpstreamQuery插入记录
        phoneOrderRecordService.operatorSuccess(phoneOrderRecord);
        PhoneOrderRecord record = phoneOrderRecordService.getOne(Wrappers.lambdaQuery(PhoneOrderRecord.class)
                .eq(PhoneOrderRecord::getUpstreamOrderNo, bo.getOrderNo())
                .eq(PhoneOrderRecord::getUpstreamUserId, userInfo.getId()));
        UpstreamCheckOrderVO vo = new UpstreamCheckOrderVO();
        TpPhoneOrderCallBack tpPhoneOrderCallBack = tpPhoneOrderCallBackService.getOne(Wrappers.lambdaQuery(TpPhoneOrderCallBack.class)
                .eq(TpPhoneOrderCallBack::getPlatformOrderNo, record.getPlatformOrderNo()));
        vo.setPhoneOperator(record.getPhoneOperator())
                .setPhoneNum(record.getPhoneNum())
                .setOrderNo(record.getUpstreamOrderNo())
                .setBizOrderNo(record.getPlatformOrderNo())
                .setOrderPrice(record.getOrderPrice())
                .setType(record.getType())
                .setProvince(record.getProvince())
                .setCity(record.getCity())
                .setOrderSn(record.getOrderSn());
        if (OrderStatusCodeEnum.PUSH_FAILED.getCode().equals(record.getPlatformOrderStatus())) {
            vo.setOrderStatus(UpstreamCallBackStatusEnum.FAILED.getCode()).setCertificate("");
        } else if (tpPhoneOrderCallBack == null) {
            vo.setOrderStatus(UpstreamCallBackStatusEnum.ORDER_PROCESSING.getCode()).setCertificate("");
        } else {
            vo.setOrderStatus(tpPhoneOrderCallBack.getStatus()).setCertificate(tpPhoneOrderCallBack.getCertificate());
        }
        TpPhoneOrderUpstreamQueryLog tpPhoneOrderUpstreamQueryLog = new TpPhoneOrderUpstreamQueryLog();
        tpPhoneOrderUpstreamQueryLog.setAppId(bo.getAppId()).setRemark("查询成功").setStatus(1);
        tpPhoneOrderUpstreamQueryLogService.save(tpPhoneOrderUpstreamQueryLog);
        return returnResultMap(ResultMapInfo.GETSUCCESS, vo);
    }

}
