package com.ape.common;

import com.ape.common.utils.RsaUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/7/3
 */
@Slf4j
public class RsaUtilTest {

    @Test
    @DisplayName("公钥数据加密测试")
    public void test_encryptData() throws Exception {
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC5hLd9FryBcXdlKKGICd3/axQ85V5QmB/0P7a5KhZr0vJaGX+7YRJt4NYpH1+pEob0TkFaFXzYZSZIZa3R63tS1pWpvKSWdSEy1Spb9qBS1FMp0j8vhQN1ydFv1Fh3Ds6vqBoGYyvqmkRLworLDUiRWuEQqxNcsNjx2HMJnhpdxwIDAQAB";
        String encryptData = RsaUtil.encryptData(publicKey, "ape-space");
        System.out.println(encryptData);
    }

    @Test
    @DisplayName("私钥数据解密测试")
    public void test_descryptData() throws Exception {
        String privateKey = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBALmEt30WvIFxd2UooYgJ3f9rFDzlXlCYH/Q/trkqFmvS8loZf7thEm3g1ikfX6kShvROQVoVfNhlJkhlrdHre1LWlam8pJZ1ITLVKlv2oFLUUynSPy+FA3XJ0W/UWHcOzq+oGgZjK+qaREvCissNSJFa4RCrE1yw2PHYcwmeGl3HAgMBAAECgYEAgcFvzQ/v/OFtztUiVdIA8brlRspusxQTlXRSyyPC1tuOIrKfAmIcz7loUQ7ei5Sny4xIbUeGMJxesFhdwOthLxdcM54/lLW5ZCukUdUeLhjec1zTwCYZ9P1Ihrt63HOFRIVymak3f+eIWi4vmlNghSxyUGmXz1TYj/hI8wzWeZECQQDt7O5o+tZDGNPTzb4ppSyeZcRClcaLnvenig7Jyi3EOfv/gydANcUex5Fr8AnCkzAPIF0R3jU1vBbc0KKfnX1dAkEAx5yYjYQMJIFrJB6PYjhlwsOQi+cKFnphHnvaxPWP+LVAEduxjcKonvMmwqDq+6163omeXYEDn02pqeZoWMlxcwJAIpy7Oi5ziSNNfZyKs4hB63EmkgEz9w/TO15MNHLjIY7F6C/uP9sSqB2kPC2ZXeMHtMuifnzzBLQuJ0V6wvmoSQJBAKtfzLGi7vHgkuXdvuhq1yMR1+XlJAoMY5lSaI607ThwFGPApH265B4jT+HFWjldxaGNsYNBoqSAfuu5P1kLCfUCQQCJOv9FKXh5uWNO+Rcg4l7xwpnfCWtZKqrEWDy0cCfllfUPs0t8uRNZPKuroVGrZKJuvSGvQwM5g2pTAwRuctpM";
        String encryptData = "kNiXFpZ1G82IuvVj156P8Y93W5JprW6nzxq8vLbvcHGWvmE4wJhBE0ymvsZn4h2YVMacLwKcWvpXJDjCbwCUAR0uqghnZkezkU+SdLC3ShJXuf+swkaqlpuk1EfdOI24FQ0mWFtv+XVtetWvTQQ/bRs8OEy3PELsVnznar+YzRk=";
        String descryptData = RsaUtil.descryptData(privateKey, encryptData);
        System.out.println(descryptData);
    }
}
