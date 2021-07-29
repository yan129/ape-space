package com.ape.gateway;

import com.ape.gateway.ignore.CustomScanFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

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
}
