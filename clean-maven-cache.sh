#!/bin/bash

echo "正在清理 Maven 缓存..."

# 清理本地 Maven 仓库中的失败缓存
echo "清理本地 Maven 仓库缓存..."
if [ -d "$HOME/.m2/repository/org/jetbrains/kotlin" ]; then
    rm -rf "$HOME/.m2/repository/org/jetbrains/kotlin"
    echo "已删除 Kotlin 相关缓存"
fi

if [ -d "$HOME/.m2/repository/org/springframework/ai" ]; then
    rm -rf "$HOME/.m2/repository/org/springframework/ai"
    echo "已删除 Spring AI 相关缓存"
fi

# 清理项目 target 目录
echo "清理项目 target 目录..."
if [ -d "target" ]; then
    rm -rf "target"
    echo "已删除 target 目录"
fi

echo "缓存清理完成！"
echo "现在可以运行以下命令重新构建项目："
echo "mvn clean compile -U"
