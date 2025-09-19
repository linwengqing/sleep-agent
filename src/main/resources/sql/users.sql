-- 核心用户身份表
CREATE TABLE IF NOT EXISTS `users` (
  `id` varchar(36) NOT NULL COMMENT '用户唯一标识（UUID）',
  `wallet_address` varchar(42) NOT NULL COMMENT '用户链上钱包地址（含0x）',
  `username` varchar(50) NULL COMMENT '用户自定义昵称（唯一）',
  `avatar` varchar(255) NULL COMMENT '用户头像URL（存储图片链接，如https://xxx.com/avatar/123.png）',
  `created_at` timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
  `updated_at` timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  CONSTRAINT `uk_users_wallet_address` UNIQUE (`wallet_address`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='核心用户身份表';
