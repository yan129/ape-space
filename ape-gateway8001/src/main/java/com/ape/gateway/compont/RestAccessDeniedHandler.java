package com.ape.gateway.compont;

import com.ape.common.model.ResponseCode;
import com.ape.common.model.ResultVO;
import com.ape.common.utils.HttpResponseUtil;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/7/4
 *
 * 自定义返回结果：没有权限访问时
 */
@Component
public class RestAccessDeniedHandler implements ServerAccessDeniedHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException e) {
        ServerHttpResponse response = exchange.getResponse();
        ResultVO<ResponseCode> error = ResultVO.ERROR(ResponseCode.FORBIDDEN);
        return HttpResponseUtil.MonoOutput(response, error);
    }
}
