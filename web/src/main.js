import './style.scss'
import './assets/scss/media.scss'
import 'globalthis/auto'
import App from './App.vue'
import { createApp } from 'vue'
import store from './store'
import router from './router'
import mitt from 'mitt'
import { useGlobalStore } from "@/store"; 
import { watchFun } from "@/compositions/initUptate";
import 'katex/dist/katex.min.css'
import '@/assets/font/iconfont.css'
import '@/assets/font/avatarIcon.scss'
import '@/assets/scss/tailwind.css'
import '@/assets/scss/highlight.scss'
import '@/assets/scss/github-markdown.scss'
const bus = mitt()
const app = createApp(App).use(store).use(router)
app.config.globalProperties.$bus = bus
app.mount('#app')

window.addEventListener('load', () => {
    // 延迟调用，给vscode注入api时间 acquireVsCodeApi
    // setTimeout(() => {
    const useGlobal = useGlobalStore();
    useGlobal.keepAlive(() => {
        watchFun()
    })
    // }, 5000)
}) 