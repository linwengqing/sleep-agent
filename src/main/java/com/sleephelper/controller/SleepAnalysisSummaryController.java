package com.sleephelper.controller;

import com.sleephelper.common.Result;
import com.sleephelper.entity.SleepAnalysisSummary;
import com.sleephelper.service.SleepAnalysisSummaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * 睡眠分析汇总表 控制器
 * 提供睡眠数据管理的REST API接口
 * 
 * @author SleepHelper
 * @since 2024-01-01
 */
@Slf4j
@RestController
@RequestMapping("/api/sleep")
@CrossOrigin(origins = "*")
public class SleepAnalysisSummaryController {

    @Autowired
    private SleepAnalysisSummaryService sleepAnalysisSummaryService;

    /**
     * 新增睡眠数据
     * POST /api/sleep/data
     * 
     * @param sleepData 睡眠数据对象（JSON格式）
     * @return Result<SleepAnalysisSummary> 统一返回结果
     */
    @PostMapping("/data")
    public Result<SleepAnalysisSummary> addSleepData(@RequestBody SleepAnalysisSummary sleepData) {
        try {
            log.info("接收到新增睡眠数据请求：用户ID={}，睡眠日期={}", 
                    sleepData.getUserId(), sleepData.getDateOfSleep());
            
            // 参数校验
            if (sleepData.getUserId() == null || sleepData.getUserId().trim().isEmpty()) {
                return Result.error("用户ID不能为空");
            }
            if (sleepData.getDateOfSleep() == null) {
                return Result.error("睡眠日期不能为空");
            }
            if (sleepData.getTotalSleepDuration() == null || sleepData.getTotalSleepDuration() <= 0) {
                return Result.error("总睡眠时长必须大于0");
            }
            if (sleepData.getSleepScore() == null || sleepData.getSleepScore() < 0 || sleepData.getSleepScore() > 100) {
                return Result.error("睡眠评分必须在0-100之间");
            }
            
            // 调用服务层新增数据
            boolean success = sleepAnalysisSummaryService.addSleepData(sleepData);
            
            if (success) {
                log.info("新增睡眠数据成功：用户ID={}，睡眠日期={}", 
                        sleepData.getUserId(), sleepData.getDateOfSleep());
                return Result.success("新增睡眠数据成功", sleepData);
            } else {
                log.error("新增睡眠数据失败：用户ID={}，睡眠日期={}", 
                        sleepData.getUserId(), sleepData.getDateOfSleep());
                return Result.error("新增睡眠数据失败");
            }
        } catch (Exception e) {
            log.error("新增睡眠数据异常：{}", e.getMessage(), e);
            return Result.error("系统异常：" + e.getMessage());
        }
    }

    /**
     * 根据用户ID和睡眠日期查询单条睡眠记录
     * GET /api/sleep/data?userId=xxx&dateOfSleep=xxx
     * 
     * @param userId 用户ID
     * @param dateOfSleep 睡眠日期（格式：yyyy-MM-dd）
     * @return Result<SleepAnalysisSummary> 统一返回结果
     */
    @GetMapping("/data")
    public Result<SleepAnalysisSummary> getSleepData(
            @RequestParam("userId") String userId,
            @RequestParam("dateOfSleep") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateOfSleep) {
        try {
            log.info("接收到查询睡眠数据请求：用户ID={}，睡眠日期={}", userId, dateOfSleep);
            
            // 参数校验
            if (userId == null || userId.trim().isEmpty()) {
                return Result.error("用户ID不能为空");
            }
            if (dateOfSleep == null) {
                return Result.error("睡眠日期不能为空");
            }
            
            // 调用服务层查询数据
            SleepAnalysisSummary sleepData = sleepAnalysisSummaryService.getSleepDataByUserIdAndDate(userId, dateOfSleep);
            
            if (sleepData != null) {
                log.info("查询睡眠数据成功：用户ID={}，睡眠日期={}", userId, dateOfSleep);
                return Result.success("查询睡眠数据成功", sleepData);
            } else {
                log.info("未找到睡眠数据：用户ID={}，睡眠日期={}", userId, dateOfSleep);
                return Result.error("未找到对应的睡眠数据");
            }
        } catch (Exception e) {
            log.error("查询睡眠数据异常：{}", e.getMessage(), e);
            return Result.error("系统异常：" + e.getMessage());
        }
    }

    /**
     * 根据用户ID查询所有睡眠记录
     * GET /api/sleep/data/user/{userId}
     * 
     * @param userId 用户ID
     * @return Result<List<SleepAnalysisSummary>> 统一返回结果
     */
    @GetMapping("/data/user/{userId}")
    public Result<List<SleepAnalysisSummary>> getSleepDataByUserId(@PathVariable("userId") String userId) {
        try {
            log.info("接收到查询用户所有睡眠数据请求：用户ID={}", userId);
            
            // 参数校验
            if (userId == null || userId.trim().isEmpty()) {
                return Result.error("用户ID不能为空");
            }
            
            // 调用服务层查询数据
            List<SleepAnalysisSummary> sleepDataList = sleepAnalysisSummaryService.getSleepDataByUserId(userId);
            
            log.info("查询用户睡眠数据成功：用户ID={}，记录数={}", userId, sleepDataList.size());
            return Result.success("查询用户睡眠数据成功", sleepDataList);
        } catch (Exception e) {
            log.error("查询用户睡眠数据异常：{}", e.getMessage(), e);
            return Result.error("系统异常：" + e.getMessage());
        }
    }

    /**
     * 批量插入模拟测试数据
     * POST /api/sleep/data/init
     * 
     * @return Result<String> 统一返回结果
     */
    @PostMapping("/data/init")
    public Result<String> initTestData() {
        try {
            log.info("接收到初始化测试数据请求");
            
            // 调用服务层初始化测试数据
            boolean success = sleepAnalysisSummaryService.initTestData();
            
            if (success) {
                log.info("初始化测试数据成功");
                return Result.success("初始化测试数据成功，已插入5条模拟数据");
            } else {
                log.error("初始化测试数据失败");
                return Result.error("初始化测试数据失败");
            }
        } catch (Exception e) {
            log.error("初始化测试数据异常：{}", e.getMessage(), e);
            return Result.error("系统异常：" + e.getMessage());
        }
    }

    /**
     * 健康检查接口
     * GET /api/sleep/health
     * 
     * @return Result<String> 统一返回结果
     */
    @GetMapping("/health")
    public Result<String> health() {
        return Result.success("睡眠数据管理服务运行正常");
    }
}
