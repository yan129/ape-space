package com.ape.common.config.ignore.mvc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/5/13
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket webApiConfig(){
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Ape Space")
                .apiInfo(webApiInfo())
                .select()
                //指定提供接口所在的基包
                .apis(RequestHandlerSelectors.basePackage("com.ape"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo webApiInfo() {
        return new ApiInfoBuilder()
                .title("猿空间接口文档")
                .description("Ape Space For Spring Boot Application")
                .version("1.0.0")
                .contact(new Contact("蓝胖子", "", "x129124@outlook.com"))
                .termsOfServiceUrl("localhost:9898")
                .build();
    }

}
