# AuraLink积分系统实现说明

## 概述

本项目实现了 `AuraLinkPointsService` 接口，主要用于根据用户的睡眠积分数据在区块链上铸造代币。核心功能是根据用户的钱包地址和积分数量进行代币铸造。

## 主要功能

### 1. 核心接口实现

- **`mintPoints(String toAddress, BigInteger amount)`**: 根据用户钱包地址铸造积分代币
- 其他接口方法提供空壳实现，可根据需要扩展

### 2. 数据库设计

#### users 表
```sql
CREATE TABLE IF NOT EXISTS `users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` varchar(50) NOT NULL COMMENT '用户ID',
  `username` varchar(100) NOT NULL COMMENT '用户名',
  `email` varchar(255) DEFAULT NULL COMMENT '用户邮箱',
  `wallet_address` varchar(255) NOT NULL COMMENT '钱包地址',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除标志（0：未删除，1：已删除）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_id` (`user_id`, `deleted`) COMMENT '用户ID唯一索引',
  UNIQUE KEY `uk_wallet_address` (`wallet_address`, `deleted`) COMMENT '钱包地址唯一索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';
```

#### sleep_points 表
```sql
CREATE TABLE IF NOT EXISTS `sleep_points` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` varchar(50) NOT NULL COMMENT '用户ID',
  `date` date NOT NULL COMMENT '积分日期',
  `points` int(11) NOT NULL DEFAULT '0' COMMENT '积分数量',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除标志（0：未删除，1：已删除）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_date` (`user_id`, `date`, `deleted`) COMMENT '用户ID和日期唯一索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='睡眠积分表';
```

### 3. 核心逻辑

#### mintPoints 方法实现逻辑：

1. **验证用户地址**: 检查 `toAddress` 是否在 `users` 表中存在
2. **计算用户总积分**: 从 `sleep_points` 表查询用户的所有积分记录并求和
3. **验证铸造数量**: 确保铸造数量不超过用户总积分的2倍（可配置）
4. **执行代币铸造**: 调用区块链合约进行代币铸造
5. **返回交易哈希**: 返回区块链交易哈希

### 4. 配置说明

在 `application.yml` 中添加区块链配置：

```yaml
# 区块链配置
blockchain:
  rpc-url: https://mainnet.infura.io/v3/YOUR_PROJECT_ID
  contract-address: 0x...
  private-key: YOUR_PRIVATE_KEY
  gas-limit: "21000"
  gas-price: "20000000000"
```

### 5. 依赖库

项目添加了以下区块链相关依赖：

```xml
<!-- Web3j 区块链交互库 -->
<dependency>
    <groupId>org.web3j</groupId>
    <artifactId>core</artifactId>
    <version>4.9.8</version>
</dependency>

<!-- Bouncy Castle 加密库 -->
<dependency>
    <groupId>org.bouncycastle</groupId>
    <artifactId>bcprov-jdk15on</artifactId>
    <version>1.70</version>
</dependency>

<!-- OkHttp 用于HTTP请求 -->
<dependency>
    <groupId>com.squareup.okhttp3</groupId>
    <artifactId>okhttp</artifactId>
    <version>4.12.0</version>
</dependency>
```

## API 接口

### 铸造积分
```
POST /api/aura-link/mint
参数:
- toAddress: 用户钱包地址
- amount: 铸造数量
```

### 查询余额
```
GET /api/aura-link/balance
参数:
- userAddress: 用户钱包地址
```

### 其他接口
- `GET /api/aura-link/name` - 获取代币名称
- `GET /api/aura-link/symbol` - 获取代币符号
- `GET /api/aura-link/total-supply` - 获取总供应量
- `GET /api/aura-link/has-minter-role` - 检查铸造权限
- `POST /api/aura-link/burn-meta` - 元交易销毁
- `POST /api/aura-link/burn-direct` - 直接销毁
- `GET /api/aura-link/burn-forwarder-params` - 获取销毁Forwarder参数
- `GET /api/aura-link/pending-transaction-count` - 获取待处理交易数量
- `POST /api/aura-link/print-account-info` - 打印账户信息

## 使用示例

### 1. 创建用户
```java
User user = new User();
user.setUserId("user001");
user.setUsername("testuser");
user.setEmail("test@example.com");
user.setWalletAddress("0x742d35Cc6634C0532925a3b8D4C9db96C4b4d8b6");
userService.createUser(user);
```

### 2. 铸造积分
```java
// 为用户铸造100个积分代币
String transactionHash = auraLinkPointsService.mintPoints(
    "0x742d35Cc6634C0532925a3b8D4C9db96C4b4d8b6", 
    BigInteger.valueOf(100)
);
```

## 注意事项

1. **智能合约**: 当前实现使用模拟交易哈希，实际部署时需要：
   - 部署ERC20代币合约
   - 更新 `contract-address` 配置
   - 实现真实的合约调用逻辑

2. **安全性**: 
   - 私钥应存储在安全的环境变量中
   - 建议使用硬件钱包或多签钱包
   - 实现适当的权限控制

3. **Gas费用**: 
   - 根据网络状况调整gas价格
   - 实现gas费用估算功能

4. **错误处理**: 
   - 实现重试机制
   - 添加交易状态监控
   - 记录详细的错误日志

## 扩展建议

1. **添加事件监听**: 监听区块链事件，实时更新用户余额
2. **实现批量操作**: 支持批量铸造和销毁
3. **添加缓存机制**: 缓存用户余额和交易状态
4. **实现监控告警**: 监控合约状态和异常交易
5. **添加测试用例**: 完善单元测试和集成测试
