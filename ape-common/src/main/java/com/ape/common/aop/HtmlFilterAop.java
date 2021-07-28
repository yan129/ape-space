package com.ape.common.aop;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ape.common.annotation.HtmlFilter;
import com.ape.common.factory.HtmlFilterFactory;
import com.ape.common.model.BaseEntity;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private HtmlFilterFactory htmlFilterFactory;
//    private static String[] path = aa.split(".");
//    private static String packagePrefix = path.length == 1 ? path[0] : (path[0] + "." + path[1]);
    private static String packagePrefix = "com.ape";

    @Pointcut(value = "@annotation(htmlFilter)", argNames = "htmlFilter")
    public void pointcut(HtmlFilter htmlFilter){}

    @Around(value = "pointcut(htmlFilter)", argNames = "joinPoint,htmlFilter")
    public Object around(ProceedingJoinPoint joinPoint, HtmlFilter htmlFilter) throws Throwable {
        Object[] args = joinPoint.getArgs();
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < args.length; i++) {
            // 注解在方法上 "com.ape"
            if (args[i].getClass().getName().contains(packagePrefix)){
                JSONObject parseObj = JSONUtil.parseObj(args[i]);
                sb.append(parseObj);

                Signature signature = joinPoint.getSignature();
                Class<?>[] parameterTypes = ((MethodSignature) signature).getMethod().getParameterTypes();
                for (Class<?> parameterType : parameterTypes) {
                    if (parameterType.getName().contains(packagePrefix)) {
                        BaseEntity model = htmlFilterFactory.buildModel(parameterType, sb.toString());
                        args[i] = model;
                    }
                }
            }
        }

        return joinPoint.proceed(args);
    }
}
