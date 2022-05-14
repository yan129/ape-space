package com.ape.common.component;

import cn.hutool.core.net.URLDecoder;
import cn.hutool.json.JSONUtil;
import com.ape.common.model.UserInfoWrapper;
import com.ape.common.utils.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2022/4/23
 */
@Slf4j
@Component
public class RequestContextWrapper {

    private static HttpServletRequest request = null;

    public static HttpServletRequest getHttpServletRequest(){
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    }

    public static Map<String, Object> readRequestArgs(){
        request = getHttpServletRequest();
        return CommonUtil.encapsulateRequestArgs(request);
    }

    public static UserInfoWrapper readRequestUserInfo(){
        request = getHttpServletRequest();
        Assert.notNull(request.getHeader("userInfo"), "request header userInfo is null");
        String userInfo = URLDecoder.decode(request.getHeader("userInfo"), StandardCharsets.UTF_8);
        UserInfoWrapper userInfoWrapper = JSONUtil.toBean(userInfo, UserInfoWrapper.class);
        return userInfoWrapper;
    }
}
