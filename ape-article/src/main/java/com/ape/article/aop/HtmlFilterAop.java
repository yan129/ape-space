package com.ape.article.aop;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ape.article.annotation.HtmlFilter;
import com.ape.article.factory.HtmlFilterFactory;
import com.ape.common.model.BaseEntity;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/5/16
 */
@Aspect
@Component
public class HtmlFilterAop {

    @Pointcut(value = "@annotation(htmlFilter)", argNames = "htmlFilter")
    public void pointcut(HtmlFilter htmlFilter){}

    @Around(value = "pointcut(htmlFilter)", argNames = "joinPoint,htmlFilter")
    public Object around(ProceedingJoinPoint joinPoint, HtmlFilter htmlFilter) throws Throwable {
        Object[] args = joinPoint.getArgs();
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < args.length; i++) {
            // 注解在方法上
            if (args[i].getClass().getName().contains("com.ape")){
                JSONObject parseObj = JSONUtil.parseObj(args[i]);
                sb.append(parseObj);

                Signature signature = joinPoint.getSignature();
                Class<?>[] parameterTypes = ((MethodSignature) signature).getMethod().getParameterTypes();
                for (Class<?> parameterType : parameterTypes) {
                    if (parameterType.getName().contains("com.ape")) {
                        BaseEntity model = HtmlFilterFactory.buildModel(parameterType, sb.toString());
                        args[i] = model;
                    }
                }
            }
        }

        return joinPoint.proceed(args);
    }
}
