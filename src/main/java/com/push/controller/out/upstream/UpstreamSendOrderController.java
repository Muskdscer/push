package com.push.controller.out.upstream;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.zjkxtech.DistrictHelper;
import com.push.common.constants.ParamConstants;
import com.push.common.controller.BaseController;
import com.push.common.controller.ResultMapInfo;
import com.push.common.enums.*;
import com.push.common.enums.statistics.UpstreamSendLogEnum;
import com.push.common.utils.*;
import com.push.common.webfacade.bo.out.UpstreamSendOrderBO;
import com.push.common.webfacade.vo.out.UpstreamSendOrderVO;
import com.push.config.system.SystemConfig;
import com.push.entity.platform.PhoneOrderAvailable;
import com.push.entity.platform.PhoneOrderRecord;
import com.push.entity.upstream.UpstreamAisleConfig;
import com.push.entity.upstream.UpstreamSendOrderLog;
import com.push.entity.upstream.UpstreamUserInfo;
import com.push.service.platform.PhoneOrderAvailableService;
import com.push.service.platform.SystemBlackPhoneService;
import com.push.service.platform.UpstreamAisleConfigService;
import com.push.service.upstream.UpstreamBalanceService;
import com.push.service.upstream.UpstreamSendOrderLogService;
import com.push.service.upstream.UpstreamUserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

/**
 * Description: 上游发送订单号平台接收Controller
 * Create DateTime: 2020/3/31 17:04
 *
 *

 */
@Slf4j
@RestController
@RequestMapping("out")
public class UpstreamSendOrderController extends BaseController {

    @Resource
    private PhoneOrderAvailableService phoneOrderAvailableService;

    @Resource
    private UpstreamUserInfoService upstreamUserInfoService;

    @Resource
    private UpstreamSendOrderLogService upstreamSendOrderLogService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private SystemBlackPhoneService systemBlackPhoneService;

    @Resource
    private UpstreamAisleConfigService upstreamAisleConfigService;

    @Resource
    private UpstreamBalanceService upstreamBalanceService;

    @Value("${whiteIp.allBan}")
    private String allBanWhiteIp;

    @Value("${whiteIp.allPass}")
    private String allPassWhiteIp;

    @Resource
    private SystemConfig systemConfig;

