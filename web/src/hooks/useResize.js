import { onMounted, onUnmounted, ref } from 'vue'
import { isMobile, isSmall } from "@/common/utils";
import { isWeb, isPC, isWebMobile, isWebSmall } from '@/common/env'

export default function useResize() {
    const webRef = ref(false) // 浏览器环境，非IDE
    const IDERef = ref(false) // IDE浏览器环境 
    const mobileRef = ref(false) // IDE浏览器环境 
    const pcRef = ref(false) // pc环境 web端非移动端环境
    const webMobileRef = ref(false) // web端浏览器手机环境
    const webSmallRef = ref(false) // web端浏览器 窄屏
    const webWideRef = ref(false) // web端浏览器 宽屏
    const smallRef = ref(false) // 窄屏 web端浏览器宽度<600 和 IDE环境
    const funs = []
    const change = (fun) => { funs.push(fun) }

    const resize = () => {
        // pcRef.value = isPC()
        webRef.value = isWeb()
        mobileRef.value = isMobile()
        IDERef.value = !webRef.value
        // webMobileRef.value = isWebMobile()
        webSmallRef.value = isSmall() && webRef.value
        webWideRef.value = !isSmall() && webRef.value
        smallRef.value = IDERef.value || webSmallRef.value  
        funs.forEach(f => f())
        console.log('IDERef',IDERef.value );
    }
    resize()
    onMounted(() => {
        window.addEventListener('resize', resize)
    })
    onUnmounted(() => {
        window.removeEventListener('resize', resize)
    })

    return { smallRef, webRef, mobileRef, IDERef, webSmallRef, webWideRef, change }
}