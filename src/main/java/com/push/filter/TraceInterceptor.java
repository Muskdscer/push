package com.push.filter;

import com.plumelog.core.TraceId;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * Description:
 * Create DateTime: 2020/8/24 18:02
 *
 *

 */
@Component
public class TraceInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        TraceId.logTraceID.set(UUID.randomUUID().toString().replace("-", "").substring(0, 7));
        return true;
    }
}
