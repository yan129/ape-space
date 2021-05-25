package com.ape.common.config;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/5/25
 * 该配置用于解决mockMvc测试时带上servlet.context-path问题
 */
@Configuration
public class ServletConfig implements ServletContextAware {
    @Setter
    @Getter
    private ServletContext servletContext;

    @Override
    public void setServletContext(@NonNull ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}
