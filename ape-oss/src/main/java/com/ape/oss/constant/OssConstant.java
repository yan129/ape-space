package com.ape.oss.constant;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/11/25
 */
@Component
public class OssConstant implements InitializingBean {

    @Value("${aliyun.oss.file.endpoint}")
    private String endpoint;

    @Value("${aliyun.oss.file.bucketName}")
    private String bucketName;

    @Value("${aliyun.oss.file.accesskeyId}")
    private String accesskeyId;

    @Value("${aliyun.oss.file.accesskeySecret}")
    private String accesskeySecret;

    public static String END_POINT;
    public static String BUCKET_NAME;
    public static String ACCESSKEY_ID;
    public static String ACCESSKEY_SECRET;

    @Override
    public void afterPropertiesSet() throws Exception {
        END_POINT = this.endpoint;
        BUCKET_NAME = this.bucketName;
        ACCESSKEY_ID = this.accesskeyId;
        ACCESSKEY_SECRET = this.accesskeySecret;
    }
}
