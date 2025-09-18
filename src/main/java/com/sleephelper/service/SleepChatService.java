package com.sleephelper.service;

import com.sleephelper.ai.ChatContextManager;
import com.sleephelper.ai.PromptTemplates;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 睡眠AI聊天服务类
 * 实现基于阿里云百炼大模型的多轮对话功能
 * 
 * @author SleepHelper
 * @since 2024-01-01
 */
@Slf4j
@Service
public class SleepChatService {

    @Autowired
    private BailianService bailianService;

    @Autowired
    private ChatContextManager chatContextManager;

    /**
     * 处理用户聊天消息
     * 
     * @param userId 用户ID
     * @param userMessage 用户消息
     * @return AI回复
     */
    public String chatWithUser(String userId, String userMessage) {
        try {
            log.info("处理用户聊天消息，用户ID: {}, 消息: {}", userId, userMessage);
            
            // 1. 参数验证
            if (userId == null || userId.trim().isEmpty()) {
                log.warn("用户ID为空");
                return "抱歉，无法识别您的身份，请重新登录。";
            }
            
            if (userMessage == null || userMessage.trim().isEmpty()) {
                log.warn("用户消息为空");
                return "您好！我是您的睡眠健康助手，有什么睡眠问题可以咨询我。";
            }
            
            // 2. 获取历史对话记录
            String history = chatContextManager.getFormattedHistory(userId);
            log.debug("用户 {} 历史对话记录: {}", userId, history);
            
            // 3. 构建Prompt
            String prompt = PromptTemplates.getChatPrompt(userMessage.trim(), history);
            log.debug("聊天Prompt: {}", prompt);
            
            // 4. 调用阿里云百炼生成回复
            String aiResponse = bailianService.generateText(prompt);
            
            // 5. 保存对话记录
            chatContextManager.addConversation(userId, userMessage.trim(), aiResponse);
            
            log.info("AI聊天回复生成成功，用户ID: {}, 回复长度: {} 字符", userId, aiResponse.length());
            
            return aiResponse;
            
        } catch (Exception e) {
            log.error("处理用户聊天消息失败，用户ID: {}, 消息: {}, 错误: {}", 
                    userId, userMessage, e.getMessage(), e);
            return generateErrorResponse();
        }
    }

    /**
     * 清空用户对话历史
     * 
     * @param userId 用户ID
     * @return 操作结果
     */
    public String clearChatHistory(String userId) {
        try {
            chatContextManager.clearConversationHistory(userId);
            log.info("清空用户对话历史成功，用户ID: {}", userId);
            return "对话历史已清空，我们可以重新开始聊天。";
        } catch (Exception e) {
            log.error("清空对话历史失败，用户ID: {}, 错误: {}", userId, e.getMessage(), e);
            return "清空对话历史失败，请稍后重试。";
        }
    }

    /**
     * 获取用户对话统计信息
     * 
     * @param userId 用户ID
     * @return 统计信息
     */
    public String getChatStatistics(String userId) {
        try {
            int conversationCount = chatContextManager.getUserConversationCount(userId);
            int activeUserCount = chatContextManager.getActiveUserCount();
            
            return String.format("您已进行 %d 轮对话，当前系统共有 %d 个活跃用户。", 
                    conversationCount / 2, activeUserCount);
        } catch (Exception e) {
            log.error("获取对话统计失败，用户ID: {}, 错误: {}", userId, e.getMessage(), e);
            return "无法获取对话统计信息。";
        }
    }

    /**
     * 生成错误回复
     * 
     * @return 错误回复内容
     */
    private String generateErrorResponse() {
        return """
            抱歉，我暂时无法回复您的消息。可能是由于网络问题或系统繁忙。
            
            请稍后重试，或者您可以：
            1. 检查网络连接
            2. 重新发送消息
            3. 联系技术支持
            
            我会尽快恢复正常服务。
            """;
    }

    /**
     * 验证用户消息
     * 
     * @param userMessage 用户消息
     * @return 是否有效
     */
    private boolean validateUserMessage(String userMessage) {
        if (userMessage == null || userMessage.trim().isEmpty()) {
            return false;
        }
        
        // 检查消息长度
        if (userMessage.length() > 500) {
            log.warn("用户消息过长: {} 字符", userMessage.length());
            return false;
        }
        
        // 检查是否包含敏感词（简单示例）
        String[] sensitiveWords = {"暴力", "色情", "政治"};
        String lowerMessage = userMessage.toLowerCase();
        for (String word : sensitiveWords) {
            if (lowerMessage.contains(word)) {
                log.warn("用户消息包含敏感词: {}", word);
                return false;
            }
        }
        
        return true;
    }

    /**
     * 处理特殊指令
     * 
     * @param userId 用户ID
     * @param userMessage 用户消息
     * @return 是否处理了特殊指令
     */
    private boolean handleSpecialCommands(String userId, String userMessage) {
        String lowerMessage = userMessage.toLowerCase().trim();
        
        switch (lowerMessage) {
            case "/clear":
            case "/清空":
            case "清空对话":
                return true;
            case "/stats":
            case "/统计":
            case "对话统计":
                return true;
            case "/help":
            case "/帮助":
            case "帮助":
                return true;
            default:
                return false;
        }
    }

    /**
     * 生成模拟回复
     * 
     * @param userMessage 用户消息
     * @return 模拟回复
     */
    private String generateMockResponse(String userMessage) {
        String lowerMessage = userMessage.toLowerCase();
        
        if (lowerMessage.contains("失眠") || lowerMessage.contains("睡不着")) {
            return "失眠是很常见的问题，建议您：\n1. 建立规律的作息时间\n2. 睡前1小时避免使用电子设备\n3. 创造舒适的睡眠环境\n4. 如果持续失眠，建议咨询专业医生";
        } else if (lowerMessage.contains("睡眠质量") || lowerMessage.contains("睡眠不好")) {
            return "改善睡眠质量的方法：\n1. 保持规律作息，每天固定时间上床和起床\n2. 睡前避免刺激性活动\n3. 保持卧室安静、黑暗、凉爽\n4. 适当运动，但避免睡前3小时运动";
        } else if (lowerMessage.contains("深睡") || lowerMessage.contains("深度睡眠")) {
            return "提高深睡质量的方法：\n1. 保持规律的作息时间\n2. 睡前避免咖啡因和酒精\n3. 保持卧室温度适宜（18-22度）\n4. 睡前进行放松活动，如冥想或深呼吸";
        } else if (lowerMessage.contains("帮助") || lowerMessage.contains("功能")) {
            return getHelpMessage();
        } else {
            return "感谢您的咨询！我是您的睡眠健康助手，可以为您提供睡眠相关的专业建议。如果您有具体的睡眠问题，请详细描述，我会尽力帮助您。";
        }
    }

    /**
     * 生成帮助信息
     * 
     * @return 帮助信息
     */
    public String getHelpMessage() {
        return """
            我是您的睡眠健康助手，可以为您提供以下帮助：
            
            📊 睡眠数据分析
            💡 睡眠改善建议
            🛏️ 睡眠环境优化
            😴 睡眠习惯指导
            🏥 睡眠问题咨询
            
            特殊指令：
            - 输入"清空对话"可以清空聊天历史
            - 输入"对话统计"可以查看统计信息
            - 输入"帮助"可以查看此帮助信息
            
            有什么睡眠问题随时问我吧！
            """;
    }
}
