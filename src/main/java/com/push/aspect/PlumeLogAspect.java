package com.push.aspect;

import com.plumelog.core.LogMessageThreadLocal;
import com.plumelog.core.TraceId;
import com.plumelog.core.TraceMessage;
import com.plumelog.core.constant.LogMessageConstant;
import com.plumelog.core.util.GfJsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Description: PlumeLog切面
 * Create DateTime: 2020/8/25 14:20
 *
 *

 */
@Slf4j
//@Profile({"dev", "prod"})
//@Aspect
//@Component
public class PlumeLogAspect {

    /**
     * 不需要经过plumeLog的线程
     */
    private static final List<String> NOT_PLUME_LOG_THREAD = Stream.of("taskScheduler").collect(Collectors.toList());

    /**
     * 日常业务日志
     *
     * @param proceedingJoinPoint ProceedingJoinPoint
     * @return 处理结果
     */
//    @Around("within(com.push..*))")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Thread thread = Thread.currentThread();

        if (!allow(thread.getName())) {
            return proceedingJoinPoint.proceed();
        } else {
            return process(proceedingJoinPoint);
        }
    }

    /**
     * 定时任务业务日志
     *
     * @param proceedingJoinPoint ProceedingJoinPoint
     * @return 处理结果
     */
/*    @Around("within(com.push.schedule.*)")
    public Object scheduleAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        TraceId.logTraceID.set(UUID.randomUUID().toString().replace("-", "").substring(0, 7));
        return process(proceedingJoinPoint);
    }*/
    private Object process(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        TraceMessage traceMessage = LogMessageThreadLocal.logMessageThreadLocal.get();
        String traceId = TraceId.logTraceID.get();
        if (traceMessage == null || traceId == null) {
            traceMessage = new TraceMessage();
            traceMessage.getPositionNum().set(0);
        }
        traceMessage.setTraceId(traceId);
        traceMessage.setMessageType(proceedingJoinPoint.getSignature().toString());
        traceMessage.setPosition(LogMessageConstant.TRACE_START);
        traceMessage.getPositionNum().incrementAndGet();
        LogMessageThreadLocal.logMessageThreadLocal.set(traceMessage);
        log.warn(LogMessageConstant.TRACE_PRE + GfJsonUtil.toJSONString(traceMessage));
        Object proceed = proceedingJoinPoint.proceed();
        traceMessage.setMessageType(proceedingJoinPoint.getSignature().toString());
        traceMessage.setPosition(LogMessageConstant.TRACE_END);
        traceMessage.getPositionNum().incrementAndGet();
        log.warn(LogMessageConstant.TRACE_PRE + GfJsonUtil.toJSONString(traceMessage));
        return proceed;
    }

    private boolean allow(String threadName) {
        boolean allow = true;
        for (String name : NOT_PLUME_LOG_THREAD) {
            if (threadName.startsWith(name)) {
                allow = false;
                break;
            }
        }
        return allow;
    }

}
