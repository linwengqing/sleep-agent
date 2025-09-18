package com.sleephelper.mapper;

import com.sleephelper.entity.SleepPoints;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * 睡眠积分表 Mapper 接口
 * 
 * @author SleepHelper
 * @since 2024-01-01
 */
@Mapper
public interface SleepPointsMapper {

    /**
     * 插入睡眠积分记录
     * 
     * @param sleepPoints 睡眠积分对象
     * @return 影响行数
     */
    int insert(SleepPoints sleepPoints);

    /**
     * 根据ID查询睡眠积分记录
     * 
     * @param id 主键ID
     * @return SleepPoints 睡眠积分对象
     */
    SleepPoints selectById(@Param("id") Long id);

    /**
     * 根据用户ID和日期查询积分记录
     * 
     * @param userId 用户ID
     * @param date 积分日期
     * @return SleepPoints 睡眠积分对象
     */
    SleepPoints selectByUserIdAndDate(@Param("userId") String userId, 
                                     @Param("date") String date);

    /**
     * 根据用户ID查询积分历史记录
     * 
     * @param userId 用户ID
     * @return List<SleepPoints> 积分历史列表
     */
    List<SleepPoints> selectByUserId(@Param("userId") String userId);

    /**
     * 计算用户当前总积分
     * 
     * @param userId 用户ID
     * @return Integer 总积分数量
     */
    Integer calculateTotalPoints(@Param("userId") String userId);

    /**
     * 查询所有睡眠积分记录
     * 
     * @return List<SleepPoints> 睡眠积分列表
     */
    List<SleepPoints> selectAll();

    /**
     * 更新睡眠积分记录
     * 
     * @param sleepPoints 睡眠积分对象
     * @return 影响行数
     */
    int updateById(SleepPoints sleepPoints);

    /**
     * 根据ID删除睡眠积分记录
     * 
     * @param id 主键ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);
}
