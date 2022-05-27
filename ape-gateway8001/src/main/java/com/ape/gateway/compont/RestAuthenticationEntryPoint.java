package com.ape.gateway.compont;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ape.common.model.ResponseCode;
import com.ape.common.model.ResultVO;
import com.ape.common.utils.HttpResponseUtil;
import com.ape.common.utils.StringUtils;
import com.ape.gateway.constant.AuthConstant;
import com.nimbusds.jose.JWSObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.*;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.text.ParseException;
import java.util.List;
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
    @Autowired
    private DiscoveryClient discoveryClient;

    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException e) {
        ServerHttpResponse response = exchange.getResponse();
        ResultVO<Object> error = ResultVO.ERROR();
        if (e instanceof InsufficientAuthenticationException){
            error.setData(ResponseCode.UNAUTHORIZED);
        }

        // InvalidBearerTokenException jwt过期或无效
        if (e instanceof InvalidBearerTokenException){
            String userInfo;
            String realToken;
            try {
                String token = exchange.getRequest().getHeaders().getFirst(AuthConstant.JWT_TOKEN_HEADER);
                realToken = token.replace("Bearer ", "");
                JWSObject jwsObject = JWSObject.parse(realToken);
                userInfo = jwsObject.getPayload().toString();
            }catch (ParseException parseEx){
                parseEx.printStackTrace();
                log.error("RestAuthenticationEntryPoint --> JWT parse error：{}", e.getMessage());
                // 抛异常，无效令牌
                error.setMessage("Token无效");
                return HttpResponseUtil.MonoOutput(response, error);
            }
            Map userInfoMap = JSONUtil.toBean(userInfo, Map.class);

            long exp = Long.parseLong(String.valueOf(userInfoMap.get("exp"))) - System.currentTimeMillis() / 1000;
            // token过期，刷新token
            if (exp <= 0){
                String jti = (String) userInfoMap.get("jti");
                String cacheRefreshToken = stringRedisTemplate.opsForValue().get("refreshToken:" + jti);

                // 缓存在Redis中的refreshToken过期，提醒用户重新登录，否则调用刷新token接口
                if (StringUtils.isBlank(cacheRefreshToken)){
                    error.setData("token已过期，请重新登录");
                }else {
                    ResponseEntity<JSONObject> entity = this.sendTokenRefreshRequest(realToken);
                    if (StringUtils.isEmpty(entity)) {
                        return HttpResponseUtil.MonoOutput(response, "服务请求异常");
                    }
                    this.deleteOldRefreshToken(entity.getStatusCode().is2xxSuccessful(), jti);
                    return HttpResponseUtil.MonoOutput(response, entity.getBody());
                }
            }

        }

        return HttpResponseUtil.MonoOutput(response, error);
    }

    /**
     *
     * 发送token刷新请求
     * @param accessToken
     * @return
     */
    private ResponseEntity<JSONObject> sendTokenRefreshRequest(String accessToken) {
        // response.setStatusCode(HttpStatus.SEE_OTHER);
        // response.getHeaders().set(HttpHeaders.ALLOW, "http://127.0.0.1:9526/ape-user/user/oauth/token?loginType=refresh&token=" + refreshToken);
        // return response.setComplete();

        MultiValueMap<String, String> requestMap = this.buildRefreshTokenMap(accessToken);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.setBasicAuth("ape","ape");
        //构造请求实体和头
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestMap, httpHeaders);
        String refreshTokenUrl = this.getRefreshTokenUrl();
        if (StringUtils.isBlank(refreshTokenUrl)) {
            return null;
        }
        return restTemplate.postForEntity(refreshTokenUrl, requestEntity, JSONObject.class);
    }

    private String getRefreshTokenUrl(){
        List<ServiceInstance> instances = discoveryClient.getInstances("APE-USER-SERVICE");
        if (CollectionUtils.isEmpty(instances)) {
            return null;
        }
        String refreshToken = instances.get(0).getUri() + "/ape-user/user/oauth/token";
        return refreshToken;
    }

    private MultiValueMap<String, String> buildRefreshTokenMap(String accessToken){
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.set("loginType", "refresh");
        parameters.set("token", accessToken);
        return parameters;
    }

    private void deleteOldRefreshToken(Boolean is2xxSuccessful, String tokenKey) {
        if (is2xxSuccessful) {
            stringRedisTemplate.delete("refreshToken:" + tokenKey);
        }
    }
}
