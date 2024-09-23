package com.push.schedule;

import com.push.service.schedule.UpstreamCallbackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Description:
 * Create DateTime: 2020/4/2 17:30
 *
 *

 */
@Slf4j
@Component
public class UpCallbackStatusTask {

    @Resource
    private UpstreamCallbackService upstreamCallbackService;

    /**
     * 消费上游回调消息
     * <p>
     * 每5秒执行一次
     */
    //@Scheduled(fixedDelay = 1000)
    void handleUpstreamCallbackSuccess() {
//        log.info("【上游回调成功】处理开始:{}", DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
        upstreamCallbackService.handleUpstreamCallbackSuccess();
//        log.info("【上游回调成功】处理结束:{}", DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 上游回调失败
     * <p>
     * 每5秒执行一次
     */
    //@Scheduled(fixedDelay = 180000)
    @Scheduled(fixedDelay = 5000)
    void handleUpstreamCallbackFail() {
        upstreamCallbackService.handleUpstreamCallbackFail();
    }
}
