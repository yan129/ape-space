package com.ape.user.controller;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.ape.common.annotation.ApiIdempotent;
import com.ape.common.model.ResultVO;
import com.ape.common.utils.CaptchaUtil;
import com.ape.user.service.UserService;
import com.ape.user.vo.LoginVO;
import com.ape.user.vo.RegisterVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

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
//    @PreAuthorize("hasAuthority('ROLE_NORMAL')")
    public ResultVO<String> aa(){
        return ResultVO.OK("aabb");
    }

    @ApiOperation(value = "用户注册", notes = "用户注册")
    @PostMapping("/register")
    @ApiIdempotent
    public ResultVO<OAuth2AccessToken> register(@RequestBody @Valid LoginVO loginVO){
        OAuth2AccessToken oAuth2AccessToken = userService.register(loginVO);
        log.info("用户{}于{}注册成功！", loginVO.getUsername(), DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        return ResultVO.OK(oAuth2AccessToken);
    }

    @ApiOperation(value = "免密注册", notes = "免密注册")

    @PostMapping("/noSecretRegister")
    @ApiIdempotent
    public ResultVO<OAuth2AccessToken> noSecretRegister(@RequestBody @Valid RegisterVO registerVO){
        OAuth2AccessToken oAuth2AccessToken = userService.noSecretRegister(registerVO);
        log.info("用户{}于{}注册成功！", registerVO.getUsername(), DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        return ResultVO.OK(oAuth2AccessToken);
    }

    @ApiOperation(value = "获取图形验证码", notes = "获取图形验证码")
    @GetMapping("/captchaImg")
    public ResultVO<Map<String, Object>> getCaptchaImg(HttpServletResponse response) throws IOException {
//        response.setDateHeader("Expires",0);
//        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
//        // 设置响应头信息，告诉浏览器不要缓存此内容
//        response.setHeader("Pragma", "No-cache");
//        response.setContentType("image/jpeg");
        Map<String, Object> captchaImg = userService.getCaptchaImg();
        return ResultVO.OK(captchaImg);
    }

}
