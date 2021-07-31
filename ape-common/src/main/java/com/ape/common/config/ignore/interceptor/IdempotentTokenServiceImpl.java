package com.ape.common.config.ignore.interceptor;

import com.ape.common.exception.ServiceException;
import com.ape.common.model.ResponseCode;
import com.ape.common.utils.IpUtil;
import com.ape.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/7/29
 */
@Service
public class IdempotentTokenServiceImpl implements IdempotentTokenService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void generateToken(HttpServletRequest request) {
        String methodName = (String) request.getAttribute(METHOD_NAME_KEY);
        String token = StringUtils.joinWith("-", CACHE_TOKEN_KEY, IpUtil.getIpAddress(request), methodName);

        //将token放入redis中，设置有效期为15S，不存在key则设置
        Boolean absent = stringRedisTemplate.opsForValue().setIfAbsent(token, token, TOKEN_VALID_PERIOD, TimeUnit.SECONDS);
        // 已存在key
        if (!absent){
            // 再次设置刷新key过期时间
            stringRedisTemplate.opsForValue().set(token, token, TOKEN_VALID_PERIOD, TimeUnit.SECONDS);
            throw new ServiceException(ResponseCode.REPEAT_SUBMIT.getMsg());
        }
    }

    @Override
    public void checkTokenExist(HttpServletRequest request) {
        String methodName = ((String) request.getAttribute(METHOD_NAME_KEY));
        String token = StringUtils.joinWith("-", CACHE_TOKEN_KEY, IpUtil.getIpAddress(request), methodName);

        // 存在token抛异常，请勿重复提交
        if (stringRedisTemplate.hasKey(token)){
            // 再次设置刷新key过期时间
            stringRedisTemplate.opsForValue().set(token, token, TOKEN_VALID_PERIOD, TimeUnit.SECONDS);
            throw new ServiceException(ResponseCode.REPEAT_SUBMIT.getMsg());
        }

    }
}
