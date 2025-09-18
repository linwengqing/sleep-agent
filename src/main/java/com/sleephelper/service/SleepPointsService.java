package com.sleephelper.service;

import com.sleephelper.entity.SleepPoints;

import java.time.LocalDate;
import java.util.List;

/**
 * 睡眠积分表 服务接口
 * 
 * @author SleepHelper
 * @since 2024-01-01
 */
public interface SleepPointsService {

    /**
     * 生成睡眠积分
     * 根据用户ID和睡眠日期，从睡眠数据中计算积分并存储
     * 
     * @param userId 用户ID
     * @param dateOfSleep 睡眠日期
     * @return SleepPoints 生成的积分记录
     */
    SleepPoints generateSleepPoints(String userId, LocalDate dateOfSleep);

    /**
     * 根据用户ID和日期查询积分记录
     * 
     * @param userId 用户ID
     * @param date 积分日期
     * @return SleepPoints 睡眠积分对象
     */
    SleepPoints getSleepPointsByUserIdAndDate(String userId, LocalDate date);

    /**
     * 根据用户ID查询积分历史记录
     * 
     * @param userId 用户ID
     * @return List<SleepPoints> 积分历史列表
     */
    List<SleepPoints> getSleepPointsHistory(String userId);

    /**
     * 计算用户当前总积分
     * 
     * @param userId 用户ID
     * @return Integer 总积分数量
     */
    Integer getCurrentTotalPoints(String userId);

    /**
     * 计算睡眠积分
     * 根据睡眠评分和深睡时长计算积分
     * 
     * @param sleepScore 睡眠评分
     * @param deepSleepDuration 深睡时长（分钟）
     * @return Integer 计算出的积分
     */
    Integer calculatePoints(Integer sleepScore, Integer deepSleepDuration);
}
