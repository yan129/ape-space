package com.ape.common.utils.secure;

import org.bouncycastle.asn1.gm.GMNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.crypto.generators.ECKeyPairGenerator;
import org.bouncycastle.crypto.params.*;
import org.bouncycastle.crypto.signers.SM2Signer;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECPoint;
import org.springframework.util.Assert;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/9/11
 *
 * 国密SM2加密
 */
public class SM2Secure implements Secure {

    public static final String SM2 = "SM2";
    public static final String CRYPTO_NAME_SM2 = "sm2p256v1";
    private X9ECParameters sm2ECParameters;
    private ECDomainParameters ecDomainParameters;
    private ECPrivateKeyParameters ecPrivateKey;
    private ECPublicKeyParameters ecPublicKey;

    public SM2Secure() throws NoSuchAlgorithmException {
        keyGenerator();
    }

    /**
     * 生成密钥对
     */
    private void keyGenerator() throws NoSuchAlgorithmException {
        sm2ECParameters = GMNamedCurves.getByName(CRYPTO_NAME_SM2);
        ecDomainParameters = new ECDomainParameters(sm2ECParameters.getCurve(), sm2ECParameters.getG(), sm2ECParameters.getN());
        ECKeyPairGenerator keyPairGenerator = new ECKeyPairGenerator();
        keyPairGenerator.init(new ECKeyGenerationParameters(ecDomainParameters, SecureRandom.getInstance("SHA1PRNG")));
        AsymmetricCipherKeyPair asymmetricCipherKeyPair = keyPairGenerator.generateKeyPair();
        ecPrivateKey = ((ECPrivateKeyParameters) asymmetricCipherKeyPair.getPrivate());
        ecPublicKey = ((ECPublicKeyParameters) asymmetricCipherKeyPair.getPublic());
    }

    @Override
    public ECPublicKeyParameters getPublicKey() {
        return ecPublicKey;
    }

    @Override
    public ECPrivateKeyParameters getPrivateKey() {
        return ecPrivateKey;
    }

    @Override
    public String encryptData(String data, String publicKey) {
        try {
            Assert.notNull(publicKey, "publicKey is not null.");
            ECPublicKeyParameters ecPublicKey = decodePublicKey(publicKey);
            SM2Engine sm2Engine = new SM2Engine(SM2Engine.Mode.C1C3C2);
            sm2Engine.init(true, new ParametersWithRandom(ecPublicKey, new SecureRandom()));
            byte[] dataBytes = data.getBytes();
            byte[] processBlock = sm2Engine.processBlock(dataBytes, 0, dataBytes.length);
            String marshal = new HexBinaryAdapter().marshal(processBlock);

            System.out.println("加密后的密文: " + marshal);
            return marshal;
        } catch (InvalidCipherTextException e) {
            e.printStackTrace();
            throw new RuntimeException("SM2加密失败");
        }
    }

    @Override
    public String decryptData(String encryptData, String privateKey) {
        try {
            Assert.notNull(privateKey, "privateKey is not null.");
            ECPrivateKeyParameters ecPrivateKey = decodePrivateKey(privateKey);
            byte[] cipherText = new HexBinaryAdapter().unmarshal(encryptData);
            SM2Engine sm2Engine = new SM2Engine(SM2Engine.Mode.C1C3C2);
            sm2Engine.init(false, ecPrivateKey);
            byte[] processBlock = sm2Engine.processBlock(cipherText, 0, cipherText.length);
            String plainText = new String(processBlock);

            System.out.println("解密后的明文: " + plainText);
            return plainText;
        }catch (InvalidCipherTextException e){
            e.printStackTrace();
            throw new RuntimeException("SM2解密失败");
        }
    }

    /**
     * 私钥签名
     * @param data 双方签名的数据
     * @param ecPrivateKey 私钥
     * @return
     * @throws CryptoException
     */
    public byte[] sign(String data, ECPrivateKeyParameters ecPrivateKey) throws CryptoException {
        SM2Signer sm2Signer = new SM2Signer();
        sm2Signer.init(true, ecPrivateKey);
        byte[] signData = data.getBytes();
        sm2Signer.update(signData, 0, signData.length);
        return sm2Signer.generateSignature();
    }

    /**
     * 用公钥检验数字签名的合法性
     * @param data 双方签名的数据
     * @param signData 签名数据
     * @param ecPublicKey 公钥
     * @return
     */
    public boolean verify(String data, byte[] signData, ECPublicKeyParameters ecPublicKey){
        SM2Signer sm2Signer = new SM2Signer();
        sm2Signer.init(false, ecPublicKey);
        byte[] verifyData = data.getBytes();
        sm2Signer.update(verifyData, 0, verifyData.length);
        return sm2Signer.verifySignature(signData);
    }

    /**
     * 打印密钥信息
     * @param ecPublicKey 公钥
     */
    public void printSM2Info(ECPublicKeyParameters ecPublicKey, ECPrivateKeyParameters ecPrivateKey){
        ECPoint ecPoint = ecPublicKey.getQ();
        ECCurve ecCurve = ecPoint.getCurve();
        BigInteger a = ecCurve.getA().toBigInteger();
        BigInteger b = ecCurve.getB().toBigInteger();
        BigInteger q = ((ECCurve.Fp) ecCurve).getQ();
        String publicKey = new HexBinaryAdapter().marshal(ecPoint.getEncoded(false));

        ECPoint basePoint = ecPublicKey.getParameters().getG();
        BigInteger pointX = basePoint.getAffineXCoord().toBigInteger();
        BigInteger pointY = basePoint.getAffineYCoord().toBigInteger();
        BigInteger pubX = ecPublicKey.getQ().getAffineXCoord().toBigInteger();
        BigInteger pubY = ecPublicKey.getQ().getAffineYCoord().toBigInteger();

        StringBuilder sb = new StringBuilder();
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
    private ECPublicKeyParameters decodePublicKey(String publicKey){
        // 提取公钥点
        ECPoint ecPoint = sm2ECParameters.getCurve().decodePoint(new HexBinaryAdapter().unmarshal(publicKey));
        // 公钥前面的02或者03表示是压缩公钥，04表示未压缩公钥, 04的时候，可以去掉前面的04
        return new ECPublicKeyParameters(ecPoint, ecDomainParameters);
    }

    private ECPrivateKeyParameters decodePrivateKey(String privateKey){
        BigInteger privateKeyD = new BigInteger(privateKey, 16);
        return new ECPrivateKeyParameters(privateKeyD, ecDomainParameters);
    }
}
