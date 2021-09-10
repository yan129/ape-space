package com.ape.common.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.SM2;
import org.bouncycastle.asn1.gm.GMNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.generators.ECKeyPairGenerator;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECKeyGenerationParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.jce.interfaces.ECPrivateKey;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.util.encoders.Hex;

import javax.crypto.Cipher;
import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/9/9
 *
 * 客户端先连上服务端
 * 服务端生成一个随机数 s 作为自己的私钥，然后根据算法参数计算出公钥 S（算法参数通常是固定的）
 * 服务端使用某种签名算法把“算法参数（模数p，基数g）和服务端公钥S”作为一个整体进行签名
 * 服务端把“算法参数（模数p，基数g）、服务端公钥S、签名”发送给客户端
 * 客户端收到后验证签名是否有效
 * 客户端生成一个随机数 c 作为自己的私钥，然后根据算法参数计算出公钥 C
 * 客户端把 C 发送给服务端
 * 客户端和服务端（根据上述 DH 算法）各自计算出 k 作为会话密钥
 */
public class EcdsaUtil {
    private static final String SIGNALGORITHMS = "SHA256withECDSA";
    private static final String ALGORITHM = "EC";
    private static final String SECP256K1 = "secp256k1";
    private static final char[] HEX = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static void main(String[] args) throws Exception {


//        byte[] plainText = "Hello World!".getBytes();
//        byte[] cipherText = null;
//
//        Security.addProvider(new BouncyCastleProvider());
//        //生成公钥和私钥
//        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("ECIES", "BC");
//        KeyPair keyPair = keyPairGenerator.generateKeyPair();
//        ECPublicKey ecPublicKey = (ECPublicKey) keyPair.getPublic();
//        ECPrivateKey ecPrivateKey = (ECPrivateKey) keyPair.getPrivate();
//        //打印密钥信息
//        ECCurve ecCurve = ecPublicKey.getParameters().getCurve();
//        System.out.println("椭圆曲线参数a = " + ecCurve.getA().toBigInteger());
//        System.out.println("椭圆曲线参数b = " + ecCurve.getB().toBigInteger());
//        System.out.println("椭圆曲线参数q = " + ((ECCurve.Fp) ecCurve).getQ());
//        ECPoint basePoint = ecPublicKey.getParameters().getG();
//        System.out.println("基点橫坐标             "
//                + basePoint.getAffineXCoord().toBigInteger());
//        System.out.println("基点纵坐标             "
//                + basePoint.getAffineYCoord().toBigInteger());
//        System.out.println("公钥横坐标             "
//                + ecPublicKey.getQ().getAffineXCoord().toBigInteger());
//        System.out.println("公钥纵坐标             "
//                + ecPublicKey.getQ().getAffineYCoord().toBigInteger());
//        System.out.println("私钥                        " + ecPrivateKey.getD());
//
//        Cipher cipher = Cipher.getInstance("ECIES", "BC");
//        // 加密
//        cipher.init(Cipher.ENCRYPT_MODE, ecPublicKey);
//        cipherText = cipher.doFinal(plainText);
//        System.out.println("密文: " + new HexBinaryAdapter().marshal(cipherText));
//        // 解密
//        cipher.init(Cipher.DECRYPT_MODE, ecPrivateKey);
//        plainText = cipher.doFinal(cipherText);
//        // 打印解密后的明文
//        System.out.println("解密后的明文: " + new String(plainText));

//        生成公钥私钥
        KeyPair keyPair1 = getKeyPair();
        PublicKey publicKey1 = keyPair1.getPublic();
        PrivateKey privateKey1 = keyPair1.getPrivate();
//        //密钥转16进制字符串
//        String publicKey = encodeHexString(publicKey1.getEncoded());
//        String privateKey = encodeHexString(privateKey1.getEncoded());
//        System.out.println("生成公钥："+publicKey);
//        System.out.println("生成私钥："+privateKey);
//        //16进制字符串转密钥对象
//        PrivateKey privateKey2 = getPrivateKey(privateKey);
//        PublicKey publicKey2 = getPublicKey(publicKey);
//        //加签验签
////        String data1="需要签名的数据";
//        String signECDSA = signECDSA(privateKey2, "aa");
//        boolean verifyECDSA = verifyECDSA(publicKey2, signECDSA, "aa");
//        System.out.println("验签结果："+verifyECDSA);
//
        long start = System.currentTimeMillis();

        //生成密钥对
//        X9ECParameters sm2ECParameters = GMNamedCurves.getByName("sm2p256v1");
//        ECDomainParameters domainParameters = new ECDomainParameters(sm2ECParameters.getCurve(), sm2ECParameters.getG(), sm2ECParameters.getN());
//        ECKeyPairGenerator keyPairGenerator = new ECKeyPairGenerator();
//        keyPairGenerator.init(new ECKeyGenerationParameters(domainParameters, SecureRandom.getInstance("SHA1PRNG")));
//        AsymmetricCipherKeyPair asymmetricCipherKeyPair = keyPairGenerator.generateKeyPair();
//
//        //私钥，16进制格式，自己保存，格式如a2081b5b81fbea0b6b973a3ab6dbbbc65b1164488bf22d8ae2ff0b8260f64853
//        BigInteger privatekey = ((ECPrivateKeyParameters) asymmetricCipherKeyPair.getPrivate()).getD();
//        String privateKeyHex = privatekey.toString(16);
//
////公钥，16进制格式，发给前端，格式如04813d4d97ad31bd9d18d785f337f683233099d5abed09cb397152d50ac28cc0ba43711960e811d90453db5f5a9518d660858a8d0c57e359a8bf83427760ebcbba
//        ECPoint ecPoint = ((ECPublicKeyParameters) asymmetricCipherKeyPair.getPublic()).getQ();
//        String publicKeyHex = Hex.toHexString(ecPoint.getEncoded(false));

//        byte[] encrypt = encryptByPublicKey("aa".getBytes(), publicKey1);
//        System.out.println(new String(encrypt));
//        System.out.println(new String(decryptByPrivateKey(encrypt, privateKey1)));

        String text = "我是一段测试aaaa127318y31123213123121ed21d123123213213";
//
        SM2 sm2 = SmUtil.sm2();
        System.out.println(((ECPublicKey) sm2.getPublicKey()).getQ().getAffineXCoord());
        System.out.println(((ECPublicKey) sm2.getPublicKey()).getQ().getAffineYCoord());
//
//        // 公钥加密，私钥解密
        String encryptStr = sm2.encryptBcd(text, KeyType.PublicKey);
//        System.out.println(encryptStr);
//        String aa = "0463038A44F1CE02DE9CE77F28559F179EEB907C4070ADA2C8733B406F6AB3B4746EA4FA786D6028CBE1E61D3279070F01654B08F26CF1AD79943ED0F48076124C0A5916F01D082DBA6B2A756BFE011AB3C418CB67630DCB25172E4EBB7A971968BF242BCDD9EF6E0F57046AA06B8AF134674EA882F65F091DB213503D0644DBBEDE5DF44D26E2D18D024E5C8539E588197F61FF8BEC831B941D3A77C3";
        String decryptStr = StrUtil.utf8Str(sm2.decryptFromBcd(encryptStr, KeyType.PrivateKey));
        System.out.println(decryptStr);
        long stop = System.currentTimeMillis();
        System.out.println(stop - start);

    }

