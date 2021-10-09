package com.ape.user.oauth2;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/10/8
 *
 * TokenGranter扩展，将自定义的grant_type类型添加到oauth2中
 * 使用方法:
 * 在configure(AuthorizationServerEndpointsConfigurer endpoints)中:
 *      获取自定义tokenGranter
 *      TokenGranter tokenGranter = TokenGranterExt.getTokenGranter(authenticationManager, endpoints, baseRedis, userClient, socialProperties);
 *      endpoints.tokenGranter(tokenGranter);
 */
public class TokenGranterExtend {

    public static TokenGranter getTokenGranter(final AuthenticationManager authenticationManager,
                                               final AuthorizationServerEndpointsConfigurer endpointsConfigurer,
                                               StringRedisTemplate stringRedisTemplate){
        // 默认tokenGranter集合 security 自带的
        List<TokenGranter> granterList = new ArrayList<>(Collections.singletonList(endpointsConfigurer.getTokenGranter()));
        // 添加图形验证码
        granterList.add(new CaptchaTokenGranter(authenticationManager, stringRedisTemplate, endpointsConfigurer.getTokenServices(), endpointsConfigurer.getClientDetailsService(), endpointsConfigurer.getOAuth2RequestFactory()));
        return new CompositeTokenGranter(granterList);
    }
}
