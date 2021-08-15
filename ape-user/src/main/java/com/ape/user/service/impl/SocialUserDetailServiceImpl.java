package com.ape.user.service.impl;

import com.ape.common.utils.StringUtils;
import com.ape.user.bo.UserBO;
import com.ape.user.mapper.RoleMapper;
import com.ape.user.mapstruct.UserDOMapper;
import com.ape.user.model.RoleDO;
import com.ape.user.model.SocialUserDetailDO;
import com.ape.user.mapper.SocialUserDetailMapper;
import com.ape.user.model.UserDO;
import com.ape.user.service.SocialUserDetailService;
import com.ape.user.service.UserService;
import com.ape.user.social.SocialService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import org.hibernate.validator.internal.util.CollectionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 第三方登录用户表 服务实现类
 * </p>
 *
 * @author Yan
 * @since 2021-08-06
 */
@Service
public class SocialUserDetailServiceImpl extends ServiceImpl<SocialUserDetailMapper, SocialUserDetailDO> implements SocialUserDetailService {

    /**
     * 在AuthorizationServerConfig配置类中获取jwtTokenStore、tokenEnhancerChain
     */
    @Autowired()
    private TokenStore jwtTokenStore;
    @Autowired
    private TokenEnhancerChain tokenEnhancerChain;

    @Autowired
    private ClientDetailsService clientDetailsService;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleMapper roleMapper;

    @Override
    public OAuth2AccessToken generateToken(UserBO userBO) {
        Map<String, Object> clientDetail = this.baseMapper.selectOauthClientDetailsByClientId(clientId);

        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        // 设置生成刷新token
        defaultTokenServices.setSupportRefreshToken(true);
        defaultTokenServices.setTokenStore(jwtTokenStore);
        // access_token过期时间
        defaultTokenServices.setAccessTokenValiditySeconds((int) TimeUnit.SECONDS.toSeconds((int)clientDetail.get("access_token_validity")));
        // refresh_token过期时间
        defaultTokenServices.setRefreshTokenValiditySeconds((int) TimeUnit.SECONDS.toSeconds((int)clientDetail.get("refresh_token_validity")));
        // 增强token信息
        defaultTokenServices.setTokenEnhancer(tokenEnhancerChain);

        Map<String, String> parameters = CollectionHelper.newHashMap();
        parameters.put("client_id", clientId);
        parameters.put("client_secret", (String) clientDetail.get("client_secret"));
        parameters.put("grant_type", "password");
        ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);
        TokenRequest tokenRequest = new TokenRequest(parameters, clientId, Collections.singleton((String) clientDetail.get("scope")), "password");
        OAuth2Request oAuth2Request = tokenRequest.createOAuth2Request(clientDetails);

        // 创建UsernamePasswordAuthenticationToken
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userBO, "[PROTECTED]", userBO.getAuthorities());

        // 将OAuth2Request 和 Authorization 两个对象组合起来形成一个 OAuth2Authorization 对象
        OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, authenticationToken);
        // OAuth2Authentication对象会传递到AuthorizationServerTokenServices的实现类DefaultTokenServices中，最终会生成一个OAuth2AccessToken
        return defaultTokenServices.createAccessToken(oAuth2Authentication);
    }

    @Override
    public SocialUserDetailDO saveSocialUserDetail(AuthUser authUser) {
        SocialUserDetailDO socialUserDetail = new SocialUserDetailDO();
        socialUserDetail.setUuid(authUser.getUuid());
        socialUserDetail.setSource(authUser.getSource());
        socialUserDetail.setTokenType(authUser.getToken().getTokenType());
        socialUserDetail.setAccessToken(authUser.getToken().getAccessToken());
        socialUserDetail.setExpireIn(authUser.getToken().getExpireIn());
        socialUserDetail.setRefreshToken(authUser.getToken().getRefreshToken());
        socialUserDetail.setRefreshTokenExpireIn(authUser.getToken().getRefreshTokenExpireIn());
        this.save(socialUserDetail);
        return socialUserDetail;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OAuth2AccessToken generateOAuth2AccessToken(AuthResponse authResponse) {
        // 回调成功，首先判断该第三方账号是否注册过，没有则进行注册
        if (authResponse.ok()){
            AuthUser authUser = (AuthUser) authResponse.getData();
            QueryWrapper<UserDO> wrapper = new QueryWrapper<>();
            wrapper.eq("username", authUser.getUuid());
            UserDO detailDO = userService.getOne(wrapper);
            // 第一次登录进行注册
            if (StringUtils.isEmpty(detailDO)){
                this.saveSocialUserDetail(authUser);
                UserDO userDO = userService.registerSocialUser(authUser);
                UserBO userBO = UserDOMapper.INSTANCE.doToBO(userDO);
                userBO.setRoles(SocialService.buildRoleList());

                // 生成token
                return this.generateToken(userBO);
            }else {
                // 已注册
                List<RoleDO> roles = roleMapper.searchAllRoleByUid(detailDO.getId());
                UserBO userBO = UserDOMapper.INSTANCE.doToBO(detailDO);
                userBO.setRoles(roles);
                return this.generateToken(userBO);
            }
        }
        return null;
    }
}
