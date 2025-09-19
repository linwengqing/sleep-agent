@echo off
echo 正在清理 Maven 缓存...

REM 清理本地 Maven 仓库中的失败缓存
echo 清理本地 Maven 仓库缓存...
if exist "%USERPROFILE%\.m2\repository\org\jetbrains\kotlin" (
    rmdir /s /q "%USERPROFILE%\.m2\repository\org\jetbrains\kotlin"
    echo 已删除 Kotlin 相关缓存
)

if exist "%USERPROFILE%\.m2\repository\org\springframework\ai" (
    rmdir /s /q "%USERPROFILE%\.m2\repository\org\springframework\ai"
    echo 已删除 Spring AI 相关缓存
)

REM 清理项目 target 目录
echo 清理项目 target 目录...
if exist "target" (
    rmdir /s /q "target"
    echo 已删除 target 目录
)

echo 缓存清理完成！
echo 现在可以运行以下命令重新构建项目：
echo mvn clean compile -U
pause
