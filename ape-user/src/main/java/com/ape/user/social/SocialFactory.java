package com.ape.user.social;

import org.springframework.security.oauth2.common.OAuth2AccessToken;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/8/8
 */
public abstract class SocialFactory {

    public static final String GITHUB_SOURCE = "GitHub";
    public static final String GITEE_SOURCE = "Gitee";

    /**
     * 获取第三方登录页地址
     * @param source
     * @return
     */
    public abstract String obtainUrl(String source);

    /**
     * 处理第三方登录回调接口
     * @param code
     * @param state
     * @param source
     * @return
     */
    public abstract OAuth2AccessToken obtainOAuth2AccessToken(String code, String state, String source);

    /**
     * 获取 SocialService
     * @param source
     * @return
     */
    public abstract SocialService getSocialService(String source);
}
