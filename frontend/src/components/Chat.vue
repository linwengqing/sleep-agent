<template>
  <div class="bg-white rounded-lg shadow-card p-4 sm:p-6">
    <!-- 标题和时间 -->
    <div class="flex items-center justify-between mb-4">
      <div class="flex items-center space-x-3">
        <FontAwesomeIcon 
          :icon="['fas', 'comments']" 
          class="h-6 w-6 text-sleep-600"
        />
        <h2 class="text-xl font-semibold text-gray-900">睡眠咨询</h2>
      </div>
      <div class="text-sm text-gray-500">
        {{ formatTime(new Date()) }}
      </div>
    </div>

    <!-- 聊天消息区域 -->
    <div 
      ref="chatContainer"
      class="h-80 sm:h-96 overflow-y-auto space-y-3 mb-4 pr-2 scrollbar-thin scrollbar-thumb-gray-300 scrollbar-track-gray-100"
    >
      <!-- 欢迎消息 -->
      <div v-if="messages.length === 0" class="text-center py-8">
        <FontAwesomeIcon 
          :icon="['fas', 'robot']" 
          class="h-12 w-12 text-sleep-400 mx-auto mb-4"
        />
        <p class="text-gray-600 mb-2">您好！我是您的睡眠健康助手</p>
        <p class="text-sm text-gray-500">有什么睡眠问题可以随时咨询我</p>
      </div>

      <!-- 消息列表 -->
      <div 
        v-for="(message, index) in messages" 
        :key="index"
        class="flex"
        :class="message.type === 'user' ? 'justify-end' : 'justify-start'"
      >
        <div 
          class="max-w-xs lg:max-w-md px-4 py-3 rounded-2xl"
          :class="[
            message.type === 'user' 
              ? 'bg-primary-600 text-white' 
              : 'bg-gray-100 text-gray-900',
            'animate-fade-in'
          ]"
        >
          <!-- 消息内容 -->
          <div class="text-sm leading-relaxed whitespace-pre-wrap">
            {{ message.content }}
          </div>
          
          <!-- 消息时间 -->
          <div 
            class="text-xs mt-2"
            :class="message.type === 'user' ? 'text-primary-100' : 'text-gray-500'"
          >
            {{ formatTime(message.timestamp) }}
          </div>
        </div>
      </div>

      <!-- 正在输入指示器 -->
      <div v-if="isTyping" class="flex justify-start">
        <div class="bg-gray-100 text-gray-900 px-4 py-3 rounded-2xl max-w-xs">
          <div class="flex items-center space-x-2">
            <div class="flex space-x-1">
              <div class="w-2 h-2 bg-gray-400 rounded-full animate-bounce"></div>
              <div class="w-2 h-2 bg-gray-400 rounded-full animate-bounce" style="animation-delay: 0.1s"></div>
              <div class="w-2 h-2 bg-gray-400 rounded-full animate-bounce" style="animation-delay: 0.2s"></div>
            </div>
            <span class="text-xs text-gray-500">AI正在思考...</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 输入区域 -->
    <div class="flex items-end space-x-3">
      <div class="flex-1 relative">
        <textarea
          v-model="inputMessage"
          @keydown="handleKeyDown"
          @input="handleInput"
          placeholder="输入你的睡眠问题..."
          class="w-full px-4 py-3 pr-12 border border-gray-300 rounded-lg focus:ring-2 focus:ring-sleep-500 focus:border-transparent resize-none"
          rows="1"
          :disabled="isTyping"
        ></textarea>
        
        <!-- 字符计数 -->
        <div class="absolute bottom-2 right-2 text-xs text-gray-400">
          {{ inputMessage.length }}/500
        </div>
      </div>
      
      <button
        @click="sendMessage"
        :disabled="!inputMessage.trim() || isTyping"
        class="flex-shrink-0 w-12 h-12 bg-primary-600 text-white rounded-lg hover:bg-primary-700 disabled:opacity-50 disabled:cursor-not-allowed transition-colors duration-200 flex items-center justify-center"
      >
        <FontAwesomeIcon 
          :icon="['fas', 'paper-plane']" 
          class="h-5 w-5"
        />
      </button>
    </div>

    <!-- 快捷问题 -->
    <div v-if="messages.length === 0" class="mt-4">
      <p class="text-sm text-gray-600 mb-3">常见问题：</p>
      <div class="flex flex-wrap gap-2">
        <button
          v-for="(question, index) in quickQuestions"
          :key="index"
          @click="sendQuickQuestion(question)"
          class="px-3 py-1 text-sm text-sleep-600 bg-sleep-50 hover:bg-sleep-100 rounded-full transition-colors duration-200"
        >
          {{ question }}
        </button>
      </div>
    </div>

    <!-- 清空对话按钮 -->
    <div v-if="messages.length > 0" class="mt-4 text-center">
      <button
        @click="clearMessages"
        class="text-sm text-gray-500 hover:text-gray-700 transition-colors duration-200"
      >
        <FontAwesomeIcon 
          :icon="['fas', 'trash']" 
          class="h-4 w-4 mr-1"
        />
        清空对话
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, nextTick, onMounted, onUnmounted } from 'vue'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import { useAIStore } from '@/stores/ai'

