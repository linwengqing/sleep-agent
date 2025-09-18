#!/bin/bash

echo "================================="
echo "睡眠数据管理系统完整测试脚本"
echo "================================="

echo "1. 启动应用程序..."
mvn spring-boot:run &
APP_PID=$!

echo "等待应用程序启动..."
sleep 30

echo ""
echo "2. 测试睡眠数据模块..."
echo "初始化测试数据..."
curl -X POST http://localhost:8080/api/sleep/data/init

echo ""
echo "查询睡眠数据..."
curl -X GET "http://localhost:8080/api/sleep/data?userId=user001&dateOfSleep=2024-01-01"

echo ""
echo "3. 测试睡眠积分模块..."
echo "生成睡眠积分..."
curl -X POST "http://localhost:8080/api/points/generate?userId=user001&dateOfSleep=2024-01-01"

echo ""
echo "查询积分历史..."
curl -X GET "http://localhost:8080/api/points/history?userId=user001"

echo ""
echo "查询当前总积分..."
curl -X GET "http://localhost:8080/api/points/current?userId=user001"

echo ""
echo "4. 健康检查..."
curl -X GET http://localhost:8080/api/sleep/health
curl -X GET http://localhost:8080/api/points/health

echo ""
echo "================================="
echo "测试完成！"
echo "================================="

# 停止应用程序
kill $APP_PID
