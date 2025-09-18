package com.sleephelper.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sleephelper.entity.SleepAnalysisSummary;
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
 * 睡眠分析汇总控制器测试类
 * 
 * @author SleepHelper
 * @since 2024-01-01
 */
@SpringBootTest
@AutoConfigureWebMvc
public class SleepAnalysisSummaryControllerTest {

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
        mockMvc.perform(get("/api/sleep/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("睡眠数据管理服务运行正常"));
    }

    /**
     * 测试初始化测试数据接口
     */
    @Test
    public void testInitTestData() throws Exception {
        setup();
        mockMvc.perform(post("/api/sleep/data/init"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("初始化测试数据成功，已插入5条模拟数据"));
    }

    /**
     * 测试新增睡眠数据接口
     */
    @Test
    public void testAddSleepData() throws Exception {
        setup();
        
        SleepAnalysisSummary sleepData = new SleepAnalysisSummary();
        sleepData.setUserId("test001");
        sleepData.setDateOfSleep(LocalDate.now());
        sleepData.setTotalSleepDuration(480);
        sleepData.setDeepSleepDuration(90);
        sleepData.setRemSleepDuration(120);
        sleepData.setSleepScore(85);

        mockMvc.perform(post("/api/sleep/data")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sleepData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("新增睡眠数据成功"));
    }

    /**
     * 测试查询睡眠数据接口
     */
    @Test
    public void testGetSleepData() throws Exception {
        setup();
        
        String userId = "test001";
        String dateOfSleep = LocalDate.now().toString();

        mockMvc.perform(get("/api/sleep/data")
                .param("userId", userId)
                .param("dateOfSleep", dateOfSleep))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }
}
