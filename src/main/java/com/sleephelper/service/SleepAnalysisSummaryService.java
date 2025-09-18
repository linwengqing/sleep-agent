package com.sleephelper.service;

import com.sleephelper.entity.SleepAnalysisSummary;

import java.time.LocalDate;
import java.util.List;

/**
 * 睡眠分析汇总表 服务接口
 * 
 * @author SleepHelper
 * @since 2024-01-01
 */
public interface SleepAnalysisSummaryService {

    /**
     * 新增睡眠数据
     * 
     * @param sleepData 睡眠数据对象
     * @return boolean 是否新增成功
     */
    boolean addSleepData(SleepAnalysisSummary sleepData);

    /**
     * 根据用户ID和睡眠日期查询睡眠记录
     * 
     * @param userId 用户ID
     * @param dateOfSleep 睡眠日期
     * @return SleepAnalysisSummary 睡眠分析汇总对象
     */
    SleepAnalysisSummary getSleepDataByUserIdAndDate(String userId, LocalDate dateOfSleep);

    /**
     * 批量插入模拟测试数据
     * 
     * @return boolean 是否插入成功
     */
    boolean initTestData();

    /**
     * 根据用户ID查询所有睡眠记录
     * 
     * @param userId 用户ID
     * @return List<SleepAnalysisSummary> 睡眠记录列表
     */
    List<SleepAnalysisSummary> getSleepDataByUserId(String userId);
}
