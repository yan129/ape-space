package com.ape.user.init;

import com.ape.user.service.impl.RolePermissionServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

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

    @Autowired
    private RolePermissionServiceImpl rolePermissionServiceImpl;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<Map<String, Object>> rolePermissionDataList = rolePermissionServiceImpl.selectAllLinkedData();

        if (CollectionUtils.isEmpty(rolePermissionDataList)){
            log.error("权限数据初始化失败，请检查数据表是否配置权限数据。");
            return;
        }

        Map<String, List<String>> resourceRolesMap = rolePermissionServiceImpl.buildResourceRoleMap(rolePermissionDataList);
        log.info("角色权限数据初始化成功：{}", resourceRolesMap);
    }


}
