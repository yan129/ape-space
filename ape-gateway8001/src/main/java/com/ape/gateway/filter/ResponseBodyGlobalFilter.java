package com.ape.gateway.filter;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.antherd.smcrypto.sm2.Sm2;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/12/19
 *
 * 修改微服务响应体数据
 */
@Slf4j
@Component
public class ResponseBodyGlobalFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest httpRequest = exchange.getRequest();
        ServerHttpResponse httpResponse = exchange.getResponse();
        DataBufferFactory bufferFactory = httpResponse.bufferFactory();

        ServerHttpResponseDecorator responseDecorator = new ServerHttpResponseDecorator(httpResponse){
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                if (body instanceof Flux) {
                    Flux<? extends DataBuffer> fluxBody = (Flux<? extends DataBuffer>) body;
                    return super.writeWith(fluxBody.buffer().map(dataBuffers -> {
                        //DefaultDataBufferFactory join 乱码的问题解决
                        DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();
                        DataBuffer join = dataBufferFactory.join(dataBuffers);
                        byte[] content = new byte[join.readableByteCount()];
                        join.read(content);
                        //释放掉内存
                        DataBufferUtils.release(join);

//                        StringBuilder sb = new StringBuilder();
//                        dataBuffers.forEach(dataBuffer -> {
//                            byte[] content = new byte[dataBuffer.readableByteCount()];
//                            dataBuffer.read(content);
//                            // 释放掉内存
//                            DataBufferUtils.release(dataBuffer);
//                            // 读取的response分段内容
//                            String data = new String(content, Charset.forName("UTF-8"));
//                            sb.append(data);
//                        });
                        String sb = new String(content, Charset.forName("UTF-8"));

                        if (!isJsonStr(sb)){
                            byte[] uppedContent = sb.getBytes(Charset.forName("UTF-8"));
                            return bufferFactory.wrap(uppedContent);
                        }

                        JSONObject jsonObject = JSONUtil.parseObj(sb);
                        Boolean success = jsonObject.get("success", Boolean.class);
                        Boolean isEncrypt = jsonObject.get("encrypt", Boolean.class);
                        if (success && isEncrypt){
                            // 随机生成AES密钥
                            byte[] aesKey = SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue()).getEncoded();
                            // 构建AES
                            SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, aesKey);
                            // aes加密数据
                            String data = jsonObject.getStr("data");
                            jsonObject.set("data", aes.encryptHex(data, Charset.forName("UTF-8")));

                            if ("wx".equals(httpRequest.getHeaders().getFirst("browser"))){
                                // sm2加密aesKey
                                String encryptAesKey = encryptAesKey(httpRequest, Base64.encode(aesKey));
                                httpResponse.getHeaders().set("aesKey", encryptAesKey);
                                httpResponse.getHeaders().set("sm2uuid", httpRequest.getHeaders().getFirst("sm2uuid"));
                            }
                        }
                        byte[] uppedContent = jsonObject.toString().getBytes(Charset.forName("UTF-8"));
                        return bufferFactory.wrap(uppedContent);
                    }));
                }
                return super.writeWith(body);
            }
        };
        return chain.filter(exchange.mutate().response(responseDecorator).build());
    }

    @Override
    public int getOrder() {
        return -2;
    }

    private boolean isJsonStr(String data) {
        if (JSONUtil.isJson(data) || JSONUtil.isJsonArray(data)) {
            return true;
        }
        return false;
    }

    private String encryptAesKey(ServerHttpRequest request, String aesKey){
        HttpHeaders headers = request.getHeaders();
        String publicKey = headers.getFirst("publicKey");
        // 加密生成的数据没有04前缀
        return Sm2.doEncrypt(aesKey, publicKey);
    }

}