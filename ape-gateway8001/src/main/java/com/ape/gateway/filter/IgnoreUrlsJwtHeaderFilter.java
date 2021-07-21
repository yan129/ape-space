package com.ape.gateway.filter;

import com.ape.gateway.config.IgnoreUrlsConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/7/4
 *
 * 白名单路径访问时需要移除JWT请求头
 */
@Component
public class IgnoreUrlsJwtHeaderFilter implements WebFilter {

    @Autowired
    private IgnoreUrlsConfig ignoreUrlsConfig;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        URI uri = request.getURI();
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        List<String> ignoreUrls = ignoreUrlsConfig.getUrls();
        //白名单路径移除JWT请求头
        for (String ignoreUrl : ignoreUrls) {
            if (antPathMatcher.match(ignoreUrl, uri.getPath())){
                request = exchange.getRequest().mutate().header("Authorization", "").build();
                exchange = exchange.mutate().request(request).build();
                return chain.filter(exchange);
            }
        }
        return chain.filter(exchange);
    }
}
