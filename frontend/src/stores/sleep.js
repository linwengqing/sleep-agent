import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { sleepApi } from '@/api/sleep'

export const useSleepStore = defineStore('sleep', () => {
  // 状态
  const sleepData = ref(null)
  const sleepHistory = ref([])
  const loading = ref(false)
  const error = ref(null)

  // 计算属性
  const hasSleepData = computed(() => sleepData.value !== null)
  const sleepScore = computed(() => sleepData.value?.sleepScore || 0)
  const totalSleepDuration = computed(() => sleepData.value?.totalSleepDuration || 0)
  const deepSleepDuration = computed(() => sleepData.value?.deepSleepDuration || 0)
  const remSleepDuration = computed(() => sleepData.value?.remSleepDuration || 0)
  const deepSleepPercentage = computed(() => {
    if (totalSleepDuration.value === 0) return 0
    return Math.round((deepSleepDuration.value / totalSleepDuration.value) * 100)
  })

  // 格式化睡眠时长
  const formatSleepDuration = (minutes) => {
    const hours = Math.floor(minutes / 60)
    const mins = minutes % 60
    return mins > 0 ? `${hours}小时${mins}分钟` : `${hours}小时`
  }

  // 获取睡眠评分等级
  const getSleepScoreLevel = (score) => {
    if (score >= 90) return { level: '优秀', color: 'text-green-600' }
    if (score >= 80) return { level: '良好', color: 'text-blue-600' }
    if (score >= 70) return { level: '一般', color: 'text-yellow-600' }
    if (score >= 60) return { level: '较差', color: 'text-orange-600' }
    return { level: '很差', color: 'text-red-600' }
  }

  // 获取深睡占比等级
  const getDeepSleepLevel = (percentage) => {
    if (percentage >= 25) return { level: '优秀', color: 'text-green-600' }
    if (percentage >= 20) return { level: '良好', color: 'text-blue-600' }
    if (percentage >= 15) return { level: '一般', color: 'text-yellow-600' }
    return { level: '较差', color: 'text-orange-600' }
  }

  // 动作
  const fetchSleepData = async (userId, dateOfSleep) => {
    try {
      loading.value = true
      error.value = null
      const response = await sleepApi.getSleepData(userId, dateOfSleep)
      sleepData.value = response.data
      return response.data
    } catch (err) {
      error.value = err.message
      throw err
    } finally {
      loading.value = false
    }
  }

  const fetchSleepHistory = async (userId) => {
    try {
      loading.value = true
      error.value = null
      const response = await sleepApi.getSleepHistory(userId)
      sleepHistory.value = response.data
      return response.data
    } catch (err) {
      error.value = err.message
      throw err
    } finally {
      loading.value = false
    }
  }

  const addSleepData = async (data) => {
    try {
      loading.value = true
      error.value = null
      const response = await sleepApi.addSleepData(data)
      sleepData.value = response.data
      return response.data
    } catch (err) {
      error.value = err.message
      throw err
    } finally {
      loading.value = false
    }
  }

  const initTestData = async () => {
    try {
      loading.value = true
      error.value = null
      const response = await sleepApi.initTestData()
      return response.data
    } catch (err) {
      error.value = err.message
      throw err
    } finally {
      loading.value = false
    }
  }

  const clearData = () => {
    sleepData.value = null
    sleepHistory.value = []
    error.value = null
  }

  // 强制刷新数据
  const refreshData = async (userId, dateOfSleep) => {
    try {
      // 清除缓存数据
      clearData()
      // 重新获取数据
      return await fetchSleepData(userId, dateOfSleep)
    } catch (err) {
      error.value = err.message
      throw err
    }
  }

  return {
    // 状态
    sleepData,
    sleepHistory,
    loading,
    error,
    // 计算属性
    hasSleepData,
    sleepScore,
    totalSleepDuration,
    deepSleepDuration,
    remSleepDuration,
    deepSleepPercentage,
    // 方法
    formatSleepDuration,
    getSleepScoreLevel,
    getDeepSleepLevel,
    fetchSleepData,
    fetchSleepHistory,
    addSleepData,
    initTestData,
    clearData,
    refreshData,
  }
})
