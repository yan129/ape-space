package com.ape.user.controller;


import com.ape.common.model.ResultVO;
import com.ape.user.model.UserRoleDO;
import com.ape.user.service.UserRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <p>
 * 用户角色关联表 前端控制器
 * </p>
 *
 * @author Yan
 * @since 2021-06-03
 */
@Api(value = "用户角色关联控制器", description = "用户角色关联控制器")
@Slf4j
@RestController
@RequestMapping("/user-role")
public class UserRoleController {

    @Autowired
    private UserRoleService userRoleService;

    @ApiOperation(value = "分配用户角色", notes = "添加用户角色")
    @PostMapping("/create")
    public ResultVO<String> assignUserRole(@RequestBody @Valid UserRoleDO userRoleDO){
        userRoleService.save(userRoleDO);
        return ResultVO.OK("角色分配成功");
    }

    @ApiOperation(value = "删除用户角色", notes = "删除用户角色")
    @DeleteMapping("/delete")
    public ResultVO<String> deleteUserRole(@ApiParam("关联ID") @RequestParam String id){
        boolean result = userRoleService.removeById(id);
        log.info("删除用户角色，关联ID：[{}]", id);
        return result ? ResultVO.OK("删除用户角色成功") : ResultVO.ERROR("删除用户角色失败");
    }

}
