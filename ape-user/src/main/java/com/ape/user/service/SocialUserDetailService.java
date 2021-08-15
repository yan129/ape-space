package com.ape.user.service;

import com.ape.user.bo.UserBO;
import com.ape.user.model.SocialUserDetailDO;
import com.baomidou.mybatisplus.extension.service.IService;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

/**
 * <p>
 * 第三方登录用户表 服务类
 * </p>
 *
 * @author Yan
 * @since 2021-08-06
 */
public interface SocialUserDetailService extends IService<SocialUserDetailDO> {

    String clientId = "ape";

    /**
     * 对第三方账号自定义生成token
     * @param userBO
     * @return OAuth2AccessToken
     */
    OAuth2AccessToken generateToken(UserBO userBO);

    /**
     * 构建第三方登录用户信息
     * @param authUser
     * @return
     */
    SocialUserDetailDO saveSocialUserDetail(AuthUser authUser);

    /**
     * 注册用户并生成OAuth2AccessToken
     * @param authResponse
     * @return
     */
    OAuth2AccessToken generateOAuth2AccessToken(AuthResponse authResponse);

}
