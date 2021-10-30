package com.ape.user.config;

import com.ape.user.bo.UserBO;
import com.ape.user.model.RoleDO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/10/25
 *
 * 自定义CustomerAccessTokenConverter 这个类的作用主要用于AccessToken的转换,
 * 默认使用DefaultAccessTokenConverter 这个装换器
 * DefaultAccessTokenConverter有个UserAuthenticationConverter，这个转换器作用是把用户的信息放入token中，
 * 默认只是放入username
 */
public class CustomAccessTokenConverter extends DefaultAccessTokenConverter {

    public CustomAccessTokenConverter() {
        super.setUserTokenConverter(new CustomUserAuthenticationConverter());
    }

    private class CustomUserAuthenticationConverter extends DefaultUserAuthenticationConverter{

        @Override
        public Map<String, ?> convertUserAuthentication(Authentication authentication) {

            UserBO userBO = (UserBO) authentication.getPrincipal();

            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("id", userBO.getId());
            userInfo.put("nickname", userBO.getNickname());
            userInfo.put("avatar", userBO.getAvatar());
            userInfo.put("remark", userBO.getRemark());
            userInfo.put("profile", userBO.getProfile());
            userInfo.put("gender", userBO.getGender());
            userInfo.put("lastLoginTime", userBO.getLastLoginTime());
            userInfo.put("roles", this.getRoleToList(userBO));

            Map<String, Object> response = new LinkedHashMap<>();
            response.put("user_name", userBO.getNickname());
            response.put("userInfo", userInfo);
            if (authentication.getAuthorities() != null && !authentication.getAuthorities().isEmpty()){
                response.put("authorities", AuthorityUtils.authorityListToSet(authentication.getAuthorities()));
            }
            return response;
        }

        private List<String> getRoleToList(UserBO userBO) {
            if (CollectionUtils.isEmpty(userBO.getRoles())){
                return new ArrayList<>();
            }
            return userBO.getRoles().stream().map(RoleDO::getRoleName).collect(Collectors.toList());
        }

    }
}
