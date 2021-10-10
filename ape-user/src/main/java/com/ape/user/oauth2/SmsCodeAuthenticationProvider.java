package com.ape.user.oauth2;

import com.ape.common.model.ResponseCode;
import com.ape.common.utils.CommonUtil;
import com.ape.common.utils.StringUtils;
import com.ape.user.service.impl.UserServiceImpl;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/10/10
 *
 * 自定义短信模式的提供者
 * 判断token类型是否为SmsCodeAuthenticationToken,如果是则使用此provider
 * 编写完后还需要在 WebSecurityConfigurerAdapter 的实现类中的 AuthenticationManagerBuilder 配置 .authenticationProvider(new SmsCodeAuthenticationProvider())
 */
@Service
public class SmsCodeAuthenticationProvider implements AuthenticationProvider, MessageSourceAware {

    private MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();
    private StringRedisTemplate stringRedisTemplate;
    /**
     * userDetailsService的实现类 不需要自动注入，直接传入
     */
    private UserServiceImpl userService;

    public void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public void setUserService(UserServiceImpl userService) {
        this.userService = userService;
    }

    @Override
    public void setMessageSource(MessageSource messageSource) {
        this.messages = new MessageSourceAccessor(messageSource);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // principal 未认证之前是手机号，认证之后是客户信息
        String principal = ((String) authentication.getPrincipal()).trim();
        if (StringUtils.isBlank(principal)){
            throw new BadCredentialsException("手机号码不能为空");
        }
        if (!CommonUtil.telephoneRegex(principal)) {
            throw new UsernameNotFoundException("手机号码格式错误");
        }

        // 这里的Credentials是先通过AbstractTokenGranter组装  new SmsCodeAuthenticationToken() 传入的
        String smsCode = (String) authentication.getCredentials();
        String cacheKey = "SMS:" + principal;
        String cacheCode = stringRedisTemplate.opsForValue().get(cacheKey);

        // 校验验证码
        if (StringUtils.isBlank(smsCode)) {
            throw new BadCredentialsException(this.messages.getMessage("AbstractUserDetailsAuthenticationProvider sms code is null", "验证码不能为空"));
        }else if (StringUtils.isBlank(cacheCode)){
            throw new BadCredentialsException(this.messages.getMessage("AbstractUserDetailsAuthenticationProvider sms cache code is null", ResponseCode.REGISTER_CODE_EXPIRED.getMsg()));
        }else if (!StringUtils.equalsIgnoreCase(smsCode, cacheCode)){
            throw new BadCredentialsException(this.messages.getMessage("AbstractUserDetailsAuthenticationProvider sms code check error", ResponseCode.REGISTER_CHECK_CODE.getMsg()));
        }

        UserDetails user;
        try {
            user = userService.loadUserByUsername(principal);
        }catch (UsernameNotFoundException e){
            throw new BadCredentialsException(this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", ResponseCode.USERNAME_NOT_EXIST.getMsg()));
        }

        SmsCodeAuthenticationToken authenticationToken = new SmsCodeAuthenticationToken(user.getAuthorities(), user, smsCode);
        authenticationToken.setDetails(authenticationToken.getDetails());
        stringRedisTemplate.delete(cacheKey);
        return authenticationToken;
    }

    /**
     * 判断是否为 SmsCodeAuthenticationToken 类型  如果是直接调用切断过滤器链，如果不是继续查找
     * @param authentication
     * @return
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return SmsCodeAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
