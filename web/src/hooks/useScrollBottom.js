import { debance } from "@/common/utils";
import { ref, onMounted, onUnmounted } from 'vue'

/**
 * 滚动到底部处理
 */
export function useScrollBottom({
    listDom = ref(),
    scolllDom = ref(),
    bottomH = 50,
    type = 'bottom'
} = {}) {
    const wrapperH = ref(0);
    const scrollFuns = []
    const addScrollFun = (fun) => { resize(); scrollFuns.push(fun) }
    const resize = () => { 
        if (listDom.value) {
            wrapperH.value = parseFloat(getComputedStyle(listDom.value)["height"]);
        }
    };

    const scrollFun = debance(() => { scrollFuns.forEach(c => c()) }, 100);
    let scrollTopY = 0;
    const scroll = (e) => { 
        if (!listDom.value || !wrapperH.value) { resize() }
        if (scolllDom.value) {
            const h = parseFloat(getComputedStyle(scolllDom.value)["height"]);
            const scrollTop = e.target.scrollTop;
            const diffH = h - (wrapperH.value + scrollTop);
            if (type == 'bottom' && scrollTop > scrollTopY && diffH < bottomH) {
                scrollFun();
            } else if (type === 'top' && scrollTop < scrollTopY && scrollTop < 50) {
                scrollFun();
            }
            scrollTopY = scrollTop;
        }
    };

    onMounted(async () => {
        window.addEventListener("resize", resize);
        resize();
    })

    onUnmounted(() => {
        window.removeEventListener("resize", resize);
    })

    return {
        listDom,
        scolllDom,
        wrapperH,
        scroll,
        addScrollFun
    }
}