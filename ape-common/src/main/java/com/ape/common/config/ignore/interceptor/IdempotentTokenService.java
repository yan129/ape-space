package com.ape.common.config.ignore.interceptor;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/7/29
 *
 * 生成和校验幂等性token
 */
public interface IdempotentTokenService {

    String METHOD_NAME_KEY = "methodName";
    String CACHE_TOKEN_KEY = "idempotent:token";
    String EXPIRED_TIME = "expiredTime";

    /**
     * 生成token
     * @param request
     * @return
     */
    void generateToken(HttpServletRequest request);

    /**
     * 校验token
     * @param request
     */
    void checkTokenExist(HttpServletRequest request);
}
