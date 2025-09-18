package com.sleephelper.mapper;

import com.sleephelper.entity.SleepAnalysisSummary;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 睡眠分析汇总表 Mapper 接口
 * 
 * @author SleepHelper
 * @since 2024-01-01
 */
@Mapper
public interface SleepAnalysisSummaryMapper {

    /**
     * 插入睡眠分析汇总记录
     * 
     * @param sleepAnalysisSummary 睡眠分析汇总对象
     * @return 影响行数
     */
    int insert(SleepAnalysisSummary sleepAnalysisSummary);

    /**
     * 根据ID查询睡眠分析汇总记录
     * 
     * @param id 主键ID
     * @return SleepAnalysisSummary 睡眠分析汇总对象
     */
    SleepAnalysisSummary selectById(@Param("id") Long id);

    /**
     * 根据用户ID和睡眠日期查询睡眠记录
     * 
     * @param userId 用户ID
     * @param dateOfSleep 睡眠日期
     * @return SleepAnalysisSummary 睡眠分析汇总对象
     */
    SleepAnalysisSummary selectByUserIdAndDate(@Param("userId") String userId, 
                                               @Param("dateOfSleep") String dateOfSleep);

    /**
     * 查询所有睡眠分析汇总记录
     * 
     * @return List<SleepAnalysisSummary> 睡眠分析汇总列表
     */
    List<SleepAnalysisSummary> selectAll();

    /**
     * 根据用户ID查询睡眠分析汇总记录
     * 
     * @param userId 用户ID
     * @return List<SleepAnalysisSummary> 睡眠分析汇总列表
     */
    List<SleepAnalysisSummary> selectByUserId(@Param("userId") String userId);

    /**
     * 更新睡眠分析汇总记录
     * 
     * @param sleepAnalysisSummary 睡眠分析汇总对象
     * @return 影响行数
     */
    int updateById(SleepAnalysisSummary sleepAnalysisSummary);

    /**
     * 根据ID删除睡眠分析汇总记录
     * 
     * @param id 主键ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);
}
