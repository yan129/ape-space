package com.ape.user.config;

import com.ape.common.model.ResponseCode;
import com.ape.common.model.ResultVO;
import com.ape.common.utils.HttpResponseUtil;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/6/11
 * 登录失败处理器，这个拦截只对表单提交有效，对oauth2提供的oauth/token提供的登录方法无效
 */
@Component
public class CustomLoginFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        ResultVO<Object> error = ResultVO.ERROR(ResponseCode.USER_LOGIN_FAILURE);
        if (e instanceof LockedException){
            error.setData(ResponseCode.USER_ACCOUNT_LOCKED);
        }else if (e instanceof CredentialsExpiredException){
            error.setData(ResponseCode.USER_CREDENTIALS_EXPIRED);
        }else if (e instanceof AccountExpiredException){
            error.setData(ResponseCode.USER_ACCOUNT_EXPIRED);
        }else if(e instanceof DisabledException){
            error.setData(ResponseCode.USER_ACCOUNT_DISABLE);
        }else if (e instanceof BadCredentialsException){
            error.setData(ResponseCode.USER_CREDENTIALS_ERROR);
        }
        HttpResponseUtil.output(response, error);
    }
}
