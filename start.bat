@echo off
echo =================================
echo 睡眠数据管理系统启动脚本
echo =================================

echo 检查Java环境...
java -version
if %errorlevel% neq 0 (
    echo 错误：未找到Java环境，请确保已安装JDK 17或更高版本
    pause
    exit /b 1
)

echo.
echo 检查Maven环境...
mvn --version
if %errorlevel% neq 0 (
    echo 错误：未找到Maven环境，请确保已安装Maven 3.6+
    pause
    exit /b 1
)

echo.
echo 开始编译项目...
mvn clean compile -q
if %errorlevel% neq 0 (
    echo 编译失败，请检查代码和依赖
    pause
    exit /b 1
)

echo 编译成功！
echo.
echo 启动应用程序...
echo 访问地址：http://localhost:8080
echo API文档：http://localhost:8080/api/sleep/health
echo.

mvn spring-boot:run

pause
