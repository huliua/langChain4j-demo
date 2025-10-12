import {createApp} from 'vue'
import './style.css'
import App from './App.vue'

// 引入Element Plus
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

// 引入Pinia
import {createPinia} from 'pinia'

const pinia = createPinia()

// 创建应用实例
const app = createApp(App)

// 使用插件
app.use(ElementPlus)
app.use(pinia)

// 挂载应用
app.mount('#app')
