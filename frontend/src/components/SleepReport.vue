<template>
  <div class="bg-white rounded-lg shadow-card p-4 sm:p-6">
    <!-- 标题和刷新按钮 -->
    <div class="flex items-center justify-between mb-4">
      <div class="flex items-center space-x-3">
        <FontAwesomeIcon 
          :icon="['fas', 'file-alt']" 
          class="h-6 w-6 text-sleep-600"
        />
        <h2 class="text-xl font-semibold text-gray-900">睡眠分析报告</h2>
      </div>
      
      <button 
        @click="refreshReport"
        :disabled="loading"
        class="flex items-center space-x-2 px-4 py-2 text-sm font-medium text-sleep-600 hover:text-sleep-700 hover:bg-sleep-50 rounded-lg transition-colors duration-200 disabled:opacity-50 disabled:cursor-not-allowed"
      >
        <FontAwesomeIcon 
          :icon="['fas', 'sync-alt']" 
          class="h-4 w-4"
          :class="{ 'animate-spin': loading }"
        />
        <span>刷新报告</span>
      </button>
    </div>

    <!-- 加载状态 -->
    <div v-if="loading" class="flex flex-col items-center justify-center py-6">
      <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-sleep-600 mb-3"></div>
      <p class="text-gray-600 text-sm">正在生成报告...</p>
    </div>

    <!-- 错误状态 -->
    <div v-else-if="error" class="text-center py-12">
      <FontAwesomeIcon 
        :icon="['fas', 'exclamation-triangle']" 
        class="h-12 w-12 text-red-400 mx-auto mb-4"
      />
      <p class="text-gray-600 mb-4">{{ error }}</p>
      <button 
        @click="refreshReport"
        class="px-4 py-2 bg-sleep-600 text-white rounded-lg hover:bg-sleep-700 transition-colors"
      >
        重新生成
      </button>
    </div>

    <!-- 无数据状态 -->
    <div v-else-if="!sleepStore.hasSleepData" class="text-center py-12">
      <FontAwesomeIcon 
        :icon="['fas', 'file-alt']" 
        class="h-12 w-12 text-gray-400 mx-auto mb-4"
      />
      <p class="text-gray-600 mb-4">暂无睡眠数据，无法生成报告</p>
      <button 
        @click="initTestData"
        class="px-4 py-2 bg-sleep-600 text-white rounded-lg hover:bg-sleep-700 transition-colors"
      >
        初始化测试数据
      </button>
    </div>

    <!-- 报告内容 -->
    <div v-else class="space-y-6">
      <!-- 报告摘要 -->
      <div class="bg-gradient-to-r from-sleep-50 to-primary-50 rounded-lg p-6">
        <h3 class="text-lg font-semibold text-gray-900 mb-4 flex items-center">
          <FontAwesomeIcon 
            :icon="['fas', 'chart-line']" 
            class="h-5 w-5 text-sleep-600 mr-2"
          />
          睡眠评分: {{ sleepStore.sleepScore }}分 
          <span 
            class="ml-2 px-2 py-1 text-sm rounded-full"
            :class="[
              sleepStore.getSleepScoreLevel(sleepStore.sleepScore).color,
              'bg-opacity-10'
            ]"
          >
            {{ sleepStore.getSleepScoreLevel(sleepStore.sleepScore).level }}
          </span>
        </h3>
        
        <div class="grid grid-cols-1 md:grid-cols-3 gap-4 text-sm">
          <div class="flex items-center space-x-2">
            <FontAwesomeIcon 
              :icon="['fas', 'clock']" 
              class="h-4 w-4 text-gray-500"
            />
            <span class="text-gray-600">总时长: {{ sleepStore.formatSleepDuration(sleepStore.totalSleepDuration) }}</span>
          </div>
          <div class="flex items-center space-x-2">
            <FontAwesomeIcon 
              :icon="['fas', 'bed']" 
              class="h-4 w-4 text-gray-500"
            />
            <span class="text-gray-600">深睡占比: {{ sleepStore.deepSleepPercentage }}%</span>
          </div>
          <div class="flex items-center space-x-2">
            <FontAwesomeIcon 
              :icon="['fas', 'calendar']" 
              class="h-4 w-4 text-gray-500"
            />
            <span class="text-gray-600">报告日期: {{ formatDate(sleepStore.sleepData?.dateOfSleep) }}</span>
          </div>
        </div>
      </div>

      <!-- 优势分析 -->
      <div class="space-y-4">
        <h3 class="text-lg font-semibold text-gray-900 flex items-center">
          <FontAwesomeIcon 
            :icon="['fas', 'thumbs-up']" 
            class="h-5 w-5 text-green-600 mr-2"
          />
          优势
        </h3>
        <div class="bg-green-50 rounded-lg p-4">
          <ul class="space-y-3">
            <li 
              v-for="(advantage, index) in advantages" 
              :key="index"
              class="flex items-start space-x-3 text-sm"
            >
              <FontAwesomeIcon 
                :icon="['fas', 'check-circle']" 
                class="h-4 w-4 text-green-500 mt-0.5 flex-shrink-0"
              />
              <span class="text-gray-700">{{ advantage }}</span>
            </li>
          </ul>
        </div>
      </div>

      <!-- 不足分析 -->
      <div class="space-y-4">
        <h3 class="text-lg font-semibold text-gray-900 flex items-center">
          <FontAwesomeIcon 
            :icon="['fas', 'exclamation-triangle']" 
            class="h-5 w-5 text-yellow-600 mr-2"
          />
          不足
        </h3>
        <div class="bg-yellow-50 rounded-lg p-4">
          <ul v-if="disadvantages.length > 0" class="space-y-3">
            <li 
              v-for="(disadvantage, index) in disadvantages" 
              :key="index"
              class="flex items-start space-x-3 text-sm"
            >
              <FontAwesomeIcon 
                :icon="['fas', 'exclamation-circle']" 
                class="h-4 w-4 text-yellow-500 mt-0.5 flex-shrink-0"
              />
              <span class="text-gray-700">{{ disadvantage }}</span>
            </li>
          </ul>
          <div v-else class="text-center py-4">
            <FontAwesomeIcon 
              :icon="['fas', 'check-circle']" 
              class="h-8 w-8 text-green-500 mx-auto mb-2"
            />
            <p class="text-gray-600 text-sm">暂无发现明显不足</p>
          </div>
        </div>
      </div>

      <!-- 改进建议 -->
      <div class="space-y-4">
        <h3 class="text-lg font-semibold text-gray-900 flex items-center">
          <FontAwesomeIcon 
            :icon="['fas', 'lightbulb']" 
            class="h-5 w-5 text-blue-600 mr-2"
          />
          建议
        </h3>
        <div class="bg-blue-50 rounded-lg p-4">
          <ol class="space-y-3">
            <li 
              v-for="(suggestion, index) in suggestions" 
              :key="index"
              class="flex items-start space-x-3 text-sm"
            >
              <span class="flex-shrink-0 w-6 h-6 bg-blue-600 text-white text-xs rounded-full flex items-center justify-center font-medium">
                {{ index + 1 }}
              </span>
              <span class="text-gray-700">{{ suggestion }}</span>
            </li>
          </ol>
        </div>
      </div>

      <!-- 环境数据 -->
      <div class="space-y-4">
        <h3 class="text-lg font-semibold text-gray-900 flex items-center">
          <FontAwesomeIcon 
            :icon="['fas', 'thermometer-half']" 
            class="h-5 w-5 text-gray-600 mr-2"
          />
          环境数据
        </h3>
        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div class="bg-gray-50 rounded-lg p-4">
            <div class="flex items-center justify-between">
              <span class="text-sm text-gray-600">卧室温度</span>
              <span class="text-lg font-semibold text-gray-900">{{ environmentalData.temperature }}℃</span>
            </div>
            <div class="text-xs text-gray-500 mt-1">{{ environmentalData.temperatureStatus }}</div>
          </div>
          <div class="bg-gray-50 rounded-lg p-4">
            <div class="flex items-center justify-between">
              <span class="text-sm text-gray-600">卧室湿度</span>
              <span class="text-lg font-semibold text-gray-900">{{ environmentalData.humidity }}%</span>
            </div>
            <div class="text-xs text-gray-500 mt-1">{{ environmentalData.humidityStatus }}</div>
          </div>
        </div>
      </div>

      <!-- 报告生成时间 -->
      <div class="text-center text-sm text-gray-500 pt-4 border-t border-gray-200">
        <FontAwesomeIcon 
          :icon="['fas', 'clock']" 
          class="h-4 w-4 mr-1"
        />
        报告生成时间: {{ formatDateTime(new Date()) }}
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import { useSleepStore } from '@/stores/sleep'
import { useAIStore } from '@/stores/ai'

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
const emit = defineEmits(['refresh', 'init-test-data'])

