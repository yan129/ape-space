package com.ape.user.oauth2;

import com.ape.common.model.ResponseCode;
import com.ape.common.utils.CaptchaUtil;
import com.ape.common.utils.CommonUtil;
import com.ape.common.utils.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/10/8
 *
 * oauth2加入图形验证码登录
 */
public class CaptchaTokenGranter extends AbstractTokenGranter {

    private static final String GRANT_TYPE = "captcha";
    private final AuthenticationManager authenticationManager;
    private StringRedisTemplate stringRedisTemplate;

    public CaptchaTokenGranter(AuthenticationManager authenticationManager,
                               StringRedisTemplate stringRedisTemplate,
                               AuthorizationServerTokenServices tokenServices,
                               ClientDetailsService clientDetailsService,
                               OAuth2RequestFactory requestFactory){
        this(authenticationManager, tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
        this.stringRedisTemplate = stringRedisTemplate;
    }

    protected CaptchaTokenGranter(AuthenticationManager authenticationManager,
                                  AuthorizationServerTokenServices tokenServices,
                                  ClientDetailsService clientDetailsService,
                                  OAuth2RequestFactory requestFactory,
                                  String grantType) {
        //调用父类 接管GRANT_TYPE类型
        super(tokenServices, clientDetailsService, requestFactory, grantType);
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
        Map<String, String> parameters = new LinkedHashMap<>(tokenRequest.getRequestParameters());
        String username = parameters.get("username");
        String password = parameters.get("password");
        String captchaCode = parameters.get("captchaCode");

        if (!CommonUtil.telephoneRegex(username)){
            throw new InvalidGrantException("手机号码错误");
        }

        checkCaptchaCode(captchaCode);
        parameters.remove("password");
        Authentication userAuth = new UsernamePasswordAuthenticationToken(username, password);
        ((AbstractAuthenticationToken)userAuth).setDetails(parameters);

        try {
            userAuth = this.authenticationManager.authenticate(userAuth);
        } catch (AccountStatusException ase) {
            throw new InvalidGrantException(ase.getMessage());
        } catch (BadCredentialsException bce) {
            // Bad credentials
            throw new InvalidGrantException("账号密码错误");
        }

        if (userAuth != null && userAuth.isAuthenticated()) {
            OAuth2Request storedOAuth2Request = this.getRequestFactory().createOAuth2Request(client, tokenRequest);
            return new OAuth2Authentication(storedOAuth2Request, userAuth);
        } else {
            throw new InvalidGrantException("Could not authenticate user: " + username);
        }
    }

    private void checkCaptchaCode(String code) {
        if (StringUtils.isBlank(code)){
            throw new InvalidGrantException("验证码不能为空");
        }
        // 获取 header 头部的 UUID
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        String uuid = CaptchaUtil.PREFIX + request.getHeader("uuid");
        String cacheCode = stringRedisTemplate.opsForValue().get(uuid);
        if (StringUtils.isBlank(cacheCode)){
            throw new InvalidGrantException(ResponseCode.REGISTER_CODE_EXPIRED.getMsg());
        }
        if (!StringUtils.equalsIgnoreCase(code, cacheCode)){
            throw new InvalidGrantException(ResponseCode.REGISTER_CHECK_CODE.getMsg());
        }else {
            stringRedisTemplate.delete(uuid);
        }
    }
}
