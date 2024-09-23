package com.push.controller.platform;

import com.alibaba.fastjson.JSONObject;
import com.push.common.constants.RedisConstant;
import com.push.common.controller.BaseController;
import com.push.common.controller.ResultMapInfo;
import com.push.common.webfacade.vo.platform.AlarmSystemVO;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Description:
 * Create DateTime: 2020/4/16 10:14
 *
 *

 */
@RestController
@RequestMapping("alarm")
public class AlarmSystemController extends BaseController {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @RequestMapping(value = "statistics", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> alarmStatistics() {
        String jsonObject = stringRedisTemplate.opsForValue().get(RedisConstant.PUSH_ORDER_CALL_BACK_NUMBER_ALARM);
        AlarmSystemVO alarmSystemVO = JSONObject.parseObject(jsonObject, AlarmSystemVO.class);
        return returnResultMap(ResultMapInfo.GETSUCCESS, alarmSystemVO);
    }

}
