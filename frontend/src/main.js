import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import './style.css'

// Font Awesome 配置
import { library } from '@fortawesome/fontawesome-svg-core'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'

// 导入需要的图标
import { 
  faMoon, 
  faUser, 
  faRobot, 
  faSyncAlt, 
  faStar, 
  faChevronRight,
  faChartBar, 
  faClock, 
  faBed, 
  faFileAlt, 
  faComments, 
  faBook, 
  faSearch, 
  faPaperPlane, 
  faTrash, 
  faLightbulb, 
  faThumbsUp, 
  faExclamationTriangle, 
  faCheckCircle, 
  faExclamationCircle, 
  faChartLine, 
  faThermometerHalf, 
  faArrowUp, 
  faTimes, 
  faTag, 
  faHome, 
  faHeart, 
  faStethoscope,
  faSpinner,
  faCalendar
} from '@fortawesome/free-solid-svg-icons'

// 将图标添加到库中
library.add(
  faMoon, 
  faUser, 
  faRobot, 
  faSyncAlt, 
  faStar, 
  faChevronRight,
  faChartBar, 
  faClock, 
  faBed, 
  faFileAlt, 
  faComments, 
  faBook, 
  faSearch, 
  faPaperPlane, 
  faTrash, 
  faLightbulb, 
  faThumbsUp, 
  faExclamationTriangle, 
  faCheckCircle, 
  faExclamationCircle, 
  faChartLine, 
  faThermometerHalf, 
  faArrowUp, 
  faTimes, 
  faTag, 
  faHome, 
  faHeart, 
  faStethoscope,
  faSpinner,
  faCalendar
)

const app = createApp(App)
const pinia = createPinia()

app.use(pinia)
app.component('FontAwesomeIcon', FontAwesomeIcon)

app.mount('#app')
