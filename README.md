# 睡眠数据管理系统

基于 Spring Boot + MyBatis-Plus 开发的睡眠数据管理模块，包含睡眠数据管理和睡眠积分系统，提供完整的睡眠数据CRUD操作和积分计算API接口。

## 项目特性

- ✅ Spring Boot 2.7.14 框架
- ✅ MyBatis-Plus 3.5.3.1 数据访问层
- ✅ MySQL 数据库支持
- ✅ Druid 连接池
- ✅ 分页插件和乐观锁支持
- ✅ 统一返回格式
- ✅ 完整的日志记录
- ✅ 模拟测试数据功能
- ✅ 睡眠积分系统（Sleep Points, SP）
- ✅ 积分计算规则和事务管理

## 数据库表结构

### sleep_analysis_summaries 表

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | bigint | 主键ID（自增） |
| user_id | varchar(50) | 用户ID |
| date_of_sleep | date | 睡眠日期 |
| total_sleep_duration | int | 总睡眠时长（分钟） |
| deep_sleep_duration | int | 深度睡眠时长（分钟） |
| rem_sleep_duration | int | REM睡眠时长（分钟） |
| sleep_score | int | 睡眠评分（0-100分） |
| processed_at | datetime | 数据处理时间 |
| created_at | datetime | 创建时间 |
| updated_at | datetime | 更新时间 |
| deleted | tinyint | 逻辑删除标志 |

### sleep_points 表

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | bigint | 主键ID（自增） |
| user_id | varchar(50) | 用户ID |
| date | date | 积分日期 |
| points | int | 积分数量 |
| created_at | datetime | 创建时间 |
| updated_at | datetime | 更新时间 |
| deleted | tinyint | 逻辑删除标志 |

## API 接口

### 1. 新增睡眠数据
- **接口地址**: `POST /api/sleep/data`
- **请求体**: JSON格式的睡眠数据
- **示例**:
```json
{
  "userId": "user001",
  "dateOfSleep": "2024-01-01",
  "totalSleepDuration": 480,
  "deepSleepDuration": 90,
  "remSleepDuration": 120,
  "sleepScore": 85
}
```

### 2. 查询单条睡眠记录
- **接口地址**: `GET /api/sleep/data?userId=user001&dateOfSleep=2024-01-01`
- **参数**:
  - `userId`: 用户ID
  - `dateOfSleep`: 睡眠日期（格式：yyyy-MM-dd）

### 3. 查询用户所有睡眠记录
- **接口地址**: `GET /api/sleep/data/user/{userId}`
- **参数**:
  - `userId`: 用户ID（路径参数）

### 4. 初始化测试数据
- **接口地址**: `POST /api/sleep/data/init`
- **功能**: 批量插入5条模拟测试数据

### 5. 健康检查
- **接口地址**: `GET /api/sleep/health`

## 睡眠积分系统

### 积分计算规则

1. **基础分**：
   - 睡眠总分 ≥ 80分 → 20 SP
   - 睡眠总分 70-79分 → 15 SP
   - 睡眠总分 60-69分 → 10 SP
   - 睡眠总分 < 60分 → 5 SP

2. **额外分**：
   - 深睡时长每满30分钟 → +5 SP
   - 额外分最多加30 SP

3. **每日上限**：50 SP

### 睡眠积分API接口

### 1. 生成睡眠积分
- **接口地址**: `POST /api/points/generate`
- **参数**:
  - `userId`: 用户ID
  - `dateOfSleep`: 睡眠日期（格式：yyyy-MM-dd）

### 2. 查询积分历史
- **接口地址**: `GET /api/points/history?userId=xxx`
- **参数**:
  - `userId`: 用户ID

### 3. 查询当前总积分
- **接口地址**: `GET /api/points/current?userId=xxx`
- **参数**:
  - `userId`: 用户ID

### 4. 查询积分记录
- **接口地址**: `GET /api/points/record?userId=xxx&date=xxx`
- **参数**:
  - `userId`: 用户ID
  - `date`: 积分日期（格式：yyyy-MM-dd）

### 5. 积分系统健康检查
- **接口地址**: `GET /api/points/health`

