package com.ape.common.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RsaUtil {

    public static final String SIGNATURE_TYPE = "RSA";
    public static final int DEFAULT_KEY_SIZE = 2048;
    public static final String DEFAULT_SECRET = "xiong";

    /**
     * 从文件中读取公钥
     *
     * @param filename 公钥保存路径
     * @return 公钥对象
     * @throws Exception
     */
    public static PublicKey getPublicKey(String filename) throws Exception {
        byte[] bytes = readFile(filename);
        return getPublicKey(bytes);
    }

    /**
     * 从文件中读取私钥
     *
     * @param filename 私钥保存路径
     * @return 私钥对象
     * @throws Exception
     */
    public static PrivateKey getPrivateKey(String filename) throws Exception {
        byte[] bytes = readFile(filename);
        return getPrivateKey(bytes);
    }

    /**
     * 获取公钥
     *
     * @param bytes 公钥的字节形式
     * @return
     * @throws Exception
     */
    private static PublicKey getPublicKey(byte[] bytes) throws Exception {
        bytes = Base64.getDecoder().decode(bytes);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(bytes);
        KeyFactory factory = KeyFactory.getInstance(SIGNATURE_TYPE);
        return factory.generatePublic(spec);
    }

    /**
     * 获取密钥
     *
     * @param bytes 私钥的字节形式
     * @return
     * @throws Exception
     */
    private static PrivateKey getPrivateKey(byte[] bytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
        bytes = Base64.getDecoder().decode(bytes);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(bytes);
        KeyFactory factory = KeyFactory.getInstance(SIGNATURE_TYPE);
        return factory.generatePrivate(spec);
    }

    /**
     * 根据密文，生存rsa公钥和私钥,并写入指定文件
     *
     * @param publicKeyFilename  公钥文件路径
     * @param privateKeyFilename 私钥文件路径
     * @param secret             生成密钥的密文
     */
    public static void generateKey(String publicKeyFilename, String privateKeyFilename, String secret, int keySize) throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(SIGNATURE_TYPE);
        SecureRandom secureRandom = new SecureRandom(secret.getBytes());
        keyPairGenerator.initialize(Math.max(keySize, DEFAULT_KEY_SIZE), secureRandom);
        KeyPair keyPair = keyPairGenerator.genKeyPair();
        // 获取公钥并写出
        byte[] publicKeyBytes = keyPair.getPublic().getEncoded();
        publicKeyBytes = Base64.getEncoder().encode(publicKeyBytes);
        writeFile(publicKeyFilename, publicKeyBytes);
        // 获取私钥并写出
        byte[] privateKeyBytes = keyPair.getPrivate().getEncoded();
        privateKeyBytes = Base64.getEncoder().encode(privateKeyBytes);
        writeFile(privateKeyFilename, privateKeyBytes);
    }

    private static byte[] readFile(String fileName) throws Exception {
        return Files.readAllBytes(new File(fileName).toPath());
    }

    private static void writeFile(String destPath, byte[] bytes) throws IOException {
        File dest = new File(destPath);
        if (!dest.exists()) {
            dest.createNewFile();
        }
        Files.write(dest.toPath(), bytes);
    }

    /**
     * 使用公钥加密数据
     * @param publicKey 公钥
     * @param srcData 要加密的数据
     * @return
     * @throws Exception
     */
    public static String encryptData(String publicKey, String srcData) throws Exception {
        // 解密
        byte[] pk = new BASE64Decoder().decodeBuffer(publicKey);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(pk);
        KeyFactory factory = KeyFactory.getInstance(SIGNATURE_TYPE);
        // 获取公钥
        PublicKey key = factory.generatePublic(spec);
        // 对数据加密
        Cipher cipher = Cipher.getInstance(SIGNATURE_TYPE);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] doFinal = cipher.doFinal(srcData.getBytes());
        return new BASE64Encoder().encodeBuffer(doFinal).replace("\r", "").replace("\n", "");
    }

    /**
     * 使用私钥解密数据
     * @param privateKey 私钥
     * @param encryptData 加密的数据
     * @return
     * @throws Exception
     */
    public static String descryptData(String privateKey, String encryptData) throws Exception {
        // BASE64转码解密私钥
        byte[] pk = new BASE64Decoder().decodeBuffer(privateKey);
        // BASE64转码解密密文
        byte[] decodeBuffer = new BASE64Decoder().decodeBuffer(encryptData);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(pk);
        KeyFactory factory = KeyFactory.getInstance(SIGNATURE_TYPE);
        // 获取私钥
        PrivateKey key = factory.generatePrivate(spec);
        // 对数据解密
        Cipher cipher = Cipher.getInstance(SIGNATURE_TYPE);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] doFinal = cipher.doFinal(decodeBuffer);
        return new String(doFinal);
    }
}
