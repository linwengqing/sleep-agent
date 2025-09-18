package com.sleephelper.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sleephelper.service.SleepAIService;
import com.sleephelper.service.SleepChatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 睡眠AI控制器测试类
 * 
 * @author SleepHelper
 * @since 2024-01-01
 */
@SpringBootTest
@AutoConfigureWebMvc
public class SleepAIControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private SleepAIService sleepAIService;

    @MockBean
    private SleepChatService sleepChatService;

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
     * 测试生成AI睡眠报告接口
     */
    @Test
    public void testGenerateSleepReport() throws Exception {
        setup();
        
        // Mock AI服务返回
        String mockReport = "【睡眠分析报告】\n\n【优势】\n睡眠质量良好\n\n【不足】\n需要改善\n\n【建议】\n1. 保持规律作息\n2. 改善睡眠环境\n3. 睡前放松";
        when(sleepAIService.generateSleepReport(anyString(), any())).thenReturn(mockReport);

        // 构建请求
        String requestJson = """
            {
                "userId": "user001",
                "dateOfSleep": "2024-01-01"
            }
            """;

        mockMvc.perform(post("/api/ai/report")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("AI睡眠报告生成成功"))
                .andExpect(jsonPath("$.data").value(mockReport));
    }

    /**
     * 测试AI聊天接口
     */
    @Test
    public void testChatWithAI() throws Exception {
        setup();
        
        // Mock AI聊天服务返回
        String mockResponse = "根据您的睡眠问题，我建议您保持规律的作息时间。";
        when(sleepChatService.chatWithUser(anyString(), anyString())).thenReturn(mockResponse);

        // 构建请求
        String requestJson = """
            {
                "userId": "user001",
                "message": "我最近总是失眠，怎么办？"
            }
            """;

        mockMvc.perform(post("/api/ai/chat")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("AI回复生成成功"))
                .andExpect(jsonPath("$.data").value(mockResponse));
    }

    /**
     * 测试清空对话历史接口
     */
    @Test
    public void testClearChatHistory() throws Exception {
        setup();
        
        // Mock 清空对话历史服务返回
        String mockResult = "对话历史已清空，我们可以重新开始聊天。";
        when(sleepChatService.clearChatHistory(anyString())).thenReturn(mockResult);

        // 构建请求
        String requestJson = """
            {
                "userId": "user001"
            }
            """;

        mockMvc.perform(post("/api/ai/chat/clear")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("清空对话历史成功"))
                .andExpect(jsonPath("$.data").value(mockResult));
    }

    /**
     * 测试获取对话统计接口
     */
    @Test
    public void testGetChatStatistics() throws Exception {
        setup();
        
        // Mock 对话统计服务返回
        String mockStats = "您已进行 5 轮对话，当前系统共有 10 个活跃用户。";
        when(sleepChatService.getChatStatistics(anyString())).thenReturn(mockStats);

        mockMvc.perform(get("/api/ai/chat/stats")
                .param("userId", "user001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("获取对话统计成功"))
                .andExpect(jsonPath("$.data").value(mockStats));
    }

    /**
     * 测试获取帮助信息接口
     */
    @Test
    public void testGetHelp() throws Exception {
        setup();
        
        // Mock 帮助信息服务返回
        String mockHelp = "我是您的睡眠健康助手，可以为您提供睡眠相关的专业建议。";
        when(sleepChatService.getHelpMessage()).thenReturn(mockHelp);

        mockMvc.perform(get("/api/ai/help"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("获取帮助信息成功"))
                .andExpect(jsonPath("$.data").value(mockHelp));
    }

    /**
     * 测试AI服务健康检查接口
     */
    @Test
    public void testHealthCheck() throws Exception {
        setup();

        mockMvc.perform(get("/api/ai/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("AI服务运行正常"))
                .andExpect(jsonPath("$.data.status").value("UP"))
                .andExpect(jsonPath("$.data.service").value("Sleep AI Service"));
    }

    /**
     * 测试参数验证 - 用户ID为空
     */
    @Test
    public void testGenerateSleepReportWithEmptyUserId() throws Exception {
        setup();

        String requestJson = """
            {
                "userId": "",
                "dateOfSleep": "2024-01-01"
            }
            """;

        mockMvc.perform(post("/api/ai/report")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.message").value("用户ID不能为空"));
    }

    /**
     * 测试参数验证 - 消息为空
     */
    @Test
    public void testChatWithEmptyMessage() throws Exception {
        setup();

        String requestJson = """
            {
                "userId": "user001",
                "message": ""
            }
            """;

        mockMvc.perform(post("/api/ai/chat")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.message").value("消息内容不能为空"));
    }
}
