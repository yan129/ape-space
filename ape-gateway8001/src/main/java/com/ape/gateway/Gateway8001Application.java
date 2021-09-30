package com.ape.gateway;

import com.ape.gateway.ignore.CustomScanFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/5/21
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableEurekaClient
@ComponentScan(basePackages = "com.ape", excludeFilters = @ComponentScan.Filter(
        type = FilterType.CUSTOM,
        classes = {CustomScanFilter.class}
        ))
public class Gateway8001Application {
    public static void main(String[] args) {
        SpringApplication.run(Gateway8001Application.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setOutputStreaming(false);
        // 解决401报错时，报java.net.HttpRetryException: cannot retry due to server authentication, in streaming mode
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        restTemplate.setErrorHandler(new RtErrorHandler());

        return restTemplate;
    }

    public class RtErrorHandler extends DefaultResponseErrorHandler {

        @Override
        public boolean hasError(ClientHttpResponse response) throws IOException {
            return super.hasError(response);
        }

        @Override
        public void handleError(ClientHttpResponse response) throws IOException {
            HttpStatus statusCode = HttpStatus.resolve(response.getRawStatusCode());
            List<HttpStatus> whiteList = new ArrayList<>(); // 白名单
            whiteList.add(HttpStatus.UNAUTHORIZED);

            if (!whiteList.contains(statusCode)) { // 非白名单则处理
                super.handleError(response);
            }
        }

    }
}
