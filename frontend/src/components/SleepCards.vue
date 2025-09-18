<template>
  <div class="bg-white rounded-lg shadow-card p-4 sm:p-6">
    <!-- 标题 -->
    <div class="flex items-center justify-between mb-4">
      <div class="flex items-center space-x-3">
        <FontAwesomeIcon 
          :icon="['fas', 'chart-bar']" 
          class="h-6 w-6 text-sleep-600"
        />
        <h2 class="text-xl font-semibold text-gray-900">睡眠数据概览</h2>
      </div>
      <!-- 刷新按钮 -->
      <button 
        @click="refreshData"
        :disabled="sleepStore.loading"
        class="flex items-center space-x-2 px-3 py-2 text-sm text-gray-600 hover:text-sleep-600 hover:bg-gray-50 rounded-lg transition-colors duration-200"
        title="刷新数据"
      >
        <FontAwesomeIcon 
          :icon="['fas', 'sync-alt']" 
          class="h-4 w-4"
          :class="{ 'animate-spin': sleepStore.loading }"
        />
        <span class="hidden sm:inline">刷新</span>
      </button>
    </div>

    <!-- 加载状态 -->
    <div v-if="sleepStore.loading" class="flex justify-center items-center py-12">
      <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-sleep-600"></div>
    </div>

    <!-- 错误状态 -->
    <div v-else-if="sleepStore.error" class="text-center py-12">
      <FontAwesomeIcon 
        :icon="['fas', 'exclamation-triangle']" 
        class="h-12 w-12 text-red-400 mx-auto mb-4"
      />
      <p class="text-gray-600 mb-4">{{ sleepStore.error }}</p>
      <button 
        @click="refreshData"
        class="px-4 py-2 bg-sleep-600 text-white rounded-lg hover:bg-sleep-700 transition-colors"
      >
        重新加载
      </button>
    </div>

    <!-- 无数据状态 -->
    <div v-else-if="!sleepStore.hasSleepData" class="text-center py-12">
      <FontAwesomeIcon 
        :icon="['fas', 'bed']" 
        class="h-12 w-12 text-gray-400 mx-auto mb-4"
      />
      <p class="text-gray-600 mb-4">暂无睡眠数据</p>
      <div class="space-y-2">
        <button 
          @click="initTestData"
          class="w-full px-4 py-2 bg-sleep-600 text-white rounded-lg hover:bg-sleep-700 transition-colors"
        >
          初始化测试数据
        </button>
        <button 
          @click="generatePoints"
          class="w-full px-4 py-2 bg-yellow-600 text-white rounded-lg hover:bg-yellow-700 transition-colors"
        >
          生成积分
        </button>
      </div>
    </div>

    <!-- 数据卡片 -->
    <div v-else class="grid grid-cols-1 md:grid-cols-3 gap-4">
      <!-- 睡眠评分卡片 -->
      <div 
        class="bg-gradient-to-br from-blue-50 to-blue-100 rounded-xl p-6 hover:shadow-card-hover transition-all duration-300 transform hover:-translate-y-1"
        @click="showSleepScoreDetail"
      >
        <div class="flex items-center justify-between mb-4">
          <div class="flex items-center space-x-2">
            <FontAwesomeIcon 
              :icon="['fas', 'star']" 
              class="h-5 w-5 text-blue-600"
            />
            <h3 class="text-sm font-medium text-gray-700">睡眠评分</h3>
          </div>
          <FontAwesomeIcon 
            :icon="['fas', 'chevron-right']" 
            class="h-4 w-4 text-gray-400"
          />
        </div>
        
        <div class="mb-3">
          <div class="text-3xl font-bold text-blue-600 mb-1">
            {{ sleepStore.sleepScore }}分
          </div>
          <div 
            class="text-sm font-medium"
            :class="sleepStore.getSleepScoreLevel(sleepStore.sleepScore).color"
          >
            {{ sleepStore.getSleepScoreLevel(sleepStore.sleepScore).level }}
          </div>
        </div>

        <!-- 趋势指示器 -->
        <div class="flex items-center space-x-1">
          <FontAwesomeIcon 
            :icon="['fas', 'arrow-up']" 
            class="h-3 w-3 text-green-500"
          />
          <span class="text-xs text-green-600">较昨日提升3分</span>
        </div>
      </div>

      <!-- 总睡眠时长卡片 -->
      <div 
        class="bg-gradient-to-br from-green-50 to-green-100 rounded-xl p-6 hover:shadow-card-hover transition-all duration-300 transform hover:-translate-y-1"
        @click="showSleepDurationDetail"
      >
        <div class="flex items-center justify-between mb-4">
          <div class="flex items-center space-x-2">
            <FontAwesomeIcon 
              :icon="['fas', 'clock']" 
              class="h-5 w-5 text-green-600"
            />
            <h3 class="text-sm font-medium text-gray-700">总睡眠时长</h3>
          </div>
          <FontAwesomeIcon 
            :icon="['fas', 'chevron-right']" 
            class="h-4 w-4 text-gray-400"
          />
        </div>
        
        <div class="mb-3">
          <div class="text-3xl font-bold text-green-600 mb-1">
            {{ sleepStore.formatSleepDuration(sleepStore.totalSleepDuration) }}
          </div>
          <div class="text-xs text-gray-600">
            成年人推荐: 7-9小时
          </div>
        </div>

        <!-- 推荐范围指示器 -->
        <div class="flex items-center space-x-1">
          <div 
            class="w-2 h-2 rounded-full"
            :class="isSleepDurationOptimal ? 'bg-green-500' : 'bg-yellow-500'"
          ></div>
          <span 
            class="text-xs"
            :class="isSleepDurationOptimal ? 'text-green-600' : 'text-yellow-600'"
          >
            {{ isSleepDurationOptimal ? '达到推荐范围' : '接近推荐范围' }}
          </span>
        </div>
      </div>

      <!-- 深睡占比卡片 -->
      <div 
        class="bg-gradient-to-br from-purple-50 to-purple-100 rounded-xl p-6 hover:shadow-card-hover transition-all duration-300 transform hover:-translate-y-1"
        @click="showDeepSleepDetail"
      >
        <div class="flex items-center justify-between mb-4">
          <div class="flex items-center space-x-2">
            <FontAwesomeIcon 
              :icon="['fas', 'bed']" 
              class="h-5 w-5 text-purple-600"
            />
            <h3 class="text-sm font-medium text-gray-700">深睡占比</h3>
          </div>
          <FontAwesomeIcon 
            :icon="['fas', 'chevron-right']" 
            class="h-4 w-4 text-gray-400"
          />
        </div>
        
        <div class="mb-3">
          <div class="text-3xl font-bold text-purple-600 mb-1">
            {{ sleepStore.deepSleepPercentage }}%
          </div>
          <div 
            class="text-sm font-medium"
            :class="sleepStore.getDeepSleepLevel(sleepStore.deepSleepPercentage).color"
          >
            {{ sleepStore.getDeepSleepLevel(sleepStore.deepSleepPercentage).level }}
          </div>
        </div>

        <!-- 状态指示器 -->
        <div class="flex items-center space-x-1">
          <FontAwesomeIcon 
            :icon="['fas', 'check-circle']" 
            class="h-3 w-3 text-green-500"
          />
          <span class="text-xs text-green-600">达到理想范围</span>
        </div>
      </div>
    </div>

    <!-- 详细数据表格（可折叠） -->
    <div v-if="showDetails" class="mt-6 border-t border-gray-200 pt-6">
      <h3 class="text-lg font-medium text-gray-900 mb-4">详细数据</h3>
      <div class="overflow-x-auto">
        <table class="min-w-full divide-y divide-gray-200">
          <thead class="bg-gray-50">
            <tr>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                指标
              </th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                数值
              </th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                状态
              </th>
            </tr>
          </thead>
          <tbody class="bg-white divide-y divide-gray-200">
            <tr>
              <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                睡眠评分
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                {{ sleepStore.sleepScore }}分
              </td>
              <td class="px-6 py-4 whitespace-nowrap">
                <span 
                  class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full"
                  :class="[
                    sleepStore.getSleepScoreLevel(sleepStore.sleepScore).color,
                    'bg-opacity-10'
                  ]"
                >
                  {{ sleepStore.getSleepScoreLevel(sleepStore.sleepScore).level }}
                </span>
              </td>
            </tr>
            <tr>
              <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                总睡眠时长
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                {{ sleepStore.formatSleepDuration(sleepStore.totalSleepDuration) }}
              </td>
              <td class="px-6 py-4 whitespace-nowrap">
                <span 
                  class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full"
                  :class="isSleepDurationOptimal ? 'text-green-600 bg-green-100' : 'text-yellow-600 bg-yellow-100'"
                >
                  {{ isSleepDurationOptimal ? '达标' : '接近' }}
                </span>
              </td>
            </tr>
            <tr>
              <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                深睡时长
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                {{ sleepStore.formatSleepDuration(sleepStore.deepSleepDuration) }}
              </td>
              <td class="px-6 py-4 whitespace-nowrap">
                <span 
                  class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full"
                  :class="[
                    sleepStore.getDeepSleepLevel(sleepStore.deepSleepPercentage).color,
                    'bg-opacity-10'
                  ]"
                >
                  {{ sleepStore.getDeepSleepLevel(sleepStore.deepSleepPercentage).level }}
                </span>
              </td>
            </tr>
            <tr>
              <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                REM睡眠时长
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                {{ sleepStore.formatSleepDuration(sleepStore.remSleepDuration) }}
              </td>
              <td class="px-6 py-4 whitespace-nowrap">
                <span class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full text-blue-600 bg-blue-100">
                  正常
                </span>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import { useSleepStore } from '@/stores/sleep'

