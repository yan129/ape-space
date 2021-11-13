package com.ape.user.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ape.user.model.RoleDO;
import com.ape.user.vo.LoginVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/6/8
 */
@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
//@WebAppConfiguration
@ActiveProfiles("test")
public class RoleControllerTest {

    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @DisplayName("添加角色测试")
    // 模拟用户登录
    @WithMockUser
    public void test_saveRole() throws Exception {
//        RoleDO roleDO = new RoleDO();
//        roleDO.setRoleName("ROLE_NORMAL");
//        roleDO.setRoleDescription("普通用户角色");
//
//        JSONObject jsonData = JSONUtil.parseObj(roleDO);
//        MvcResult mvcResult = mockMvc.perform(post("/role/save")
//                .accept(MediaType.APPLICATION_JSON)
//                // 传入CSRF
//                .with(SecurityMockMvcRequestPostProcessors.csrf().asHeader())
//                //传参,因为后端是@RequestBody所以这里直接传json字符串
//                .content(jsonData.toString())
//                .characterEncoding("UTF-8")
//                // 请求type : json
//                .contentType(MediaType.APPLICATION_JSON))
//                // 期望的结果状态 200
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn();
//        mvcResult.getResponse().setCharacterEncoding("UTF-8");
//
//        String data = mvcResult.getResponse().getContentAsString();
//        Map map =  JSONUtil.toBean(data, Map.class);
//        Assertions.assertTrue((Boolean) map.get("success"));
    }

}
