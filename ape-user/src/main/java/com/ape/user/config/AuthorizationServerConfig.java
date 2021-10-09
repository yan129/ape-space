package com.ape.user.config;

import com.ape.user.oauth2.TokenGranterExtend;
import com.ape.user.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import javax.sql.DataSource;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/6/11
 * 认证服务配置
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private CustomOauthException oauthException;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        // 使用client模式支持用户名、密码登录
        security.allowFormAuthenticationForClients()
                // 开启/oauth/token_key验证端口无权限访问
                .tokenKeyAccess("permitAll()")
                // 开启/oauth/check_token验证端口认证权限访问
                .checkTokenAccess("isAuthenticated()");
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//        clients.inMemory()
//                .withClient("ape")
//                .secret(passwordEncoder.encode("ape"))
//                .authorizedGrantTypes("authorization_code", "password", "refresh_token")
//                .scopes("all")
//                .autoApprove(true);
        // 读取持久化到数据库的客户端授权凭证
        clients.jdbc(dataSource).passwordEncoder(passwordEncoder);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // 获取grant_type类型组合
        TokenGranter tokenGranter = TokenGranterExtend.getTokenGranter(authenticationManager, endpoints, stringRedisTemplate);
        DefaultTokenServices tokenServices = setTokenServices(endpoints);
        endpoints.authenticationManager(authenticationManager)
                // 配置加载用户信息的服务
                .userDetailsService(userService)
//                .tokenStore(jwtTokenStore())
//                .accessTokenConverter(jwtAccessTokenConverter())
//                .tokenEnhancer(tokenEnhancerChain())
                .tokenServices(tokenServices)
                // 设置grant_type类型集合
                .tokenGranter(tokenGranter)
                .exceptionTranslator(oauthException);
    }

    private DefaultTokenServices setTokenServices(AuthorizationServerEndpointsConfigurer endpoints) {
        DefaultTokenServices tokenServices = (DefaultTokenServices) endpoints.getDefaultAuthorizationServerTokenServices();
        tokenServices.setTokenStore(jwtTokenStore());
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setClientDetailsService(endpoints.getClientDetailsService());
        tokenServices.setTokenEnhancer(tokenEnhancerChain());
        // 设置access_token有效期，这里持久化到数据库了
//        tokenServices.setAccessTokenValiditySeconds((int) TimeUnit.SECONDS.toSeconds(500));
        // 设置refresh_token有效期
//        tokenServices.setRefreshTokenValiditySeconds(((int) TimeUnit.SECONDS.toSeconds(1000)));
        return tokenServices;
    }

    @Bean
    public TokenStore jwtTokenStore(){
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter(){
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setKeyPair(keyPair());
        return converter;
    }

    @Bean
    public KeyPair keyPair(){
        KeyStoreKeyFactory factory = new KeyStoreKeyFactory(new ClassPathResource("jwt.jks"), "1291248490".toCharArray());
        return factory.getKeyPair("ape-space", "1291248490".toCharArray());
    }

    @Bean
    public TokenEnhancerChain tokenEnhancerChain(){
        TokenEnhancerChain enhancerChain = new TokenEnhancerChain();

        List<TokenEnhancer> delegates = new ArrayList<>();
        // 必须 new 对象，使用component和autowired注入使用会报错
        delegates.add(new CustomTokenEnhancer());
        delegates.add(jwtAccessTokenConverter());
        // 配置JWT的内容增强
        enhancerChain.setTokenEnhancers(delegates);
        return enhancerChain;
    }
}
