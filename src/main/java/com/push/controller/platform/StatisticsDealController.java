package com.push.controller.platform;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.push.common.constants.RedisConstant;
import com.push.common.controller.BaseController;
import com.push.common.controller.ResultMapInfo;
import com.push.common.enums.DeleteFlagEnum;
import com.push.common.enums.OperatorTypeEnum;
import com.push.common.enums.UserTypeCodeEnum;
import com.push.common.webfacade.bo.platform.CountGroupByBO;
import com.push.common.webfacade.vo.platform.*;
import com.push.common.webfacade.vo.platform.statistics.*;
import com.push.entity.platform.PhoneOrderFailRecord;
import com.push.entity.platform.PhoneOrderRecord;
import com.push.entity.platform.PhoneOrderStatistics;
import com.push.entity.platform.PhoneOrderSuccessProbability;
import com.push.entity.shop.ShopUserInfo;
import com.push.entity.upstream.UpstreamUserInfo;
import com.push.service.platform.*;
import com.push.service.shop.ShopUserInfoService;
import com.push.service.upstream.UpstreamUserInfoService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Description: 统计
 * Create DateTime: 2020/4/22 9:38
 *
 *

 */

@RestController
@RequestMapping("statistics")
public class StatisticsDealController extends BaseController {

    @Resource
    private StatisticsBillService statisticsBillService;
    @Resource
    private PhoneOrderRecordService phoneOrderRecordService;
    @Resource
    private PhoneOrderStatisticsService phoneOrderStatisticsService;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private PhoneOrderFailRecordService phoneOrderFailRecordService;

    @Resource
    private UpstreamUserInfoService upstreamUserInfoService;

    @Resource
    private ShopUserInfoService shopUserInfoService;

    @Resource
    private PhoneOrderSuccessProbabilityService phoneOrderSuccessProbabilityService;

