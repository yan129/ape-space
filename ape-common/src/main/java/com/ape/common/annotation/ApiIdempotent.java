package com.ape.common.annotation;

import java.lang.annotation.*;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/7/29
 *
 * 作用域controller方法上，保证接口幂等性
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiIdempotent {
}
