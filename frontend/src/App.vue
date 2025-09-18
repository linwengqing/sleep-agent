<template>
  <div id="app" class="min-h-screen bg-gray-50">
    <!-- 顶部导航栏 -->
    <Navbar 
      :user-id="currentUserId"
      @ai-assistant="handleAIAssistant"
      @show-points-history="showPointsHistory"
    />

    <!-- 主要内容区域 -->
    <main class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-2 sm:py-4 lg:py-6">
      <!-- 移动端布局 -->
      <div class="block lg:hidden space-y-3 sm:space-y-4">
        <!-- 睡眠数据概览 -->
        <div class="animate-fade-in">
          <SleepCards 
            :user-id="currentUserId"
            :date-of-sleep="currentDate"
            @show-detail="handleShowDetail"
            @init-test-data="handleInitTestData"
          />
        </div>

        <!-- 睡眠分析报告 -->
        <div class="animate-fade-in" style="animation-delay: 0.1s">
          <SleepReport 
            :user-id="currentUserId"
            :date-of-sleep="currentDate"
            @refresh="handleReportRefresh"
            @init-test-data="handleInitTestData"
          />
        </div>

        <!-- 睡眠咨询 -->
        <div class="animate-fade-in" style="animation-delay: 0.2s">
          <Chat 
            @send-message="handleChatMessage"
          />
        </div>
      </div>

      <!-- 桌面端布局 -->
      <div class="hidden lg:block">
        <!-- 第一行：数据概览和报告 -->
        <div class="grid grid-cols-1 xl:grid-cols-2 gap-4 mb-4">
          <!-- 睡眠数据概览 -->
          <div class="animate-fade-in">
            <SleepCards 
              :user-id="currentUserId"
              :date-of-sleep="currentDate"
              @show-detail="handleShowDetail"
              @init-test-data="handleInitTestData"
            />
          </div>

          <!-- 睡眠分析报告 -->
          <div class="animate-fade-in" style="animation-delay: 0.1s">
            <SleepReport 
              :user-id="currentUserId"
              :date-of-sleep="currentDate"
              @refresh="handleReportRefresh"
              @init-test-data="handleInitTestData"
            />
          </div>
        </div>

        <!-- 第二行：睡眠咨询 -->
        <div class="grid grid-cols-1 gap-4">
          <div class="animate-fade-in" style="animation-delay: 0.2s">
            <Chat 
              @send-message="handleChatMessage"
            />
          </div>
        </div>
      </div>
    </main>

    <!-- 积分历史模态框 -->
    <div v-if="showPointsModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
      <div class="bg-white rounded-lg max-w-2xl w-full max-h-96 overflow-y-auto">
        <div class="p-6">
          <div class="flex items-center justify-between mb-4">
            <h3 class="text-xl font-semibold text-gray-900">积分历史</h3>
            <button
              @click="closePointsModal"
              class="text-gray-400 hover:text-gray-600 transition-colors"
            >
              <FontAwesomeIcon 
                :icon="['fas', 'times']" 
                class="h-6 w-6"
              />
            </button>
          </div>
          
          <div v-if="pointsStore.loading" class="flex justify-center py-8">
            <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-sleep-600"></div>
          </div>
          
          <div v-else-if="pointsStore.pointsHistory.length === 0" class="text-center py-8">
            <FontAwesomeIcon 
              :icon="['fas', 'star']" 
              class="h-12 w-12 text-gray-400 mx-auto mb-4"
            />
            <p class="text-gray-600">暂无积分记录</p>
          </div>
          
          <div v-else class="space-y-4">
            <div
              v-for="(point, index) in pointsStore.pointsHistory"
              :key="index"
              class="flex items-center justify-between p-4 bg-gray-50 rounded-lg"
            >
              <div class="flex items-center space-x-3">
                <FontAwesomeIcon 
                  :icon="['fas', 'star']" 
                  class="h-5 w-5 text-yellow-500"
                />
                <div>
                  <p class="font-medium text-gray-900">{{ point.date }}</p>
                  <p class="text-sm text-gray-500">睡眠积分</p>
                </div>
              </div>
              <div class="text-right">
                <p class="text-lg font-bold text-sleep-600">+{{ point.points }} SP</p>
                <p class="text-xs text-gray-500">{{ formatDateTime(point.createdAt) }}</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 全局加载遮罩 -->
    <div v-if="globalLoading" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white rounded-lg p-6 flex flex-col items-center space-y-4">
        <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-sleep-600"></div>
        <p class="text-gray-600">{{ loadingMessage }}</p>
      </div>
    </div>

    <!-- 全局通知 -->
    <div v-if="notification.show" class="fixed top-4 right-4 z-50">
      <div 
        class="px-6 py-4 rounded-lg shadow-lg flex items-center space-x-3 animate-slide-right"
        :class="notification.type === 'success' ? 'bg-green-500 text-white' : 'bg-red-500 text-white'"
      >
        <FontAwesomeIcon 
          :icon="notification.type === 'success' ? ['fas', 'check-circle'] : ['fas', 'exclamation-circle']" 
          class="h-5 w-5"
        />
        <span>{{ notification.message }}</span>
        <button
          @click="hideNotification"
          class="ml-2 text-white hover:text-gray-200"
        >
          <FontAwesomeIcon 
            :icon="['fas', 'times']" 
            class="h-4 w-4"
          />
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import { useSleepStore } from '@/stores/sleep'
import { usePointsStore } from '@/stores/points'

// 组件导入
import Navbar from './components/Navbar.vue'
import SleepCards from './components/SleepCards.vue'
import SleepReport from './components/SleepReport.vue'
import Chat from './components/Chat.vue'

