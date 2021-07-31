package com.ape.user.service;

import cn.hutool.core.collection.CollUtil;
import com.ape.user.constant.AuthConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 资源与角色匹配关系管理业务类
 * Created by macro on 2020/6/19.
 */
@Service
public class ResourceServiceImpl {

    private Map<String, List<String>> resourceRolesMap;

    @Autowired
    private RedisTemplate redisTemplate;

    @PostConstruct
    public void initData() {
        resourceRolesMap = new TreeMap<>();
        resourceRolesMap.put("/ape/hello", CollUtil.toList("ROLE_NORMAL"));
        resourceRolesMap.put("/ape/test", CollUtil.toList("ROLE_TEST"));
        resourceRolesMap.put("/ape/test22", CollUtil.toList("ROLE_TEST"));
        resourceRolesMap.put("/ape/user", CollUtil.toList("ROLE_NORMAL", "ROLE_TEST"));
//        redisTemplate.opsForHash().putAll(AuthConstant.RESOURCE_ROLES_KEY, resourceRolesMap);
    }
}
