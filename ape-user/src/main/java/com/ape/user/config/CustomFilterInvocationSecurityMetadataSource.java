package com.ape.user.config;

import com.ape.user.constant.AuthConstant;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.util.CollectionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 根据用户传来的请求地址，分析出请求需要的角色
 * @author x1291
 */
@Slf4j
@Component
public class CustomFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private AntPathMatcher antPathMatcher;

    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
        String requestPath = ((FilterInvocation) o).getRequestUrl();

        Map<String, List<String>> resourceRoleMap = redisTemplate.opsForHash().entries(AuthConstant.RESOURCE_ROLES_KEY);

        // 请求路径匹配到的资源需要的角色权限集合authorities
        List<String> authorities = CollectionHelper.newArrayList();
        Iterator<String> iterator = resourceRoleMap.keySet().iterator();
        while (iterator.hasNext()){
            String pattern = iterator.next();
            if (antPathMatcher.match(pattern, requestPath)){
                authorities.addAll(resourceRoleMap.get(pattern));
                return SecurityConfig.createList(authorities.toArray(new String[authorities.size()]));
            }
        }

        //没有匹配上的资源，都是登录访问
        //return SecurityConfig.createList("ROLE_LOGIN");
        //如果没有匹配的url直接返回null，也就是没有配置权限的url默认都为白名单，想要换成默认是黑名单只要修改这里即可。
        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
