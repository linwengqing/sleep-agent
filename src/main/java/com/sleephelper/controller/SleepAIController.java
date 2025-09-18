package com.sleephelper.controller;

import com.sleephelper.common.Result;
import com.sleephelper.service.SleepAIService;
import com.sleephelper.service.SleepChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * 睡眠AI控制器
 * 提供AI相关的REST API接口
 * 
 * @author SleepHelper
 * @since 2024-01-01
 */
@Slf4j
@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "*")
public class SleepAIController {

    @Autowired
    private SleepAIService sleepAIService;

    @Autowired
    private SleepChatService sleepChatService;

    /**
     * 生成AI睡眠报告
     * POST /api/ai/report
     * 
     * @param request 请求参数
     * @return AI生成的睡眠报告
     */
    @PostMapping("/report")
    public Result<String> generateSleepReport(@RequestBody ReportRequest request) {
        try {
            log.info("接收到生成AI睡眠报告请求：用户ID={}，睡眠日期={}", 
                    request.getUserId(), request.getDateOfSleep());
            
            // 参数校验
            if (request.getUserId() == null || request.getUserId().trim().isEmpty()) {
                return Result.error("用户ID不能为空");
            }
            if (request.getDateOfSleep() == null) {
                return Result.error("睡眠日期不能为空");
            }
            
            // 调用AI服务生成报告
            String aiReport = sleepAIService.generateSleepReport(request.getUserId(), request.getDateOfSleep());
            
            log.info("AI睡眠报告生成成功：用户ID={}，睡眠日期={}，报告长度={}", 
                    request.getUserId(), request.getDateOfSleep(), aiReport.length());
            
            return Result.success("AI睡眠报告生成成功", aiReport);
            
        } catch (Exception e) {
            log.error("生成AI睡眠报告异常：{}", e.getMessage(), e);
            return Result.error("生成AI睡眠报告失败：" + e.getMessage());
        }
    }

    /**
     * AI多轮对话
     * POST /api/ai/chat
     * 
     * @param request 请求参数
     * @return AI回复
     */
    @PostMapping("/chat")
    public Result<String> chatWithAI(@RequestBody ChatRequest request) {
        try {
            log.info("接收到AI聊天请求：用户ID={}，消息={}", 
                    request.getUserId(), request.getMessage());
            
            // 参数校验
            if (request.getUserId() == null || request.getUserId().trim().isEmpty()) {
                return Result.error("用户ID不能为空");
            }
            if (request.getMessage() == null || request.getMessage().trim().isEmpty()) {
                return Result.error("消息内容不能为空");
            }
            
            // 调用AI聊天服务
            String aiResponse = sleepChatService.chatWithUser(request.getUserId(), request.getMessage());
            
            log.info("AI聊天回复生成成功：用户ID={}，回复长度={}", 
                    request.getUserId(), aiResponse.length());
            
            return Result.success("AI回复生成成功", aiResponse);
            
        } catch (Exception e) {
            log.error("AI聊天异常：{}", e.getMessage(), e);
            return Result.error("AI聊天失败：" + e.getMessage());
        }
    }

    /**
     * 清空对话历史
     * POST /api/ai/chat/clear
     * 
     * @param request 请求参数
     * @return 操作结果
     */
    @PostMapping("/chat/clear")
    public Result<String> clearChatHistory(@RequestBody ClearChatRequest request) {
        try {
            log.info("接收到清空对话历史请求：用户ID={}", request.getUserId());
            
            // 参数校验
            if (request.getUserId() == null || request.getUserId().trim().isEmpty()) {
                return Result.error("用户ID不能为空");
            }
            
            // 清空对话历史
            String result = sleepChatService.clearChatHistory(request.getUserId());
            
            log.info("清空对话历史成功：用户ID={}", request.getUserId());
            
            return Result.success("清空对话历史成功", result);
            
        } catch (Exception e) {
            log.error("清空对话历史异常：{}", e.getMessage(), e);
            return Result.error("清空对话历史失败：" + e.getMessage());
        }
    }

    /**
     * 获取对话统计信息
     * GET /api/ai/chat/stats?userId=xxx
     * 
     * @param userId 用户ID
     * @return 统计信息
     */
    @GetMapping("/chat/stats")
    public Result<String> getChatStatistics(@RequestParam("userId") String userId) {
        try {
            log.info("接收到获取对话统计请求：用户ID={}", userId);
            
            // 参数校验
            if (userId == null || userId.trim().isEmpty()) {
                return Result.error("用户ID不能为空");
            }
            
            // 获取统计信息
            String statistics = sleepChatService.getChatStatistics(userId);
            
            log.info("获取对话统计成功：用户ID={}", userId);
            
            return Result.success("获取对话统计成功", statistics);
            
        } catch (Exception e) {
            log.error("获取对话统计异常：{}", e.getMessage(), e);
            return Result.error("获取对话统计失败：" + e.getMessage());
        }
    }

    /**
     * 获取帮助信息
     * GET /api/ai/help
     * 
     * @return 帮助信息
     */
    @GetMapping("/help")
    public Result<String> getHelp() {
        try {
            String helpMessage = sleepChatService.getHelpMessage();
            return Result.success("获取帮助信息成功", helpMessage);
        } catch (Exception e) {
            log.error("获取帮助信息异常：{}", e.getMessage(), e);
            return Result.error("获取帮助信息失败：" + e.getMessage());
        }
    }

    /**
     * AI服务健康检查
     * GET /api/ai/health
     * 
     * @return 健康状态
     */
    @GetMapping("/health")
    public Result<Map<String, Object>> healthCheck() {
        try {
            Map<String, Object> healthInfo = new HashMap<>();
            healthInfo.put("status", "UP");
            healthInfo.put("service", "Sleep AI Service");
            healthInfo.put("timestamp", System.currentTimeMillis());
            healthInfo.put("version", "1.0.0");
            
            return Result.success("AI服务运行正常", healthInfo);
        } catch (Exception e) {
            log.error("AI服务健康检查异常：{}", e.getMessage(), e);
            return Result.error("AI服务健康检查失败：" + e.getMessage());
        }
    }

    /**
     * 睡眠报告生成请求参数
     */
    public static class ReportRequest {
        private String userId;
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDate dateOfSleep;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public LocalDate getDateOfSleep() {
            return dateOfSleep;
        }

        public void setDateOfSleep(LocalDate dateOfSleep) {
            this.dateOfSleep = dateOfSleep;
        }
    }

    /**
     * AI聊天请求参数
     */
    public static class ChatRequest {
        private String userId;
        private String message;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    /**
     * 清空对话历史请求参数
     */
    public static class ClearChatRequest {
        private String userId;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }
}
