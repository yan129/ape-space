package com.ape.user.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/6/23
 * ResourceServerConfig 比 WebSecurityConfigurerAdapter的优先级高
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    private CustomLoginSuccessHandler successHandler;
    @Autowired
    private CustomLoginFailureHandler loginFailureHandler;
    @Autowired
    private CustomAuthenticationEntryPoint authenticationEntryPoint;

    private static final String[] PERMIT_ALL_REQUEST = {
            "/rsa/publicKey"
    };

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        // 当认证失败时返回消息
        resources.authenticationEntryPoint(authenticationEntryPoint);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.cors()
                .and()
                .csrf()
                .disable()
                .authorizeRequests()
                .requestMatchers(EndpointRequest.toAnyEndpoint())
                .permitAll()
                .antMatchers(PERMIT_ALL_REQUEST)
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .successHandler(successHandler)
                .failureHandler(loginFailureHandler);
    }
}
