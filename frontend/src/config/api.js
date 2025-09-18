/**
 * API 配置文件
 * 统一管理所有API接口的配置和常量
 */

// API 基础配置
export const API_CONFIG = {
  // 基础URL
  BASE_URL: '/api',
  
  // 请求超时时间
  TIMEOUT: 10000,
  
  // 重试次数
  RETRY_COUNT: 3,
  
  // 重试延迟
  RETRY_DELAY: 1000,
}

// 接口路径常量
export const API_PATHS = {
  // 睡眠数据相关
  SLEEP: {
    DATA: '/sleep/data',
    HISTORY: '/sleep/data/user',
    INIT: '/sleep/data/init',
    HEALTH: '/sleep/health'
  },
  
  // 积分相关
  POINTS: {
    CURRENT: '/points/current',
    HISTORY: '/points/history',
    RECORD: '/points/record',
    GENERATE: '/points/generate'
  },
  
  // AI相关
  AI: {
    CHAT: '/ai/chat',
    REPORT: '/ai/report',
    CLEAR: '/ai/chat/clear',
    STATS: '/ai/chat/stats',
    HELP: '/ai/help',
    HEALTH: '/ai/health'
  }
}

// 请求方法常量
export const HTTP_METHODS = {
  GET: 'GET',
  POST: 'POST',
  PUT: 'PUT',
  DELETE: 'DELETE'
}

// 响应状态码常量
export const RESPONSE_CODES = {
  SUCCESS: 200,
  BAD_REQUEST: 400,
  UNAUTHORIZED: 401,
  FORBIDDEN: 403,
  NOT_FOUND: 404,
  INTERNAL_SERVER_ERROR: 500
}

// 错误消息常量
export const ERROR_MESSAGES = {
  NETWORK_ERROR: '网络连接失败，请检查网络',
  TIMEOUT_ERROR: '请求超时，请稍后重试',
  SERVER_ERROR: '服务器内部错误',
  UNAUTHORIZED: '未授权，请重新登录',
  FORBIDDEN: '拒绝访问',
  NOT_FOUND: '请求资源不存在',
  BAD_REQUEST: '请求参数错误',
  UNKNOWN_ERROR: '未知错误'
}

// 用户相关常量
export const USER_CONFIG = {
  DEFAULT_USER_ID: 'user001',
  DEFAULT_USERNAME: '睡眠用户',
  SESSION_KEY: 'sleep_user_session',
  TOKEN_KEY: 'sleep_user_token'
}

// 睡眠数据相关常量
export const SLEEP_CONFIG = {
  // 睡眠评分等级
  SCORE_LEVELS: {
    EXCELLENT: { min: 90, max: 100, label: '优秀', color: 'text-green-600' },
    GOOD: { min: 80, max: 89, label: '良好', color: 'text-blue-600' },
    FAIR: { min: 70, max: 79, label: '一般', color: 'text-yellow-600' },
    POOR: { min: 60, max: 69, label: '较差', color: 'text-orange-600' },
    BAD: { min: 0, max: 59, label: '很差', color: 'text-red-600' }
  },
  
  // 深睡占比等级
  DEEP_SLEEP_LEVELS: {
    EXCELLENT: { min: 25, max: 100, label: '优秀', color: 'text-green-600' },
    GOOD: { min: 20, max: 24, label: '良好', color: 'text-blue-600' },
    FAIR: { min: 15, max: 19, label: '一般', color: 'text-yellow-600' },
    POOR: { min: 0, max: 14, label: '较差', color: 'text-orange-600' }
  },
  
  // 睡眠时长建议
  DURATION_RECOMMENDATIONS: {
    ADULT: { min: 420, max: 540 }, // 7-9小时
    TEEN: { min: 480, max: 600 },  // 8-10小时
    CHILD: { min: 540, max: 720 }  // 9-12小时
  }
}

