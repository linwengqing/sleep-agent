<template>
  <div class="interface-example">
    <h2 class="text-2xl font-bold mb-6">接口对接示例</h2>
    
    <!-- 睡眠数据示例 -->
    <div class="mb-8">
      <h3 class="text-lg font-semibold mb-4">睡眠数据接口示例</h3>
      <div class="bg-gray-100 p-4 rounded-lg">
        <div class="mb-4">
          <label class="block text-sm font-medium mb-2">用户ID:</label>
          <input 
            v-model="userId" 
            type="text" 
            class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            placeholder="请输入用户ID"
          />
        </div>
        <div class="mb-4">
          <label class="block text-sm font-medium mb-2">睡眠日期:</label>
          <input 
            v-model="sleepDate" 
            type="date" 
            class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
        </div>
        <button 
          @click="fetchSleepData" 
          :disabled="sleepStore.loading"
          class="px-4 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600 disabled:opacity-50"
        >
          {{ sleepStore.loading ? '加载中...' : '获取睡眠数据' }}
        </button>
        
        <div v-if="sleepStore.sleepData" class="mt-4 p-4 bg-white rounded-md">
          <h4 class="font-semibold mb-2">睡眠数据:</h4>
          <pre class="text-sm">{{ JSON.stringify(sleepStore.sleepData, null, 2) }}</pre>
        </div>
      </div>
    </div>

    <!-- 积分查询示例 -->
    <div class="mb-8">
      <h3 class="text-lg font-semibold mb-4">积分查询接口示例</h3>
      <div class="bg-gray-100 p-4 rounded-lg">
        <button 
          @click="fetchPoints" 
          :disabled="pointsStore.loading"
          class="px-4 py-2 bg-green-500 text-white rounded-md hover:bg-green-600 disabled:opacity-50"
        >
          {{ pointsStore.loading ? '加载中...' : '获取积分数据' }}
        </button>
        
        <div v-if="pointsStore.currentPoints" class="mt-4 p-4 bg-white rounded-md">
          <h4 class="font-semibold mb-2">积分数据:</h4>
          <p>当前积分: <span class="font-bold text-green-600">{{ pointsStore.currentPoints }}</span></p>
          <p>积分等级: <span class="font-bold">{{ pointsStore.pointsLevel.level }}</span></p>
        </div>
      </div>
    </div>

    <!-- AI对话示例 -->
    <div class="mb-8">
      <h3 class="text-lg font-semibold mb-4">AI对话接口示例</h3>
      <div class="bg-gray-100 p-4 rounded-lg">
        <div class="mb-4">
          <label class="block text-sm font-medium mb-2">消息内容:</label>
          <textarea 
            v-model="chatMessage" 
            class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            rows="3"
            placeholder="请输入您的问题..."
          ></textarea>
        </div>
        <button 
          @click="sendChatMessage" 
          :disabled="aiStore.isChatting"
          class="px-4 py-2 bg-purple-500 text-white rounded-md hover:bg-purple-600 disabled:opacity-50"
        >
          {{ aiStore.isChatting ? 'AI思考中...' : '发送消息' }}
        </button>
        
        <div v-if="aiStore.chatMessages.length > 0" class="mt-4 space-y-2">
          <h4 class="font-semibold">对话记录:</h4>
          <div 
            v-for="(message, index) in aiStore.chatMessages" 
            :key="index"
            class="p-3 rounded-md"
            :class="message.type === 'user' ? 'bg-blue-100 ml-8' : 'bg-gray-100 mr-8'"
          >
            <div class="flex justify-between items-start">
              <span class="font-medium">
                {{ message.type === 'user' ? '您' : 'AI助手' }}
              </span>
              <span class="text-xs text-gray-500">
                {{ formatTime(message.timestamp) }}
              </span>
            </div>
            <p class="mt-1">{{ message.content }}</p>
          </div>
        </div>
      </div>
    </div>

    <!-- AI报告生成示例 -->
    <div class="mb-8">
      <h3 class="text-lg font-semibold mb-4">AI报告生成接口示例</h3>
      <div class="bg-gray-100 p-4 rounded-lg">
        <button 
          @click="generateAIReport" 
          :disabled="aiStore.isGeneratingReport"
          class="px-4 py-2 bg-orange-500 text-white rounded-md hover:bg-orange-600 disabled:opacity-50"
        >
          {{ aiStore.isGeneratingReport ? '生成中...' : '生成AI报告' }}
        </button>
        
        <div v-if="aiStore.aiReport" class="mt-4 p-4 bg-white rounded-md">
          <h4 class="font-semibold mb-2">AI睡眠报告:</h4>
          <div class="whitespace-pre-wrap text-sm">{{ aiStore.aiReport }}</div>
        </div>
      </div>
    </div>

    <!-- 错误信息显示 -->
    <div v-if="errorMessage" class="mb-4 p-4 bg-red-100 border border-red-400 text-red-700 rounded-md">
      <h4 class="font-semibold">错误信息:</h4>
      <p>{{ errorMessage }}</p>
    </div>

    <!-- 调试信息 -->
    <div class="mt-8">
      <h3 class="text-lg font-semibold mb-4">调试信息</h3>
      <div class="bg-gray-900 text-green-400 p-4 rounded-lg font-mono text-sm">
        <div>用户ID: {{ userId }}</div>
        <div>睡眠日期: {{ sleepDate }}</div>
        <div>睡眠数据加载状态: {{ sleepStore.loading ? '加载中' : '空闲' }}</div>
        <div>积分数据加载状态: {{ pointsStore.loading ? '加载中' : '空闲' }}</div>
        <div>AI对话状态: {{ aiStore.isChatting ? '对话中' : '空闲' }}</div>
        <div>AI报告生成状态: {{ aiStore.isGeneratingReport ? '生成中' : '空闲' }}</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useSleepStore } from '@/stores/sleep'
