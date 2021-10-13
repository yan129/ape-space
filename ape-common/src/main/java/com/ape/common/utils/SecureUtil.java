package com.ape.common.utils;

import com.ape.common.utils.secure.AlgorithmEnum;
import com.ape.common.utils.secure.ECIESSecure;
import com.ape.common.utils.secure.SM2Secure;
import com.ape.common.utils.secure.Secure;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.crypto.CryptoException;
import org.springframework.util.Assert;

import javax.crypto.NoSuchPaddingException;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.security.*;
import java.util.Base64;

import org.bouncycastle.jce.interfaces.ECPrivateKey;
import org.bouncycastle.jce.interfaces.ECPublicKey;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/9/9
 *
 * 加解密工具
 */
@Slf4j
public class SecureUtil {

    public static void main(String[] args) {
        aa();
        bb();
    }

    private static void aa(){
        long sm2start = System.currentTimeMillis();
        SM2Secure sm2Secure = ((SM2Secure) execute(AlgorithmEnum.SM2));
        String publicKey = new HexBinaryAdapter().marshal(sm2Secure.getPublicKey().getQ().getEncoded(false));
        String data = sm2Secure.encryptData("helljhdioqhdoiahdo   jeo", publicKey);
        sm2Secure.decryptData(data, sm2Secure.getPrivateKey().getD().toString(16));
        sm2Secure.printSM2Info(sm2Secure.getPublicKey(), sm2Secure.getPrivateKey());
        long sm2stop = System.currentTimeMillis();
        System.out.println(sm2stop - sm2start);

        try {
            byte[] signs = sm2Secure.sign("sign", sm2Secure.getPrivateKey());
            System.out.println("sing = " + new HexBinaryAdapter().marshal(signs));
            boolean verify = sm2Secure.verify("sign", signs, sm2Secure.getPublicKey());
            System.out.println(verify);
        } catch (CryptoException e) {
            e.printStackTrace();
        }
    }

    private static void bb(){
        long start = System.currentTimeMillis();
        ECIESSecure secure = ((ECIESSecure) execute(AlgorithmEnum.ECIES));
        String publicKey = Base64.getEncoder().encodeToString(secure.getPublicKey().getEncoded());
        String hello = secure.encryptData("helljhdioqhdoiahdo   jeo", publicKey);
        String decryptData = secure.decryptData(hello, Base64.getEncoder().encodeToString(secure.getPrivateKey().getEncoded()));
        secure.printECCurveInfo(((ECPublicKey) secure.getPublicKey()), ((ECPrivateKey) secure.getPrivateKey()));
        long stop = System.currentTimeMillis();
        System.out.println(stop - start);
    }

    static Secure execute(AlgorithmEnum algorithmEnum) {
        Assert.notNull(algorithmEnum, "Please select the algorithm to be used");
        if (StringUtils.equalsIgnoreCase(algorithmEnum.name(), ECIESSecure.ECIES)){
            try {
                return new ECIESSecure();
            } catch (NoSuchPaddingException | NoSuchAlgorithmException | NoSuchProviderException e) {
                e.printStackTrace();
                log.error(e.getMessage());
            }
        }
        if (StringUtils.equalsIgnoreCase(algorithmEnum.name(), SM2Secure.SM2)){
            try {
                return new SM2Secure();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                log.error(e.getMessage());
            }
        }
        throw new RuntimeException("Algorithm initialization failed");
    }
}
