package com.sleephelper.service.impl;

import com.sleephelper.entity.SleepAnalysisSummary;
import com.sleephelper.mapper.SleepAnalysisSummaryMapper;
import com.sleephelper.service.SleepAnalysisSummaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 睡眠分析汇总表 服务实现类
 * 
 * @author SleepHelper
 * @since 2024-01-01
 */
@Slf4j
@Service
public class SleepAnalysisSummaryServiceImpl implements SleepAnalysisSummaryService {

    @Autowired
    private SleepAnalysisSummaryMapper sleepAnalysisSummaryMapper;

    /**
     * 新增睡眠数据
     * 
     * @param sleepData 睡眠数据对象
     * @return boolean 是否新增成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addSleepData(SleepAnalysisSummary sleepData) {
        try {
            // 设置数据处理时间为当前时间
            sleepData.setProcessedAt(LocalDateTime.now());
            
            // 保存到数据库
            int result = sleepAnalysisSummaryMapper.insert(sleepData);
            
            if (result > 0) {
                log.info("新增睡眠数据成功，用户ID：{}，睡眠日期：{}", 
                        sleepData.getUserId(), sleepData.getDateOfSleep());
                return true;
            } else {
                log.error("新增睡眠数据失败，用户ID：{}，睡眠日期：{}", 
                        sleepData.getUserId(), sleepData.getDateOfSleep());
                return false;
            }
        } catch (Exception e) {
            log.error("新增睡眠数据异常，用户ID：{}，睡眠日期：{}，异常信息：{}", 
                    sleepData.getUserId(), sleepData.getDateOfSleep(), e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 根据用户ID和睡眠日期查询睡眠记录
     * 
     * @param userId 用户ID
     * @param dateOfSleep 睡眠日期
     * @return SleepAnalysisSummary 睡眠分析汇总对象
     */
    @Override
    public SleepAnalysisSummary getSleepDataByUserIdAndDate(String userId, LocalDate dateOfSleep) {
        try {
            // 使用自定义的Mapper方法查询
            SleepAnalysisSummary result = sleepAnalysisSummaryMapper.selectByUserIdAndDate(userId, dateOfSleep.toString());
            
            if (result != null) {
                log.info("查询睡眠数据成功，用户ID：{}，睡眠日期：{}", userId, dateOfSleep);
            } else {
                log.info("未找到睡眠数据，用户ID：{}，睡眠日期：{}", userId, dateOfSleep);
            }
            
            return result;
        } catch (Exception e) {
            log.error("查询睡眠数据异常，用户ID：{}，睡眠日期：{}，异常信息：{}", 
                    userId, dateOfSleep, e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 批量插入模拟测试数据
     * 
     * @return boolean 是否插入成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean initTestData() {
        try {
            List<SleepAnalysisSummary> testDataList = new ArrayList<>();
            LocalDate today = LocalDate.now();
            LocalDateTime now = LocalDateTime.now();
            
            // 生成5条测试数据，覆盖不同用户和日期
            for (int i = 1; i <= 5; i++) {
                SleepAnalysisSummary testData = new SleepAnalysisSummary();
                testData.setUserId("user00" + i);
                testData.setDateOfSleep(today.minusDays(i - 1)); // 近5天的日期
                testData.setTotalSleepDuration(420 + i * 30); // 420-570分钟（7-9.5小时）
                testData.setDeepSleepDuration(60 + i * 10); // 60-110分钟
                testData.setRemSleepDuration(90 + i * 5); // 90-115分钟
                testData.setSleepScore(70 + i * 4); // 70-90分
                testData.setProcessedAt(now);
                
                testDataList.add(testData);
            }
            
            // 批量保存
            boolean allSuccess = true;
            for (SleepAnalysisSummary testData : testDataList) {
                int result = sleepAnalysisSummaryMapper.insert(testData);
                if (result <= 0) {
                    allSuccess = false;
                    break;
                }
            }
            
            if (allSuccess) {
                log.info("批量插入测试数据成功，共插入{}条记录", testDataList.size());
            } else {
                log.error("批量插入测试数据失败");
            }
            
            return allSuccess;
        } catch (Exception e) {
            log.error("批量插入测试数据异常，异常信息：{}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 根据用户ID查询所有睡眠记录
     * 
     * @param userId 用户ID
     * @return List<SleepAnalysisSummary> 睡眠记录列表
     */
    @Override
    public List<SleepAnalysisSummary> getSleepDataByUserId(String userId) {
        try {
            // 使用Mapper方法查询
            List<SleepAnalysisSummary> result = sleepAnalysisSummaryMapper.selectByUserId(userId);
            
            log.info("查询用户睡眠数据成功，用户ID：{}，记录数：{}", userId, result.size());
            
            return result;
        } catch (Exception e) {
            log.error("查询用户睡眠数据异常，用户ID：{}，异常信息：{}", userId, e.getMessage(), e);
            throw e;
        }
    }
}