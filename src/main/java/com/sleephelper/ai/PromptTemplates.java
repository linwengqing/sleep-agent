package com.sleephelper.ai;

/**
 * AI Prompt 模板管理类
 * 定义各种场景下的 Prompt 模板
 * 
 * @author SleepHelper
 * @since 2024-01-01
 */
public class PromptTemplates {

    /**
     * 睡眠报告生成 Prompt 模板
     */
    public static final String SLEEP_REPORT_TEMPLATE = """
        基于用户睡眠数据生成专业的睡眠分析报告。
        
        用户睡眠数据：
        - 睡眠总分：{{sleepScore}}分
        - 深睡时长：{{deepSleep}}分钟
        - 总睡眠时长：{{totalDuration}}分钟
        - 睡眠日期：{{date}}
        
        请生成包含以下内容的自然语言报告：
        1. 【优势】- 分析睡眠数据中的积极方面
        2. 【不足】- 指出需要改进的地方
        3. 【建议】- 提供3条具体可执行的改善建议
        
        要求：
        - 语言通俗易懂，避免专业术语
        - 建议要具体可操作
        - 语气温和友善
        - 字数控制在300字以内
        """;

    /**
     * 多轮对话 Prompt 模板
     */
    public static final String CHAT_TEMPLATE = """
        你是专业的睡眠健康助手，请根据用户的问题提供专业、友好的回答。
        
        用户当前问题：{{userMessage}}
        
        历史对话记录：
        {{history}}
        
        回答要求：
        - 基于睡眠健康专业知识
        - 语言通俗易懂
        - 提供具体可行的建议
        - 语气温和友善
        - 如果问题与睡眠无关，礼貌地引导到睡眠健康话题
        - 回答长度控制在200字以内
        """;

    /**
     * 获取睡眠报告 Prompt
     * 
     * @param sleepScore 睡眠评分
     * @param deepSleep 深睡时长（分钟）
     * @param totalDuration 总睡眠时长（分钟）
     * @param date 睡眠日期
     * @return 格式化后的 Prompt
     */
    public static String getSleepReportPrompt(int sleepScore, int deepSleep, int totalDuration, String date) {
        return SLEEP_REPORT_TEMPLATE
                .replace("{{sleepScore}}", String.valueOf(sleepScore))
                .replace("{{deepSleep}}", String.valueOf(deepSleep))
                .replace("{{totalDuration}}", String.valueOf(totalDuration))
                .replace("{{date}}", date);
    }

    /**
     * 获取对话 Prompt
     * 
     * @param userMessage 用户消息
     * @param history 历史对话记录
     * @return 格式化后的 Prompt
     */
    public static String getChatPrompt(String userMessage, String history) {
        return CHAT_TEMPLATE
                .replace("{{userMessage}}", userMessage)
                .replace("{{history}}", history.isEmpty() ? "无历史对话" : history);
    }

    /**
     * 格式化历史对话记录
     * 
     * @param conversations 对话记录数组
     * @return 格式化后的历史记录字符串
     */
    public static String formatHistory(String[] conversations) {
        if (conversations == null || conversations.length == 0) {
            return "";
        }
        
        StringBuilder history = new StringBuilder();
        for (int i = 0; i < conversations.length; i++) {
            history.append("对话").append(i + 1).append(": ").append(conversations[i]).append("\n");
        }
        return history.toString();
    }
}
