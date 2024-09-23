package com.push.common.utils;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Title: WebUtil.class
 * Description:
 * Create DateTime: 2018/6/26 19:14
 *
 * 

 */
public class WebUtil {

    public WebUtil() {
    }

    public static String getUserIp(HttpServletRequest request) {
        return getUserIp(request, 2);
    }

    public static String getUserIp(HttpServletRequest request, int proxy) {
        String userIp = null;
        if (request != null) {
            if (proxy >= 1) {
                String fwd = StringUtils.trimToNull(request.getHeader("X-Forwarded-For"));
                if (fwd != null) {
                    String[] ips = fwd.length() > 80 ? fwd.substring(fwd.length() - 80).split(",") : fwd.split(",");
                    String ip;
                    if (proxy > 1 && ips.length != 1) {
                        ip = StringUtils.trimToEmpty(ips[ips.length - Math.min(proxy, ips.length)]);
                        userIp = ip.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}") ? ip : null;
                    } else {
                        ip = StringUtils.trimToEmpty(ips[ips.length - 1]);
                        userIp = ip.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}") ? ip : null;
                    }
                }
            }
            if (userIp == null) {
                userIp = request.getRemoteAddr();
            }
            if ("0:0:0:0:0:0:0:1".equals(userIp)) {
                userIp = "127.0.0.1";
            }
        }
        return userIp;
    }


    public static String getFirstServerName(HttpServletRequest request) {
        String serverName = request.getServerName();
        return serverName.substring(0, serverName.indexOf("."));
    }

}
