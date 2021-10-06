package com.ape.common.utils.secure;

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
    <T> T getPublicKey();

    /**
     * 获取私钥
     * @return
     */
    <T> T getPrivateKey();
    /**
     * 公钥加密
     * @param data
     * @param publicKey
     * @return
     */
    String encryptData(String data, String publicKey);

    /**
     * 私钥解密
     * @param encryptData
     * @param privateKey
     * @return
     */
    String decryptData(String encryptData, String privateKey);
}