// 定义props
const props = defineProps({
  userId: {
    type: String,
    default: 'user001'
  },
  dateOfSleep: {
    type: String,
    default: () => new Date().toISOString().split('T')[0]
  }
})

// 定义emits
const emit = defineEmits(['show-detail', 'init-test-data'])

// 使用stores
const sleepStore = useSleepStore()

// 响应式数据
const showDetails = ref(false)

// 计算属性
const isSleepDurationOptimal = computed(() => {
  const hours = sleepStore.totalSleepDuration / 60
  return hours >= 7 && hours <= 9
})

// 方法
const refreshData = async () => {
  try {
    await sleepStore.refreshData(props.userId, props.dateOfSleep)
    emit('show-detail', '数据已刷新')
  } catch (error) {
    console.error('刷新数据失败:', error)
    // 显示错误通知
    emit('show-detail', '刷新失败: ' + error.message)
  }
}

const generatePoints = async () => {
  try {
    await pointsStore.generatePoints(props.userId, props.dateOfSleep)
    emit('show-detail', '积分生成成功')
  } catch (error) {
    console.error('生成积分失败:', error)
    emit('show-detail', '积分生成失败: ' + error.message)
  }
}

const initTestData = async () => {
  try {
    await sleepStore.initTestData()
    
    // 初始化测试数据后，自动生成积分
    try {
      await pointsStore.generatePoints(props.userId, props.dateOfSleep)
      console.log('积分生成成功')
    } catch (pointsError) {
      console.log('积分生成失败:', pointsError)
    }
    
    emit('init-test-data')
    // 重新获取数据
    await refreshData()
  } catch (error) {
    console.error('初始化测试数据失败:', error)
  }
}

const showSleepScoreDetail = () => {
  emit('show-detail', 'sleep-score')
}

const showSleepDurationDetail = () => {
  emit('show-detail', 'sleep-duration')
}

const showDeepSleepDetail = () => {
  emit('show-detail', 'deep-sleep')
}

// 生命周期
onMounted(() => {
  refreshData()
})
</script>

<style scoped>
/* 自定义动画 */
.hover\:shadow-card-hover:hover {
  box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.04);
}

/* 渐变背景动画 */
.bg-gradient-to-br {
  background-size: 200% 200%;
  animation: gradient-shift 6s ease infinite;
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

/* 卡片悬停效果 */
.hover\:-translate-y-1:hover {
  transform: translateY(-4px);
}
</style>
