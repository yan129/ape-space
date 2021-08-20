package com.ape.gateway.compont;

import com.ape.common.model.ResponseCode;
import com.ape.common.model.ResultVO;
import com.ape.common.utils.HttpResponseUtil;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/7/4
 *
 * 自定义返回结果：没有登录或token过期时
 */
@Component
public class RestAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {

    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException e) {
        System.out.println(exchange.getRequest().getURI().getPath()+"====");
        System.out.println(exchange.getRequest().getURI().getRawPath()+"====");
        System.out.println(e);
        ServerHttpResponse response = exchange.getResponse();
        ResultVO<Object> error = ResultVO.ERROR();
        if (e instanceof InsufficientAuthenticationException){
            error.setData(ResponseCode.UNAUTHORIZED);
        }
        return HttpResponseUtil.MonoOutput(response, error);
    }
}
