package com.ape.gateway.filter;

import com.ape.common.utils.StringUtils;
import com.ape.gateway.constant.AuthConstant;
import com.nimbusds.jose.JWSObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

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

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String token = exchange.getRequest().getHeaders().getFirst(AuthConstant.JWT_TOKEN_HEADER);
        // exchange.getRequest().getURI().getRawPath().contains("/oauth/token") 解决password模式登录时，由于存在Basic头部，防止下面代码去解释jwt报错
        if (StringUtils.isBlank(token) || exchange.getRequest().getURI().getRawPath().contains("/oauth/token")){
            return chain.filter(exchange);
        }

        try {
            // 从token中解析用户信息并设置到Header中去
            String realToken = token.replace("Bearer ", "");
            JWSObject jwsObject = JWSObject.parse(realToken);
            String userInfo = jwsObject.getPayload().toString();
            log.info("Authorization.filter() -- userInfo：{}", userInfo);
            ServerHttpRequest request = exchange.getRequest().mutate().header("userInfo", userInfo).build();
            exchange = exchange.mutate().request(request).build();
        } catch (ParseException e) {
            e.printStackTrace();
            log.error("JWT parse error：{}", e.getMessage());
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
