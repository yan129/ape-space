package com.ape.common.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/8/9
 *
 * AES加解密
 */
public class AESUnit {

    private static final String key = "wqHpvf8rATZTyg0ESnT66A==";

    /**
     * 加密数据
     * @param data
     * @return
     */
    public static String encrypt(String data){
        // base64解码密钥
        byte[] decodeKey = Base64.decode(key.getBytes());
        //构建
        SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, decodeKey);
        //加密
        byte[] encryptData = aes.encrypt(data);
        // base64加密数据
        return Base64.encode(encryptData);
    }

    /**
     * 解密数据
     * @param encryptData
     * @return
     */
    public static String decrypt(String encryptData){
        // base64解码密钥
        byte[] decodeKey = Base64.decode(key.getBytes());
        //构建
        SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, decodeKey);
        //解密
        byte[] decrypt = aes.decrypt(encryptData);

        return new String(decrypt);
    }
}
