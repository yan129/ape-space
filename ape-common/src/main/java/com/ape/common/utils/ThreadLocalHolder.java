package com.ape.common.utils;

import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2022/5/14
 */
@Component
public class ThreadLocalHolder {

    private static final ThreadLocal<Object> threadLocal = new ThreadLocal<>();

    public void set(Object o) {
        threadLocal.set(o);
    }

    public Object get(){
        return threadLocal.get();
    }

    public void remove(){
        threadLocal.remove();
    }
}
