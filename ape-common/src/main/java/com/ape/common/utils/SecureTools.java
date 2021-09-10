package com.ape.common.utils;

import com.ape.common.utils.secure.AlgorithmEnum;
import com.ape.common.utils.secure.EciesSecure;
import com.ape.common.utils.secure.Secure;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.interfaces.ECPrivateKey;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECPoint;
import org.springframework.util.Assert;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.math.BigInteger;
import java.security.*;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/9/9
 *
 * 加解密工具
 */
@Slf4j
public class SecureTools {

    public static void main(String[] args) throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        long start = System.currentTimeMillis();

        Secure secure = execute(AlgorithmEnum.ECIES);
        String hello = secure.encryptData("hello", secure.getPublicKey());
        String aa = "041C6126E2841360C1CE031E144C472A3E3F5B1CCA6D4A910B056A633538EC5488959E5250D78DA45072B048CA957B2E506478E448FAC818338F5DD3263DCA866266A9AF66F9BCB990B6AE15F5BA089DF07A3349B063";
        String decryptData = secure.decryptData(aa, secure.getPrivateKey());

        long stop = System.currentTimeMillis();
        System.out.println(stop - start);

        System.out.println("==========");

        byte[] plainText = "Hello World!".getBytes();
        byte[] cipherText = null;

        Security.addProvider(new BouncyCastleProvider());
        //生成公钥和私钥
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("ECIES", "BC");
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        ECPublicKey ecPublicKey = (ECPublicKey) keyPair.getPublic();
        ECPrivateKey ecPrivateKey = (ECPrivateKey) keyPair.getPrivate();
        //打印密钥信息
        ECCurve ecCurve = ecPublicKey.getParameters().getCurve();
        System.out.println("椭圆曲线参数a = " + ecCurve.getA().toBigInteger());
        System.out.println("椭圆曲线参数b = " + ecCurve.getB().toBigInteger());
        System.out.println("椭圆曲线参数q = " + ((ECCurve.Fp) ecCurve).getQ());
        ECPoint basePoint = ecPublicKey.getParameters().getG();
        System.out.println("基点橫坐标             "
                + basePoint.getAffineXCoord().toBigInteger());
        System.out.println("基点纵坐标             "
                + basePoint.getAffineYCoord().toBigInteger());
        System.out.println("公钥横坐标             "
                + ecPublicKey.getQ().getAffineXCoord().toBigInteger());
        System.out.println("公钥纵坐标             "
                + ecPublicKey.getQ().getAffineYCoord().toBigInteger());
        System.out.println("私钥                        " + ecPrivateKey.getD());

        Cipher cipher = Cipher.getInstance("ECIES", "BC");
        // 加密
        cipher.init(Cipher.ENCRYPT_MODE, ecPublicKey);
        cipherText = cipher.doFinal(plainText);
        System.out.println("密文: " + new HexBinaryAdapter().marshal(cipherText));

        // 解密
        cipher.init(Cipher.DECRYPT_MODE, ecPrivateKey);
        plainText = cipher.doFinal(cipherText);
        // 打印解密后的明文
        System.out.println("解密后的明文: " + new String(plainText));
    }

    static Secure execute(AlgorithmEnum algorithmEnum) {
        System.out.println(algorithmEnum.name() + "====");
        Assert.notNull(algorithmEnum, "Please select the algorithm to be used");
        if (StringUtils.equalsIgnoreCase(algorithmEnum.name(), "ECIES")){
            try {
                return new EciesSecure();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchProviderException e) {
                e.printStackTrace();
            }
        }
        throw new RuntimeException("Algorithm initialization failed");
    }
}