    @RequestMapping(value = "dealMoney", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> statisticsDealMoney() {
        StatisticsDealMoneyVO statisticsDealMoneyVO = statisticsBillService.statisticsDealMoney();
        return returnResultMap(ResultMapInfo.GETSUCCESS, statisticsDealMoneyVO);
    }

    @RequestMapping(value = "upstreamExtractionMoney", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> statisticsUpstreamExtraction() {
        StatisticsUpstreamExtractionMoneyVO statisticsUpstreamExtractionMoneyVO = statisticsBillService.statisticsUpstreamExtraction();
        return returnResultMap(ResultMapInfo.GETSUCCESS, statisticsUpstreamExtractionMoneyVO);
    }

    @RequestMapping(value = "platformExtractionMoney", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> statisticsPlatformExtraction() {
        StatisticsPlatformExtractionMoneyVO statisticsPlatformExtractionMoneyVO = statisticsBillService.statisticsPlatformExtraction();
        return returnResultMap(ResultMapInfo.GETSUCCESS, statisticsPlatformExtractionMoneyVO);
    }

    @RequestMapping(value = "shopExtractionMoney", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> statisticsShopExtraction() {
        StatisticsShopExtractionMoneyVO statisticsShopExtractionMoneyVO = statisticsBillService.statisticsShopExtraction();
        return returnResultMap(ResultMapInfo.GETSUCCESS, statisticsShopExtractionMoneyVO);
    }

    @RequestMapping(value = "upstreamRechargeMoney", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> statisticsUpstreamRecharge() {
        StatisticsUpstreamRechargeMoneyVO statisticsUpstreamRechargeMoneyVO = statisticsBillService.statisticsUpstreamRecharge();
        return returnResultMap(ResultMapInfo.GETSUCCESS, statisticsUpstreamRechargeMoneyVO);
    }

    @RequestMapping(value = "totalDeal", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> statisticsTotalDeal() {
        StatisticsDealVO statisticsDealVO = new StatisticsDealVO();
        StatisticsDealMoneyVO statisticsDealMoneyVO = statisticsBillService.statisticsDealMoney();
        StatisticsUpstreamExtractionMoneyVO statisticsUpstreamExtractionMoneyVO = statisticsBillService.statisticsUpstreamExtraction();
        StatisticsPlatformExtractionMoneyVO statisticsPlatformExtractionMoneyVO = statisticsBillService.statisticsPlatformExtraction();
        StatisticsShopExtractionMoneyVO statisticsShopExtractionMoneyVO = statisticsBillService.statisticsShopExtraction();
        StatisticsUpstreamRechargeMoneyVO statisticsUpstreamRechargeMoneyVO = statisticsBillService.statisticsUpstreamRecharge();
        StatisticsShopAgentExtractionMoneyVO statisticsShopAgentExtractionMoneyVO = statisticsBillService.statisticsShopAgentExtraction();
        StatisticsUpstreamAgentExtractionMoneyVO statisticsUpstreamAgentExtractionMoneyVO = statisticsBillService.statisticsUpstreamAgentExtraction();
        statisticsDealVO.setStatisticsDealMoneyVO(statisticsDealMoneyVO)
                .setStatisticsUpstreamExtractionMoneyVO(statisticsUpstreamExtractionMoneyVO)
                .setStatisticsUpstreamRechargeMoneyVO(statisticsUpstreamRechargeMoneyVO)
                .setPlatformExtractionMoneyVO(statisticsPlatformExtractionMoneyVO)
                .setStatisticsShopExtractionMoneyVO(statisticsShopExtractionMoneyVO)
                .setStatisticsShopAgentExtractionMoneyVO(statisticsShopAgentExtractionMoneyVO)
                .setStatisticsUpstreamAgentExtractionMoneyVO(statisticsUpstreamAgentExtractionMoneyVO);
        return returnResultMap(ResultMapInfo.GETSUCCESS, statisticsDealVO);

    }

    //获取成功率
    @RequestMapping(value = "getSuccessProbability", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> getSuccessProbability() {
        Date date = new Date();
        //创建开始时间和结束时间
        DateTime begin = DateUtil.beginOfDay(date);
        DateTime end = DateUtil.endOfDay(date);
        //记录时间
        String recordTime = new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.yesterday());
        //定义redis的key
        String key = "push-order:" + "successProbability:" + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);
        //判断这个key是否存在,存在则直接返回
        String voString = redisTemplate.opsForValue().get(key);
        CountProbabilityVO parseObject = JSON.parseObject(voString, CountProbabilityVO.class);
        if (parseObject != null) {
            return returnResultMap(ResultMapInfo.GETSUCCESS, parseObject);
        }
        /**
         * 原来的方式如果今天他没有下单就拿不到用户id,那么昨天的就显示不出来
         */
        /*Map<String, Long> idMap = phoneOrderRecordService.list(Wrappers.lambdaQuery(PhoneOrderRecord.class)
                .groupBy(PhoneOrderRecord::getUpstreamUserId)
                .between(PhoneOrderRecord::getCreateTime, begin, end))
                .stream()
                .collect(Collectors.toMap(PhoneOrderRecord::getUpstreamName, PhoneOrderRecord::getUpstreamUserId));*/
        /**
         * 已修复,查询所有上游用户获取id
         */
        Map<String, Long> idMap = upstreamUserInfoService.list(Wrappers.lambdaQuery(UpstreamUserInfo.class)
                .eq(UpstreamUserInfo::getDeleteFlag, DeleteFlagEnum.NOT_DELETE.getCode()))
                .stream()
                .collect(Collectors.toMap(UpstreamUserInfo::getUserName, UpstreamUserInfo::getId));
        CountProbabilityVO vo = new CountProbabilityVO();
        List<Map<String, Object>> yesUpList = new ArrayList<>();
        List<Map<String, Object>> yesShopList = new ArrayList<>();
        List<Map<String, Object>> toUpList = new ArrayList<>();
        List<Map<String, Object>> toShopList = new ArrayList<>();
        idMap.forEach((x, y) -> {
            int count = phoneOrderRecordService.count(Wrappers.lambdaQuery(PhoneOrderRecord.class).eq(PhoneOrderRecord::getUpstreamUserId, y).between(PhoneOrderRecord::getCreateTime, begin, end));
            int upSuccess = phoneOrderRecordService.count(Wrappers.lambdaQuery(PhoneOrderRecord.class).eq(PhoneOrderRecord::getUpstreamUserId, y).eq(PhoneOrderRecord::getUpstreamCallbackStatus, 1).between(PhoneOrderRecord::getCreateTime, begin, end));
            Map<String, Object> map = new HashMap<>();
            map.put("upstreamName", x);
            if (count == 0) {
                map.put("success", 0.0000);
            } else {
                map.put("success", Double.valueOf(new DecimalFormat("0.0000").format((double) upSuccess / (double) count)));
            }
            PhoneOrderSuccessProbability probability = phoneOrderSuccessProbabilityService
                    .getOne(Wrappers.lambdaQuery(PhoneOrderSuccessProbability.class)
                            .eq(PhoneOrderSuccessProbability::getUserType, UserTypeCodeEnum.UPSTREAM_USER.getCode())
                            .eq(PhoneOrderSuccessProbability::getUserId, y)
                            .eq(PhoneOrderSuccessProbability::getRecordDate, recordTime));
            Map<String, Object> YesMap = new HashMap<>();
            YesMap.put("upstreamName", x);
            if (probability != null) {
                YesMap.put("success", probability.getSuccessProbability());
            } else {
                YesMap.put("success", 0.0000);
            }
            toUpList.add(map);
            yesUpList.add(YesMap);
        });
        /**
         * 原来的方式如果今天他没有被推送就拿不到用户id,那么昨天的就显示不出来
         */
        //商户成功率
        /*Map<String, Long> sIdMap = phoneOrderRecordService.list(Wrappers.lambdaQuery(PhoneOrderRecord.class)
                .groupBy(PhoneOrderRecord::getShopUserId)
                .between(PhoneOrderRecord::getCreateTime, begin, end))
                .stream()
                .collect(Collectors.toMap(PhoneOrderRecord::getShopName, PhoneOrderRecord::getShopUserId));*/
        Map<String, Long> sIdMap = shopUserInfoService.list(Wrappers.lambdaQuery(ShopUserInfo.class)
                .eq(ShopUserInfo::getDeleteFlag, DeleteFlagEnum.NOT_DELETE.getCode()))
                .stream()
                .collect(Collectors.toMap(ShopUserInfo::getUserName, ShopUserInfo::getId));
        sIdMap.forEach((x, y) -> {
            int count = phoneOrderRecordService.count(Wrappers.lambdaQuery(PhoneOrderRecord.class).eq(PhoneOrderRecord::getShopUserId, y).between(PhoneOrderRecord::getCreateTime, begin, end));
            int upSuccess = phoneOrderRecordService.count(Wrappers.lambdaQuery(PhoneOrderRecord.class).eq(PhoneOrderRecord::getShopUserId, y).eq(PhoneOrderRecord::getShopCallbackStatus, 1).between(PhoneOrderRecord::getCreateTime, begin, end));
            Map<String, Object> map = new HashMap<>();
            map.put("shopName", x);
            if (count == 0) {
                map.put("success", 0.0000);
            } else {
                map.put("success", Double.valueOf(new DecimalFormat("0.0000").format((double) upSuccess / (double) count)));
            }
            PhoneOrderSuccessProbability probability = phoneOrderSuccessProbabilityService
                    .getOne(Wrappers.lambdaQuery(PhoneOrderSuccessProbability.class)
                            .eq(PhoneOrderSuccessProbability::getUserType, UserTypeCodeEnum.SHOP_USER.getCode())
                            .eq(PhoneOrderSuccessProbability::getUserId, y)
                            .eq(PhoneOrderSuccessProbability::getRecordDate, recordTime));
            Map<String, Object> YesMap = new HashMap<>();
            YesMap.put("shopName", x);
            if (probability != null) {
                YesMap.put("success", probability.getSuccessProbability());
            } else {
                YesMap.put("success", 0.0000);
            }
            toShopList.add(map);
            yesShopList.add(YesMap);
        });
        vo.setToShopList(toShopList).setToUpList(toUpList).setYesShopList(yesShopList).setYesUpList(yesUpList);
        redisTemplate.opsForValue().set(key, JSON.toJSONString(vo), 1, TimeUnit.MINUTES);
        return returnResultMap(ResultMapInfo.GETSUCCESS, vo);
    }


    //获取当天订单的统计数量
    @RequestMapping(value = "orderCount", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> orderCount() {
        Date date = new Date();
        DateTime begin = DateUtil.beginOfDay(date);
        DateTime end = DateUtil.endOfDay(date);
        String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);
        String key = RedisConstant.CURRENT_PUSH_NUM + currentDate;
        String countOrderString = redisTemplate.opsForValue().get(key);
        CountOrderVO countOrderVO = JSON.parseObject(countOrderString, CountOrderVO.class);
        if (countOrderVO != null) {
            return returnResultMap(ResultMapInfo.ADDSUCCESS, countOrderVO);
        }
        OperatorTypeEnum[] values = OperatorTypeEnum.values();
        CountOrderVO vo = new CountOrderVO();
        for (OperatorTypeEnum value : values) {
            int count = phoneOrderRecordService.count(Wrappers.lambdaQuery(PhoneOrderRecord.class).eq(PhoneOrderRecord::getPhoneOperator, value.getCode()).between(PhoneOrderRecord::getCreateTime, begin, end));
            int fail = phoneOrderFailRecordService.count(Wrappers.lambdaQuery(PhoneOrderFailRecord.class).eq(PhoneOrderFailRecord::getPhoneOperator, value.getCode()).between(PhoneOrderFailRecord::getCreateTime, begin, end));
            if (OperatorTypeEnum.MOBILE.getCode().equals(value.getCode())) {
                vo.setTodayMobileSu(count - fail).setTodayMobileRe(count).setTodayMobileFa(fail);
            } else if (OperatorTypeEnum.UNICOM.getCode().equals(value.getCode())) {
                vo.setTodayUnicomSu(count - fail).setTodayUnicomRe(count).setTodayUnicomFa(fail);
            } else {
                vo.setTodayTelecomSu(count - fail).setTodayTelecomRe(count).setTodayTelecomFa(fail);
            }
        }
        CountOrderVO statistics = getYesterdayStatistics(vo);
        redisTemplate.opsForValue().set(key, JSON.toJSONString(statistics), 1, TimeUnit.MINUTES);
        return returnResultMap(ResultMapInfo.ADDSUCCESS, statistics);
    }

    //获取昨天的数据
    private CountOrderVO getYesterdayStatistics(CountOrderVO vo) {
        DateTime yesterday = DateUtil.yesterday();
        String resultDate = new SimpleDateFormat("yyyy-MM-dd").format(yesterday);
        List<PhoneOrderStatistics> statistics = phoneOrderStatisticsService.list(Wrappers.lambdaQuery(PhoneOrderStatistics.class).eq(PhoneOrderStatistics::getStatisticsDate, resultDate));
        statistics.forEach(a -> {
            if (OperatorTypeEnum.MOBILE.getCode().equals(a.getPhoneOperator())) {
                vo.setYesMobileSu(a.getOrderSuccessNum()).setYesMobileRe(a.getOrderResultNum()).setYesMobileFa(a.getOrderFailNum());
            } else if (OperatorTypeEnum.UNICOM.getCode().equals(a.getPhoneOperator())) {
                vo.setYesUnicomSu(a.getOrderSuccessNum()).setYesUnicomRe(a.getOrderResultNum()).setYesUnicomFa(a.getOrderFailNum());
            } else {
                vo.setYesTelecomSu(a.getOrderSuccessNum()).setYesTelecomRe(a.getOrderResultNum()).setYesTelecomFa(a.getOrderFailNum());
            }
        });
        return vo;
    }

    /**
     * 上游订单分组统计
     *
     * @return 统计对象
     */
    @PostMapping("upOrderCountGroupBy")
    public Map<String, Object> orderCountGroupBy(@RequestBody CountGroupByBO bo) {
        bo.validate();
        DateTime endTime = DateUtil.endOfDay(bo.getStartTime());
        List<PhoneOrderRecord> upCount = phoneOrderRecordService.list(Wrappers.lambdaQuery(PhoneOrderRecord.class)
                .groupBy(PhoneOrderRecord::getUpstreamUserId)
                .ge(PhoneOrderRecord::getCreateTime,bo.getStartTime())
                .lt(PhoneOrderRecord::getCreateTime,endTime)
                .select(PhoneOrderRecord::getUpstreamUserId, PhoneOrderRecord::getUpstreamName));
        List<UpGroupVO> list = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(upCount)) {
            upCount.forEach(o -> {
                UpstreamUserInfo upstreamUserInfo = upstreamUserInfoService.getById(o.getUpstreamUserId());
                List<UpOrderCountGroupVO> upOrderCountGroupVOS = phoneOrderRecordService
                        .getSumOrderCount(o.getUpstreamUserId(), upstreamUserInfo.getRate());
                UpGroupVO upGroupVO = new UpGroupVO();
                upOrderCountGroupVOS.forEach(u -> {
                    upGroupVO.setName(o.getUpstreamName());
                    if (null == u.getUpstreamCallbackStatus()){
                        upGroupVO.setPriceSum(upGroupVO.getPriceSum().add(u.getOrderPrice()))
                                .setOrderSum(upGroupVO.getOrderSum() + u.getOrderCount());
                        return;
                    } else if (1 == u.getUpstreamCallbackStatus()) {
                        upGroupVO.setOrderSuccess(u.getOrderCount()).setPriceSuccess(u.getOrderPrice());
                    } else {
                        upGroupVO.setOrderFail(u.getOrderCount()).setPriceFail(u.getOrderPrice());
                    }
                    upGroupVO.setPriceSum(upGroupVO.getPriceSum().add(u.getOrderPrice()))
                            .setOrderSum(upGroupVO.getOrderSum() + u.getOrderCount());
                });
                list.add(upGroupVO);
            });
        }
        return returnResultMap(ResultMapInfo.GETSUCCESS, list);
    }

    /**
     * 商户订单分组统计
     *
     * @return 统计对象
     */
    @PostMapping("downOrderCountGroupBy")
    public Map<String, Object> DownOrderCountGroupBy(@RequestBody CountGroupByBO bo) {
        bo.validate();
        DateTime endTime = DateUtil.endOfDay(bo.getStartTime());
        List<DownGroupVO> list = new ArrayList<>();
        List<DownOrderCountGroupVO> downOrderCountGroupVOS = phoneOrderRecordService
                .getSumOrderCountToShop(bo.getStartTime(),endTime);
        if (CollectionUtils.isEmpty(downOrderCountGroupVOS)){
            return returnResultMap(ResultMapInfo.GETSUCCESS);
        }
        Map<String, List<DownOrderCountGroupVO>> nameMap = downOrderCountGroupVOS.stream().collect(Collectors.groupingBy(DownOrderCountGroupVO::getName));
        if (CollectionUtils.isEmpty(nameMap)){
            return returnResultMap(ResultMapInfo.GETSUCCESS);
        }
        nameMap.forEach((k, v) -> {
            DownGroupVO downGroupVO = new DownGroupVO();
            downGroupVO.setName(k);
            v.forEach(d -> {
                if (null == d.getShopCallbackStatus()){
                    downGroupVO.setPriceSum(downGroupVO.getPriceSum().add(d.getOrderPrice()))
                            .setOrderSum(downGroupVO.getOrderSum() + d.getOrderCount());
                    return;
                } else if (1 == d.getShopCallbackStatus()) {
                    downGroupVO.setOrderSuccess(d.getOrderCount()).setPriceSuccess(d.getOrderPrice());
                } else {
                    downGroupVO.setOrderFail(d.getOrderCount()).setPriceFail(d.getOrderPrice());
                }
                downGroupVO.setPriceSum(downGroupVO.getPriceSum().add(d.getOrderPrice()))
                        .setOrderSum(downGroupVO.getOrderSum() + d.getOrderCount());
            });
            list.add(downGroupVO);
        });
        return returnResultMap(ResultMapInfo.GETSUCCESS, list);
    }
}
