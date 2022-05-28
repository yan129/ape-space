package com.ape.user.config;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2022/5/27
 */
@Configuration
public class BeanConfig {

    @Bean
    public Snowflake snowflake(){
        return IdUtil.getSnowflake(1, 1);
    }
}
