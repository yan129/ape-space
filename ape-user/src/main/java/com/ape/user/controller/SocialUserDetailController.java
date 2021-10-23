package com.ape.user.controller;


import com.ape.common.model.ResultVO;
import com.ape.common.utils.CommonUtil;
import com.ape.user.social.SocialFactory;
import com.ape.user.social.SocialFactoryProducer;
import com.ape.user.social.wechat.WeChatService;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <p>
 * 第三方登录用户表 前端控制器
 * </p>
 *
 * @author Yan
 * @since 2021-08-06
 */
@Slf4j
@Controller
@RequestMapping("/oauth2")
public class SocialUserDetailController {

    @Autowired
    private SocialFactoryProducer socialFactoryProducer;
    @Autowired
    private WeChatService weChatService;

    /****************** WeChat 小程序 *************************/
    @ApiOperation(value = "微信小程序", notes = "微信小程序")
    @PostMapping("/wxApp/login")
    @ResponseBody
    public ResultVO wxMiniLogin(HttpServletRequest request){
        Map<String, Object> requestParam = CommonUtil.encapsulateRequestArgs(request);
        weChatService.login(requestParam);
        Map<String, Object> userInfo = weChatService.getUserInfo(requestParam);
        OAuth2AccessToken oAuth2AccessToken = weChatService.generateOAuth2AccessToken(userInfo);
        return ResultVO.OK(oAuth2AccessToken);
    }

    /****************** GitHub *************************/

    /**
     * 请求重定向到第三方登录页
     * @param response
     */
    @ApiOperation(value = "GitHub第三方登录页请求", notes = "GitHub第三方登录页请求")
    @SneakyThrows
    @GetMapping("/github/login")
    public void gitHubLogin(HttpServletResponse response){
        response.sendRedirect(socialFactoryProducer.getSocialService(SocialFactory.GITHUB_SOURCE).buildLoginUrl());
    }

    @ApiOperation(value = "GitHub登录回调拦截请求", notes = "GitHub登录回调拦截请求")
    @GetMapping("/github/callback")
    @ResponseBody
    public ResultVO<OAuth2AccessToken> gitHubCallback(@RequestParam("code") String code, @RequestParam("state") String state){
        OAuth2AccessToken oAuth2AccessToken = socialFactoryProducer.getSocialService(SocialFactory.GITHUB_SOURCE).buildOAuth2AccessToken(code, state);
        return ResultVO.OK(oAuth2AccessToken);
    }

    /****************** Gitee *************************/

    @ApiOperation(value = "Gitee第三方登录页请求", notes = "Gitee第三方登录页请求")
    @SneakyThrows
    @GetMapping("/gitee/login")
    public void giteeLogin(HttpServletResponse response){
        response.sendRedirect(socialFactoryProducer.getSocialService(SocialFactory.GITEE_SOURCE).buildLoginUrl());
    }

    @ApiOperation(value = "Gitee登录回调拦截请求", notes = "Gitee登录回调拦截请求")
    @GetMapping("/gitee/callback")
    @ResponseBody
    public ResultVO<OAuth2AccessToken> giteeCallback(@RequestParam("code") String code, @RequestParam("state") String state){
        OAuth2AccessToken oAuth2AccessToken = socialFactoryProducer.getSocialService(SocialFactory.GITEE_SOURCE).buildOAuth2AccessToken(code, state);
        return ResultVO.OK(oAuth2AccessToken);
    }

}