import { usePointsStore } from '@/stores/points'
import { useAIStore } from '@/stores/ai'
import { useUserStore } from '@/stores/user'
import { API_CONFIG, VALIDATION_RULES } from '@/config/api'

// 使用stores
const sleepStore = useSleepStore()
const pointsStore = usePointsStore()
const aiStore = useAIStore()
const userStore = useUserStore()

// 响应式数据
const userId = ref('user001')
const sleepDate = ref('2024-01-01')
const chatMessage = ref('')
const errorMessage = ref('')

// 组件挂载时初始化
onMounted(() => {
  // 设置默认用户
  userStore.setCurrentUser({
    userId: userId.value,
    username: '睡眠用户'
  })
  
  // 设置默认日期为今天
  sleepDate.value = new Date().toISOString().split('T')[0]
})

// 获取睡眠数据
const fetchSleepData = async () => {
  try {
    errorMessage.value = ''
    
    // 参数验证
    if (!validateUserId(userId.value)) {
      errorMessage.value = VALIDATION_RULES.USER_ID.message
      return
    }
    
    if (!validateDate(sleepDate.value)) {
      errorMessage.value = VALIDATION_RULES.DATE.message
      return
    }
    
    // 调用API
    await sleepStore.fetchSleepData(userId.value, sleepDate.value)
    
    console.log('睡眠数据获取成功:', sleepStore.sleepData)
  } catch (error) {
    console.error('获取睡眠数据失败:', error)
    errorMessage.value = error.message || '获取睡眠数据失败'
  }
}

// 获取积分数据
const fetchPoints = async () => {
  try {
    errorMessage.value = ''
    
    if (!validateUserId(userId.value)) {
      errorMessage.value = VALIDATION_RULES.USER_ID.message
      return
    }
    
    await pointsStore.fetchCurrentPoints(userId.value)
    
    console.log('积分数据获取成功:', pointsStore.currentPoints)
  } catch (error) {
    console.error('获取积分数据失败:', error)
    errorMessage.value = error.message || '获取积分数据失败'
  }
}

// 发送聊天消息
const sendChatMessage = async () => {
  try {
    errorMessage.value = ''
    
    if (!validateUserId(userId.value)) {
      errorMessage.value = VALIDATION_RULES.USER_ID.message
      return
    }
    
    if (!validateMessage(chatMessage.value)) {
      errorMessage.value = VALIDATION_RULES.MESSAGE.message
      return
    }
    
    await aiStore.sendChatMessage(userId.value, chatMessage.value)
    chatMessage.value = '' // 清空输入框
    
    console.log('聊天消息发送成功')
  } catch (error) {
    console.error('发送聊天消息失败:', error)
    errorMessage.value = error.message || '发送聊天消息失败'
  }
}

// 生成AI报告
const generateAIReport = async () => {
  try {
    errorMessage.value = ''
    
    if (!validateUserId(userId.value)) {
      errorMessage.value = VALIDATION_RULES.USER_ID.message
      return
    }
    
    if (!validateDate(sleepDate.value)) {
      errorMessage.value = VALIDATION_RULES.DATE.message
      return
    }
    
    await aiStore.generateSleepReport(userId.value, sleepDate.value)
    
    console.log('AI报告生成成功:', aiStore.aiReport)
  } catch (error) {
    console.error('生成AI报告失败:', error)
    errorMessage.value = error.message || '生成AI报告失败'
  }
}

// 格式化时间
const formatTime = (timestamp) => {
  return new Date(timestamp).toLocaleTimeString()
}

// 验证函数
const validateUserId = (id) => {
  return VALIDATION_RULES.USER_ID.pattern.test(id)
}

const validateDate = (date) => {
  return VALIDATION_RULES.DATE.pattern.test(date)
}

const validateMessage = (message) => {
  return message.trim().length >= VALIDATION_RULES.MESSAGE.minLength && 
         message.length <= VALIDATION_RULES.MESSAGE.maxLength
}
</script>

<style scoped>
.interface-example {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

pre {
  white-space: pre-wrap;
  word-wrap: break-word;
}
</style>
