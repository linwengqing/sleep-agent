import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { pointsApi } from '@/api/points'

export const usePointsStore = defineStore('points', () => {
  // 状态
  const currentPoints = ref(0)
  const pointsHistory = ref([])
  const loading = ref(false)
  const error = ref(null)

  // 计算属性
  const hasPoints = computed(() => currentPoints.value > 0)
  const pointsLevel = computed(() => {
    if (currentPoints.value >= 1000) return { level: '睡眠大师', color: 'text-purple-600' }
    if (currentPoints.value >= 500) return { level: '睡眠专家', color: 'text-blue-600' }
    if (currentPoints.value >= 200) return { level: '睡眠达人', color: 'text-green-600' }
    if (currentPoints.value >= 100) return { level: '睡眠新手', color: 'text-yellow-600' }
    return { level: '睡眠初学者', color: 'text-gray-600' }
  })

  // 获取积分等级颜色
  const getPointsColor = (points) => {
    if (points >= 1000) return 'text-purple-600'
    if (points >= 500) return 'text-blue-600'
    if (points >= 200) return 'text-green-600'
    if (points >= 100) return 'text-yellow-600'
    return 'text-gray-600'
  }

  // 格式化积分显示
  const formatPoints = (points) => {
    if (points >= 1000) {
      return `${(points / 1000).toFixed(1)}K`
    }
    return points.toString()
  }

  // 动作
  const fetchCurrentPoints = async (userId) => {
    try {
      loading.value = true
      error.value = null
      const response = await pointsApi.getCurrentPoints(userId)
      currentPoints.value = response.data || 0
      return response.data
    } catch (err) {
      error.value = err.message
      throw err
    } finally {
      loading.value = false
    }
  }

  const fetchPointsHistory = async (userId) => {
    try {
      loading.value = true
      error.value = null
      const response = await pointsApi.getPointsHistory(userId)
      pointsHistory.value = response.data || []
      return response.data
    } catch (err) {
      error.value = err.message
      throw err
    } finally {
      loading.value = false
    }
  }

  const generatePoints = async (userId, dateOfSleep) => {
    try {
      loading.value = true
      error.value = null
      const response = await pointsApi.generatePoints(userId, dateOfSleep)
      // 重新获取当前积分
      await fetchCurrentPoints(userId)
      return response.data
    } catch (err) {
      error.value = err.message
      throw err
    } finally {
      loading.value = false
    }
  }

  const getPointsRecord = async (userId, date) => {
    try {
      loading.value = true
      error.value = null
      const response = await pointsApi.getPointsRecord(userId, date)
      return response.data
    } catch (err) {
      error.value = err.message
      throw err
    } finally {
      loading.value = false
    }
  }

  const clearData = () => {
    currentPoints.value = 0
    pointsHistory.value = []
    error.value = null
  }

  return {
    // 状态
    currentPoints,
    pointsHistory,
    loading,
    error,
    // 计算属性
    hasPoints,
    pointsLevel,
    // 方法
    getPointsColor,
    formatPoints,
    fetchCurrentPoints,
    fetchPointsHistory,
    generatePoints,
    getPointsRecord,
    clearData,
  }
})