// 定义props
const props = defineProps({
  userId: {
    type: String,
    default: 'user001'
  }
})

// 定义emits
const emit = defineEmits(['send-message'])

// 使用stores
const aiStore = useAIStore()

// 响应式数据
const messages = ref([])
const inputMessage = ref('')
const isTyping = ref(false)
const chatContainer = ref(null)

// 快捷问题
const quickQuestions = ref([
  '我最近总凌晨3点醒，怎么办？',
  '如何提高睡眠质量？',
  '睡前应该做什么？',
  '失眠的原因有哪些？',
  '如何建立良好的睡眠习惯？'
])

// 模拟AI回复的预设回答
const aiResponses = {
  '我最近总凌晨3点醒，怎么办？': '凌晨早醒可能与睡前压力或褪黑素不足有关，建议：\n1. 睡前1小时做5分钟深呼吸放松；\n2. 避免睡前看手机（蓝光会延迟睡眠周期）；\n3. 保持卧室温度在18-22℃。',
  '如何提高睡眠质量？': '提高睡眠质量的方法：\n1. 保持规律的睡眠时间；\n2. 睡前1小时避免电子设备；\n3. 创造舒适的睡眠环境；\n4. 适度运动，但避免睡前剧烈运动；\n5. 避免睡前大量进食。',
  '睡前应该做什么？': '睡前建议活动：\n1. 阅读轻松书籍；\n2. 听轻柔音乐；\n3. 做简单拉伸或冥想；\n4. 写日记记录一天；\n5. 准备第二天衣物。',
  '失眠的原因有哪些？': '失眠常见原因：\n1. 心理因素：压力、焦虑、抑郁；\n2. 环境因素：噪音、光线、温度；\n3. 生活习惯：咖啡因、酒精、不规律作息；\n4. 身体因素：疼痛、疾病、药物；\n5. 年龄因素：老年人睡眠需求减少。',
  '如何建立良好的睡眠习惯？': '建立良好睡眠习惯：\n1. 固定睡眠时间，包括周末；\n2. 睡前1小时开始放松；\n3. 保持卧室安静、黑暗、凉爽；\n4. 避免午睡过长（超过30分钟）；\n5. 规律运动，但避免睡前3小时运动。'
}

// 方法
const sendMessage = async () => {
  if (!inputMessage.value.trim() || isTyping.value) return

  const userMessage = {
    type: 'user',
    content: inputMessage.value.trim(),
    timestamp: new Date()
  }

  messages.value.push(userMessage)
  const currentInput = inputMessage.value.trim()
  inputMessage.value = ''

  // 滚动到底部
  await nextTick()
  scrollToBottom()

  // 显示正在输入
  isTyping.value = true

  try {
    // 调用AI服务
    const aiResponse = await aiStore.sendChatMessage(props.userId, currentInput)
    
    const aiMessage = {
      type: 'ai',
      content: aiResponse,
      timestamp: new Date()
    }

    messages.value.push(aiMessage)
    
    // 滚动到底部
    await nextTick()
    scrollToBottom()

    // 发送消息事件
    emit('send-message', {
      user: currentInput,
      ai: aiResponse
    })
  } catch (error) {
    console.error('发送消息失败:', error)
    
    const errorMessage = {
      type: 'ai',
      content: '抱歉，我暂时无法回复您的消息，请稍后再试。',
      timestamp: new Date()
    }
    
    messages.value.push(errorMessage)
  } finally {
    isTyping.value = false
  }
}

