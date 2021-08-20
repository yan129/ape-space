package com.ape.common.config.ignore.interceptor;

import com.ape.common.annotation.ApiIdempotent;
import com.ape.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/7/30
 *
 * 接口幂等性校验拦截器
 */
@Component
public class IdempotentTokenInterceptor implements HandlerInterceptor {

    @Autowired
    private IdempotentTokenService idempotentTokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)){
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        //幂等性校验, 校验通过则放行, 校验失败则抛出异常, 并通过统一异常处理返回友好提示
        ApiIdempotent apiIdempotent = handlerMethod.getMethod().getAnnotation(ApiIdempotent.class);
        if (StringUtils.isNotEmpty(apiIdempotent)){
            // 获取请求接口的方法名
            String methodName = handlerMethod.getMethod().getName();
            request.setAttribute(IdempotentTokenService.METHOD_NAME_KEY, methodName);
            request.setAttribute(IdempotentTokenService.EXPIRED_TIME, apiIdempotent.value());
            // 方法抛异常会阻断向下执行
            idempotentTokenService.checkTokenExist(request);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
