package com.ape.common.utils.secure;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/9/10
 */
public interface Secure {

    /**
     * 获取公钥
     * @return
     */
    PublicKey getPublicKey();

    /**
     * 获取私钥
     * @return
     */
    PrivateKey getPrivateKey();
    /**
     * 公钥加密
     * @param data
     * @param publicKey
     * @return
     */
    String encryptData(String data, PublicKey publicKey);

    /**
     * 私钥解密
     * @param encryptData
     * @param privateKey
     * @return
     */
    String decryptData(String encryptData, PrivateKey privateKey);
}