    //上游发送订单入库
    @RequestMapping(value = "sendOrder", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> receiveOrder(@RequestBody UpstreamSendOrderBO bo) {
        log.info("【渠道发送订单】参数：{}", bo.toString());
        //参数校验
        bo.validate();
        //bo.setOrderNo(TokenUtil.generateOrderNo());
        //校验手机号是否正确
        if (!ValidateUtil.isMobile(bo.getPhoneNum())) {
            createUpstreamSendOrderLog(bo, ResultMapInfo.PHONENUMFORMATERROR.getMessage(), null,
                    UpstreamOrderLogEnum.FAIL.getCode(), UpstreamSendLogEnum.PHONE_NUM_FAIL.getCode());
            return returnResultMap(ResultMapInfo.PHONENUMFORMATERROR);
        }

        //验证是否在黑名单内
        String blackListFilterFlag = SystemConfig.getParam(ParamConstants.BLACK_LIST_FILTER_FLAG);
        if (BlackListAddFilterEnum.OPEN.getValue().equals(blackListFilterFlag)) {
            if (systemConfig.isBlackPhone(bo.getPhoneNum())) {
                createUpstreamSendOrderLog(bo, ResultMapInfo.BLACK_PHONE_NUM.getMessage(), null,
                        UpstreamOrderLogEnum.FAIL.getCode(), UpstreamSendLogEnum.DB_BLACK_PHONE.getCode());
                return returnResultMap(ResultMapInfo.BLACK_PHONE_NUM);
            }
        }

        //校验是否自动添加黑名单
        String blackListAddFlag = SystemConfig.getParam(ParamConstants.BLACK_LIST_ADD_FLAG);
        if (BlackListAddFlagEnum.OPEN.getValue().equals(blackListAddFlag)) {
            //判断手机号在二分钟内是否重复发送   手机号加面值
            String phoneKey = "isExistOneM:" + bo.getAppId() + bo.getPhoneNum() + bo.getOrderPrice().toPlainString();
            String exist = (String) redisTemplate.opsForValue().get(phoneKey);
            if (StringUtils.isBlank(exist)) {
                redisTemplate.opsForValue().set(phoneKey, "exist", 2, TimeUnit.MINUTES);
            } else {
                systemBlackPhoneService.saveAndReload(bo);
                createUpstreamSendOrderLog(bo, "手机号二分钟内重复发送", null, UpstreamOrderLogEnum.FAIL.getCode(),
                        UpstreamSendLogEnum.REDIS_BLACK_PHONE.getCode());
                return returnResultMap(ResultMapInfo.ADDFAIL);
            }
        }


        UpstreamUserInfo userInfo = upstreamUserInfoService.getOne(Wrappers.lambdaQuery(UpstreamUserInfo.class)
                .eq(UpstreamUserInfo::getAppId, bo.getAppId())
                .eq(UpstreamUserInfo::getDeleteFlag, DeleteFlagEnum.NOT_DELETE.getCode()));
        if (userInfo == null) {
            log.error("【渠道发送订单】用户为空，appId为：{}", bo.getAppId());
            //用户为空记录推送日志
            createUpstreamSendOrderLog(bo, ResultMapInfo.UPSTREAM_NOT_EXIST.getMessage(), null,
                    UpstreamOrderLogEnum.FAIL.getCode(), UpstreamSendLogEnum.UPSTREAM_USER_NOT_EXIST.getCode());
            return returnResultMap(ResultMapInfo.UPSTREAM_NOT_EXIST);
        }
        if (userInfo.getStatus().equals(UserStatusEnum.DISABLE.getValue())) {
            //账号已被禁用
            createUpstreamSendOrderLog(bo, ResultMapInfo.ACCOUNT_BAN.getMessage(), null, UpstreamOrderLogEnum.FAIL.getCode(),
                    UpstreamSendLogEnum.UPSTREAM_USER_DISABLE.getCode());
            return returnResultMap(ResultMapInfo.ACCOUNT_BAN);
        }
        //判断是否是在白名单之内的
        String ipAddr = getIpAddr();
        String whiteIp = userInfo.getWhiteIp();
        if (StringUtils.isBlank(whiteIp)) {
            createUpstreamSendOrderLog(bo, ResultMapInfo.LONGINIPISNULL.getMessage(), userInfo.getId(),
                    UpstreamOrderLogEnum.FAIL.getCode(), UpstreamSendLogEnum.UPSTREAM_USER_NOT_IN_WHITE_IP.getCode());
            return returnResultMap(ResultMapInfo.LONGINIPISNULL);
        }

        if (!whiteIp.contains(allPassWhiteIp)) {
            if (whiteIp.contains(allBanWhiteIp)) {
                log.error("【渠道发送订单】用户白名单IP全部被禁用，请求方的IP为：{}", ipAddr);
                //用户IP全部被禁用
                createUpstreamSendOrderLog(bo, ResultMapInfo.LONGINIPALLBAN.getMessage(), userInfo.getId(),
                        UpstreamOrderLogEnum.FAIL.getCode(), UpstreamSendLogEnum.UPSTREAM_USER_NOT_IN_WHITE_IP.getCode());
                return returnResultMap(ResultMapInfo.LONGINIPALLBAN);
            }
            if (StringUtils.isBlank(whiteIp) || !whiteIp.contains(ipAddr)) {
                log.error("【渠道发送订单】用户白名单为空或者访问IP不在配置之内，请求方的IP为：{}", ipAddr);
                //用户不在白名单之内
                createUpstreamSendOrderLog(bo, ResultMapInfo.LONGINIPFAIL.getMessage(), userInfo.getId(),
                        UpstreamOrderLogEnum.FAIL.getCode(), UpstreamSendLogEnum.UPSTREAM_USER_NOT_IN_WHITE_IP.getCode());
                return returnResultMap(ResultMapInfo.LONGINIPFAIL);
            }
        }
        PhoneOrderAvailable order = BeanUtils.copyPropertiesChaining(bo, PhoneOrderAvailable::new);
        if (StringUtils.isBlank(order.getType())) {
            order.setType(order.getPhoneOperator());
        }
        order.setUpstreamOrderNo(bo.getOrderNo());
        if (StringUtils.isBlank(order.getPhoneOperator())
                || StringUtils.isBlank(order.getPhoneNum())
                || order.getOrderExpireTime() == null
                || StringUtils.isBlank(order.getUpstreamOrderNo())
                || order.getOrderPrice() == null) {
            log.error("【渠道发送订单】用户传参为无效数据, 传入的数据为：{}", order.toString());
            //订单参数不正确
            createUpstreamSendOrderLog(bo, ResultMapInfo.PARAMERROR.getMessage(), userInfo.getId(),
                    UpstreamOrderLogEnum.FAIL.getCode(), UpstreamSendLogEnum.ORDER_PARAM_FAIL.getCode());
            return returnResultMap(ResultMapInfo.PARAMERROR);
        }
        //获取上游用户appKey生成签名-->签名以appId+appKey+token前14位以SHA256加密
        TreeMap<String, String> data = new TreeMap<>();
        data.put("a", userInfo.getAppKey());
        data.put("b", userInfo.getAppId());
        data.put("c", order.getUpstreamOrderNo());
        data.put("d", order.getPhoneNum());
        data.put("e", order.getOrderPrice().toPlainString());
        data.put("f", order.getPhoneOperator());
        data.put("g", userInfo.getAppKey());
        String sign = SignUtils.generateSignature(data, SignTypeEnum.SHA256);
        //验签
        if (!StringUtils.equals(sign, bo.getSign())) {
            //验签失败记录推送日志
            createUpstreamSendOrderLog(bo, ResultMapInfo.FAILESIGN.getMessage(), userInfo.getId(),
                    UpstreamOrderLogEnum.FAIL.getCode(), UpstreamSendLogEnum.SIGN_FAIL.getCode());
            return returnResultMap(ResultMapInfo.FAILESIGN);
        }
        //因为直接发到redis,所以数据库所有状态全部为已推送
        String addr = DistrictHelper.ofTelNumber(order.getPhoneNum());

        if (StringUtils.isNotBlank(addr) && addr.contains("/")) {
            String[] split = addr.split("/");
            String provinceCode = SystemConfig.getProvinceCode(split[0]);
            String cityCode = SystemConfig.getCityCode(split[1]);

            Boolean cityEnable = SystemConfig.getCityStatus(cityCode, bo.getPhoneOperator());
            if (!cityEnable) {
                //地区未开通,保存日志
                createUpstreamSendOrderLog(bo, ResultMapInfo.ADDR_NOT_ACTIVE.getMessage(), userInfo.getId(),
                        UpstreamOrderLogEnum.FAIL.getCode(), UpstreamSendLogEnum.ADD_NOT_OPEN.getCode());
                return returnResultMap(ResultMapInfo.ADDR_NOT_ACTIVE);
            }
            order.setProvince(split[0]).setProvinceCode(provinceCode).setCity(split[1]).setCityCode(cityCode);
        }

        //根据渠道ID和运营商获取通道
        UpstreamAisleConfig upstreamAisleConfig = upstreamAisleConfigService.getAisleByUpstreamUserIdAndOperator(userInfo.getId(), bo.getPhoneOperator());

        if (upstreamAisleConfig == null) {
            //服务器出错,记录推送日志
            createUpstreamSendOrderLog(bo, ResultMapInfo.ADDFAIL.getMessage(), userInfo.getId(),
                    UpstreamOrderLogEnum.FAIL.getCode(), UpstreamSendLogEnum.NOT_MATCH_AISLE.getCode());
            return returnResultMap(ResultMapInfo.ADDFAIL);
        }

        //判断是否需要授信
        if (upstreamAisleConfig.getIsCredit() == CreditEnum.ENABLE.getCode()) {

            BigDecimal balance = upstreamBalanceService.getBalanceByUpstreamUserIdAndAisleId(userInfo.getId(), upstreamAisleConfig.getAisleId());

            if (BigDecimalUtil.isMax(order.getOrderPrice(), balance)) {
                //渠道授信金额不足
                createUpstreamSendOrderLog(bo, ResultMapInfo.UPSTREAM_BALANCE_INSUFFICIENT.getMessage(), userInfo.getId(),
                        UpstreamOrderLogEnum.FAIL.getCode(), UpstreamSendLogEnum.UPSTREAM_BALANCE_INSUFFICIENT.getCode());
                return returnResultMap(ResultMapInfo.UPSTREAM_BALANCE_INSUFFICIENT);
            }
        }

        //为订单赋初始值
        String platformOrderNo = TokenUtil.generateOrderNo();
        order.setPlatformOrderStatus(OrderStatusCodeEnum.SUBMITTED.getCode())
                .setPushStatus(PushStatusEnum.PUSH.getValue())
                .setUpstreamUserId(userInfo.getId())
                .setUpstreamName(userInfo.getUpstreamName())
                .setPlatformOrderNo(platformOrderNo);
        PhoneOrderRecord phoneOrderRecord = BeanUtils.copyPropertiesChaining(order, PhoneOrderRecord::new);
        //余额足够-->将手机号码入库并发送到redis里面去
        Boolean result = phoneOrderAvailableService.saveBatchByAvailableAndSaveBatchByRecord(order, phoneOrderRecord,
                upstreamAisleConfig.getAisleId(), bo.getRechargeType());
        if (!result) {
            //服务器出错,记录推送日志
            createUpstreamSendOrderLog(bo, ResultMapInfo.ADDFAIL.getMessage(), userInfo.getId(),
                    UpstreamOrderLogEnum.FAIL.getCode(), UpstreamSendLogEnum.SYSTEM_FAIL.getCode());
            return returnResultMap(ResultMapInfo.ADDFAIL);
        }
        log.info("【渠道发送订单】接收处理完成, 渠道订单号为:{}, 平台订单号为:{}", bo.getOrderNo(), platformOrderNo);
        UpstreamSendOrderVO vo = new UpstreamSendOrderVO();
        vo.setOrderNo(bo.getOrderNo()).setBizOrderNo(platformOrderNo);
        return returnResultMap(ResultMapInfo.ADDSUCCESS, vo);
    }

    //生成验签日志对象
    private void createUpstreamSendOrderLog(UpstreamSendOrderBO bo, String remark, Long id, Integer status, String type) {
        UpstreamSendOrderLog orderLog = BeanUtils.copyPropertiesChaining(bo, UpstreamSendOrderLog::new);
        orderLog.setPushNum(1)
                .setRemark(remark)
                .setUpstreamUserId(id)
                .setStatus(status)
                .setUpstreamOrderNo(bo.getOrderNo())
                .setPhoneNum(bo.getPhoneNum())
                .setPhoneOperator(bo.getPhoneOperator())
                .setType(type);
        upstreamSendOrderLogService.save(orderLog);
    }

}
