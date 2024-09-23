package com.push.controller.platform;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.push.common.constants.RedisConstant;
import com.push.common.controller.BaseController;
import com.push.common.controller.ResultMapInfo;
import com.push.common.webfacade.bo.platform.ModifyProvinceCityStatusBO;
import com.push.common.webfacade.bo.platform.PhoneOperatorBO;
import com.push.common.webfacade.vo.platform.AreaVO;
import com.push.entity.platform.SystemCity;
import com.push.entity.platform.SystemProvince;
import com.push.entity.platform.SystemProvinceCity;
import com.push.service.platform.SystemCityService;
import com.push.service.platform.SystemProvinceCityService;
import com.push.service.platform.SystemProvinceService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Description:
 * Create DateTime: 2020-04-16 17:50
 *
 * 

 */
@RestController
@RequestMapping("area")
public class AreaController extends BaseController {

    @Resource
    private SystemProvinceCityService systemProvinceCityService;

    @Resource
    private SystemProvinceService systemProvinceService;

    @Resource
    private SystemCityService systemCityService;

    /**
     * 获取所有的地区
     *
     * @return 所有的地区
     */
    @Cacheable(value = RedisConstant.PUSH_ORDER_CACHE_ALL_AREA, unless = "#result.get('code') == 2")
    @RequestMapping(value = "getAllArea", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> getAllArea(@RequestBody PhoneOperatorBO phoneOperatorBO) {

        List<SystemProvinceCity> list = systemProvinceCityService.list(Wrappers.lambdaQuery(SystemProvinceCity.class)
                .eq(SystemProvinceCity::getPhoneOperator, phoneOperatorBO.getPhoneOperator()));

        Map<String, List<SystemProvinceCity>> provinceGroup = list.stream().collect(Collectors.groupingBy(SystemProvinceCity::getProvinceCode));

        List<SystemCity> cityList = systemCityService.list();

        List<SystemProvince> provinceList = systemProvinceService.list();

        List<AreaVO> areaVOList = new ArrayList<>();
        provinceGroup.forEach((key, value) -> {
            SystemProvince systemProvince = provinceList.stream().filter(p -> p.getCode().equals(key)).findFirst().get();
            AreaVO areaVO = new AreaVO("parent-" + key, systemProvince.getName());

            List<AreaVO> childrenAreaList = new ArrayList<>();
            value.forEach(it -> {
                SystemCity systemCity = cityList.stream().filter(c -> c.getCode().equals(it.getCityCode())).findFirst().get();
                childrenAreaList.add(new AreaVO(systemCity.getCode(), systemCity.getName()));
            });
            areaVO.setChildren(childrenAreaList);
            areaVOList.add(areaVO);
        });
        return returnResultMap(ResultMapInfo.GETSUCCESS, areaVOList);
    }

    /**
     * 获取启用的地区
     *
     * @return 启用的地区list
     */
    @Cacheable(value = RedisConstant.PUSH_ORDER_CACHE_ALL_ENABLE_AREA, key = "#phoneOperatorBO.phoneOperator", unless = "#result.get('code') == 2")
    @RequestMapping(value = "getAllEnableArea", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> getAllEnableArea(@RequestBody PhoneOperatorBO phoneOperatorBO) {
        List<SystemProvinceCity> list = systemProvinceCityService
                .list(Wrappers.lambdaQuery(SystemProvinceCity.class)
                        .eq(SystemProvinceCity::getStatus, 1)
                        .eq(SystemProvinceCity::getPhoneOperator, phoneOperatorBO.getPhoneOperator())
                        .select(SystemProvinceCity::getCityCode));
        List<String> enableAreaList = list.stream().map(SystemProvinceCity::getCityCode).collect(Collectors.toList());
        return returnResultMap(ResultMapInfo.GETSUCCESS, enableAreaList);
    }

    /**
     * 修改地区的启用状态
     *
     * @param bo
     * @return
     */
    @CacheEvict(value = RedisConstant.PUSH_ORDER_CACHE_ALL_ENABLE_AREA, key = "#bo.phoneOperator", allEntries = true)
    @RequestMapping(value = "modifyEnableStatus", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> modifyStatus(@RequestBody ModifyProvinceCityStatusBO bo) {
        bo.validate();
        List<String> list = Arrays.asList(bo.getCityCodes().split(","));
        systemProvinceCityService.distribute(list, bo.getPhoneOperator());
        return returnResultMap(ResultMapInfo.ADDSUCCESS);
    }
}
