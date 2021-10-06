package com.ape.common.utils.secure;

import org.bouncycastle.jce.interfaces.ECPrivateKey;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/9/10
 *
 * 使用Bouncy Castle算法库加解密ECIES椭圆曲线密码
 */
public class ECIESSecure implements Secure {

    private static final Logger log = LoggerFactory.getLogger(ECIESSecure.class);

    public static final String ECIES = "ECIES";
    private ECPrivateKey ecPrivateKey;
    private ECPublicKey ecPublicKey;
    private Cipher cipher;

    public ECIESSecure() throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException {
        Security.addProvider(new BouncyCastleProvider());
        cipher = Cipher.getInstance(ECIES, "BC");
        keyGenerator();
    }

    @Override
    public PublicKey getPublicKey() {
        return ecPublicKey;
    }

    @Override
    public PrivateKey getPrivateKey() {
        return ecPrivateKey;
    }

    private void keyGenerator() throws NoSuchProviderException, NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("ECDH", "BC");
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        ecPublicKey = (ECPublicKey) keyPair.getPublic();
        ecPrivateKey = (ECPrivateKey) keyPair.getPrivate();
    }

    /**
     * ECIES 公钥加密数据
     * @param data
     * @param publicKey
     * @return
     */
    @Override
    public String encryptData(String data, String publicKey) {
        try {
            ECPublicKey ecPublicKey = decodePublicKey(publicKey);
            cipher.init(Cipher.ENCRYPT_MODE, ecPublicKey);
            byte[] doFinal = cipher.doFinal(data.getBytes());
            String marshal = new HexBinaryAdapter().marshal(doFinal);

            System.out.println("加密后的密文: " + marshal);
            return marshal;
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            log.error(e.getMessage());
            throw new RuntimeException(ECIES + "加密失败");
        }
    }

    /**
     * ECIES 私钥解密数据
     * @param encryptData
     * @param privateKey
     * @return
     */
    @Override
    public String decryptData(String encryptData, String privateKey) {
        try {
            ECPrivateKey ecPrivateKey = decodePrivateKey(privateKey);
            cipher.init(Cipher.DECRYPT_MODE, ecPrivateKey);
            byte[] cipherText = new HexBinaryAdapter().unmarshal(encryptData);
            byte[] doFinal = cipher.doFinal(cipherText);

            String plainText = new String(doFinal);
            System.out.println("解密后的明文: " + plainText);
            return plainText;
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            log.error(e.getMessage());
            throw new RuntimeException(ECIES + "解密失败");
        }
    }

    /**
     * 打印密钥信息
     * @param ecPublicKey
     */
    public void printECCurveInfo(ECPublicKey ecPublicKey, ECPrivateKey ecPrivateKey){
        ECCurve ecCurve = ecPublicKey.getParameters().getCurve();
        BigInteger a = ecCurve.getA().toBigInteger();
        BigInteger b = ecCurve.getB().toBigInteger();
        BigInteger q = ((ECCurve.Fp) ecCurve).getQ();

        ECPoint basePoint = ecPublicKey.getParameters().getG();
        BigInteger pointX = basePoint.getAffineXCoord().toBigInteger();
        BigInteger pointY = basePoint.getAffineYCoord().toBigInteger();
        BigInteger pubX = ecPublicKey.getQ().getAffineXCoord().toBigInteger();
        BigInteger pubY = ecPublicKey.getQ().getAffineYCoord().toBigInteger();
        String publicKey = new HexBinaryAdapter().marshal(ecPublicKey.getQ().getEncoded(false));

        String algorithm = ecPublicKey.getAlgorithm();
        StringBuilder sb = new StringBuilder();
        sb.append("算法：").append(algorithm).append("\n");
        sb.append("椭圆曲线参数a = ").append(a).append("\n");
        sb.append("椭圆曲线参数b = ").append(b).append("\n");
        sb.append("椭圆曲线参数q = ").append(q).append("\n");
        sb.append("基点橫坐标 = ").append(pointX).append("\n");
        sb.append("基点纵坐标 = ").append(pointY).append("\n");
        sb.append("公钥横坐标 = ").append(pubX).append("\n");
        sb.append("公钥纵坐标 = ").append(pubY).append("\n");
        sb.append("私钥 = ").append(ecPrivateKey.getD().toString(16)).append("\n");
        sb.append("公钥 = ").append(publicKey).append("\n");
        System.out.println(sb.toString());
    }

    /**
     * 解析前端字符串公钥
     * @param publicKey
     * @return
     */
    private ECPublicKey decodePublicKey(String publicKey){
        Assert.notNull(publicKey, "publicKey is not null.");
        try {
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKey));
            KeyFactory keyFactory = KeyFactory.getInstance("ECDH", "BC");
            return  ((ECPublicKey) keyFactory.generatePublic(x509EncodedKeySpec));
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private ECPrivateKey decodePrivateKey(String privateKey){
        Assert.notNull(privateKey, "privateKey is not null.");
        try {
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey));
            KeyFactory keyFactory = KeyFactory.getInstance("ECDH", "BC");
            return ((ECPrivateKey) keyFactory.generatePrivate(pkcs8EncodedKeySpec));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
