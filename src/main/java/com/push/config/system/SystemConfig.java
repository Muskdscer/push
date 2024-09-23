package com.push.config.system;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.push.common.constants.ParamConstants;
import com.push.common.constants.RedisConstant;
import com.push.common.enums.DeleteFlagEnum;
import com.push.dao.mapper.platform.SystemParamMapper;
import com.push.entity.platform.*;
import com.push.service.platform.SystemBlackPhoneService;
import com.push.service.platform.SystemCityService;
import com.push.service.platform.SystemProvinceCityService;
import com.push.service.platform.SystemProvinceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Description:
 * Create DateTime: 2020-04-16 11:07
 *
 * 

 */
@Slf4j
@Component
public class SystemConfig implements Runnable {

    private static final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    private static final int DEFAULT_REFRESH_INTERVAL = 15;
    /**
     * 用于存储系统参数
     */
    private static volatile Map<String, String> param = new ConcurrentHashMap<>(48, 0.75f, 5);
    /**
     * 用于存储城市
     */
    private static volatile Map<String, String> city = new ConcurrentHashMap<>(512, 0.75f, 5);
    /**
     * 用于存储省份
     */
    private static volatile Map<String, String> province = new ConcurrentHashMap<>(512, 0.75f, 5);
    /**
     * 用于存储启用的城市
     */
    private static volatile Map<String, List<SystemProvinceCity>> enableCity = new ConcurrentHashMap<>(512, 0.75f, 5);
    private static volatile String version = null;

    private Thread worker = null;

    @Resource
    private SystemParamMapper systemParamMapper;

    @Resource
    private SystemCityService systemCityService;

    @Resource
    private SystemProvinceService systemProvinceService;

    @Resource
    private SystemProvinceCityService systemProvinceCityService;

    @Resource
    private SystemBlackPhoneService systemBlackPhoneService;

    @Resource
    private RedisTemplate<String, String> redisTemplate;


    /**
     * 初始化
     */
    @PostConstruct
    public void init() {
        worker = new Thread(this);
        worker.start();
    }

    @Override
    public void run() {
        scheduledExecutorService
                .scheduleWithFixedDelay(() -> {
                    try {
                        checkAndRefresh();
                    } catch (Exception e) {
                        log.error("加载系统参数出现异常:", e);
                    }
                }, 0, DEFAULT_REFRESH_INTERVAL, TimeUnit.SECONDS);
    }


    /**
     * 检查并刷新缓存
     */
    private void checkAndRefresh() {
        if (isVersionChanged()) {
            load();
        }
    }

    //=============================================加载参数===========================================

    /**
     * 执行加载
     */
    public void load() {
        loadSystemParam();
        loadArea();
        loadEnableArea();
        loadBlackPhone();
    }

    /**
     * 加载系统参数
     */
    private void loadSystemParam() {
        List<SystemParam> systemParams = systemParamMapper.selectList(Wrappers.lambdaQuery(SystemParam.class));
        if (CollectionUtils.isEmpty(systemParams)) {
            return;
        }

        systemParams.forEach(item -> {
            param.put(StringUtils.trimToEmpty(item.getName()), StringUtils.trimToEmpty(item.getValue()));
            redisTemplate.opsForHash().put(RedisConstant.PUSH_ORDER_SYSTEM_PARAM, StringUtils.trimToEmpty(item.getName()),
                    StringUtils.trimToEmpty(item.getValue()));
        });
    }

    /**
     * 加载地区
     */
    private void loadArea() {
        List<SystemCity> cityList = systemCityService.list();
        if (CollectionUtils.isNotEmpty(cityList)) {
            cityList.forEach(item -> city.put(StringUtils.trimToEmpty(item.getName()), StringUtils.trimToEmpty(item.getCode())));
        }

        List<SystemProvince> provinceList = systemProvinceService.list();
        if (CollectionUtils.isNotEmpty(provinceList)) {
            provinceList.forEach(item -> province.put(StringUtils.trimToEmpty(item.getName()), StringUtils.trimToEmpty(item.getCode())));
        }
    }

    /**
     * 加载地区的启用状态
     */
    private void loadEnableArea() {
        List<SystemProvinceCity> list = systemProvinceCityService.list();
        enableCity = list.stream().collect(Collectors.groupingBy(SystemProvinceCity::getPhoneOperator));
    }

