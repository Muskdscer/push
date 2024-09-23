package com.push.sso;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.push.common.constants.RedisConstant;
import com.push.common.utils.ResponseUtil;
import com.push.common.webfacade.error.ErrorEnum;
import com.push.common.webfacade.result.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Description: JWT过滤器
 * Create DateTime: 2020-03-23 17:54
 *
 * 

 */
@Slf4j
public class JwtAuthorizationTokenInterceptor implements HandlerInterceptor {

    @Resource
    private JwtTokenUtils jwtTokenUtils;

    @Value("${jwt.token.header}")
    private String tokenHeader;

    @Value("${jwt.expiration}")
    private Integer expiration;

    @Value("${jwt.token.start}")
    private String tokenStart;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String requestHeader = request.getHeader(tokenHeader);
        String username = null;
        String authToken = null;
        String headerStart = tokenStart + " ";
        if (requestHeader != null && requestHeader.startsWith(headerStart)) {
            authToken = requestHeader.substring(headerStart.length());
            try {
                username = jwtTokenUtils.getUsernameFromToken(authToken);
            } catch (Exception e) {
                ResponseUtil.responseAsText(JSON.toJSONString(Response.failed(ErrorEnum.TOKEN_INVALID)), response);
                return false;
            }
        }

        if (username != null) {
            String result = redisTemplate.opsForValue().get(RedisConstant.PUSH_ORDER_TOKEN_LOGIN_USER_PRE_KEY + username);

            //如果长时间未操作result为null
            if (StringUtils.isNotBlank(result)) {

                JSONObject object = JSON.parseObject(result);
                String tokenFromRedis = getTokenFromRedis(object);

                //保证用户单终端登录
                if (StringUtils.equals(authToken, tokenFromRedis)) {
                    //校验token的有效性
                    if (jwtTokenUtils.validateToken(authToken, username)) {

                        Boolean isAdmin = getIsAdmin(object);

                        if (isAdmin) {
                            //获取接口权限集合
                            List<String> interfaceListFromRedis = getInterfaceListFromRedis(object);
                            if (interfaceListFromRedis.contains(request.getRequestURI())) {
                                refreshTokenExpire(username, result);
                                return true;
                            } else {
                                SessionInterceptor.response403(response);
                                return false;
                            }
                        } else {
                            refreshTokenExpire(username, result);
                            return true;
                        }
                    }
                }
            }
        }
        SessionInterceptor.response401(response);
        return false;
    }

    private String getTokenFromRedis(JSONObject parseObject) {
        return parseObject.getString("token");
    }

    private List<String> getInterfaceListFromRedis(JSONObject parseObject) {
        return parseObject.getObject("interfaceList", List.class);
    }

    private Boolean getIsAdmin(JSONObject parseObject) {
        return parseObject.getBoolean("isAdmin");
    }

    private void refreshTokenExpire(String username, String result) {
        redisTemplate.opsForValue().set(RedisConstant.PUSH_ORDER_TOKEN_LOGIN_USER_PRE_KEY + username, result, expiration, TimeUnit.HOURS);
    }

}
