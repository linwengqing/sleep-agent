import request from '@/utils/request'

/**
 * 睡眠数据相关API
 */
export const sleepApi = {
  /**
   * 获取睡眠数据
   * @param {string} userId - 用户ID
   * @param {string} dateOfSleep - 睡眠日期
   * @returns {Promise} 睡眠数据
   */
  getSleepData(userId, dateOfSleep) {
    return request.get('/sleep/data', {
      params: { userId, dateOfSleep }
    })
  },

  /**
   * 获取用户所有睡眠记录
   * @param {string} userId - 用户ID
   * @returns {Promise} 睡眠记录列表
   */
  getSleepHistory(userId) {
    return request.get(`/sleep/data/user/${userId}`)
  },

  /**
   * 新增睡眠数据
   * @param {Object} data - 睡眠数据
   * @returns {Promise} 新增结果
   */
  addSleepData(data) {
    return request.post('/sleep/data', data)
  },

  /**
   * 初始化测试数据
   * @returns {Promise} 初始化结果
   */
  initTestData() {
    return request.post('/sleep/data/init')
  },

  /**
   * 健康检查
   * @returns {Promise} 健康状态
   */
  healthCheck() {
    return request.get('/sleep/health')
  }
}
