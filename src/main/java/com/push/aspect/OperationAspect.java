package com.push.aspect;

import com.push.common.annotation.Operation;
import com.push.common.enums.OperationResultEnum;
import com.push.common.enums.UserTypeCodeEnum;
import com.push.entity.agent.ShopAgentLogOperate;
import com.push.entity.agent.UpstreamAgentLogOperate;
import com.push.entity.platform.PlatformLogOperate;
import com.push.entity.shop.ShopLogOperate;
import com.push.entity.upstream.UpstreamLogOperate;
import com.push.service.agent.ShopAgentLogOperateService;
import com.push.service.agent.UpstreamAgentLogOperateService;
import com.push.service.platform.PlatformLogOperateService;
import com.push.service.shop.ShopLogOperateService;
import com.push.service.upstream.UpstreamLogOperateService;
import com.push.sso.JwtTokenUtils;
import com.push.sso.SessionUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * Description:
 * Create DateTime: 2020-04-07 09:43
 *
 * 

 */
@Slf4j
@Aspect
@Component
public class OperationAspect {

    @Value("${sso.session}")
    private Boolean session;

    @Resource
    private JwtTokenUtils jwtTokenUtils;

    @Resource
    private SessionUtils sessionUtils;

    @Resource
    private UpstreamLogOperateService upstreamLogOperateService;

    @Resource
    private PlatformLogOperateService platformLogOperateService;

    @Resource
    private ShopLogOperateService shopLogOperateService;

    @Resource
    private ShopAgentLogOperateService shopAgentLogOperateService;

    @Resource
    private UpstreamAgentLogOperateService upstreamAgentLogOperateService;

    @Pointcut("@annotation(com.push.common.annotation.Operation)")
    public void operationLog() {
    }


    @AfterReturning(pointcut = "operationLog()", returning = "result")
    public void doUpstreamAfterReturning(JoinPoint joinPoint, Object result) {
        handlerOperationLog(joinPoint, result, null);
    }

    @AfterThrowing(pointcut = "operationLog()", throwing = "e")
    public void doUpstreamAfterThrowing(JoinPoint joinPoint, Exception e) {
        handlerOperationLog(joinPoint, null, e);
    }

    private void handlerOperationLog(JoinPoint joinPoint, Object result, Exception e) {
        Operation operation = getAnnotation(joinPoint);
        if (operation != null) {
            UserTypeCodeEnum userTypeCodeEnum = operation.userType();

            Long userId = getUserId();

            String operationFeedback = e == null ? OperationResultEnum.SUCCESS.getMessage() : OperationResultEnum.FAILED.getMessage() + "————错误信息为：" + e.getMessage();

            switch (userTypeCodeEnum) {
                case UPSTREAM_USER:
                    UpstreamLogOperate upstreamLogOperate = new UpstreamLogOperate(userId, operation.operationType().getMessage(),
                            operation.operationDescribe(), operationFeedback);
                    upstreamLogOperateService.save(upstreamLogOperate);
                    break;
                case PLATFORM_USER:
                    PlatformLogOperate platformLogOperate = new PlatformLogOperate(userId, operation.operationType().getMessage(),
                            operation.operationDescribe(), operationFeedback);
                    platformLogOperateService.save(platformLogOperate);
                    break;
                case SHOP_USER:
                    ShopLogOperate shopLogOperate = new ShopLogOperate(userId, operation.operationType().getMessage(),
                            operation.operationDescribe(), operationFeedback);
                    shopLogOperateService.save(shopLogOperate);
                    break;
                case SHOP_AGENT_USER:
                    ShopAgentLogOperate shopAgentLogOperate = new ShopAgentLogOperate(userId,operation.operationType().getMessage(),
                            operation.operationDescribe(),operationFeedback);
                    shopAgentLogOperateService.save(shopAgentLogOperate);
                    break;
                case UPSTREAM_AGENT_USER:
                    UpstreamAgentLogOperate upstreamAgentLogOperate = new UpstreamAgentLogOperate(userId, operation.operationType().getMessage(),
                            operation.operationDescribe(), operationFeedback);
                    upstreamAgentLogOperateService.save(upstreamAgentLogOperate);
                    break;
            }
        }
    }

    private Operation getAnnotation(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        if (method != null) {
            return method.getAnnotation(Operation.class);
        }
        return null;
    }

    private Long getUserId() {
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = sra.getRequest();
        Long userId;
        if (session) {
            userId = sessionUtils.getUserId(request);
        } else {
            userId = jwtTokenUtils.getUserIdFromToken(request);
        }
        return userId;
    }
}
