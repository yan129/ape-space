package com.ape.user.aop;

import cn.hutool.json.JSONUtil;
import com.ape.common.model.ResultVO;
import com.ape.common.utils.StringUtils;
import com.nimbusds.jose.JWSObject;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/6/23
 * oauth登录成功后，对其信息进行再封装返回
 */
@Slf4j
@Aspect
@Component
public class OauthAuthAop {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 拦截oauth2的 /token 路径方法
     * @return
     */
    @Around("execution(* org.springframework.security.oauth2.provider.endpoint.TokenEndpoint.postAccessToken(..))")
    public ResponseEntity<ResultVO<Object>> handleOauthTokenRequest(ProceedingJoinPoint point) throws Throwable {
        Object proceed = point.proceed();

        ResultVO<Object> resultVO = new ResultVO<>();
        if (StringUtils.isNotEmpty(proceed)){
            ResponseEntity<OAuth2AccessToken> responseEntity = (ResponseEntity<OAuth2AccessToken>)proceed;
            OAuth2AccessToken body = responseEntity.getBody();

            String refreshToken = body.getRefreshToken().getValue();
            // 解析 refreshToken
            JWSObject jwsObject = JWSObject.parse(refreshToken);
            String refreshTokenInfo = jwsObject.getPayload().toString();
            Map refreshTokenMap = JSONUtil.toBean(refreshTokenInfo, Map.class);
            System.out.println(refreshToken);
            long exp = Long.parseLong(String.valueOf(refreshTokenMap.get("exp"))) - System.currentTimeMillis() / 1000;
            String userName = (String) refreshTokenMap.get("user_name");

            // 将 RefreshToken 存入 Redis
            stringRedisTemplate.opsForValue().set(userName, refreshToken, exp, TimeUnit.SECONDS);
            // 设置 RefreshToken 为空，不返回
            ((DefaultOAuth2AccessToken) body).setRefreshToken(null);

            if (responseEntity.getStatusCode().is2xxSuccessful()){
                resultVO = ResultVO.OK(body);
            }else {
                log.error("Oauth2 Authorization Fail: {}", responseEntity.getStatusCode().toString());
                resultVO = ResultVO.ERROR("认证失败");
            }
        }
        return ResponseEntity.status(HttpStatus.OK.value()).body(resultVO);
    }
}
