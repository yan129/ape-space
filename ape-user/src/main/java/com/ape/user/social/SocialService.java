package com.ape.user.social;

import com.ape.user.model.RoleDO;
import com.ape.user.service.UserService;
import org.hibernate.validator.internal.util.CollectionHelper;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/8/8
 */
public interface SocialService {

    /**
     * 构建默认的角色列表
     * @return
     */
    static List<RoleDO> buildRoleList(){
        List<RoleDO> roleDOList = CollectionHelper.newArrayList();
        RoleDO roleDO = new RoleDO().setRoleName(UserService.ACCOUNT_DEFAULT_ROLE);
        roleDOList.add(roleDO);
        return roleDOList;
    }

    /**
     * 构建请求到第三方登录页地址
     * @return
     */
    String buildLoginUrl();

    /**
     * 构建处理回调接口返回的数据，然后生成token
     * @param code
     * @param state
     * @return
     */
    OAuth2AccessToken buildOAuth2AccessToken(String code, String state);
}
