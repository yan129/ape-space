package com.ape.user.social.impl;

import com.ape.user.service.SocialUserDetailService;
import com.ape.user.social.SocialService;
import com.ape.user.social.properties.GitHubProperties;
import com.xkcoding.http.config.HttpConfig;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.request.AuthGithubRequest;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/8/8
 */
@Slf4j
@Service
public class GitHubSocialServiceImpl implements SocialService {

    @Autowired
    private GitHubProperties gitHubProperties;
    @Autowired
    private SocialUserDetailService socialUserDetailService;

    @Override
    public String buildLoginUrl() {
        return buildGitHubAuthRequest().authorize(AuthStateUtils.createState());
    }

    @Override
    public OAuth2AccessToken buildOAuth2AccessToken(String code, String state) {
        AuthRequest authRequest = buildGitHubAuthRequest();
        AuthResponse authResponse = authRequest.login(AuthCallback.builder()
                .code(code)
                .state(state)
                .build());
        return socialUserDetailService.generateOAuth2AccessToken(authResponse);
    }

    private AuthRequest buildGitHubAuthRequest() {
        return new AuthGithubRequest(AuthConfig.builder()
                .clientId(gitHubProperties.getClientId())
                .clientSecret(gitHubProperties.getClientSecret())
                .redirectUri(gitHubProperties.getRedirectUrl())
                // 针对国外平台配置代理
                .httpConfig(HttpConfig.builder()
                        // Http 请求超时时间
                        .timeout(30*1000)
                        // host 和 port 请修改为开发环境的参数
                        // host: 本地一般为127.0.0.1，如果部署到服务器，可以配置为公网 IP
                        //port: 需要根据使用的唯皮嗯软件修改，以我本地使用的某款工具为例，查看代理端口
//                            .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 10080)))
                        .build())
                .build());
    }
}
