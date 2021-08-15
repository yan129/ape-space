package com.ape.user.config;

import com.ape.common.model.ResultVO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/6/27
 *
 * 自定义 Oauth2 异常
 */
@Component
public class CustomOauthException implements WebResponseExceptionTranslator {

    @Override
    public ResponseEntity translate(Exception e) throws Exception {

        return ResponseEntity.ok(ResultVO.ERROR(e.getMessage()));
    }

}