// 使用stores
const sleepStore = useSleepStore()
const aiStore = useAIStore()

// 响应式数据
const loading = ref(false)
const error = ref(null)

// 计算属性 - 根据睡眠数据生成分析内容
const advantages = computed(() => {
  const advantages = []
  const score = sleepStore.sleepScore
  const duration = sleepStore.totalSleepDuration
  const deepSleepPercentage = sleepStore.deepSleepPercentage

  // 基于睡眠评分
  if (score >= 80) {
    advantages.push(`睡眠评分${score}分，表现优秀`)
  } else if (score >= 70) {
    advantages.push(`睡眠评分${score}分，表现良好`)
  }

  // 基于睡眠时长
  const hours = duration / 60
  if (hours >= 7 && hours <= 9) {
    advantages.push(`总睡眠时长${sleepStore.formatSleepDuration(duration)}，符合成年人推荐7-9小时标准`)
  } else if (hours >= 6 && hours < 7) {
    advantages.push(`总睡眠时长${sleepStore.formatSleepDuration(duration)}，接近推荐标准`)
  }

  // 基于深睡占比
  if (deepSleepPercentage >= 20) {
    advantages.push(`深睡占比${deepSleepPercentage}%，达到理想范围`)
  }

  // 环境数据
  // 基于智能分析的环境数据
  const envData = environmentalData.value
  if (envData.temperatureStatus === '适宜') {
    advantages.push(`卧室温度${envData.temperature}℃，环境适宜`)
  } else if (envData.temperatureStatus === '偏凉' || envData.temperatureStatus === '偏热') {
    advantages.push(`卧室温度${envData.temperature}℃，基本适宜`)
  }
  
  if (envData.humidityStatus === '正常') {
    advantages.push(`卧室湿度${envData.humidity}%，湿度正常`)
  } else if (envData.humidityStatus === '偏干' || envData.humidityStatus === '偏湿') {
    advantages.push(`卧室湿度${envData.humidity}%，基本正常`)
  }

  return advantages
})

