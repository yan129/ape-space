package com.ape.user.social.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/8/6
 */
@Data
@Component
@ConfigurationProperties(prefix = "oauth2.gitee")
public class GiteeProperties {

    private String clientId;
    private String clientSecret;
    private String authorizeUrl;
    private String redirectUrl;
}
