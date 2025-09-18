import request from '@/utils/request'

/**
 * 睡眠积分相关API
 */
export const pointsApi = {
  /**
   * 生成睡眠积分
   * @param {string} userId - 用户ID
   * @param {string} dateOfSleep - 睡眠日期
   * @returns {Promise} 积分生成结果
   */
  generatePoints(userId, dateOfSleep) {
    return request.post('/points/generate', null, {
      params: { userId, dateOfSleep }
    })
  },

  /**
   * 获取积分历史
   * @param {string} userId - 用户ID
   * @returns {Promise} 积分历史列表
   */
  getPointsHistory(userId) {
    return request.get('/points/history', {
      params: { userId }
    })
  },

  /**
   * 获取当前总积分
   * @param {string} userId - 用户ID
   * @returns {Promise} 总积分
   */
  getCurrentPoints(userId) {
    return request.get('/points/current', {
      params: { userId }
    })
  },

  /**
   * 获取积分记录
   * @param {string} userId - 用户ID
   * @param {string} date - 积分日期
   * @returns {Promise} 积分记录
   */
  getPointsRecord(userId, date) {
    return request.get('/points/record', {
      params: { userId, date }
    })
  },

  /**
   * 健康检查
   * @returns {Promise} 健康状态
   */
  healthCheck() {
    return request.get('/points/health')
  }
}
