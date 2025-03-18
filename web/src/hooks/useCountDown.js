import { format } from '@/common/utils'
import { computed, onMounted, onUnmounted, ref } from 'vue';


export function useCountDown(timeRef, formatStr = 'hh:mm:ss') {
    let countDownTimer
    const timeStr = ref('')
    let cbarr = []
    const start = () => {
        const change = () => {
            cbarr.forEach(c => c())
            timeStr.value = format(new Date(+new Date('2020/12/2 00:00:00') + timeRef.value * 1000), formatStr)
            if (timeRef.value <= 0) {  
                clearInterval(countDownTimer) 
            }
        }
        change()
        clearInterval(countDownTimer)
        countDownTimer = setInterval(() => {
            timeRef.value--
            change()
        }, 1000)
    }
    const timeChange = c => cbarr.push(c)
    const stop = () => {
        cbarr.length = 0
        clearInterval(countDownTimer)
    }

    onUnmounted(() => { stop() })

    return { start, timeChange, stop, timeStr }
}