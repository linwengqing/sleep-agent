package com.sleephelper.service;

import com.sleephelper.ai.PromptTemplates;
import com.sleephelper.entity.SleepAnalysisSummary;
import com.sleephelper.service.SleepAnalysisSummaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 睡眠AI服务类
 * 实现基于阿里云百炼大模型的睡眠报告生成功能
 * 
 * @author SleepHelper
 * @since 2024-01-01
 */
@Slf4j
@Service
public class SleepAIService {

    @Autowired
    private BailianService bailianService;

    @Autowired
    private SleepAnalysisSummaryService sleepAnalysisSummaryService;

    /**
     * 生成AI睡眠报告
     * 
     * @param userId 用户ID
     * @param dateOfSleep 睡眠日期
     * @return AI生成的睡眠报告
     */
    public String generateSleepReport(String userId, LocalDate dateOfSleep) {
        try {
            log.info("开始生成AI睡眠报告，用户ID: {}, 睡眠日期: {}", userId, dateOfSleep);
            
            // 1. 获取用户睡眠数据
            SleepAnalysisSummary sleepData = sleepAnalysisSummaryService.getSleepDataByUserIdAndDate(userId, dateOfSleep);
            if (sleepData == null) {
                log.warn("未找到睡眠数据，用户ID: {}, 睡眠日期: {}", userId, dateOfSleep);
                return generateDefaultReport();
            }
            
            // 2. 构建Prompt
            String prompt = PromptTemplates.getSleepReportPrompt(
                sleepData.getSleepScore(),
                sleepData.getDeepSleepDuration(),
                sleepData.getTotalSleepDuration(),
                dateOfSleep.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日"))
            );
            
            log.debug("生成睡眠报告Prompt: {}", prompt);
            
            // 3. 调用阿里云百炼生成报告
            String aiReport = bailianService.generateText(prompt);
            
            log.info("AI睡眠报告生成成功，用户ID: {}, 报告长度: {} 字符", userId, aiReport.length());
            
            return aiReport;
            
        } catch (Exception e) {
            log.error("生成AI睡眠报告失败，用户ID: {}, 睡眠日期: {}, 错误: {}", 
                    userId, dateOfSleep, e.getMessage(), e);
            return generateErrorReport();
        }
    }

    /**
     * 生成默认报告（当没有睡眠数据时）
     * 
     * @return 默认报告内容
     */
    private String generateDefaultReport() {
        return """
            【睡眠分析报告】
            
            【优势】
            感谢您使用睡眠健康助手！虽然暂无睡眠数据，但您已经开始关注睡眠健康，这是很好的开始。
            
            【不足】
            需要记录睡眠数据才能进行详细分析。建议您：
            1. 使用睡眠监测设备记录数据
            2. 手动记录睡眠时间和质量
            3. 定期查看睡眠分析报告
            
            【建议】
            1. 建立规律的睡眠时间表，每天固定时间上床和起床
            2. 创造舒适的睡眠环境，保持卧室温度适宜、光线昏暗
            3. 睡前1小时避免使用电子设备，可以阅读或听轻音乐放松
            """;
    }

    /**
     * 生成错误报告（当AI调用失败时）
     * 
     * @return 错误报告内容
     */
    private String generateErrorReport() {
        return """
            【睡眠分析报告】
            
            【优势】
            您正在积极关注睡眠健康，这是维护身心健康的重要一步。
            
            【不足】
            当前无法生成详细的AI分析报告，可能是由于网络或系统问题。
            
            【建议】
            1. 保持规律的作息时间，每天7-9小时睡眠
            2. 睡前避免刺激性活动，创造安静舒适的睡眠环境
            3. 如有持续睡眠问题，建议咨询专业医生
            
            请稍后重试或联系技术支持。
            """;
    }

    /**
     * 生成模拟报告
     * 
     * @param sleepData 睡眠数据
     * @return 模拟报告
     */
    private String generateMockReport(SleepAnalysisSummary sleepData) {
        int score = sleepData.getSleepScore();
        int deepSleep = sleepData.getDeepSleepDuration();
        int totalDuration = sleepData.getTotalSleepDuration();
        
        StringBuilder report = new StringBuilder();
        report.append("【睡眠分析报告】\n\n");
        
        // 优势分析
        report.append("【优势】\n");
        if (score >= 80) {
            report.append("您的睡眠质量整体良好，睡眠评分达到").append(score).append("分，表现优秀。");
        } else if (score >= 70) {
            report.append("您的睡眠质量基本良好，睡眠评分").append(score).append("分，还有提升空间。");
        } else {
            report.append("您的睡眠质量需要改善，当前评分").append(score).append("分。");
        }
        
        if (deepSleep >= 90) {
            report.append("深睡时长").append(deepSleep).append("分钟符合健康标准。");
        }
        report.append("\n\n");
        
        // 不足分析
        report.append("【不足】\n");
        if (totalDuration > 480) {
            report.append("总睡眠时长").append(totalDuration).append("分钟略长，可能影响睡眠效率。");
        } else if (totalDuration < 360) {
            report.append("总睡眠时长").append(totalDuration).append("分钟偏短，建议增加睡眠时间。");
        }
        
        if (deepSleep < 60) {
            report.append("深睡时长").append(deepSleep).append("分钟偏少，建议改善睡眠质量。");
        }
        report.append("\n\n");
        
        // 建议
        report.append("【建议】\n");
        report.append("1. 保持规律的作息时间，每天固定时间上床和起床\n");
        report.append("2. 睡前1小时避免使用电子设备，可以阅读或听轻音乐\n");
        report.append("3. 保持卧室安静、黑暗、凉爽的环境，温度控制在18-22度\n");
        
        return report.toString();
    }

    /**
     * 验证睡眠数据完整性
     * 
     * @param sleepData 睡眠数据
     * @return 是否数据完整
     */
    private boolean validateSleepData(SleepAnalysisSummary sleepData) {
        return sleepData != null 
            && sleepData.getSleepScore() != null 
            && sleepData.getDeepSleepDuration() != null 
            && sleepData.getTotalSleepDuration() != null
            && sleepData.getSleepScore() >= 0 
            && sleepData.getSleepScore() <= 100
            && sleepData.getDeepSleepDuration() >= 0
            && sleepData.getTotalSleepDuration() > 0;
    }
}
