package com.ape.user.controller;


import com.ape.common.model.ResultVO;
import com.ape.user.model.PermissionDO;
import com.ape.user.service.PermissionService;
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
 * 权限表 前端控制器
 * </p>
 *
 * @author Yan
 * @since 2021-07-20
 */
@Api(value = "请求路径权限控制器", description = "请求路径权限控制器")
@Slf4j
@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @ApiOperation(value = "添加权限", notes = "添加权限")
    @PostMapping("/create")
    public ResultVO<String> createPermission(@RequestBody @Valid PermissionDO permissionDO){
        permissionService.save(permissionDO);
        log.info("权限添加成功，name：[{}] -- url：[{}]", permissionDO.getName(), permissionDO.getUrl());
        return ResultVO.OK("添加成功");
    }

    @ApiOperation(value = "删除权限", notes = "删除权限")
    @DeleteMapping("/delete")
    public ResultVO<String> deletePermission(@ApiParam("权限ID") @RequestParam String id){
        PermissionDO permissionDO = permissionService.getById(id);
        boolean result = permissionService.removeById(id);

        if (result){
            log.info("权限删除成功，name：[{}] -- url：[{}]", permissionDO.getName(), permissionDO.getUrl());
        }
        return result ? ResultVO.OK("删除成功") : ResultVO.ERROR("删除失败");
    }

    @ApiOperation(value = "更新权限", notes = "更新权限")
    @PostMapping("/update")
    public ResultVO<String> updatePermission(@RequestBody @Valid PermissionDO permissionDO){
        boolean result = permissionService.updateById(permissionDO);
        return result ? ResultVO.OK("更新成功") : ResultVO.ERROR("更新失败");
    }

    @ApiOperation(value = "查询所有权限", notes = "查询所有权限")
    @GetMapping("/selectAll")
    public ResultVO<List<PermissionDO>> selectAll(){
        List<PermissionDO> permissionDOList = permissionService.list();
        return ResultVO.OK(permissionDOList);
    }

}
