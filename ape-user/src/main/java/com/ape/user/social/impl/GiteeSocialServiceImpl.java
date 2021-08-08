package com.ape.user.social.impl;

import com.ape.user.service.SocialUserDetailService;
import com.ape.user.social.SocialService;
import com.ape.user.social.properties.GiteeProperties;
import com.xkcoding.http.config.HttpConfig;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.request.AuthGiteeRequest;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/8/8
 */
@Slf4j
@Service
public class GiteeSocialServiceImpl implements SocialService {

    @Autowired
    private GiteeProperties giteeProperties;
    @Autowired
    private SocialUserDetailService socialUserDetailService;

    @Override
    public String buildLoginUrl() {
        return buildGiteeAuthRequest().authorize(AuthStateUtils.createState());
    }

    @Override
    public OAuth2AccessToken buildOAuth2AccessToken(String code, String state) {
        AuthRequest authRequest = buildGiteeAuthRequest();
        AuthResponse authResponse = authRequest.login(AuthCallback.builder()
                .code(code)
                .state(state)
                .build());
        return socialUserDetailService.generateOAuth2AccessToken(authResponse);
    }

    private AuthRequest buildGiteeAuthRequest() {
        return new AuthGiteeRequest(AuthConfig.builder()
                .clientId(giteeProperties.getClientId())
                .clientSecret(giteeProperties.getClientSecret())
                .redirectUri(giteeProperties.getRedirectUrl())
                .httpConfig(HttpConfig.builder()
                        .timeout(15*1000)
                        .build())
                .build());
    }

}
