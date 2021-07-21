package com.ape.user.init;

import com.ape.common.utils.StringUtils;
import com.ape.user.service.RolePermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/7/21
 *
 * 项目启动初始化数据库权限数据
 */
@Component
@Order(100)
@Slf4j
public class InitializationDataApplicationRunner implements ApplicationRunner {

    /**
     * 对应网关取出redis数据的key
     */
    private static final String RESOURCE_ROLES_KEY = "AUTH:RESOURCE_ROLES_MAP";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private RolePermissionService rolePermissionService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<Map<String, Object>> rolePermissionDataList = rolePermissionService.selectAllLinkedData();

        if (CollectionUtils.isEmpty(rolePermissionDataList)){
            log.error("权限数据初始化失败，请检查数据表是否配置权限数据。");
            return;
        }

        Map<String, List<String>> resourceRolesMap = new TreeMap<>();
        Map<String, List<Map<String, Object>>> groupMapByPermissionUrl = rolePermissionDataList.stream().filter(Objects::nonNull).collect(Collectors.groupingBy(data -> (String) data.get("permissionUrl")));

        for (Map.Entry<String, List<Map<String, Object>>> entry : groupMapByPermissionUrl.entrySet()) {
            List<String> roleNameList = entry.getValue().stream().filter(data -> StringUtils.isNotEmpty(data.get("roleName"))).map(data -> (String) data.get("roleName")).collect(Collectors.toList());
            resourceRolesMap.put(entry.getKey(), roleNameList);
        }
        // 将角色权限对应数据存入redis
        redisTemplate.opsForHash().putAll(RESOURCE_ROLES_KEY, resourceRolesMap);
        log.info("角色权限数据：{}", resourceRolesMap);
    }
}
