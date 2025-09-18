package com.sleephelper.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sleephelper.entity.SleepPoints;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 睡眠积分控制器测试类
 * 
 * @author SleepHelper
 * @since 2024-01-01
 */
@SpringBootTest
@AutoConfigureWebMvc
public class SleepPointsControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 初始化MockMvc
     */
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    /**
     * 测试健康检查接口
     */
    @Test
    public void testHealth() throws Exception {
        setup();
        mockMvc.perform(get("/api/points/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("睡眠积分管理服务运行正常"));
    }

    /**
     * 测试生成睡眠积分接口
     */
    @Test
    public void testGenerateSleepPoints() throws Exception {
        setup();
        
        String userId = "user001";
        String dateOfSleep = LocalDate.now().toString();

        mockMvc.perform(post("/api/points/generate")
                .param("userId", userId)
                .param("dateOfSleep", dateOfSleep))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("生成睡眠积分成功"));
    }

    /**
     * 测试查询积分历史接口
     */
    @Test
    public void testGetSleepPointsHistory() throws Exception {
        setup();
        
        String userId = "user001";

        mockMvc.perform(get("/api/points/history")
                .param("userId", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("查询积分历史成功"));
    }

    /**
     * 测试查询当前总积分接口
     */
    @Test
    public void testGetCurrentTotalPoints() throws Exception {
        setup();
        
        String userId = "user001";

        mockMvc.perform(get("/api/points/current")
                .param("userId", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("查询当前总积分成功"));
    }

    /**
     * 测试查询积分记录接口
     */
    @Test
    public void testGetSleepPointsRecord() throws Exception {
        setup();
        
        String userId = "user001";
        String date = LocalDate.now().toString();

        mockMvc.perform(get("/api/points/record")
                .param("userId", userId)
                .param("date", date))
                .andExpect(status().isOk());
    }
}