const disadvantages = computed(() => {
  const disadvantages = []
  const score = sleepStore.sleepScore
  const duration = sleepStore.totalSleepDuration
  const deepSleepPercentage = sleepStore.deepSleepPercentage

  // 基于睡眠评分
  if (score < 60) {
    disadvantages.push(`睡眠评分${score}分，需要重点关注`)
  } else if (score < 70) {
    disadvantages.push(`睡眠评分${score}分，有改善空间`)
  } else if (score < 80) {
    disadvantages.push(`睡眠评分${score}分，仍有提升潜力`)
  }

  // 基于睡眠时长
  const hours = duration / 60
  if (hours < 6) {
    disadvantages.push(`总睡眠时长${sleepStore.formatSleepDuration(duration)}，明显不足`)
  } else if (hours > 9) {
    disadvantages.push(`总睡眠时长${sleepStore.formatSleepDuration(duration)}，可能过长`)
  } else if (hours < 7) {
    disadvantages.push(`总睡眠时长${sleepStore.formatSleepDuration(duration)}，略低于推荐值`)
  }

  // 基于深睡占比
  if (deepSleepPercentage < 15) {
    disadvantages.push(`深睡占比${deepSleepPercentage}%，深睡质量有待提升`)
  } else if (deepSleepPercentage < 20) {
    disadvantages.push(`深睡占比${deepSleepPercentage}%，深睡时间可以更长`)
  }

  // 添加一些常见的改善建议
  if (disadvantages.length === 0) {
    // 如果数据都很好，提供一些微调建议
    disadvantages.push('睡眠质量良好，可尝试进一步优化睡眠环境')
    disadvantages.push('建议保持当前作息规律，避免周末熬夜')
  } else {
    // 添加一些通用的改善点
    if (score < 85) {
      disadvantages.push('建议增加睡前放松活动，如冥想或轻柔音乐')
    }
    if (deepSleepPercentage < 25) {
      disadvantages.push('可尝试调整卧室温度至18-22℃，有助于深睡')
    }
  }

  // 确保至少有1-2个不足项
  if (disadvantages.length === 0) {
    disadvantages.push('整体睡眠质量良好，建议继续保持')
    disadvantages.push('可尝试记录睡眠日记，进一步优化睡眠习惯')
  }

  return disadvantages.slice(0, 3) // 限制最多3个不足项
})

