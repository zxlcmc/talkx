import { computed, ref } from 'vue'
import { IMG_TYPES } from '@/common/config'

export const files = ref('')
export const infoList = ref([
    // { url: 'https://web.talkx.cn/files/2024/01/22/743182f8e8a34a5c97ee06c6ae3c36a8.css', id: '6008b154', name: 'demo.css' },
])

export const fileList = computed(() => {
    return infoList.value.map((obj) => {
        const { name } = obj
        const names = name.split('.')
        const suffix = names[names.length - 1]
        return { ...obj, type: IMG_TYPES.includes(suffix) ? 'img' : suffix, suffix }
    })
})

const funs = []

export const addWatch = (fu) => funs.push(fu)
export const clearWatch = (fu) => funs.push(fu)

export const close = () => {
    files.value = ''
    infoList.value = []
    funs.forEach(f => f())
}