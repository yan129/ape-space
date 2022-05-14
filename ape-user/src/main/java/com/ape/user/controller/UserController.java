package com.ape.user.controller;


import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ape.common.annotation.ApiIdempotent;
import com.ape.common.model.ResultVO;
import com.ape.common.utils.StringUtils;
import com.ape.common.utils.ThreadLocalHolder;
import com.ape.user.feign.SmsServiceFeign;
import com.ape.user.service.UserService;
import com.ape.user.vo.LoginVO;
import com.ape.user.vo.RegisterVO;
import com.nimbusds.jose.JWSObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
    @Autowired
    private SmsServiceFeign smsServiceFeign;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private TokenEndpoint tokenEndpoint;
    @Autowired
    private ThreadLocalHolder threadLocalHolder;

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

    @ApiOperation(value = "获取短信验证码", notes = "获取短信验证码")
    @PostMapping("/sms/{telephone}")
    public ResultVO<String> getSmsCode(@PathVariable("telephone") String telephone){
        return smsServiceFeign.send(telephone);
    }

    @ApiOperation(value = "自定义oauth2登录接口", notes = "自定义oauth2登录接口")
    @PostMapping("/oauth/token")
    public ResponseEntity<OAuth2AccessToken> customOauthLogin(Principal principal, HttpServletRequest request) throws HttpRequestMethodNotSupportedException {
        String loginType = request.getParameter("loginType");
        Map<String, String> parameters = null;
        Map<String, Object> userInfoMap = null;

        if (StringUtils.equals("sms", loginType)){
            parameters = this.buildSmsLoginMap(request);
        }else if ((StringUtils.equals("captcha", loginType))){
            parameters = this.buildCaptchaLoginMap(request);
        }else if ((StringUtils.equals("refresh", loginType))) {
            userInfoMap = getUserInfo(request);
            parameters = this.buildRefreshTokenMap(userInfoMap);
        }

        ResponseEntity<OAuth2AccessToken> entity = tokenEndpoint.postAccessToken(principal, parameters);
        // 刷新token请求成功，删除旧的refresh_token
        deleteOldRefreshToken(loginType, entity.getStatusCode().is2xxSuccessful(), userInfoMap);
        return entity;
    }

    private Map<String, String> buildCaptchaLoginMap(HttpServletRequest request){
        Map<String, String> parameters = new HashMap<>();
        parameters.put("grant_type", "captcha");
        parameters.put("scope", "all");
        parameters.put("username", request.getParameter("username"));
        parameters.put("password", request.getParameter("password"));
        parameters.put("captchaCode", request.getParameter("captchaCode"));
        return parameters;
    }

    private Map<String, String> buildSmsLoginMap(HttpServletRequest request){
        Map<String, String> parameters = new HashMap<>();
        parameters.put("grant_type", "sms");
        parameters.put("scope", "all");
        parameters.put("username", request.getParameter("username"));
        parameters.put("smsCode", request.getParameter("smsCode"));
        return parameters;
    }

    private Map<String, String> buildRefreshTokenMap(Map<String, Object> userInfoMap){
        Map<String, String> parameters = new HashMap<>();
        parameters.put("grant_type", "refresh_token");
        parameters.put("scope", "all");
        setRefreshToken(parameters, userInfoMap);
        return parameters;
    }

    private Map<String, Object> getUserInfo(HttpServletRequest request) {
        try {
            String realToken = request.getParameter("token");
            JWSObject jwsObject = JWSObject.parse(realToken);
            String userInfo = jwsObject.getPayload().toString();
            return JSONUtil.toBean(userInfo, Map.class);
        } catch (ParseException e) {
            e.printStackTrace();
            log.error("UserController parse token error: {}", e.getMessage());
        }
        return null;
    }

    private void setRefreshToken(Map<String, String> parameters, Map<String, Object> userInfoMap) {
        String jti = (String) userInfoMap.get("jti");
        String cacheRefreshToken = stringRedisTemplate.opsForValue().get("refreshToken:" + jti);
        parameters.put("refresh_token", cacheRefreshToken);
        threadLocalHolder.set(cacheRefreshToken);
    }

    private void deleteOldRefreshToken(String loginType, Boolean is2xxSuccessful, Map<String, Object> userInfoMap) {
        if (StringUtils.equals("refresh", loginType)) {
            if (is2xxSuccessful) {
                stringRedisTemplate.delete("refreshToken:" + userInfoMap.get("jti"));
            }
        }
    }

    @ApiOperation(value = "退出登录", notes = "退出登录")
    @PostMapping("/logout")
    public ResultVO logout(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        token = token.replace("Bearer ", "");
        JWSObject jwsObject = null;
        try {
            jwsObject = JWSObject.parse(token);
            String tokenInfo = jwsObject.getPayload().toString();
            JSONObject tokenJson = JSONUtil.parseObj(tokenInfo);

            String jti = tokenJson.getStr("jti");
            long exp = tokenJson.getLong("exp");

            long systemCurrentTime = System.currentTimeMillis() / 1000;
            if (exp > systemCurrentTime){
                stringRedisTemplate.opsForValue().set("logout:token:" + jti, String.valueOf(exp), exp - systemCurrentTime, TimeUnit.SECONDS);
            }
            return ResultVO.OK("注销成功");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ResultVO.ERROR("注销失败");
    }

}
