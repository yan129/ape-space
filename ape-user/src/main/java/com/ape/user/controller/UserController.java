package com.ape.user.controller;


import cn.hutool.core.date.DateUtil;
import com.ape.common.annotation.ApiIdempotent;
import com.ape.common.model.ResultVO;
import com.ape.user.service.UserService;
import com.ape.user.vo.LoginVO;
import com.ape.user.vo.RegisterVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author Yan
 * @since 2021-06-02
 */
@Api(value = "用户控制器", description = "用户控制器")
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/bb")
    @PreAuthorize("hasAuthority('ROLE_NORMAL')")
    public ResultVO<String> aa(){
        return ResultVO.OK("aabb");
    }

    @ApiOperation(value = "用户注册", notes = "用户注册")
    @PostMapping("/register")
    @ApiIdempotent
    public ResultVO<Void> register(@RequestBody @Valid LoginVO loginVO){
        userService.register(loginVO);
        log.info("用户{}于{}注册成功！", loginVO.getUsername(), DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        return ResultVO.OK();
    }

    @ApiOperation(value = "免密注册", notes = "免密注册")
    @PostMapping("/noSecretRegister")
    @ApiIdempotent
    public ResultVO<Void> noSecretRegister(@RequestBody @Valid RegisterVO registerVO){
        userService.noSecretRegister(registerVO);
        log.info("用户{}于{}注册成功！", registerVO.getUsername(), DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        return ResultVO.OK();
    }

}
