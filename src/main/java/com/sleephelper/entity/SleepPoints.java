package com.sleephelper.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 睡眠积分表实体类
 * 对应数据库表：sleep_points
 * 
 * @author SleepHelper
 * @since 2024-01-01
 */
@Data
public class SleepPoints implements Serializable {

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
     * 积分日期
     */
    private LocalDate date;

    /**
     * 积分数量
     */
    private Integer points;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
}