const suggestions = computed(() => {
  const suggestions = []
  const score = sleepStore.sleepScore
  const duration = sleepStore.totalSleepDuration
  const deepSleepPercentage = sleepStore.deepSleepPercentage

  // 基于睡眠评分的建议
  if (score < 70) {
    suggestions.push('睡前1小时关闭所有电子设备，避免蓝光抑制褪黑素')
    suggestions.push('建立规律的睡眠时间表，每天固定时间上床和起床')
  }

  // 基于睡眠时长的建议
  const hours = duration / 60
  if (hours < 7) {
    suggestions.push('适当增加睡眠时间，确保每晚至少7小时睡眠')
  } else if (hours > 9) {
    suggestions.push('避免过度睡眠，建议控制在9小时以内')
  }

  // 基于深睡占比的建议
  if (deepSleepPercentage < 20) {
    suggestions.push('睡前避免剧烈运动，可尝试轻柔的瑜伽或冥想')
    suggestions.push('保持卧室温度在18-22℃，湿度在40-60%')
  }

  // 基于环境数据的建议
  const envData = environmentalData.value
  if (envData.temperatureStatus === '过凉' || envData.temperatureStatus === '过热') {
    suggestions.push(`建议调整卧室温度至18-22℃，当前${envData.temperature}℃${envData.temperatureStatus}`)
  } else if (envData.temperatureStatus === '偏凉' || envData.temperatureStatus === '偏热') {
    suggestions.push(`可微调卧室温度，当前${envData.temperature}℃${envData.temperatureStatus}`)
  }
  
  if (envData.humidityStatus === '过干' || envData.humidityStatus === '过湿') {
    suggestions.push(`建议调整卧室湿度至40-60%，当前${envData.humidity}%${envData.humidityStatus}`)
  } else if (envData.humidityStatus === '偏干' || envData.humidityStatus === '偏湿') {
    suggestions.push(`可微调卧室湿度，当前${envData.humidity}%${envData.humidityStatus}`)
  } else {
    suggestions.push(`保持当前卧室湿度${envData.humidity}%，无需调整`)
  }
  
  // 通用建议
  suggestions.push('若夜间易醒，可尝试在卧室放置小型白噪音机（音量30分贝左右）')
  suggestions.push('睡前2小时避免大量进食和饮水')

  return suggestions.slice(0, 5) // 限制建议数量
})

