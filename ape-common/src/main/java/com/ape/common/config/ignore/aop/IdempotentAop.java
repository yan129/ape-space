package com.ape.common.config.ignore.aop;

import com.ape.common.annotation.ApiIdempotent;
import com.ape.common.config.ignore.interceptor.IdempotentTokenService;
import com.ape.common.utils.IpUtil;
import com.ape.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/7/30
 *
 * 对有ApiIdempotent注解的接口请求生成token，用来幂等性判断
 */
@Slf4j
@Aspect
@Component
public class IdempotentAop {

    @Autowired
    private IdempotentTokenService idempotentTokenService;

    @Pointcut(value = "@annotation(apiIdempotent)", argNames = "apiIdempotent")
    public void pointcut(ApiIdempotent apiIdempotent){}

    /**
     * 前置通知，方法执行前就会执行
     * @param joinPoint
     * @param apiIdempotent
     * @return
     * @throws Throwable
     */
    @Before(value = "pointcut(apiIdempotent)", argNames = "joinPoint,apiIdempotent")
    public void doBefore(JoinPoint joinPoint, ApiIdempotent apiIdempotent) throws Throwable {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
        idempotentTokenService.generateToken(request);
    }
}
