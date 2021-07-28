package com.ape.user.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.util.AntPathMatcher;

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
    @Autowired
    private CustomFilterInvocationSecurityMetadataSource metadataSource;
    @Autowired
    private CustomUrlDecisionManager decisionManager;

    @Bean
    public AntPathMatcher antPathMatcher(){
        return new AntPathMatcher();
    }

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
//                .antMatchers(PERMIT_ALL_REQUEST)
//                .permitAll()
//                .anyRequest()
//                .authenticated()
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                        o.setAccessDecisionManager(decisionManager);
                        o.setSecurityMetadataSource(metadataSource);
                        return o;
                    }
                })
                .and()
                .formLogin()
                .successHandler(successHandler)
                .failureHandler(loginFailureHandler);
    }
}