    /**
     * 加载手机号黑名单
     */
    public void loadBlackPhone() {
        List<SystemBlackPhone> list = systemBlackPhoneService
                .list(Wrappers.lambdaQuery(SystemBlackPhone.class).eq(SystemBlackPhone::getDeleteFlag, DeleteFlagEnum.NOT_DELETE.getCode()));
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        List<String> blackPhoneList = list.stream().map(SystemBlackPhone::getPhoneNum).collect(Collectors.toList());

        redisTemplate.opsForValue().set(RedisConstant.PUSH_ORDER_CACHE_BLACK_PHONE_LIST, StringUtils.join(blackPhoneList, ","));
    }

    /**
     * 判断版本是否变动
     *
     * @return true/false
     */
    private boolean isVersionChanged() {
        SystemParam systemParam = systemParamMapper
                .selectOne(Wrappers.lambdaQuery(SystemParam.class).eq(SystemParam::getName, ParamConstants.VERSION));
        if (systemParam == null) {
            SystemParam param = new SystemParam();
            param.setName(ParamConstants.VERSION);
            param.setValue("0");
            param.setRemark("版本Version");
            systemParamMapper.insert(param);
            version = param.getValue();
            return true;
        } else {
            if (systemParam.getValue().equals(version)) {
                return false;
            }
            version = systemParam.getValue();
            return true;
        }
    }


    //========================================缓存中获取=======================================


    /**
     * 获取参数值
     *
     * @param name 参数名
     * @return 参数值
     */
    public static String getParam(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        return param.get(name.trim());
    }

    /**
     * 获取城市编码
     *
     * @param cityName 城市名称
     * @return 城市编码
     */
    public static String getCityCode(String cityName) {
        if (StringUtils.isBlank(cityName)) {
            return null;
        }
        return city.get(cityName.trim());
    }

    /**
     * 获取城市名称
     *
     * @param cityCode 城市编码
     * @return 城市名称
     */
    public static String getCityName(String cityCode) {
        if (StringUtils.isBlank(cityCode)) {
            return null;
        }
        return city.entrySet().stream().filter(c -> c.getValue().equals(cityCode)).findFirst().get().getKey();
    }

    /**
     * 获取省份编码
     *
     * @return 省份编码
     */
    public static String getProvinceCode(String provinceName) {
        if (StringUtils.isBlank(provinceName)) {
            return null;
        }
        return province.get(provinceName);
    }

    /**
     * 获取城市的启用状态
     *
     * @param cityCode 城市编码
     * @return 城市的启用状态
     */
    public static Boolean getCityStatus(String cityCode, String phoneOperator) {
        if (StringUtils.isBlank(cityCode)
                || StringUtils.isBlank(phoneOperator)) {
            return false;
        }

        List<SystemProvinceCity> cityList = enableCity.get(phoneOperator);
        if (CollectionUtils.isEmpty(cityList)) {
            return false;
        }

        SystemProvinceCity systemProvinceCity = cityList.stream().filter(city -> cityCode.equals(city.getCityCode())).findFirst().get();

        return systemProvinceCity.getStatus() == 1;
    }

    /**
     * 判断手机号是否为黑名单中的手机号
     *
     * @param phoneNum 手机号
     * @return 是否为黑名单手机号
     */
    public Boolean isBlackPhone(String phoneNum) {
        try {
            String str = redisTemplate.opsForValue().get(RedisConstant.PUSH_ORDER_CACHE_BLACK_PHONE_LIST);
            if (StringUtils.isBlank(str)) {
                return false;
            }

            return str.contains(phoneNum);
        } catch (Exception e) {
            log.info("【手机号黑名单】从redis中获取手机号黑名单并判断时出错：", e);
            return false;
        }
    }


    /**
     * 销毁
     */
    @PreDestroy
    public void destroy() {
        if (worker != null || !worker.isInterrupted()) {
            worker.interrupt();
        }
        scheduledExecutorService.shutdownNow();

        Set<String> keys = redisTemplate.keys(RedisConstant.PUSH_ORDER_CACHE_PRE + "*");
        redisTemplate.delete(keys);
    }
}
