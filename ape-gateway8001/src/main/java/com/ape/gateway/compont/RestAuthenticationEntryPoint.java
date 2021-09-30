package com.ape.gateway.compont;

import cn.hutool.json.JSONUtil;
import com.ape.common.exception.ServiceException;
import com.ape.common.model.ResponseCode;
import com.ape.common.model.ResultVO;
import com.ape.common.utils.HttpResponseUtil;
import com.ape.common.utils.StringUtils;
import com.ape.gateway.constant.AuthConstant;
import com.nimbusds.jose.JWSObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.*;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.text.ParseException;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/7/4
 *
 * 自定义返回结果：没有登录或token过期时，该认证类比 AuthGlobalFilter 和 AuthorizationManager 优先执行
 */
@Slf4j
@Component
public class RestAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException e) {
        System.out.println(e);
        ServerHttpResponse response = exchange.getResponse();
        ResultVO<Object> error = ResultVO.ERROR();
        if (e instanceof InsufficientAuthenticationException){
            error.setData(ResponseCode.UNAUTHORIZED);
        }

        // InvalidBearerTokenException jwt过期或无效
        if (e instanceof InvalidBearerTokenException){
            String userInfo = null;
            try {
                String token = exchange.getRequest().getHeaders().getFirst(AuthConstant.JWT_TOKEN_HEADER);
                String realToken = token.replace("Bearer ", "");
                JWSObject jwsObject = JWSObject.parse(realToken);
                userInfo = jwsObject.getPayload().toString();
            }catch (ParseException parseEx){
                parseEx.printStackTrace();
                log.error("RestAuthenticationEntryPoint --> JWT parse error：{}", e.getMessage());
                // 抛异常，无效令牌
            }
            Map userInfoMap = JSONUtil.toBean(userInfo, Map.class);

            long exp = Long.parseLong(String.valueOf(userInfoMap.get("exp"))) - System.currentTimeMillis() / 1000;
            // token过期，刷新token
            if (exp <= 0){
                String userName = (String) userInfoMap.get("user_name");
                String cacheRefreshToken = stringRedisTemplate.opsForValue().get(userName);

                // 缓存在Redis中的refreshToken过期，提醒用户重新登录，否则调用刷新token接口
                if (StringUtils.isBlank(cacheRefreshToken)){
                    error.setData("token已过期，请重新登录");
                }else {
                    System.out.println("====++====");
                    Map map = sendTokenRefreshRequest(cacheRefreshToken, exchange.getRequest(), exchange.getResponse());

                    return HttpResponseUtil.MonoOutput(response, map);
                }
            }

        }

        return HttpResponseUtil.MonoOutput(response, error);
    }

    /**
     *
     *
     * 发送token刷新请求
     * @param refreshToken
     * @param request
     * @param response
     * @return
     */
    private Map sendTokenRefreshRequest(String refreshToken, ServerHttpRequest request, ServerHttpResponse response) {
        MultiValueMap<String, String> requestData = new LinkedMultiValueMap<>();
        requestData.add("refresh_token", refreshToken);
        requestData.add("grant_type", "refresh_token");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth("ape", "ape");
        throw new ServiceException("vvv");
//        Map body = restTemplate.exchange("http://127.0.0.1:9526/ape-user1/oauth/token", HttpMethod.POST,
//                new HttpEntity<>(requestData, headers), Map.class).getBody();
//
//        System.out.println(body);
//
//        return body;
    }
}
