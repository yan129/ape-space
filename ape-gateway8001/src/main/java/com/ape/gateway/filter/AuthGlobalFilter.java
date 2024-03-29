package com.ape.gateway.filter;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.net.URLEncoder;
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
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/7/18
 *
 * 将登录用户的JWT转化成用户信息的全局过滤器
 */
@Slf4j
@Component
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String token = exchange.getRequest().getHeaders().getFirst(AuthConstant.JWT_TOKEN_HEADER);
        // exchange.getRequest().getURI().getRawPath().contains("/oauth/token") 解决password模式登录时，由于存在Basic头部，防止下面代码去解释jwt报错
        if (StringUtils.isBlank(token) || exchange.getRequest().getURI().getRawPath().contains("/user/oauth/token")){
            // 请求登录接口时，设置上basic认证
            ServerHttpRequest request = exchange.getRequest().mutate().header(AuthConstant.JWT_TOKEN_HEADER, "Basic " + Base64.encode("ape:ape")).build();
            exchange = exchange.mutate().request(request).build();
            return chain.filter(exchange);
        }

        try {
            // 从token中解析用户信息并设置到Header中去
            String realToken = token.replace("Bearer ", "");
            JWSObject jwsObject = JWSObject.parse(realToken);
            String userInfo = jwsObject.getPayload().toString();

            // 检查黑名单，用于退出登录
            boolean checkBlacklistExistToken = checkBlacklistExistToken(userInfo);
            if (checkBlacklistExistToken){
                return HttpResponseUtil.MonoOutput(exchange.getResponse(), ResultVO.OK(ResponseCode.UNAUTHORIZED));
            }

            log.info("Authorization.filter() -- userInfo：{}", userInfo);
            // 解决被保护的微服务获取header中文参数乱码问题
            userInfo = URLEncoder.DEFAULT.encode(userInfo, StandardCharsets.UTF_8);
            ServerHttpRequest request = exchange.getRequest().mutate().header("userInfo", userInfo).build();
            exchange = exchange.mutate().request(request).build();
        } catch (ParseException e) {
            e.printStackTrace();
            log.error("JWT parse error：{}", e.getMessage());
        }
        return chain.filter(exchange);
    }

    private boolean checkBlacklistExistToken(String payload) {
        JSONObject tokenJson = JSONUtil.parseObj(payload);
        String jti = tokenJson.getStr("jti");
        return stringRedisTemplate.hasKey("logout:token:" + jti);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