    /**
     * 私钥解密
     *
     * @param data    待解密数据
     * @param privateKey    私钥
     * @return byte[]   解密数据
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] data, PrivateKey privateKey)
            throws Exception {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        Cipher cipher = Cipher.getInstance("ECIES","BC");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }


    /**
     * 公钥加密
     *
     * @param data   待加密数据
     * @param publicKey   公钥
     * @return byte[]  加密数据
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data, PublicKey publicKey) throws Exception {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        Cipher cipher = Cipher.getInstance("ECIES","BC");//写不写 BC都可以，都是会选择BC实现来做
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(data);
    }

    /**
     * 加签
     * @param privateKey 私钥
     * @param data 数据
     * @return
     */
    public static String signECDSA(PrivateKey privateKey, String data) {
        String result = "";
        try {
            //执行签名
            Signature signature = Signature.getInstance(SIGNALGORITHMS);
            signature.initSign(privateKey);
            signature.update(data.getBytes());
            byte[] sign = signature.sign();
            return encodeHexString(sign);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 验签
     * @param publicKey 公钥
     * @param signed 签名
     * @param data 数据
     * @return
     */
    public static boolean verifyECDSA(PublicKey publicKey, String signed, String data) {
        try {
            //验证签名
            Signature signature = Signature.getInstance(SIGNALGORITHMS);
            signature.initVerify(publicKey);
            signature.update(data.getBytes());
            byte[] hex = decode(signed);
            boolean bool = signature.verify(hex);
            // System.out.println("验证：" + bool);
            return bool;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 从string转private key
     * @param key 私钥的字符串
     * @return
     * @throws Exception
     */
    public static PrivateKey getPrivateKey(String key) throws Exception {

        byte[] bytes = DatatypeConverter.parseHexBinary(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * 从string转publicKey
     * @param key 公钥的字符串
     * @return
     * @throws Exception
     */
    public static PublicKey getPublicKey(String key) throws Exception {

        byte[] bytes = DatatypeConverter.parseHexBinary(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(bytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        return keyFactory.generatePublic(keySpec);
    }




    /**
     * 生成密钥对
     * @return
     * @throws Exception
     */
    public static KeyPair getKeyPair() throws Exception {

        ECGenParameterSpec ecSpec = new ECGenParameterSpec(SECP256K1);
        KeyPairGenerator kf = KeyPairGenerator.getInstance(ALGORITHM);
        kf.initialize(ecSpec, new SecureRandom());
        KeyPair keyPair = kf.generateKeyPair();
        return keyPair;
    }

    /**
     * byte数组转16进制字符串
     * @param bytes
     * @return
     */
    public static String encodeHexString(byte[] bytes) {
        int nBytes = bytes.length;
        char[] result = new char[2 * nBytes];
        int j = 0;
        byte[] var4 = bytes;
        int var5 = bytes.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            byte aByte = var4[var6];
            result[j++] = HEX[(240 & aByte) >>> 4];
            result[j++] = HEX[15 & aByte];
        }

        return new String(result);
    }

    /**
     * 16进制字符串转byte数组
     * @param s 字符串
     * @return
     */
    public static byte[] decode(CharSequence s) {
        int nChars = s.length();
        if (nChars % 2 != 0) {
            throw new IllegalArgumentException("Hex-encoded string must have an even number of characters");
        } else {
            byte[] result = new byte[nChars / 2];

            for(int i = 0; i < nChars; i += 2) {
                int msb = Character.digit(s.charAt(i), 16);
                int lsb = Character.digit(s.charAt(i + 1), 16);
                if (msb < 0 || lsb < 0) {
                    throw new IllegalArgumentException("Detected a Non-hex character at " + (i + 1) + " or " + (i + 2) + " position");
                }

                result[i / 2] = (byte)(msb << 4 | lsb);
            }

            return result;
        }
    }

}
