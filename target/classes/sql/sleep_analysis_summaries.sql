-- 睡眠分析汇总表
CREATE TABLE IF NOT EXISTS `sleep_analysis_summaries` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` varchar(50) NOT NULL COMMENT '用户ID',
  `date_of_sleep` date NOT NULL COMMENT '睡眠日期',
  `total_sleep_duration` int(11) NOT NULL COMMENT '总睡眠时长（分钟）',
  `deep_sleep_duration` int(11) DEFAULT NULL COMMENT '深度睡眠时长（分钟）',
  `rem_sleep_duration` int(11) DEFAULT NULL COMMENT 'REM睡眠时长（分钟）',
  `sleep_score` int(11) DEFAULT NULL COMMENT '睡眠评分（0-100分）',
  `processed_at` datetime DEFAULT NULL COMMENT '数据处理时间',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除标志（0：未删除，1：已删除）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_date` (`user_id`, `date_of_sleep`, `deleted`) COMMENT '用户ID和睡眠日期唯一索引',
  KEY `idx_user_id` (`user_id`) COMMENT '用户ID索引',
  KEY `idx_date_of_sleep` (`date_of_sleep`) COMMENT '睡眠日期索引',
  KEY `idx_sleep_score` (`sleep_score`) COMMENT '睡眠评分索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='睡眠分析汇总表';

-- 创建数据库（如果不存在）
CREATE DATABASE IF NOT EXISTS `sleep_helper` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
