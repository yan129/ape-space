package com.ape.user.controller;


import com.ape.common.model.ResultVO;
import com.ape.user.model.RolePermissionDO;
import com.ape.user.service.RolePermissionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <p>
 * 角色权限关联表 前端控制器
 * </p>
 *
 * @author Yan
 * @since 2021-07-19
 */
@Api(value = "角色权限控制器", description = "角色权限控制器")
@Slf4j
@RestController
@RequestMapping("/role-permission")
public class RolePermissionController {

    @Autowired
    private RolePermissionService rolePermissionService;

    @ApiOperation(value = "分配角色权限", notes = "分配角色权限")
    @PostMapping("/create")
    public ResultVO<String> assignRolePermission(@RequestBody @Valid RolePermissionDO rolePermissionDO){
        rolePermissionService.assignRolePermission(rolePermissionDO);
        return ResultVO.OK("权限分配成功");
    }

    @ApiOperation(value = "删除角色权限", notes = "删除角色权限")
    @DeleteMapping("/delete")
    public ResultVO<String> deleteRolePermission(@ApiParam("关联ID") @RequestParam String id){
        boolean result = rolePermissionService.removeRolePermission(id);
        log.info("删除角色权限，关联ID：[{}]", id);
        return result ? ResultVO.OK("删除角色权限成功") : ResultVO.ERROR("删除角色权限失败");
    }

}
