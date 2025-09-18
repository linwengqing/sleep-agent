import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { aiApi } from '@/api/ai'

export const useAIStore = defineStore('ai', () => {
  // 状态
  const aiReport = ref('')
  const chatMessages = ref([])
  const isGeneratingReport = ref(false)
  const isChatting = ref(false)
  const error = ref(null)

  // 计算属性
  const hasAIReport = computed(() => aiReport.value !== '')
  const hasChatMessages = computed(() => chatMessages.value.length > 0)
  const lastChatMessage = computed(() => {
    return chatMessages.value.length > 0 
      ? chatMessages.value[chatMessages.value.length - 1] 
      : null
  })

  // 格式化AI报告
  const formatAIReport = (report) => {
    if (!report) return ''
    
    // 简单的格式化处理
    return report
      .replace(/【优势】/g, '\n\n**优势**\n')
      .replace(/【不足】/g, '\n\n**不足**\n')
      .replace(/【建议】/g, '\n\n**建议**\n')
      .replace(/\n\n/g, '\n')
      .trim()
  }

  // 解析AI报告内容
  const parseAIReport = (report) => {
    if (!report) return { advantages: [], disadvantages: [], suggestions: [] }
    
    const sections = {
      advantages: [],
      disadvantages: [],
      suggestions: []
    }
    
    try {
      // 简单的解析逻辑
      const lines = report.split('\n')
      let currentSection = null
      
      for (const line of lines) {
        const trimmedLine = line.trim()
        
        if (trimmedLine.includes('优势')) {
          currentSection = 'advantages'
        } else if (trimmedLine.includes('不足')) {
          currentSection = 'disadvantages'
        } else if (trimmedLine.includes('建议')) {
          currentSection = 'suggestions'
        } else if (trimmedLine && currentSection) {
          sections[currentSection].push(trimmedLine)
        }
      }
    } catch (error) {
      console.error('解析AI报告失败:', error)
    }
    
    return sections
  }

  // 动作
  const generateSleepReport = async (userId, dateOfSleep) => {
    try {
      isGeneratingReport.value = true
      error.value = null
      
      const response = await aiApi.generateSleepReport(userId, dateOfSleep)
      aiReport.value = response.data
      
      return response.data
    } catch (err) {
      error.value = err.message
      throw err
    } finally {
      isGeneratingReport.value = false
    }
  }

  const sendChatMessage = async (userId, message) => {
    try {
      isChatting.value = true
      error.value = null
      
      // 添加用户消息到聊天记录
      chatMessages.value.push({
        type: 'user',
        content: message,
        timestamp: new Date()
      })
      
      const response = await aiApi.chatWithAI(userId, message)
      const aiResponse = response.data
      
      // 添加AI回复到聊天记录
      chatMessages.value.push({
        type: 'ai',
        content: aiResponse,
        timestamp: new Date()
      })
      
      return aiResponse
    } catch (err) {
      error.value = err.message
      
      // 添加错误消息到聊天记录
      chatMessages.value.push({
        type: 'ai',
        content: '抱歉，我暂时无法回复您的消息，请稍后重试。',
        timestamp: new Date(),
        isError: true
      })
      
      throw err
    } finally {
      isChatting.value = false
    }
  }

  const clearChatHistory = async (userId) => {
    try {
      await aiApi.clearChatHistory(userId)
      chatMessages.value = []
      return true
    } catch (err) {
      error.value = err.message
      throw err
    }
  }

  const getChatStatistics = async (userId) => {
    try {
      const response = await aiApi.getChatStatistics(userId)
      return response.data
    } catch (err) {
      error.value = err.message
      throw err
    }
  }

  const getHelp = async () => {
    try {
      const response = await aiApi.getHelp()
      return response.data
    } catch (err) {
      error.value = err.message
      throw err
    }
  }

  const healthCheck = async () => {
    try {
      const response = await aiApi.healthCheck()
      return response.data
    } catch (err) {
      error.value = err.message
      throw err
    }
  }

  const clearData = () => {
    aiReport.value = ''
    chatMessages.value = []
    error.value = null
  }

  return {
    // 状态
    aiReport,
    chatMessages,
    isGeneratingReport,
    isChatting,
    error,
    // 计算属性
    hasAIReport,
    hasChatMessages,
    lastChatMessage,
    // 方法
    formatAIReport,
    parseAIReport,
    generateSleepReport,
    sendChatMessage,
    clearChatHistory,
    getChatStatistics,
    getHelp,
    healthCheck,
    clearData,
  }
})
