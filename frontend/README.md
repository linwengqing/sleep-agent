# 睡眠助手前端

基于 Vue3 + Vite + Tailwind CSS 开发的睡眠健康管理前端页面，提供睡眠数据分析、AI咨询和知识库查询功能。

## 项目特性

- ✅ Vue3 Composition API + `<script setup>`
- ✅ Vite 构建工具
- ✅ Tailwind CSS 样式框架
- ✅ Pinia 状态管理
- ✅ Axios HTTP 请求
- ✅ Font Awesome 图标库
- ✅ 响应式设计（移动端/桌面端）
- ✅ 动画效果和交互体验
- ✅ 睡眠数据可视化
- ✅ AI 对话界面
- ✅ 知识库搜索

## 技术栈

- **框架**: Vue 3.4.0
- **构建工具**: Vite 5.0.8
- **样式**: Tailwind CSS 3.3.6
- **状态管理**: Pinia 2.1.7
- **HTTP 客户端**: Axios 1.6.2
- **图标**: Font Awesome 6.5.1
- **路由**: Vue Router 4.2.5

## 项目结构

```
frontend/
├── public/                     # 静态资源
├── src/
│   ├── api/                   # API 接口
│   │   ├── sleep.js          # 睡眠数据API
│   │   └── points.js         # 积分API
│   ├── components/            # 组件
│   │   ├── Navbar.vue        # 顶部导航栏
│   │   ├── SleepCards.vue    # 睡眠数据卡片
│   │   ├── SleepReport.vue   # 睡眠分析报告
│   │   ├── Chat.vue          # AI对话组件
│   │   └── KnowledgeBase.vue # 知识库查询
│   ├── stores/               # Pinia 状态管理
│   │   ├── sleep.js          # 睡眠数据状态
│   │   └── points.js         # 积分状态
│   ├── utils/                # 工具函数
│   │   └── request.js        # Axios 封装
│   ├── App.vue               # 主应用组件
│   ├── main.js               # 应用入口
│   └── style.css             # 全局样式
├── package.json              # 项目配置
├── vite.config.js            # Vite 配置
├── tailwind.config.js        # Tailwind 配置
└── README.md                 # 项目说明
```

## 组件说明

### 1. Navbar.vue - 顶部导航栏
- 睡眠助手 Logo 和标题
- 积分显示（当前积分、等级）
- 用户登录和 AI 助手按钮
- 积分刷新功能

### 2. SleepCards.vue - 睡眠数据概览卡片
- 睡眠评分卡片（带趋势指示）
- 总睡眠时长卡片（带推荐范围）
- 深睡占比卡片（带状态指示）
- 详细数据表格（可折叠）
- 卡片悬停动画效果

### 3. SleepReport.vue - 睡眠分析报告
- 报告摘要（评分、时长、占比）
- 优势分析（基于睡眠数据）
- 不足分析（问题识别）
- 改进建议（个性化建议）
- 环境数据（温度、湿度）
- 刷新报告功能

### 4. Chat.vue - AI 对话组件
- 多轮对话界面
- 消息气泡动画
- 快捷问题按钮
- 正在输入指示器
- 清空对话功能
- 模拟 AI 回复

### 5. KnowledgeBase.vue - 知识库查询
- 搜索功能（关键词匹配）
- 热门话题展示
- 知识分类浏览
- 搜索结果列表
- 详情模态框
- 示例问题

## 功能特性

### 数据交互
- 从后端 API 获取睡眠数据和积分
- 实时数据刷新和更新
- 错误处理和加载状态
- 数据格式化和显示

### 响应式设计
- 移动端优先设计
- 桌面端网格布局
- 自适应组件尺寸
- 触摸友好的交互

### 动画效果
- 页面加载淡入动画
- 卡片悬停上浮效果
- 消息气泡淡入动画
- 按钮点击反馈
- 加载状态动画

### 用户体验
- 键盘快捷键支持
- 全局通知系统
- 模态框和弹窗
- 无障碍访问支持
- 深色模式支持

## 快速开始

### 1. 环境要求
- Node.js 16.0+
- npm 7.0+

### 2. 安装依赖
```bash
npm install
```

### 3. 启动开发服务器
```bash
# Windows
start.bat

# Linux/Mac
chmod +x start.sh
./start.sh

# 或直接使用 npm
npm run dev
```

### 4. 构建生产版本
```bash
npm run build
```

### 5. 预览生产版本
```bash
npm run preview
```

## 开发指南

### 添加新组件
1. 在 `src/components/` 目录下创建 `.vue` 文件
2. 使用 `<script setup>` 语法
3. 在 `App.vue` 中导入和使用

### 添加新 API
1. 在 `src/api/` 目录下创建 API 文件
2. 使用 `request` 工具发送请求
3. 在组件中导入和使用

### 添加新状态
1. 在 `src/stores/` 目录下创建 store 文件
2. 使用 Pinia 的 `defineStore` 定义状态
3. 在组件中使用 `useStore` 获取状态

### 样式定制
1. 修改 `tailwind.config.js` 配置
2. 在 `src/style.css` 中添加自定义样式
3. 使用 Tailwind 工具类快速开发

## API 接口

### 睡眠数据接口
- `GET /api/sleep/data` - 获取睡眠数据
- `GET /api/sleep/data/user/{userId}` - 获取用户睡眠历史
- `POST /api/sleep/data` - 新增睡眠数据
- `POST /api/sleep/data/init` - 初始化测试数据

### 积分接口
- `GET /api/points/current` - 获取当前积分
- `GET /api/points/history` - 获取积分历史
- `POST /api/points/generate` - 生成积分

## 浏览器支持

- Chrome 88+
- Firefox 85+
- Safari 14+
- Edge 88+

## 性能优化

- 代码分割和懒加载
- 图片优化和压缩
- 组件按需加载
- 缓存策略优化
- 构建产物优化

## 部署说明

### 开发环境
- 前端：http://localhost:3000
- 后端：http://localhost:8080
- 代理配置：Vite 代理 API 请求到后端

### 生产环境
1. 构建前端项目：`npm run build`
2. 将 `dist` 目录部署到 Web 服务器
3. 配置 Nginx 反向代理到后端 API
4. 设置环境变量和域名

## 常见问题

### Q: 如何修改主题颜色？
A: 在 `tailwind.config.js` 中修改 `theme.extend.colors` 配置。

### Q: 如何添加新的图标？
A: 在 `src/main.js` 中导入图标并添加到 `library` 中。

### Q: 如何自定义动画？
A: 在 `src/style.css` 中添加自定义 keyframes 和动画类。

### Q: 如何处理 API 错误？
A: 在 `src/utils/request.js` 中配置响应拦截器处理错误。

## 贡献指南

1. Fork 项目
2. 创建功能分支
3. 提交更改
4. 推送到分支
5. 创建 Pull Request

## 许可证

MIT License
