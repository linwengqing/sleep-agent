package com.sleephelper.service.impl;

import com.sleephelper.entity.SleepAnalysisSummary;
import com.sleephelper.entity.SleepPoints;
import com.sleephelper.mapper.SleepPointsMapper;
import com.sleephelper.service.SleepAnalysisSummaryService;
import com.sleephelper.service.SleepPointsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 睡眠积分表 服务实现类
 * 
 * @author SleepHelper
 * @since 2024-01-01
 */
@Slf4j
@Service
public class SleepPointsServiceImpl implements SleepPointsService {

    @Autowired
    private SleepPointsMapper sleepPointsMapper;

    @Autowired
    private SleepAnalysisSummaryService sleepAnalysisSummaryService;

    /**
     * 生成睡眠积分
     * 根据用户ID和睡眠日期，从睡眠数据中计算积分并存储
     * 
     * @param userId 用户ID
     * @param dateOfSleep 睡眠日期
     * @return SleepPoints 生成的积分记录
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SleepPoints generateSleepPoints(String userId, LocalDate dateOfSleep) {
        try {
            log.info("开始生成睡眠积分，用户ID：{}，睡眠日期：{}", userId, dateOfSleep);
            
            // 1. 查询睡眠数据
            SleepAnalysisSummary sleepData = sleepAnalysisSummaryService.getSleepDataByUserIdAndDate(userId, dateOfSleep);
            if (sleepData == null) {
                log.warn("未找到睡眠数据，无法生成积分，用户ID：{}，睡眠日期：{}", userId, dateOfSleep);
                throw new RuntimeException("未找到对应的睡眠数据，无法生成积分");
            }
            
            // 2. 检查是否已经生成过积分
            SleepPoints existingPoints = getSleepPointsByUserIdAndDate(userId, dateOfSleep);
            if (existingPoints != null) {
                log.warn("该日期的积分已存在，用户ID：{}，睡眠日期：{}，现有积分：{}", 
                        userId, dateOfSleep, existingPoints.getPoints());
                return existingPoints;
            }
            
            // 3. 计算积分
            Integer calculatedPoints = calculatePoints(sleepData.getSleepScore(), sleepData.getDeepSleepDuration());
            
            // 4. 创建积分记录
            SleepPoints sleepPoints = new SleepPoints();
            sleepPoints.setUserId(userId);
            sleepPoints.setDate(dateOfSleep);
            sleepPoints.setPoints(calculatedPoints);
            sleepPoints.setCreatedAt(LocalDateTime.now());
            
            // 5. 保存积分记录
            int saveResult = sleepPointsMapper.insert(sleepPoints);
            if (saveResult <= 0) {
                log.error("保存积分记录失败，用户ID：{}，睡眠日期：{}", userId, dateOfSleep);
                throw new RuntimeException("保存积分记录失败");
            }
            
            log.info("睡眠积分生成成功，用户ID：{}，睡眠日期：{}，积分：{}", 
                    userId, dateOfSleep, calculatedPoints);
            
            return sleepPoints;
        } catch (Exception e) {
            log.error("生成睡眠积分异常，用户ID：{}，睡眠日期：{}，异常信息：{}", 
                    userId, dateOfSleep, e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 根据用户ID和日期查询积分记录
     * 
     * @param userId 用户ID
     * @param date 积分日期
     * @return SleepPoints 睡眠积分对象
     */
    @Override
    public SleepPoints getSleepPointsByUserIdAndDate(String userId, LocalDate date) {
        try {
            SleepPoints result = sleepPointsMapper.selectByUserIdAndDate(userId, date.toString());
            
            if (result != null) {
                log.info("查询积分记录成功，用户ID：{}，日期：{}，积分：{}", userId, date, result.getPoints());
            } else {
                log.info("未找到积分记录，用户ID：{}，日期：{}", userId, date);
            }
            
            return result;
        } catch (Exception e) {
            log.error("查询积分记录异常，用户ID：{}，日期：{}，异常信息：{}", 
                    userId, date, e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 根据用户ID查询积分历史记录
     * 
     * @param userId 用户ID
     * @return List<SleepPoints> 积分历史列表
     */
    @Override
    public List<SleepPoints> getSleepPointsHistory(String userId) {
        try {
            List<SleepPoints> result = sleepPointsMapper.selectByUserId(userId);
            
            log.info("查询积分历史成功，用户ID：{}，记录数：{}", userId, result.size());
            
            return result;
        } catch (Exception e) {
            log.error("查询积分历史异常，用户ID：{}，异常信息：{}", userId, e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 计算用户当前总积分
     * 
     * @param userId 用户ID
     * @return Integer 总积分数量
     */
    @Override
    public Integer getCurrentTotalPoints(String userId) {
        try {
            Integer totalPoints = sleepPointsMapper.calculateTotalPoints(userId);
            
            log.info("计算用户总积分成功，用户ID：{}，总积分：{}", userId, totalPoints);
            
            return totalPoints;
        } catch (Exception e) {
            log.error("计算用户总积分异常，用户ID：{}，异常信息：{}", userId, e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 计算睡眠积分
     * 积分规则：
     * - 基础分：睡眠总分≥80分→20SP；70-79分→15SP；60-69分→10SP；<60分→5SP
     * - 额外分：深睡时长每满30分钟→+5SP（最多加30SP）
     * - 每日上限：50SP
     * 
     * @param sleepScore 睡眠评分
     * @param deepSleepDuration 深睡时长（分钟）
     * @return Integer 计算出的积分
     */
    @Override
    public Integer calculatePoints(Integer sleepScore, Integer deepSleepDuration) {
        try {
            // 参数校验
            if (sleepScore == null || sleepScore < 0 || sleepScore > 100) {
                log.warn("睡眠评分无效：{}，使用默认值0", sleepScore);
                sleepScore = 0;
            }
            if (deepSleepDuration == null || deepSleepDuration < 0) {
                log.warn("深睡时长无效：{}，使用默认值0", deepSleepDuration);
                deepSleepDuration = 0;
            }
            
            // 1. 计算基础分
            int basePoints = 0;
            if (sleepScore >= 80) {
                basePoints = 20;
            } else if (sleepScore >= 70) {
                basePoints = 15;
            } else if (sleepScore >= 60) {
                basePoints = 10;
            } else {
                basePoints = 5;
            }
            
            // 2. 计算额外分（深睡时长每满30分钟+5SP，最多加30SP）
            int extraPoints = Math.min((deepSleepDuration / 30) * 5, 30);
            
            // 3. 计算总积分
            int totalPoints = basePoints + extraPoints;
            
            // 4. 应用每日上限（50SP）
            int finalPoints = Math.min(totalPoints, 50);
            
            log.info("积分计算完成，睡眠评分：{}，深睡时长：{}分钟，基础分：{}，额外分：{}，总积分：{}，最终积分：{}", 
                    sleepScore, deepSleepDuration, basePoints, extraPoints, totalPoints, finalPoints);
            
            return finalPoints;
        } catch (Exception e) {
            log.error("计算睡眠积分异常，睡眠评分：{}，深睡时长：{}，异常信息：{}", 
                    sleepScore, deepSleepDuration, e.getMessage(), e);
            throw e;
        }
    }
}
