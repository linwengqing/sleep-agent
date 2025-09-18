import request from '@/utils/request'

/**
 * AI相关API
 */
export const aiApi = {
  /**
   * 生成AI睡眠报告
   * @param {string} userId - 用户ID
   * @param {string} dateOfSleep - 睡眠日期
   * @returns {Promise} AI睡眠报告
   */
  generateSleepReport(userId, dateOfSleep) {
    return request.post('/ai/report', {
      userId,
      dateOfSleep
    })
  },

  /**
   * AI多轮对话
   * @param {string} userId - 用户ID
   * @param {string} message - 用户消息
   * @returns {Promise} AI回复
   */
  chatWithAI(userId, message) {
    return request.post('/ai/chat', {
      userId,
      message
    })
  },

  /**
   * 清空对话历史
   * @param {string} userId - 用户ID
   * @returns {Promise} 操作结果
   */
  clearChatHistory(userId) {
    return request.post('/ai/chat/clear', {
      userId
    })
  },

  /**
   * 获取对话统计信息
   * @param {string} userId - 用户ID
   * @returns {Promise} 统计信息
   */
  getChatStatistics(userId) {
    return request.get('/ai/chat/stats', {
      params: { userId }
    })
  },

  /**
   * 获取帮助信息
   * @returns {Promise} 帮助信息
   */
  getHelp() {
    return request.get('/ai/help')
  },

  /**
   * AI服务健康检查
   * @returns {Promise} 健康状态
   */
  healthCheck() {
    return request.get('/ai/health')
  }
}