## 统一返回格式

```json
{
  "code": 200,
  "message": "成功",
  "data": {
    // 具体数据内容
  }
}
```

## 环境配置

### 1. 数据库配置
修改 `src/main/resources/application.yml` 中的数据库连接信息：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/sleep_helper?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
    username: root
    password: 123456
```

### 2. 创建数据库
执行以下SQL脚本创建数据库和表：
- `src/main/resources/sql/sleep_analysis_summaries.sql` - 睡眠数据表
- `src/main/resources/sql/sleep_points.sql` - 睡眠积分表

## 运行项目

### 1. 使用启动脚本（推荐）
**Windows:**
```bash
start.bat
```

**Linux/Mac:**
```bash
chmod +x start.sh
./start.sh
```

### 2. 使用Maven运行
```bash
mvn spring-boot:run
```

### 3. 打包运行
```bash
mvn clean package
java -jar target/sleep-helper-1.0.0.jar
```

### 4. IDE运行
直接运行 `SleepHelperApplication.java` 主类

## 测试数据

项目启动后，可以调用初始化接口创建测试数据：

```bash
curl -X POST http://localhost:8080/api/sleep/data/init
```

这将创建5条测试数据：
- 用户ID: user001-user005
- 日期: 近5天
- 睡眠评分: 70-90分
- 睡眠时长: 7-9.5小时

## 项目结构

```
src/main/java/com/sleephelper/
├── SleepHelperApplication.java          # 启动类
├── common/
│   └── Result.java                      # 统一返回格式
├── config/
│   └── MybatisPlusConfig.java           # MyBatis-Plus配置
├── controller/
│   └── SleepAnalysisSummaryController.java  # 控制器
├── entity/
│   └── SleepAnalysisSummary.java        # 实体类
├── mapper/
│   └── SleepAnalysisSummaryMapper.java  # Mapper接口
└── service/
    ├── SleepAnalysisSummaryService.java # 睡眠数据服务接口
    ├── SleepPointsService.java # 睡眠积分服务接口
    └── impl/
        ├── SleepAnalysisSummaryServiceImpl.java  # 睡眠数据服务实现
        └── SleepPointsServiceImpl.java # 睡眠积分服务实现
```

## 技术栈

- **框架**: Spring Boot 2.7.14
- **数据库**: MySQL 8.0+
- **ORM**: MyBatis-Plus 3.5.3.1
- **连接池**: Druid 1.2.18
- **构建工具**: Maven 3.6+
- **JDK版本**: Java 17+

## API测试

### 使用Postman测试
1. 导入 `Sleep_Helper_API_Tests.postman_collection.json` 文件到Postman
2. 设置环境变量 `baseUrl` 为 `http://localhost:8080`
3. 按顺序执行测试用例

### 使用curl测试
```bash
# 健康检查
curl -X GET http://localhost:8080/api/sleep/health

# 初始化测试数据
curl -X POST http://localhost:8080/api/sleep/data/init

# 新增睡眠数据
curl -X POST http://localhost:8080/api/sleep/data \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "user001",
    "dateOfSleep": "2024-01-01",
    "totalSleepDuration": 480,
    "deepSleepDuration": 90,
    "remSleepDuration": 120,
    "sleepScore": 85
  }'

# 查询睡眠数据
curl -X GET "http://localhost:8080/api/sleep/data?userId=user001&dateOfSleep=2024-01-01"

# 生成睡眠积分
curl -X POST "http://localhost:8080/api/points/generate?userId=user001&dateOfSleep=2024-01-01"

# 查询积分历史
curl -X GET "http://localhost:8080/api/points/history?userId=user001"

# 查询当前总积分
curl -X GET "http://localhost:8080/api/points/current?userId=user001"
```

## 注意事项

1. 确保MySQL服务已启动
2. 确保数据库连接配置正确
3. 首次运行前请执行SQL脚本创建数据库表
4. 项目使用逻辑删除，删除的数据不会真正从数据库中移除
5. 所有时间字段使用LocalDateTime和LocalDate类型
6. 项目支持Java 17，如使用其他版本请修改pom.xml中的java.version