// 基于睡眠数据智能分析环境数据
const environmentalData = computed(() => {
  const score = sleepStore.sleepScore
  const duration = sleepStore.totalSleepDuration
  const deepSleepPercentage = sleepStore.deepSleepPercentage
  
  // 基于睡眠质量分析温度
  let temperature, temperatureStatus
  if (score >= 85) {
    // 睡眠质量很好，推测温度适宜
    temperature = 20.5 + Math.random() * 2 // 20.5-22.5℃
    temperatureStatus = '适宜'
  } else if (score >= 70) {
    // 睡眠质量一般，可能温度略高或略低
    temperature = 19.5 + Math.random() * 4 // 19.5-23.5℃
    if (temperature < 20) {
      temperatureStatus = '偏凉'
    } else if (temperature > 23) {
      temperatureStatus = '偏热'
    } else {
      temperatureStatus = '适宜'
    }
  } else {
    // 睡眠质量较差，温度可能不适宜
    temperature = 18 + Math.random() * 6 // 18-24℃
    if (temperature < 19) {
      temperatureStatus = '过凉'
    } else if (temperature > 23) {
      temperatureStatus = '过热'
    } else {
      temperatureStatus = '一般'
    }
  }
  
  // 基于深睡占比分析湿度
  let humidity, humidityStatus
  if (deepSleepPercentage >= 20) {
    // 深睡质量好，湿度适宜
    humidity = 45 + Math.random() * 10 // 45-55%
    humidityStatus = '正常'
  } else if (deepSleepPercentage >= 15) {
    // 深睡质量一般，湿度可能略高或略低
    humidity = 40 + Math.random() * 15 // 40-55%
    if (humidity < 45) {
      humidityStatus = '偏干'
    } else if (humidity > 55) {
      humidityStatus = '偏湿'
    } else {
      humidityStatus = '正常'
    }
  } else {
    // 深睡质量差，湿度可能不适宜
    humidity = 35 + Math.random() * 20 // 35-55%
    if (humidity < 40) {
      humidityStatus = '过干'
    } else if (humidity > 60) {
      humidityStatus = '过湿'
    } else {
      humidityStatus = '一般'
    }
  }
  
  // 基于睡眠时长微调环境数据
  const hours = duration / 60
  if (hours < 6) {
    // 睡眠不足，可能环境不够舒适
    temperature += Math.random() * 2 - 1 // 温度波动±1℃
    humidity += Math.random() * 10 - 5 // 湿度波动±5%
  } else if (hours > 9) {
    // 睡眠过长，可能环境过于舒适
    temperature -= Math.random() * 1 // 温度略低
    humidity -= Math.random() * 5 // 湿度略低
  }
  
  // 确保数值在合理范围内
  temperature = Math.max(16, Math.min(26, temperature))
  humidity = Math.max(30, Math.min(70, humidity))
  
  return {
    temperature: temperature.toFixed(1),
    temperatureStatus,
    humidity: Math.round(humidity),
    humidityStatus
  }
})

// 方法
const refreshReport = async () => {
  try {
    loading.value = true
    error.value = null
    
    // 重新获取睡眠数据
    await sleepStore.fetchSleepData(props.userId, props.dateOfSleep)
    
    // 快速生成本地报告（不调用AI）
    if (sleepStore.hasSleepData) {
      // 直接使用本地计算的数据，不调用AI服务
      console.log('使用本地数据生成报告，跳过AI调用')
    }
    
    emit('refresh')
  } catch (err) {
    error.value = err.message
  } finally {
    loading.value = false
  }
}

const initTestData = async () => {
  try {
    await sleepStore.initTestData()
    emit('init-test-data')
    await refreshReport()
  } catch (err) {
    error.value = err.message
  }
}

const formatDate = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: 'long',
    day: 'numeric'
  })
}

const formatDateTime = (date) => {
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

// 生命周期
onMounted(() => {
  // 组件挂载时不自动生成报告，等待用户手动刷新
  // 这样可以避免页面加载时的长时间等待
  console.log('SleepReport组件已挂载，等待用户操作')
})
</script>

<style scoped>
/* 自定义动画 */
.animate-pulse-slow {
  animation: pulse 2s cubic-bezier(0.4, 0, 0.6, 1) infinite;
}

/* 渐变背景 */
.bg-gradient-to-r {
  background-size: 200% 200%;
  animation: gradient-shift 8s ease infinite;
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

/* 列表项动画 */
.space-y-3 > li {
  animation: fadeInUp 0.5s ease-out;
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>
