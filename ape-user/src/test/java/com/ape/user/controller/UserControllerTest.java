package com.ape.user.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
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
 * @date: 2021/6/10
 */
@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@WebAppConfiguration
@ActiveProfiles("test")
public class UserControllerTest {

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
    @DisplayName("用户注册")
    public void test_register() throws Exception {
        LoginVO loginVO = new LoginVO();
        loginVO.setUsername("17807641681");
        loginVO.setPassword("1234567");

        JSONObject jsonData = JSONUtil.parseObj(loginVO);
        MvcResult mvcResult = postRequest("/user/register", jsonData);

        String data = mvcResult.getResponse().getContentAsString();
        Map map =  JSONUtil.toBean(data, Map.class);
        Assertions.assertTrue((Boolean) map.get("success"));
    }

    private MvcResult postRequest(String url, JSONObject jsonData) throws Exception {
        MvcResult mvcResult = mockMvc.perform(post(url)
                .with(SecurityMockMvcRequestPostProcessors.csrf().asHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonData.toString())
                .characterEncoding("UTF-8"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        mvcResult.getResponse().setCharacterEncoding("UTF-8");
        return mvcResult;
    }

    @Test
    @WithMockUser(username = "17807641681", roles = {"ADMIN"})
    public void test() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/user/aa"))
                .andDo(MockMvcResultHandlers.print()).andReturn();
        mvcResult.getResponse().setCharacterEncoding("UTF-8");

        String data = mvcResult.getResponse().getContentAsString();
        Map map = JSONUtil.toBean(data, Map.class);
        Assertions.assertTrue((Boolean) map.get("success"));
    }

}
