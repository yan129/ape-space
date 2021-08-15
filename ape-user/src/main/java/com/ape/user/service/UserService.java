package com.ape.user.service;

import com.ape.user.model.UserDO;
import com.ape.user.vo.LoginVO;
import com.ape.user.vo.RegisterVO;
import com.baomidou.mybatisplus.extension.service.IService;
import me.zhyd.oauth.model.AuthUser;

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

    /**
     * 用户注册
     * @param loginVO
     */
    void register(LoginVO loginVO);

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
    void noSecretRegister(RegisterVO registerVO);
}
