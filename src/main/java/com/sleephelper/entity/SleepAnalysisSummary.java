package com.sleephelper.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 睡眠分析汇总表实体类
 * 对应数据库表：sleep_analysis_summaries
 * 
 * @author SleepHelper
 * @since 2024-01-01
 */
@Data
public class SleepAnalysisSummary implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 睡眠日期
     */
    private LocalDate dateOfSleep;

    /**
     * 总睡眠时长（分钟）
     */
    private Integer totalSleepDuration;

    /**
     * 深度睡眠时长（分钟）
     */
    private Integer deepSleepDuration;

    /**
     * REM睡眠时长（分钟）
     */
    private Integer remSleepDuration;

    /**
     * 睡眠评分（0-100分）
     */
    private Integer sleepScore;

    /**
     * 数据处理时间
     */
    private LocalDateTime processedAt;
}
