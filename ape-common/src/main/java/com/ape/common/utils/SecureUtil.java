package com.ape.common.utils;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
//import com.antherd.smcrypto.sm2.Sm2;
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

        // 使用 sm-crypto 包加密，小程序前端才能相应解密
//        String encryptData = Sm2.doEncrypt("98281nj", "0444c09bf346197ae17900d6223f18ead7722bec08a5242ff89261ec4dda1e402a896ddd58a1049bd9bcd4ba3753e2883174a2c39bc7085a15c2ff23c4b4c19887"); // 加密结果
//        String decryptData = Sm2.doDecrypt(encryptData, "3abc09eda4beda82f406f4605db7eed00810f9235f5e2e855ca8b694612a5563"); // 解密结果
//        System.out.println(encryptData);
//        System.out.println(decryptData);

        String data = "dcf3fd7b4671f030cb6ea0156231d89a7b2daf71590864f0a864c6f7b1f6838a4b5fd8ea14b8dff6d099cff9826c0cded39aa1c243e759f84e9b97911962e737348896300d44f3eb7a7491874a495da3972f1a2d9a1378a4e0f26f812594a1042a0dc2587edb38c70ba238020af980111af2c4d4c4efedcb379681fea58608f2c0ab9740ae9eef512ceb90b0f27bd7df27347a139f51266d75f2145e4d6cb2d5f246ca07b8cea617c9d0bbf75af6478ea3abce787c0ff95d8045e16033de43a52cf5077e5342b49dc1fb767d8de8bb35833baa9141b196e8b7f5dc7fd2319dabcd0a9752885ff98ac16036a6cfa1ac9f2131b13f8003b6fd59218d884fa6749a30af02cbc3a8b448541eb496d53dbed211468bd1460e4f472a1d93258e0dc582f52b526ba6c74f655fe55d323c0b20c43d1b17abdcf1c2f52b74a62c198e7435644120839290f5845ff3cd2de24fbc3e8a4f3892f807a12ad4be7ef31e491d5711e83c159bc52c6f9bfc704b03d6e1e8ea7e844854f0b6423d800d7a66210b148a596e9926909fa56f1b141b71fb1d64f9d5e13c64da2a3865564a4ec477f0e4ddf44f2dd0a6b6d0feb7298603df3530ad7cf121ed5fecfb6d31fe3fe754a1097d1b5dc22d741d341d218c2cd23c8170ec36d0a3128d45b769e37a383e018ae5e260177453517d6a14c9a595be8d63f3114cc1b2eb0e6ec5bd704b3d964f5fcf6e6d4a7e4d50ddca46e825141ec38049fb57fd1e8faa534725bff05059ceeb44852a81f2b34f7ef740bf0ecfc87432e4338dc28967c4a24afef0f0468bad4c1606aaccbf5e7f1d8d6478c9c67f35f960e004cbd627699ac85b47591f093d656dbcab95bdeefd879ee64dbc6362564cd73de82f36698a132048b4421ad3d23a3a39058fb1d87d95be6cc697301ab20cf643c78ce3013124264d2ba0b67c20b87e4434441a608360d256fc8cb32f181307c513e8dba77afa0347904097ea83191263316ed4b8e45202e341bf22ba8f5b1b6c26da83da532d516a2d39966cdc1e15fa32256b5fdcf3759654e9ab7f1379eb25ac6bc1205d1acfb0f0c994c86be41c2a98115fe6070d4280f961ea64d4ecc7a7c4c43ce7fff0474a1861f025b167d835c562ede15aef5bada8b277b6e702d7f2150976370d1d40c04d590e9ecb6a1ff84e62139792a1c2af96ea9600472153a3218ac4c0b243fdb2bca2e33ac58c190147b2d16661e95b3fcb7ff519adb12efe6b48c523374af12bb9cc52ab1be242bc7a2636d6586b9b871d95781f0fb4b0";
        SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, cn.hutool.core.codec.Base64.decode("NyD+lmEfFAmBQoxAeYm2Ew=="));
        String decryptStr = aes.decryptStr(data, CharsetUtil.CHARSET_UTF_8);
        System.out.println(decryptStr);


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

    public static Secure execute(AlgorithmEnum algorithmEnum) {
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
