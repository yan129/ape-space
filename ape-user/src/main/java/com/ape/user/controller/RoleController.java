package com.ape.user.controller;


import com.ape.common.model.ResultVO;
import com.ape.user.model.RoleDO;
import com.ape.user.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author Yan
 * @since 2021-06-03
 */
@Api(value = "角色控制器", description = "角色控制器")
@Slf4j
@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @ApiOperation(value = "添加角色", notes = "添加角色")
    @PostMapping("/save")
    public ResultVO<String> saveRole(@RequestBody @Valid RoleDO roleDO){
        roleService.save(roleDO);
        log.info("角色{}添加成功！", roleDO.getRoleName());
        return ResultVO.OK("添加成功");
    }

}
