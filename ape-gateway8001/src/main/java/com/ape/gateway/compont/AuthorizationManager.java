package com.ape.gateway.compont;

import com.ape.gateway.constant.AuthConstant;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.util.CollectionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Mono;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/7/4
 *
 * 鉴权管理器，用于判断是否有资源的访问权限
 */
@Slf4j
@Component
public class AuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private AntPathMatcher antPathMatcher;

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> mono, AuthorizationContext authorizationContext) {
        ServerHttpRequest request = authorizationContext.getExchange().getRequest();
        String requestPath = request.getURI().getPath();

        // token为空拒绝访问
//        if (StringUtils.isBlank(request.getHeaders().getFirst(AuthConstant.JWT_TOKEN_HEADER))){
//            return Mono.just(new AuthorizationDecision(false));
//        }

        Map<String, List<String>> resourceRoleMap = redisTemplate.opsForHash().entries(AuthConstant.RESOURCE_ROLES_KEY);
        // 请求路径匹配到的资源需要的角色权限集合authorities
        List<String> authorities = CollectionHelper.newArrayList();
        Iterator<String> iterator = resourceRoleMap.keySet().iterator();
        while (iterator.hasNext()){
            String pattern = iterator.next();
            if (antPathMatcher.match(pattern, requestPath)){
                authorities.addAll(resourceRoleMap.get(pattern));
                break;
            }
        }

        // 请求路径没有匹配上相应角色直接放行通过
        if (CollectionUtils.isEmpty(authorities)){
            return Mono.just(new AuthorizationDecision(true));
        }

        //认证通过且角色匹配的用户可访问当前路径
        return mono.filter(Authentication::isAuthenticated)
                .flatMapIterable(Authentication::getAuthorities)
                .map(GrantedAuthority::getAuthority)
//                .any(authorities::contains)
                .any(role -> {
                    log.info("请求路径：{}", requestPath);
                    log.info("用户角色：{}", role);
                    log.info("请求资源路径所需权限authorities：{}", authorities);
                    return authorities.contains(role);
                })
                .map(AuthorizationDecision::new)
                // 请求路径没有设置相应角色直接放行通过
                .defaultIfEmpty(new AuthorizationDecision(true));
    }
}
