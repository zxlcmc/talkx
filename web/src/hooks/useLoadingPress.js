import { reactive } from 'vue'

const showRef = reactive({ show: false })

export function useLoadingPress() {
    const setShow = (show) => showRef.show = show
    return { showRef, setShow }
}