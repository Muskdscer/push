package com.push.common.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.push.sso.JwtTokenUtils;
import com.push.sso.SessionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Component
public class BaseController {

    @Value("${sso.session}")
    private Boolean session;

    @Resource
    private JwtTokenUtils jwtTokenUtils;

    @Resource
    private SessionUtils sessionUtils;

    public static HttpSession getSession() {
        return getRequest().getSession();
    }

    public static HttpServletRequest getRequest() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attrs.getRequest();
    }

    public static HttpServletResponse getResponse() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attrs.getResponse();
    }

    /**
     * 获取登陆IP
     *
     * @return
     */
    public static String getIpAddr() {
        HttpServletRequest request = getRequest();
        //处理代理访问获取不到真正的ip问题的
        String ip = request.getHeader("x-forwarded-for");
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            //获取代理中中的ip
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            //获取代理中中的ip

            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            //非代理的情况获取ip
            ip = request.getRemoteAddr();
        }
        if (ip.equals("0:0:0:0:0:0:0:1")) {
            ip = "127.0.0.1";
        }
        if (StringUtils.isNotBlank(ip)) {
            String[] ips = ip.split(",");
            if (ips.length > 0) {
                ip = ips[0].trim();
            }
        }
        return ip;
    }

    public Long getUserId() {
        HttpServletRequest request = getRequest();
        Long userId;
        if (session) {
            userId = sessionUtils.getUserId(request);
        } else {
            userId = jwtTokenUtils.getUserIdFromToken(request);
        }
        return userId;
    }

    public Long getRoleId() {
        HttpServletRequest request = getRequest();
        Long roleId;
        if (session) {
            roleId = sessionUtils.getRoleId(request);
        } else {
            roleId = jwtTokenUtils.getRoleIdFromToken(request);
        }
        return roleId;
    }

    protected Map<String, Object> returnoutResultMap(ResultMapInfo info) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("code", info.getCode());
        resultMap.put("msg", info.getMessage());
        if (info.getCode() == 1) {
            resultMap.put("status", "SUCCESS");
        } else {
            resultMap.put("status", "FALT");
        }
        resultMap.put("data", null);
        return resultMap;
    }


    protected Map<String, Object> returnResultMap(ResultMapInfo info) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("code", info.getCode());
        resultMap.put("message", info.getMessage());
        if (info.getCode() == 1) {
            resultMap.put("status", "SUCCESS");
        } else {
            resultMap.put("status", "FALT");
        }
        resultMap.put("data", null);
        return resultMap;
    }

    protected Map<String, Object> returnResultMap(ResultMapInfo info, IPage<?> data) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("code", info.getCode());
        resultMap.put("data", data);
        resultMap.put("message", info.getMessage());
        if (info.getCode() == 1) {
            resultMap.put("status", "SUCCESS");
        } else {
            resultMap.put("status", "FALT");
        }
        return resultMap;
    }

    //	protected Map<String, Object> returnResultMap(ResultMapInfo info, IPage<RoleInfo> data) {
//		Map<String, Object> resultMap = new HashMap<String, Object>();
//		resultMap.put("code", info.getCode());
//		resultMap.put("data", data);
//		resultMap.put("message", info.getMessage());
//		if (info.getCode() == 1) resultMap.put("status", "SUCCESS");
//		else resultMap.put("status", "FALT");
//		return resultMap;
//	}
//
    protected Map<String, Object> returnResultMap(ResultMapInfo info, Map<String, Object> data) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("code", info.getCode());
        resultMap.put("data", data);
        resultMap.put("message", info.getMessage());
        if (info.getCode() == 1) {
            resultMap.put("status", "SUCCESS");
        } else {
            resultMap.put("status", "FALT");
        }
        return resultMap;
    }

    protected Map<String, Object> returnResultMap(ResultMapInfo info, String data) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("code", info.getCode());
        resultMap.put("data", data);
        resultMap.put("message", info.getMessage());
        if (info.getCode() == 1) {
            resultMap.put("status", "SUCCESS");
        } else {
            resultMap.put("status", "FALT");
        }
        return resultMap;
    }

    protected Map<String, Object> returnResultMap(ResultMapInfo info, Object data) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("code", info.getCode());
        resultMap.put("data", data);
        resultMap.put("message", info.getMessage());
        if (info.getCode() == 1) {
            resultMap.put("status", "SUCCESS");
        } else {
            resultMap.put("status", "FALT");
        }
        return resultMap;
    }
}
