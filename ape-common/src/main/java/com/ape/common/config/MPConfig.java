package com.ape.common.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.injector.methods.LogicDeleteByIdWithFill;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/5/13
 */
@Configuration
public class MPConfig {

    /**
     * 逻辑删除
     * 1.配置插件
     * 2.在实体属性上面添加注解 @TableLogic
     * 3.编写逻辑删除的方法
     */
    @Bean
    public LogicDeleteByIdWithFill sqlInjector(){
        return new LogicDeleteByIdWithFill();
    }

    /**
     * 分页功能
     * 1.配置分页插件
     * 2.编写分页查询接口方法
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(){
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

}
