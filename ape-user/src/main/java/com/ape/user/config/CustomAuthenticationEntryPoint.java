package com.ape.user.config;

import com.ape.common.model.ResponseCode;
import com.ape.common.model.ResultVO;
import com.ape.common.utils.HttpResponseUtil;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/6/28
 *
 * 没有认证时，在这里处理结果，不要重定向
 */
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        ResultVO<Object> error = ResultVO.ERROR();
        if (e instanceof InsufficientAuthenticationException){
            error.setData(ResponseCode.UNAUTHORIZED);
        }
        HttpResponseUtil.output(response, error);
    }
}
