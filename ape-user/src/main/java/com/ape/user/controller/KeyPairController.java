package com.ape.user.controller;

import com.ape.common.model.ResultVO;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/6/28
 *
 * 获取RSA公钥接口
 */
@Api(value = "JWT管理器", description = "JWT管理器")
@Slf4j
@RestController
@RequestMapping("/rsa")
public class KeyPairController {

    @Autowired
    private KeyPair keyPair;

    @ApiOperation(value = "获取非对称加密（RSA）算法公钥", notes = "获取非对称加密（RSA）算法公钥")
    @GetMapping("/publicKey")
    public Map<String, Object> getPublicKey(){
        return getRsaPublicKey();
    }

    /**
     * 解析获取公钥，用于网关
     * @return
     */
    private JSONObject getRsaPublicKey() {
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAKey key = new RSAKey.Builder(publicKey).build();
        return new JWKSet(key).toJSONObject();
    }

}
