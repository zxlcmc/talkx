import { ref, onUnmounted, inject } from 'vue'
import { isMac, isMobile } from '@/common/utils'

let timer

export default function tipTexts() {
    const isPhone = isMobile()
    const tipIndex = ref(0)
    const resize = inject('resize')
    const texts = [
        { text: 'Enter 发送消息', mac: '⏎ 发送消息' },
        { text: 'Shift+Enter 换行', mac: '↑+⏎ 换行' },
        { text: 'Ctrl+Alt+C 进入新话题', mac: '⌃+⌥+C 进入新话题' },
        { text: 'Ctrl+Alt+D 删除当前话题', mac: '⌃+⌥+D 删除当前话题' },
    ]

    const tipList = ref([])

    const setData = () => {
        tipList.value = isPhone ? [] : texts.map(({ text, mac }) => ({ outer: false, text: isMac() ? mac : text }))
    }
    setData()
    resize.change(setData)

    const _outer = (index) => {
        // 当前的上一个，
        // 然后是最后一个，那么第一个就是
        // 排除当前
        return (tipIndex.value - 1 == index || index == tipList.value.length - 1) && tipIndex.value !== index
    }

    const isOuter = (item, index) => _outer(index) && item.outer;
    const next = () => {
        tipIndex.value = (tipIndex.value + 1) % tipList.value.length
        tipList.value.forEach((item, i) => item.outer = _outer(i))
    }

    clearInterval(timer)
    if (!isPhone) {
        timer = setInterval(() => { next() }, 3000)
    }

    onUnmounted(() => { clearInterval(timer) })

    return { tipIndex, tipList, isOuter }
}