// 积分相关常量
export const POINTS_CONFIG = {
  // 积分等级
  LEVELS: {
    DIAMOND: { min: 1000, label: '钻石', color: 'text-blue-600' },
    GOLD: { min: 500, label: '黄金', color: 'text-yellow-600' },
    SILVER: { min: 200, label: '白银', color: 'text-gray-600' },
    BRONZE: { min: 0, label: '青铜', color: 'text-orange-600' }
  },
  
  // 积分规则
  RULES: {
    BASE_SCORE: {
      EXCELLENT: 20,  // 80分以上
      GOOD: 15,       // 70-79分
      FAIR: 10,       // 60-69分
      POOR: 5         // 60分以下
    },
    DEEP_SLEEP_BONUS: 5,  // 每30分钟深睡+5分
    MAX_DEEP_SLEEP_BONUS: 30, // 深睡奖励上限
    DAILY_CAP: 50      // 每日积分上限
  }
}

// AI相关常量
export const AI_CONFIG = {
  // 对话配置
  CHAT: {
    MAX_HISTORY: 10,        // 最大历史记录数
    MAX_MESSAGE_LENGTH: 500, // 最大消息长度
    TYPING_DELAY: 1000,     // 打字延迟
    RESPONSE_TIMEOUT: 30000  // 响应超时时间
  },
  
  // 报告配置
  REPORT: {
    MAX_LENGTH: 1000,       // 最大报告长度
    GENERATION_TIMEOUT: 60000 // 生成超时时间
  },
  
  // 提示词模板
  PROMPTS: {
    SLEEP_REPORT: '基于用户睡眠数据生成专业的睡眠分析报告...',
    CHAT_RESPONSE: '你是专业的睡眠健康助手...'
  }
}

// 日期格式常量
export const DATE_FORMATS = {
  API: 'YYYY-MM-DD',           // API接口格式
  DISPLAY: 'YYYY年MM月DD日',    // 显示格式
  INPUT: 'YYYY-MM-DD',         // 输入格式
  DATETIME: 'YYYY-MM-DD HH:mm:ss' // 日期时间格式
}

// 本地存储键名常量
export const STORAGE_KEYS = {
  USER_INFO: 'sleep_user_info',
  SLEEP_DATA: 'sleep_data_cache',
  POINTS_DATA: 'points_data_cache',
  CHAT_HISTORY: 'chat_history',
  AI_REPORT: 'ai_report_cache',
  SETTINGS: 'sleep_settings'
}

// 缓存配置
export const CACHE_CONFIG = {
  // 缓存过期时间（毫秒）
  EXPIRY: {
    SLEEP_DATA: 5 * 60 * 1000,    // 5分钟
    POINTS_DATA: 2 * 60 * 1000,   // 2分钟
    AI_REPORT: 10 * 60 * 1000,    // 10分钟
    CHAT_HISTORY: 30 * 60 * 1000  // 30分钟
  },
  
  // 最大缓存大小
  MAX_SIZE: {
    SLEEP_DATA: 50,      // 最多50条睡眠记录
    POINTS_DATA: 100,    // 最多100条积分记录
    CHAT_HISTORY: 200    // 最多200条聊天记录
  }
}

// 验证规则常量
export const VALIDATION_RULES = {
  USER_ID: {
    required: true,
    pattern: /^[a-zA-Z0-9_]{3,20}$/,
    message: '用户ID必须为3-20位字母、数字或下划线'
  },
  
  DATE: {
    required: true,
    pattern: /^\d{4}-\d{2}-\d{2}$/,
    message: '日期格式必须为YYYY-MM-DD'
  },
  
  MESSAGE: {
    required: true,
    minLength: 1,
    maxLength: 500,
    message: '消息长度必须在1-500字符之间'
  },
  
  SLEEP_SCORE: {
    min: 0,
    max: 100,
    message: '睡眠评分必须在0-100之间'
  },
  
  SLEEP_DURATION: {
    min: 0,
    max: 1440, // 24小时
    message: '睡眠时长必须在0-1440分钟之间'
  }
}

// 导出所有配置
export default {
  API_CONFIG,
  API_PATHS,
  HTTP_METHODS,
  RESPONSE_CODES,
  ERROR_MESSAGES,
  USER_CONFIG,
  SLEEP_CONFIG,
  POINTS_CONFIG,
  AI_CONFIG,
  DATE_FORMATS,
  STORAGE_KEYS,
  CACHE_CONFIG,
  VALIDATION_RULES
}
