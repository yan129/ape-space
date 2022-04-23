package com.ape.common.component;

import cn.hutool.core.net.URLDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
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

    private HttpServletRequest request = null;

    public Object readHeaders(){
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String element = headerNames.nextElement();
            System.out.println(element + "===========");
        }
        return null;
    }

    public Map<String, Object> readRequestUserInfo(){
        request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String userInfo = URLDecoder.decode(request.getHeader("userInfo"), StandardCharsets.UTF_8);
        log.info("userInfo = {}", userInfo);
        return null;
    }
}
