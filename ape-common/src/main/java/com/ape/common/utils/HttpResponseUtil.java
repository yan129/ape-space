package com.ape.common.utils;

import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.istack.internal.NotNull;
import org.apache.http.HttpHeaders;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/6/28
 */
public class HttpResponseUtil {

    /**
     * 基本 response 返回
     * @param response
     * @param data
     * @throws IOException
     */
    public static void output(@NotNull HttpServletResponse response, Object data) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();
        writer.write(new ObjectMapper().writeValueAsString(data));
        writer.flush();
        writer.close();
    }

    /**
     * 基于 webflux 的 response 返回
     * @param response
     * @param data
     * @return
     */
    public static Mono<Void> MonoOutput(ServerHttpResponse response, Object data){
        response.setStatusCode(HttpStatus.OK);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        String body = JSONUtil.toJsonStr(data);
        DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(Charset.forName("UTF-8")));
        return response.writeWith(Mono.just(buffer));
    }

}
