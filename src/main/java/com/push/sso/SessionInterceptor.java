package com.push.sso;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.push.common.constants.RedisConstant;
import com.push.common.constants.SessionConstant;
import com.push.common.utils.ResponseUtil;
import com.push.common.webfacade.error.ErrorEnum;
import com.push.common.webfacade.result.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Description: Session过滤器
 * Create DateTime: 2020-03-23 17:54
 *
 * 

 */
@Slf4j
public class SessionInterceptor implements HandlerInterceptor {

    @Value("${jwt.expiration}")
    private Integer expiration;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    static void response401(HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");

        ResponseUtil.responseAsText(JSON.toJSONString(Response.failed(ErrorEnum.RE_LOGIN)), response);
    }

    static void response403(HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");

        ResponseUtil.responseAsText(JSON.toJSONString(Response.failed(ErrorEnum.API_PERMISSION_INVALID)), response);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        HttpSession session = request.getSession();
        if (session.getAttribute(SessionConstant.LOGIN_USER_INFO) != null) {
            try {
                String adminInfoJsonStr = redisTemplate.opsForValue().get(RedisConstant.PUSH_ORDER_SESSION_LOGIN_USER_PRE_KEY + session.getId());
                if (StringUtils.isNotBlank(adminInfoJsonStr)) {
                    JSONObject jsonObject = JSONObject.parseObject(adminInfoJsonStr);

                    Boolean isAdmin = getIsAdmin(jsonObject);

                    if (isAdmin) {
                        List<String> interfaceList = getInterfaceList(jsonObject);
                        if (interfaceList.contains(request.getRequestURI())) {
                            redisTemplate.opsForValue().set(RedisConstant.PUSH_ORDER_SESSION_LOGIN_USER_PRE_KEY + session.getId(),
                                    adminInfoJsonStr, expiration, TimeUnit.HOURS);
                            return true;
                        } else {
                            response403(response);
                            return false;
                        }
                    } else {
                        redisTemplate.opsForValue().set(RedisConstant.PUSH_ORDER_SESSION_LOGIN_USER_PRE_KEY + session.getId(),
                                adminInfoJsonStr, expiration, TimeUnit.HOURS);
                        return true;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        response401(response);
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

    private Boolean getIsAdmin(JSONObject object) {
        return object.getBoolean("isAdmin");
    }

    private List<String> getInterfaceList(JSONObject object) {
        return object.getJSONObject("userInfo").getObject("interfaceList", List.class);
    }
}
