package com.ape.article.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/5/26
 * 注意：@ActiveProfiles("test") -> 这里使用了test测试文件，排除druid中web-stat-filter的影响
 */
@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class FavoritesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("分页查看用户文章收藏测试")
    public void searchAllFavoritesByUid() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/favorites/search/{uid}", "2")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("pageNum", "1")
                .param("pageSize", "30"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        mvcResult.getResponse().setCharacterEncoding("UTF-8");
        String data = mvcResult.getResponse().getContentAsString();
        log.info(data + "==============================");
    }
}
