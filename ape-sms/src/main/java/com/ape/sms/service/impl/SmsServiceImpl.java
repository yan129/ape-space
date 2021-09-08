package com.ape.sms.service.impl;

import cn.hutool.json.JSONUtil;
import com.ape.common.utils.CommonUtil;
import com.ape.common.utils.StringUtils;
import com.ape.sms.constant.SmsConstant;
import com.ape.sms.service.SmsService;
import com.zhenzi.sms.ZhenziSmsClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/5/23
 */
@Slf4j
@Service
public class SmsServiceImpl implements SmsService {

    @Autowired
    private StringRedisTemplate template;

    @Value("${zhenzi.sms.apiUri}")
    private String apiUrl;

    @Value("${zhenzi.sms.appId}")
    private String appId;

    @Value("${zhenzi.sms.appSecret}")
    private String appSecret;

    @Value("${zhenzi.sms.expire}")
    private int expire;

    @Value("${zhenzi.sms.templateId}")
    private String templateId;

    @Override
    public boolean send(String telephone) {
        telephone = telephone.trim();
        if (!CommonUtil.telephoneRegex(telephone)) {
            log.error("手机号码格式错误");
            return false;
        }
        Map<String, Object> params = buildSendParams(telephone);
        return processSending(params);
    }

    private Map<String, Object> buildSendParams(String telephone) {
        String code = String.valueOf(new Random().nextInt(899999) + 100000);
        // 给验证码设置当前系统时间
        code = StringUtils.joinWith("_", code, System.currentTimeMillis());
        String[] templateParams = {code, SmsConstant.EXPIRE};

        Map<String, Object> params = new HashMap<>(16);
        params.put("number", telephone);
        params.put("templateId", templateId);
        params.put("templateParams", templateParams);
        return params;
    }

    private boolean processSending(Map<String, Object> params) {
        String telephone = (String) params.get("number");
        String code = ((String[]) params.get("templateParams"))[0];

        try {
            ZhenziSmsClient client = new ZhenziSmsClient(apiUrl, appId, appSecret);
            Map balanceMap = JSONUtil.toBean(client.balance(), Map.class);

            int balance = (int) balanceMap.get("data");

            if (balance == 50){
                log.error("短信剩余数量：{}条", balance);
                // 通知开发人员
                return false;
            }

            String sendResult = client.send(params);
            Map resultMap = JSONUtil.toBean(sendResult, Map.class);
            int statusCode = (int) resultMap.get("code");
            log.info("data:{}", resultMap.get("data"));
            //发送短信code: 发送状态，0为成功。非0为发送失败
            if (statusCode == 0){
                template.opsForValue().set(SmsConstant.PREFIX + telephone, code, expire, TimeUnit.SECONDS);
                return true;
            }

            log.error("fail：短信服务异常");
            return false;
        }catch (Exception e){
            log.error(e.getMessage());
            return false;
        }
    }
}
