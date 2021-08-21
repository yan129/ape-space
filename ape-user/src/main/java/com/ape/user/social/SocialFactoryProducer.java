package com.ape.user.social;

import com.ape.common.utils.StringUtils;
import com.ape.user.social.impl.GitHubSocialServiceImpl;
import com.ape.user.social.impl.GiteeSocialServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/8/8
 */
@Service
public class SocialFactoryProducer extends SocialFactory {

    @Autowired
    private GitHubSocialServiceImpl gitHubSocialService;
    @Autowired
    private GiteeSocialServiceImpl giteeSocialService;

    @Override
    public String obtainUrl(String source) {
        if (StringUtils.equalsIgnoreCase(SocialFactory.GITHUB_SOURCE, source)){
            return gitHubSocialService.buildLoginUrl();
        }
        if (StringUtils.equalsIgnoreCase(SocialFactory.GITEE_SOURCE, source)){
            return giteeSocialService.buildLoginUrl();
        }
        return null;
    }

    @Override
    public OAuth2AccessToken obtainOAuth2AccessToken(String code, String state, String source) {
        if (StringUtils.equalsIgnoreCase(SocialFactory.GITHUB_SOURCE, source)){
            return gitHubSocialService.buildOAuth2AccessToken(code, state);
        }
        if (StringUtils.equalsIgnoreCase(SocialFactory.GITEE_SOURCE, source)){
            return giteeSocialService.buildOAuth2AccessToken(code, state);
        }
        return null;
    }

    @Override
    public SocialService getSocialService(String source) {
        if (StringUtils.equalsIgnoreCase(SocialFactory.GITHUB_SOURCE, source)){
            return gitHubSocialService;
        }
        if (StringUtils.equalsIgnoreCase(SocialFactory.GITEE_SOURCE, source)){
            return giteeSocialService;
        }
        return null;
    }
}
