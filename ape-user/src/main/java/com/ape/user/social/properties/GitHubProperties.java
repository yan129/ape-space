package com.ape.user.social.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/8/5
 */
@Data
@Component
@ConfigurationProperties(prefix = "oauth2.github")
public class GitHubProperties {

    private String clientId;
    private String clientSecret;
    private String authorizeUrl;
    private String redirectUrl;
    private String accessTokenUrl;
    private String userInfoUrl;
}
