package com.push.sso;

import com.alibaba.fastjson.JSONObject;
import com.push.common.constants.RedisConstant;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Description: session登录工具类
 * Create DateTime: 2020-03-26 19:40
 *
 * 

 */
@Component
public class SessionUtils {

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 获取用户ID
     *
     * @param request HttpServletRequest
     * @return 用户ID
     */
    public Long getUserId(HttpServletRequest request) {
        String result = getUserSessionFromRedis(request);
        return JSONObject.parseObject(result).getLong("id");
    }

    /**
     * 获取角色ID
     *
     * @param request HttpServletRequest
     * @return 角色ID
     */
    public Long getRoleId(HttpServletRequest request) {
        String result = getUserSessionFromRedis(request);
        return JSONObject.parseObject(result).getLong("roleId");
    }

    /**
     * 从redis中获取用户session
     *
     * @param request HttpServletRequest
     * @return 用户session
     */
    public String getUserSessionFromRedis(HttpServletRequest request) {
        String sessionId = request.getSession().getId();
        return redisTemplate.opsForValue().get(RedisConstant.PUSH_ORDER_SESSION_LOGIN_USER_PRE_KEY + sessionId);
    }

}
