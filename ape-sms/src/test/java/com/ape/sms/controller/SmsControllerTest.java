package com.ape.sms.controller;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/5/24
 */
@Slf4j
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
public class SmsControllerTest{

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("短信发送接口测试")
    public void send() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/sms/send/4231134134")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        // 设置编码，防止中文乱码
        mvcResult.getResponse().setCharacterEncoding("UTF-8");
        String data = mvcResult.getResponse().getContentAsString();
        Map map = JSONUtil.toBean(data, Map.class);
        Assertions.assertFalse(((Boolean) map.get("success")));
    }
}
