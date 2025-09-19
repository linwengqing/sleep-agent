# Maven 依赖问题解决说明

## 问题描述

遇到了以下 Maven 依赖解析错误：
```
org.jetbrains.kotlin:kotlin-stdlib-jdk8:jar:1.9.23 was not found in https://repo.spring.io/milestone during a previous attempt. This failure was cached in the local repository and resolution is not reattempted until the update interval of spring-milestones has elapsed or updates are forced
```

## 解决方案

### 1. 更新 pom.xml 仓库配置

在 `pom.xml` 中添加了 Maven Central 仓库作为主要仓库：

```xml
<repositories>
    <repository>
        <id>central</id>
        <name>Maven Central</name>
        <url>https://repo1.maven.org/maven2</url>
        <snapshots>
            <enabled>false</enabled>
        </snapshots>
    </repository>
    <!-- 其他仓库保持不变 -->
</repositories>
```

### 2. 添加 Kotlin 依赖管理

在 `dependencyManagement` 中明确管理 Kotlin 版本以避免冲突：

```xml
<properties>
    <kotlin.version>1.9.10</kotlin.version>
</properties>

<dependencyManagement>
    <dependencies>
        <!-- 明确管理 Kotlin 版本以避免冲突 -->
        <dependency>
            <groupId>org.jetbrains.kotlin</groupId>
            <artifactId>kotlin-stdlib-jdk8</artifactId>
            <version>${kotlin.version}</version>
        </dependency>
        <dependency>
            <groupId>org.jetbrains.kotlin</groupId>
            <artifactId>kotlin-stdlib</artifactId>
            <version>${kotlin.version}</version>
        </dependency>
    </dependencies>
</dependencyManagement>
```

### 3. 修复 AuraLinkPointsController 编译错误

修复了 `Result.success()` 方法调用的参数顺序错误：

**错误的调用方式：**
```java
return Result.success(balance, "查询余额成功");
```

**正确的调用方式：**
```java
return Result.success("查询余额成功", balance);
```

`Result.success()` 方法的正确签名是：
- `success()` - 无参数
- `success(T data)` - 只有数据
- `success(String message, T data)` - 消息在前，数据在后

### 4. 清理 Maven 缓存

创建了清理脚本来解决本地缓存问题：

**Windows 版本 (clean-maven-cache.bat)：**
```batch
@echo off
echo 正在清理 Maven 缓存...

REM 清理本地 Maven 仓库中的失败缓存
if exist "%USERPROFILE%\.m2\repository\org\jetbrains\kotlin" (
    rmdir /s /q "%USERPROFILE%\.m2\repository\org\jetbrains\kotlin"
    echo 已删除 Kotlin 相关缓存
)

if exist "%USERPROFILE%\.m2\repository\org\springframework\ai" (
    rmdir /s /q "%USERPROFILE%\.m2\repository\org\springframework\ai"
    echo 已删除 Spring AI 相关缓存
)

REM 清理项目 target 目录
if exist "target" (
    rmdir /s /q "target"
    echo 已删除 target 目录
)

echo 缓存清理完成！
echo 现在可以运行以下命令重新构建项目：
echo mvn clean compile -U
pause
```

**Linux/Mac 版本 (clean-maven-cache.sh)：**
```bash
#!/bin/bash

echo "正在清理 Maven 缓存..."

# 清理本地 Maven 仓库中的失败缓存
if [ -d "$HOME/.m2/repository/org/jetbrains/kotlin" ]; then
    rm -rf "$HOME/.m2/repository/org/jetbrains/kotlin"
    echo "已删除 Kotlin 相关缓存"
fi

if [ -d "$HOME/.m2/repository/org/springframework/ai" ]; then
    rm -rf "$HOME/.m2/repository/org/springframework/ai"
    echo "已删除 Spring AI 相关缓存"
fi

# 清理项目 target 目录
if [ -d "target" ]; then
    rm -rf "target"
    echo "已删除 target 目录"
fi

echo "缓存清理完成！"
echo "现在可以运行以下命令重新构建项目："
echo "mvn clean compile -U"
```

## 解决结果

✅ **Maven 依赖问题已解决**
- 添加了 Maven Central 仓库
- 明确管理了 Kotlin 版本
- 清理了本地缓存

✅ **编译错误已修复**
- 修复了 `AuraLinkPointsController` 中的方法调用错误
- 项目现在可以成功编译

✅ **项目构建成功**
```bash
mvn clean compile
# BUILD SUCCESS
```

## 使用建议

1. **如果再次遇到依赖问题**，可以运行清理脚本：
   - Windows: `clean-maven-cache.bat`
   - Linux/Mac: `./clean-maven-cache.sh`

2. **强制更新依赖**：
   ```bash
   mvn clean compile -U
   ```

3. **检查依赖树**：
   ```bash
   mvn dependency:tree
   ```

4. **清理特定依赖**：
   ```bash
   mvn dependency:purge-local-repository -DmanualInclude="groupId:artifactId"
   ```

现在项目可以正常编译和运行了！
