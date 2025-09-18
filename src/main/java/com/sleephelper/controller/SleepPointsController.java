package com.sleephelper.controller;

import com.sleephelper.common.Result;
import com.sleephelper.entity.SleepPoints;
import com.sleephelper.service.SleepPointsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * 睡眠积分表 控制器
 * 提供睡眠积分管理的REST API接口
 * 
 * @author SleepHelper
 * @since 2024-01-01
 */
@Slf4j
@RestController
@RequestMapping("/api/points")
@CrossOrigin(origins = "*")
public class SleepPointsController {

    @Autowired
    private SleepPointsService sleepPointsService;

    /**
     * 生成睡眠积分
     * POST /api/points/generate
     * 根据用户ID和睡眠日期，从睡眠数据中计算积分并存储
     * 
     * @param userId 用户ID
     * @param dateOfSleep 睡眠日期（格式：yyyy-MM-dd）
     * @return Result<SleepPoints> 统一返回结果
     */
    @PostMapping("/generate")
    public Result<SleepPoints> generateSleepPoints(
            @RequestParam("userId") String userId,
            @RequestParam("dateOfSleep") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateOfSleep) {
        try {
            log.info("接收到生成睡眠积分请求：用户ID={}，睡眠日期={}", userId, dateOfSleep);
            
            // 参数校验
            if (userId == null || userId.trim().isEmpty()) {
                return Result.error("用户ID不能为空");
            }
            if (dateOfSleep == null) {
                return Result.error("睡眠日期不能为空");
            }
            
            // 调用服务层生成积分
            SleepPoints sleepPoints = sleepPointsService.generateSleepPoints(userId, dateOfSleep);
            
            log.info("生成睡眠积分成功：用户ID={}，睡眠日期={}，积分={}", 
                    userId, dateOfSleep, sleepPoints.getPoints());
            
            return Result.success("生成睡眠积分成功", sleepPoints);
        } catch (Exception e) {
            log.error("生成睡眠积分异常：{}", e.getMessage(), e);
            return Result.error("生成睡眠积分失败：" + e.getMessage());
        }
    }

    /**
     * 查询积分历史记录
     * GET /api/points/history?userId=xxx
     * 
     * @param userId 用户ID
     * @return Result<List<SleepPoints>> 统一返回结果
     */
    @GetMapping("/history")
    public Result<List<SleepPoints>> getSleepPointsHistory(@RequestParam("userId") String userId) {
        try {
            log.info("接收到查询积分历史请求：用户ID={}", userId);
            
            // 参数校验
            if (userId == null || userId.trim().isEmpty()) {
                return Result.error("用户ID不能为空");
            }
            
            // 调用服务层查询积分历史
            List<SleepPoints> pointsHistory = sleepPointsService.getSleepPointsHistory(userId);
            
            log.info("查询积分历史成功：用户ID={}，记录数={}", userId, pointsHistory.size());
            
            return Result.success("查询积分历史成功", pointsHistory);
        } catch (Exception e) {
            log.error("查询积分历史异常：{}", e.getMessage(), e);
            return Result.error("查询积分历史失败：" + e.getMessage());
        }
    }

    /**
     * 查询用户当前总积分
     * GET /api/points/current?userId=xxx
     * 
     * @param userId 用户ID
     * @return Result<Integer> 统一返回结果
     */
    @GetMapping("/current")
    public Result<Integer> getCurrentTotalPoints(@RequestParam("userId") String userId) {
        try {
            log.info("接收到查询当前总积分请求：用户ID={}", userId);
            
            // 参数校验
            if (userId == null || userId.trim().isEmpty()) {
                return Result.error("用户ID不能为空");
            }
            
            // 调用服务层计算总积分
            Integer totalPoints = sleepPointsService.getCurrentTotalPoints(userId);
            
            log.info("查询当前总积分成功：用户ID={}，总积分={}", userId, totalPoints);
            
            return Result.success("查询当前总积分成功", totalPoints);
        } catch (Exception e) {
            log.error("查询当前总积分异常：{}", e.getMessage(), e);
            return Result.error("查询当前总积分失败：" + e.getMessage());
        }
    }

    /**
     * 根据用户ID和日期查询单条积分记录
     * GET /api/points/record?userId=xxx&date=xxx
     * 
     * @param userId 用户ID
     * @param date 积分日期（格式：yyyy-MM-dd）
     * @return Result<SleepPoints> 统一返回结果
     */
    @GetMapping("/record")
    public Result<SleepPoints> getSleepPointsRecord(
            @RequestParam("userId") String userId,
            @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        try {
            log.info("接收到查询积分记录请求：用户ID={}，日期={}", userId, date);
            
            // 参数校验
            if (userId == null || userId.trim().isEmpty()) {
                return Result.error("用户ID不能为空");
            }
            if (date == null) {
                return Result.error("日期不能为空");
            }
            
            // 调用服务层查询积分记录
            SleepPoints sleepPoints = sleepPointsService.getSleepPointsByUserIdAndDate(userId, date);
            
            if (sleepPoints != null) {
                log.info("查询积分记录成功：用户ID={}，日期={}，积分={}", 
                        userId, date, sleepPoints.getPoints());
                return Result.success("查询积分记录成功", sleepPoints);
            } else {
                log.info("未找到积分记录：用户ID={}，日期={}", userId, date);
                return Result.error("未找到对应的积分记录");
            }
        } catch (Exception e) {
            log.error("查询积分记录异常：{}", e.getMessage(), e);
            return Result.error("查询积分记录失败：" + e.getMessage());
        }
    }

    /**
     * 健康检查接口
     * GET /api/points/health
     * 
     * @return Result<String> 统一返回结果
     */
    @GetMapping("/health")
    public Result<String> health() {
        return Result.success("睡眠积分管理服务运行正常");
    }
}
