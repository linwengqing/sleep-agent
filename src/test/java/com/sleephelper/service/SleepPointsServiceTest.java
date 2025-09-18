package com.sleephelper.service;

import com.sleephelper.service.impl.SleepPointsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 睡眠积分服务测试类
 * 主要测试积分计算逻辑
 * 
 * @author SleepHelper
 * @since 2024-01-01
 */
@SpringBootTest
public class SleepPointsServiceTest {

    private SleepPointsServiceImpl sleepPointsService;

    @BeforeEach
    public void setUp() {
        sleepPointsService = new SleepPointsServiceImpl();
    }

    /**
     * 测试积分计算逻辑 - 高分睡眠
     */
    @Test
    public void testCalculatePoints_HighScore() {
        // 睡眠评分85分，深睡时长120分钟
        Integer points = sleepPointsService.calculatePoints(85, 120);
        
        // 基础分：85分 >= 80分 → 20SP
        // 额外分：120分钟 / 30分钟 = 4个30分钟 → 4 * 5 = 20SP
        // 总积分：20 + 20 = 40SP
        assertEquals(40, points);
    }

    /**
     * 测试积分计算逻辑 - 中等分数睡眠
     */
    @Test
    public void testCalculatePoints_MediumScore() {
        // 睡眠评分75分，深睡时长90分钟
        Integer points = sleepPointsService.calculatePoints(75, 90);
        
        // 基础分：75分在70-79分区间 → 15SP
        // 额外分：90分钟 / 30分钟 = 3个30分钟 → 3 * 5 = 15SP
        // 总积分：15 + 15 = 30SP
        assertEquals(30, points);
    }

    /**
     * 测试积分计算逻辑 - 低分睡眠
     */
    @Test
    public void testCalculatePoints_LowScore() {
        // 睡眠评分55分，深睡时长60分钟
        Integer points = sleepPointsService.calculatePoints(55, 60);
        
        // 基础分：55分 < 60分 → 5SP
        // 额外分：60分钟 / 30分钟 = 2个30分钟 → 2 * 5 = 10SP
        // 总积分：5 + 10 = 15SP
        assertEquals(15, points);
    }

    /**
     * 测试积分计算逻辑 - 深睡时长上限
     */
    @Test
    public void testCalculatePoints_DeepSleepLimit() {
        // 睡眠评分90分，深睡时长300分钟（5小时）
        Integer points = sleepPointsService.calculatePoints(90, 300);
        
        // 基础分：90分 >= 80分 → 20SP
        // 额外分：300分钟 / 30分钟 = 10个30分钟 → 10 * 5 = 50SP，但最多30SP
        // 总积分：20 + 30 = 50SP
        assertEquals(50, points);
    }

    /**
     * 测试积分计算逻辑 - 每日上限
     */
    @Test
    public void testCalculatePoints_DailyLimit() {
        // 睡眠评分100分，深睡时长360分钟（6小时）
        Integer points = sleepPointsService.calculatePoints(100, 360);
        
        // 基础分：100分 >= 80分 → 20SP
        // 额外分：360分钟 / 30分钟 = 12个30分钟 → 12 * 5 = 60SP，但最多30SP
        // 总积分：20 + 30 = 50SP，但每日上限50SP
        assertEquals(50, points);
    }

    /**
     * 测试积分计算逻辑 - 边界值测试
     */
    @Test
    public void testCalculatePoints_BoundaryValues() {
        // 测试睡眠评分边界值
        assertEquals(20, sleepPointsService.calculatePoints(80, 0));  // 80分边界
        assertEquals(15, sleepPointsService.calculatePoints(79, 0));  // 79分边界
        assertEquals(15, sleepPointsService.calculatePoints(70, 0));  // 70分边界
        assertEquals(10, sleepPointsService.calculatePoints(69, 0));  // 69分边界
        assertEquals(10, sleepPointsService.calculatePoints(60, 0));  // 60分边界
        assertEquals(5, sleepPointsService.calculatePoints(59, 0));   // 59分边界
        
        // 测试深睡时长边界值
        assertEquals(20, sleepPointsService.calculatePoints(85, 29));  // 29分钟
        assertEquals(25, sleepPointsService.calculatePoints(85, 30));  // 30分钟
        assertEquals(25, sleepPointsService.calculatePoints(85, 59));  // 59分钟
        assertEquals(30, sleepPointsService.calculatePoints(85, 60));  // 60分钟
    }

    /**
     * 测试积分计算逻辑 - 异常值处理
     */
    @Test
    public void testCalculatePoints_InvalidValues() {
        // 测试null值
        assertEquals(5, sleepPointsService.calculatePoints(null, 60));
        assertEquals(20, sleepPointsService.calculatePoints(85, null));
        assertEquals(5, sleepPointsService.calculatePoints(null, null));
        
        // 测试负数
        assertEquals(5, sleepPointsService.calculatePoints(-10, 60));
        assertEquals(20, sleepPointsService.calculatePoints(85, -10));
        
        // 测试超出范围的值
        assertEquals(5, sleepPointsService.calculatePoints(150, 60));
        assertEquals(20, sleepPointsService.calculatePoints(85, 1000));
    }
}
