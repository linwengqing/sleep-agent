#!/bin/bash

echo "================================="
echo "睡眠助手前端启动脚本"
echo "================================="

echo "检查Node.js环境..."
node --version
if [ $? -ne 0 ]; then
    echo "错误：未找到Node.js环境，请确保已安装Node.js 16+"
    exit 1
fi

echo ""
echo "检查npm环境..."
npm --version
if [ $? -ne 0 ]; then
    echo "错误：未找到npm环境，请确保已安装npm"
    exit 1
fi

echo ""
echo "安装依赖..."
npm install
if [ $? -ne 0 ]; then
    echo "依赖安装失败，请检查网络连接"
    exit 1
fi

echo ""
echo "启动开发服务器..."
echo "访问地址：http://localhost:3000"
echo "后端API：http://localhost:8080"
echo ""

npm run dev
