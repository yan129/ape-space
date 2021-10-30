package com.ape.user.controller;


import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ape.common.annotation.ApiIdempotent;
import com.ape.common.model.ResultVO;
import com.ape.user.feign.SmsServiceFeign;
import com.ape.user.service.UserService;
import com.ape.user.vo.LoginVO;
import com.ape.user.vo.RegisterVO;
import com.nimbusds.jose.JWSObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
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

    @Value("${oauth.token.url}")
    private String oauthTokenUrl;

    @Autowired
    private UserService userService;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private SmsServiceFeign smsServiceFeign;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

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

    @ApiOperation(value = "自定义oauth2登录接口--图形验证码登录", notes = "自定义oauth2登录接口--图形验证码登录")
    @PostMapping("/oauth2/captchaLogin")
    public JSONObject loginByCaptcha(HttpServletRequest request, @RequestHeader HttpHeaders httpHeaders){
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.set("grant_type", "captcha");
        map.set("scope", "all");
        map.set("username", request.getParameter("username"));
        map.set("password", request.getParameter("password"));
        map.set("captchaCode", request.getParameter("captchaCode"));

        ResponseEntity<JSONObject> entity = this.getLoginResponseEntity(httpHeaders, map);
        return entity.getBody();
    }

    @ApiOperation(value = "自定义oauth2登录接口--短信验证码登录", notes = "自定义oauth2登录接口--短信验证码登录")
    @PostMapping("/oauth2/smsLogin")
    public JSONObject loginBySms(HttpServletRequest request, @RequestHeader HttpHeaders httpHeaders){
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.set("grant_type", "sms");
        map.set("scope", "all");
        map.set("username", request.getParameter("username"));
        map.set("smsCode", request.getParameter("smsCode"));

        ResponseEntity<JSONObject> entity = this.getLoginResponseEntity(httpHeaders, map);
        return entity.getBody();
    }

    private ResponseEntity<JSONObject> getLoginResponseEntity(@RequestHeader HttpHeaders httpHeaders, MultiValueMap<String, String> map) {
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.setBasicAuth("ape", "ape");
        //构造请求实体和头
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(map, httpHeaders);

        return restTemplate.postForEntity(oauthTokenUrl, requestEntity, JSONObject.class);
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