const sendQuickQuestion = (question) => {
  inputMessage.value = question
  sendMessage()
}

const generateAIResponse = (userInput) => {
  // 简单的关键词匹配
  const lowerInput = userInput.toLowerCase()
  
  // 检查是否有预设回答
  for (const [question, answer] of Object.entries(aiResponses)) {
    if (lowerInput.includes(question.toLowerCase().substring(0, 10))) {
      return answer
    }
  }

  // 基于关键词生成回复
  if (lowerInput.includes('失眠') || lowerInput.includes('睡不着')) {
    return '失眠是很常见的问题，建议：\n1. 睡前1小时避免电子设备；\n2. 尝试4-7-8呼吸法；\n3. 保持卧室环境舒适；\n4. 如果持续失眠，建议咨询医生。'
  }
  
  if (lowerInput.includes('早醒') || lowerInput.includes('凌晨')) {
    return '早醒可能与压力或生物钟有关，建议：\n1. 睡前放松练习；\n2. 避免睡前看时间；\n3. 保持规律作息；\n4. 考虑遮光窗帘。'
  }
  
  if (lowerInput.includes('质量') || lowerInput.includes('改善')) {
    return '改善睡眠质量的方法：\n1. 保持规律作息；\n2. 睡前1小时开始放松；\n3. 创造舒适睡眠环境；\n4. 适度运动；\n5. 避免睡前大量进食。'
  }

  // 默认回复
  return '感谢您的咨询！关于睡眠问题，我建议：\n1. 保持规律的睡眠时间；\n2. 睡前1小时避免电子设备；\n3. 创造舒适的睡眠环境；\n4. 如有持续问题，建议咨询专业医生。\n\n还有其他问题吗？'
}

const handleKeyDown = (event) => {
  if (event.key === 'Enter' && !event.shiftKey) {
    event.preventDefault()
    sendMessage()
  }
}

const handleInput = () => {
  // 自动调整textarea高度
  const textarea = event.target
  textarea.style.height = 'auto'
  textarea.style.height = Math.min(textarea.scrollHeight, 120) + 'px'
}

const clearMessages = async () => {
  try {
    await aiStore.clearChatHistory(props.userId)
    messages.value = []
  } catch (error) {
    console.error('清空对话失败:', error)
  }
}

const scrollToBottom = () => {
  if (chatContainer.value) {
    chatContainer.value.scrollTop = chatContainer.value.scrollHeight
  }
}

const formatTime = (date) => {
  const now = new Date()
  const diff = now - date
  const minutes = Math.floor(diff / 60000)
  
  if (minutes < 1) return '刚刚'
  if (minutes < 60) return `${minutes}分钟前`
  
  const hours = Math.floor(minutes / 60)
  if (hours < 24) return `${hours}小时前`
  
  return date.toLocaleDateString('zh-CN', {
    month: 'short',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  })
}

// 生命周期
onMounted(() => {
  // 添加示例对话
  if (messages.value.length === 0) {
    // 可以在这里添加一些示例对话
  }
})

onUnmounted(() => {
  // 清理工作
})
</script>

<style scoped>
/* 自定义滚动条 */
.scrollbar-thin {
  scrollbar-width: thin;
}

.scrollbar-thumb-gray-300::-webkit-scrollbar-thumb {
  background-color: #d1d5db;
  border-radius: 0.5rem;
}

.scrollbar-track-gray-100::-webkit-scrollbar-track {
  background-color: #f3f4f6;
}

.scrollbar-thin::-webkit-scrollbar {
  width: 6px;
}

/* 淡入动画 */
.animate-fade-in {
  animation: fadeIn 0.5s ease-in-out;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 消息气泡样式 */
.max-w-xs {
  max-width: 16rem;
}

@media (min-width: 1024px) {
  .lg\:max-w-md {
    max-width: 28rem;
  }
}

/* 输入框样式 */
textarea:focus {
  outline: none;
  box-shadow: 0 0 0 2px rgba(59, 130, 246, 0.5);
}

/* 按钮悬停效果 */
button:hover:not(:disabled) {
  transform: translateY(-1px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

/* 快捷问题按钮样式 */
.quick-question-btn {
  transition: all 0.2s ease;
}

.quick-question-btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}
</style>
