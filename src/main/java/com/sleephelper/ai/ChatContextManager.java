package com.sleephelper.ai;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 对话上下文管理器
 * 使用 ConcurrentHashMap 按用户ID存储历史对话记录
 * 
 * @author SleepHelper
 * @since 2024-01-01
 */
@Slf4j
@Component
public class ChatContextManager {

    /**
     * 用户对话历史存储
     * Key: 用户ID
     * Value: 对话记录队列（最近10条）
     */
    private final ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> userConversations = new ConcurrentHashMap<>();

    /**
     * 最大历史记录数量
     */
    private static final int MAX_HISTORY_SIZE = 10;

    /**
     * 添加用户对话记录
     * 
     * @param userId 用户ID
     * @param userMessage 用户消息
     * @param aiResponse AI回复
     */
    public void addConversation(String userId, String userMessage, String aiResponse) {
        try {
            ConcurrentLinkedQueue<String> conversations = userConversations.computeIfAbsent(userId, 
                k -> new ConcurrentLinkedQueue<>());
            
            // 添加对话记录
            conversations.offer("用户: " + userMessage);
            conversations.offer("助手: " + aiResponse);
            
            // 保持最大记录数量
            while (conversations.size() > MAX_HISTORY_SIZE) {
                conversations.poll();
            }
            
            log.debug("用户 {} 添加对话记录，当前历史记录数: {}", userId, conversations.size());
        } catch (Exception e) {
            log.error("添加对话记录失败，用户ID: {}, 错误: {}", userId, e.getMessage(), e);
        }
    }

    /**
     * 获取用户历史对话记录
     * 
     * @param userId 用户ID
     * @return 历史对话记录数组
     */
    public String[] getConversationHistory(String userId) {
        try {
            ConcurrentLinkedQueue<String> conversations = userConversations.get(userId);
            if (conversations == null || conversations.isEmpty()) {
                return new String[0];
            }
            
            return conversations.toArray(new String[0]);
        } catch (Exception e) {
            log.error("获取对话历史失败，用户ID: {}, 错误: {}", userId, e.getMessage(), e);
            return new String[0];
        }
    }

    /**
     * 获取格式化的历史对话记录
     * 
     * @param userId 用户ID
     * @return 格式化后的历史记录字符串
     */
    public String getFormattedHistory(String userId) {
        String[] history = getConversationHistory(userId);
        return PromptTemplates.formatHistory(history);
    }

    /**
     * 清空用户对话历史
     * 
     * @param userId 用户ID
     */
    public void clearConversationHistory(String userId) {
        try {
            userConversations.remove(userId);
            log.info("清空用户 {} 的对话历史", userId);
        } catch (Exception e) {
            log.error("清空对话历史失败，用户ID: {}, 错误: {}", userId, e.getMessage(), e);
        }
    }

    /**
     * 获取所有活跃用户数量
     * 
     * @return 活跃用户数量
     */
    public int getActiveUserCount() {
        return userConversations.size();
    }

    /**
     * 获取用户对话记录数量
     * 
     * @param userId 用户ID
     * @return 对话记录数量
     */
    public int getUserConversationCount(String userId) {
        ConcurrentLinkedQueue<String> conversations = userConversations.get(userId);
        return conversations != null ? conversations.size() : 0;
    }

    /**
     * 清理过期对话记录（可选功能）
     * 在实际应用中，可以添加时间戳来清理过期的对话记录
     */
    public void cleanupExpiredConversations() {
        // 这里可以实现基于时间的清理逻辑
        // 例如：清理超过24小时的对话记录
        log.debug("清理过期对话记录，当前活跃用户数: {}", getActiveUserCount());
    }
}
