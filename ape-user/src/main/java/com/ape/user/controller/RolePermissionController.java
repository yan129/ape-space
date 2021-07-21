package com.ape.user.controller;


import com.ape.user.service.RolePermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 角色权限关联表 前端控制器
 * </p>
 *
 * @author Yan
 * @since 2021-07-19
 */
@Slf4j
@RestController
@RequestMapping("/role-permission")
public class RolePermissionController {

    @Autowired
    private RolePermissionService rolePermissionService;

}
