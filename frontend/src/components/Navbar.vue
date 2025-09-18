<template>
  <nav class="bg-white shadow-sm border-b border-gray-200 sticky top-0 z-50">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <div class="flex justify-between items-center h-16">
        <!-- 左侧：Logo和标题 -->
        <div class="flex items-center space-x-3">
          <div class="flex-shrink-0">
            <FontAwesomeIcon 
              :icon="['fas', 'moon']" 
              class="h-8 w-8 text-sleep-600"
            />
          </div>
          <h1 class="text-xl font-bold text-gray-900">
            睡眠健康助手
          </h1>
        </div>

        <!-- 中间：积分显示 -->
        <div class="hidden sm:flex items-center space-x-4">
          <div class="flex items-center space-x-2 bg-gradient-to-r from-sleep-50 to-primary-50 px-3 py-2 rounded-full border border-sleep-200">
            <FontAwesomeIcon 
              :icon="['fas', 'star']" 
              class="h-4 w-4 text-yellow-500"
            />
            <span class="text-xs font-medium text-gray-700">积分</span>
            <div class="flex items-center space-x-1">
              <span 
                class="text-sm font-bold"
                :class="pointsStore.pointsLevel.color"
              >
                {{ pointsStore.formatPoints(pointsStore.currentPoints) }}
              </span>
              <span class="text-xs text-gray-500">SP</span>
            </div>
          </div>
          
          <!-- 积分等级徽章 -->
          <div 
            class="px-2 py-1 rounded-full text-xs font-medium"
            :class="[
              pointsStore.pointsLevel.color,
              'bg-opacity-10'
            ]"
          >
            {{ pointsStore.pointsLevel.level }}
          </div>
        </div>

        <!-- 右侧：用户操作 -->
        <div class="flex items-center space-x-4">
          <!-- 移动端积分显示 -->
          <div class="sm:hidden flex items-center space-x-1">
            <FontAwesomeIcon 
              :icon="['fas', 'star']" 
              class="h-4 w-4 text-yellow-500"
            />
            <span 
              class="text-sm font-bold"
              :class="pointsStore.pointsLevel.color"
            >
              {{ pointsStore.formatPoints(pointsStore.currentPoints) }}
            </span>
            <span class="text-xs text-gray-500">SP</span>
          </div>


          <!-- AI助手按钮 -->
          <button 
            class="flex items-center space-x-1 sm:space-x-2 px-2 sm:px-4 py-2 text-sm font-medium text-white bg-primary-600 hover:bg-primary-700 rounded-lg transition-colors duration-200"
            @click="handleAIAssistant"
          >
            <FontAwesomeIcon 
              :icon="['fas', 'robot']" 
              class="h-4 w-4"
            />
            <span class="hidden sm:inline">AI助手</span>
          </button>

          <!-- 生成积分按钮 -->
          <button 
            class="p-2 text-yellow-600 hover:text-yellow-700 hover:bg-yellow-50 rounded-lg transition-colors duration-200"
            @click="generatePoints"
            :disabled="pointsStore.loading"
            title="生成积分"
          >
            <FontAwesomeIcon 
              :icon="['fas', 'star']" 
              class="h-4 w-4"
            />
          </button>

          <!-- 刷新积分按钮 -->
          <button 
            class="p-2 text-gray-500 hover:text-primary-600 hover:bg-gray-50 rounded-lg transition-colors duration-200"
            @click="refreshPoints"
            :disabled="pointsStore.loading"
            title="刷新积分"
          >
            <FontAwesomeIcon 
              :icon="['fas', 'sync-alt']" 
              class="h-4 w-4"
              :class="{ 'animate-spin': pointsStore.loading }"
            />
          </button>
        </div>
      </div>
    </div>

    <!-- 移动端积分详情 -->
    <div class="sm:hidden bg-gray-50 border-t border-gray-200 px-4 py-2">
      <div class="flex items-center justify-between">
        <div class="flex items-center space-x-2">
          <span class="text-xs text-gray-600">等级：</span>
          <span 
            class="text-xs font-medium"
            :class="pointsStore.pointsLevel.color"
          >
            {{ pointsStore.pointsLevel.level }}
          </span>
        </div>
        <button 
          class="text-xs text-primary-600 hover:text-primary-700"
          @click="showPointsHistory"
        >
          历史
        </button>
      </div>
    </div>
  </nav>
</template>

<script setup>
import { onMounted } from 'vue'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import { usePointsStore } from '@/stores/points'

// 定义props
const props = defineProps({
  userId: {
    type: String,
    default: 'user001'
  }
})

// 定义emits
const emit = defineEmits(['ai-assistant', 'show-points-history'])

// 使用stores
const pointsStore = usePointsStore()

// 方法
const handleAIAssistant = () => {
  emit('ai-assistant')
}

const generatePoints = async () => {
  try {
    const today = new Date().toISOString().split('T')[0]
    await pointsStore.generatePoints(props.userId, today)
    // 生成积分后刷新显示
    await pointsStore.fetchCurrentPoints(props.userId)
  } catch (error) {
    console.error('生成积分失败:', error)
  }
}

const refreshPoints = async () => {
  try {
    await pointsStore.fetchCurrentPoints(props.userId)
  } catch (error) {
    console.error('刷新积分失败:', error)
  }
}

const showPointsHistory = () => {
  emit('show-points-history')
}

// 生命周期
onMounted(() => {
  // 初始化时获取积分
  refreshPoints()
})
</script>

<style scoped>
/* 自定义样式 */
.animate-pulse-slow {
  animation: pulse 2s cubic-bezier(0.4, 0, 0.6, 1) infinite;
}

/* 渐变背景动画 */
.bg-gradient-to-r {
  background-size: 200% 200%;
  animation: gradient-shift 3s ease infinite;
}

@keyframes gradient-shift {
  0% {
    background-position: 0% 50%;
  }
  50% {
    background-position: 100% 50%;
  }
  100% {
    background-position: 0% 50%;
  }
}
</style>
