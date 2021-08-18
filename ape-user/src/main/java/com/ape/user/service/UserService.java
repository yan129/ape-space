package com.ape.user.service;

import com.ape.user.model.UserDO;
import com.ape.user.vo.LoginVO;
import com.ape.user.vo.RegisterVO;
import com.baomidou.mybatisplus.extension.service.IService;
import me.zhyd.oauth.model.AuthUser;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author Yan
 * @since 2021-06-02
 */
public interface UserService extends IService<UserDO> {

    String ACCOUNT_DEFAULT_ROLE = "ROLE_NORMAL";
    String ACCOUNT_DEFAULT_PASSWORD = "1291248490";

    String PHONE_PREFIX = "SMS:";

    /**
     * 用户注册
     * @param loginVO
     */
    OAuth2AccessToken register(LoginVO loginVO);

    /**
     * 注册第三方账号登录信息到用户表
     * @param authUser
     * @return
     */
    UserDO registerSocialUser(AuthUser authUser);

    /**
     * 免密注册
     * @param registerVO
     */
    OAuth2AccessToken noSecretRegister(RegisterVO registerVO);
}
