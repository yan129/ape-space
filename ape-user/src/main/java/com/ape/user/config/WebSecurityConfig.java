package com.ape.user.config;

import com.ape.user.oauth2.SmsCodeAuthenticationProvider;
import com.ape.user.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/6/3
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private SmsCodeAuthenticationProvider smsCodeAuthenticationProvider;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(12);
    }

    private static final String[] PERMIT_ALL_REQUEST = {
            "/rsa/publicKey"
    };

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userService);
        provider.setPasswordEncoder(passwordEncoder());
        provider.setHideUserNotFoundExceptions(false);
        return provider;
    }

    /**
     * 忽略掉认证路径
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(PERMIT_ALL_REQUEST);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        smsCodeAuthenticationProvider.setUserService(userService);
        smsCodeAuthenticationProvider.setStringRedisTemplate(stringRedisTemplate);
        // 使用这句就不要使用下面这句
        // auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
        auth.authenticationProvider(authenticationProvider())
                // 加入自定义的短信验证配置
                .authenticationProvider(smsCodeAuthenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.cors()
//                .and()
//                .csrf()
//                .disable();
//                .authorizeRequests()
//                .antMatchers("/oauth/**", "/user/bb").authenticated();
//                .and()
//                .formLogin()
//                .permitAll()
//                .loginProcessingUrl("/user/login")
//                .usernameParameter("username")
//                .passwordParameter("password")
//                .successHandler(successHandler)
//                .failureHandler(failureHandler)
//                .and()
//                .logout()
//                .permitAll()
//                .logoutUrl("/user/logout")
//                .logoutSuccessHandler(logoutSuccessHandler);
    }

                // 放行请求CSRF路径，用于生成第一个 csrf-token
//                .ignoringAntMatchers(PERMIT_ALL_REQUEST)
                // 将 csrf-token 设置回cookie返回
//                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());

}