// 响应式数据
const currentUserId = ref('user001')
const currentDate = ref(new Date().toISOString().split('T')[0])
const showPointsModal = ref(false)
const globalLoading = ref(false)
const loadingMessage = ref('')
const notification = ref({
  show: false,
  type: 'success',
  message: ''
})

// 使用stores
const sleepStore = useSleepStore()
const pointsStore = usePointsStore()

// 方法

const handleAIAssistant = () => {
  showNotification('AI助手功能开发中...', 'info')
}

const showPointsHistory = async () => {
  try {
    showPointsModal.value = true
    await pointsStore.fetchPointsHistory(currentUserId.value)
  } catch (error) {
    showNotification('获取积分历史失败', 'error')
  }
}

const closePointsModal = () => {
  showPointsModal.value = false
}

const handleShowDetail = (detailType) => {
  console.log('显示详情:', detailType)
  showNotification(`查看${detailType}详情`, 'success')
}

const handleInitTestData = async () => {
  try {
    globalLoading.value = true
    loadingMessage.value = '正在初始化测试数据...'
    
    await sleepStore.initTestData()
    
    // 初始化测试数据后，自动生成积分
    loadingMessage.value = '正在生成积分...'
    try {
      await pointsStore.generatePoints(currentUserId.value, currentDate.value)
      showNotification('测试数据和积分生成成功', 'success')
    } catch (pointsError) {
      console.log('积分生成失败，但测试数据已生成:', pointsError)
      showNotification('测试数据初始化成功，积分生成失败', 'warning')
    }
    
    // 刷新积分显示
    await pointsStore.fetchCurrentPoints(currentUserId.value)
  } catch (error) {
    showNotification('初始化测试数据失败', 'error')
  } finally {
    globalLoading.value = false
  }
}

const handleReportRefresh = () => {
  showNotification('睡眠报告已刷新', 'success')
}

const handleChatMessage = (messageData) => {
  console.log('聊天消息:', messageData)
  // 这里可以添加消息处理逻辑
}


const showNotification = (message, type = 'success') => {
  notification.value = {
    show: true,
    type,
    message
  }
  
  // 根据类型设置不同的显示时间
  const duration = type === 'error' ? 5000 : 3000
  setTimeout(() => {
    hideNotification()
  }, duration)
}

const hideNotification = () => {
  notification.value.show = false
}

const formatDateTime = (dateString) => {
  const date = new Date(dateString)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

// 初始化数据
const initializeData = async () => {
  try {
    globalLoading.value = true
    loadingMessage.value = '正在加载数据...'
    
    // 并行加载睡眠数据和积分数据
    await Promise.all([
      sleepStore.fetchSleepData(currentUserId.value, currentDate.value),
      pointsStore.fetchCurrentPoints(currentUserId.value)
    ])
    
  } catch (error) {
    console.error('初始化数据失败:', error)
    showNotification('数据加载失败，请刷新页面重试', 'error')
  } finally {
    globalLoading.value = false
  }
}

// 键盘快捷键
const handleKeyDown = (event) => {
  // Ctrl/Cmd + K 打开知识库搜索
  if ((event.ctrlKey || event.metaKey) && event.key === 'k') {
    event.preventDefault()
    // 这里可以添加聚焦到知识库搜索框的逻辑
  }
  
  // Esc 关闭模态框
  if (event.key === 'Escape') {
    if (showPointsModal.value) {
      closePointsModal()
    }
  }
}

// 自动刷新数据
const autoRefreshData = async () => {
  try {
    await sleepStore.refreshData(currentUserId.value, currentDate.value)
    console.log('数据自动刷新成功')
  } catch (error) {
    console.error('自动刷新数据失败:', error)
    // 不显示错误通知，避免干扰用户
  }
}

// 生命周期
onMounted(() => {
  initializeData()
  document.addEventListener('keydown', handleKeyDown)
  
  // 设置自动刷新（每5分钟）
  const refreshInterval = setInterval(autoRefreshData, 5 * 60 * 1000)
  
  // 页面可见性变化时刷新数据
  const handleVisibilityChange = () => {
    if (!document.hidden) {
      autoRefreshData()
    }
  }
  document.addEventListener('visibilitychange', handleVisibilityChange)
  
  // 清理函数
  onUnmounted(() => {
    clearInterval(refreshInterval)
    document.removeEventListener('keydown', handleKeyDown)
    document.removeEventListener('visibilitychange', handleVisibilityChange)
  })
})
</script>

<style scoped>
/* 自定义动画延迟 */
.animate-fade-in:nth-child(1) {
  animation-delay: 0s;
}

.animate-fade-in:nth-child(2) {
  animation-delay: 0.1s;
}

.animate-fade-in:nth-child(3) {
  animation-delay: 0.2s;
}

.animate-fade-in:nth-child(4) {
  animation-delay: 0.3s;
}

/* 模态框动画 */
.fixed {
  animation: fadeIn 0.3s ease-out;
}

/* 通知动画 */
.animate-slide-right {
  animation: slideInRight 0.3s ease-out;
}

@keyframes slideInRight {
  from {
    opacity: 0;
    transform: translateX(100%);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

/* 响应式调整 */
@media (max-width: 1024px) {
  .animate-fade-in {
    animation-delay: 0s !important;
  }
}

/* 深色模式支持 */
@media (prefers-color-scheme: dark) {
  .bg-gray-50 {
    background-color: #111827;
  }
  
  .bg-white {
    background-color: #1f2937;
  }
}
</style>
