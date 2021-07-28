package com.ape.user.controller;


import com.ape.common.model.ResultVO;
import com.ape.user.model.RoleDO;
import com.ape.user.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
    @PostMapping("/create")
    public ResultVO<String> saveRole(@RequestBody @Valid RoleDO roleDO){
        roleService.save(roleDO);
        log.info("角色 {} 添加成功！", roleDO.getRoleName());
        return ResultVO.OK("添加成功");
    }

    @ApiOperation(value = "删除角色", notes = "删除角色")
    @DeleteMapping("/delete")
    public ResultVO<String> deleteRole(@ApiParam("角色ID") @RequestParam String id){
        RoleDO roleDO = roleService.getById(id);
        boolean result = roleService.removeById(id);

        if (result){
            log.info("角色 {} 删除成功", roleDO.getRoleName());
        }
        return result ? ResultVO.OK("删除成功") : ResultVO.ERROR("删除失败");
    }

    @ApiOperation(value = "更新角色", notes = "更新角色")
    @PostMapping("/update")
    public ResultVO<String> updateRole(@RequestBody @Valid RoleDO roleDO){
        boolean result = roleService.updateById(roleDO);
        return result ? ResultVO.OK("更新成功") : ResultVO.ERROR("更新失败");
    }

    @ApiOperation(value = "查询所有角色", notes = "查询所有角色")
    @GetMapping("/selectAll")
    public ResultVO<List<RoleDO>> selectAll(){
        List<RoleDO> roleDOList = roleService.list();
        return ResultVO.OK(roleDOList);
    }

}
