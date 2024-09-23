package com.push.common.controller;

import com.alibaba.fastjson.JSON;
import com.push.common.constants.RedisConstant;
import com.push.common.constants.SessionConstant;
import com.push.sso.JwtTokenUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 * Create DateTime: 2020-03-27 09:00
 *
 *

 */
public class BaseLoginController extends BaseController {

    @Value("${jwt.expiration}")
    private Integer expiration;

    @Resource
    private JwtTokenUtils jwtTokenUtils;

    @Resource
    private RedisTemplate<String, String> redisTemplate;


    /**
     * 执行session登录
     *
     * @param request  HttpServletRequest
     * @param userInfo 用户信息
     */
    protected void doSessionLogin(HttpServletRequest request, Object userInfo, Boolean isAdmin) {
        HttpSession httpSession = request.getSession();
        httpSession.setAttribute(SessionConstant.LOGIN_USER_INFO, userInfo);
        String sessionId = httpSession.getId();
        Map<String, Object> map = new HashMap<>(2);
        map.put("userInfo", userInfo);
        map.put("isAdmin", isAdmin);
        redisTemplate.opsForValue().set(RedisConstant.PUSH_ORDER_SESSION_LOGIN_USER_PRE_KEY + sessionId, JSON.toJSONString(map), expiration, TimeUnit.HOURS);
    }

    /**
     * 执行token登录
     *
     * @param username 用户名
     * @param userId   用户ID
     * @param roleId   用户角色ID
     * @return token
     */
    protected String doTokenLogin(String username, Long userId, Long roleId, Set<String> interfaceList, Boolean isAdmin) {
        String token = jwtTokenUtils.generateToken(username, userId, roleId);
        Map<String, Object> map = new HashMap<>(2);
        if (isAdmin) {
            map.put("interfaceList", interfaceList);
        }
        map.put("token", token);
        map.put("isAdmin", isAdmin);
        redisTemplate.opsForValue().set(RedisConstant.PUSH_ORDER_TOKEN_LOGIN_USER_PRE_KEY + username, JSON.toJSONString(map), expiration, TimeUnit.HOURS);
        return token;
    }

    /**
     * 执行session退出登录
     *
     * @param request HttpServletRequest
     */
    protected void doSessionLogout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.removeAttribute(SessionConstant.LOGIN_USER_INFO);

        String sessionId = session.getId();
        redisTemplate.delete(RedisConstant.PUSH_ORDER_SESSION_LOGIN_USER_PRE_KEY + sessionId);
    }

    /**
     * 执行token退出登录
     *
     * @param username 用户名
     */
    protected void doTokenLogout(String username) {
        redisTemplate.delete(RedisConstant.PUSH_ORDER_TOKEN_LOGIN_USER_PRE_KEY + username);
    }
}
