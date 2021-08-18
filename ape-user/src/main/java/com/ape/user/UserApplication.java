package com.ape.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/6/2
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.ape")
@EnableEurekaClient
@EnableFeignClients
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }
}
