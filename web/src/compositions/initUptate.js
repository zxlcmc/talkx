import { watch } from 'vue'
import { useGlobalStore } from "@/store";
import { debance } from "@/common/utils";
import { reload } from "@/common/ideUtils";
import { useToast } from "@/hooks/useToast";
import { useCommunication } from "@/hooks/useCommunication";
const { showToast, closeWatch } = useToast()
 

const _watchFun = () => {
    const useGlobal = useGlobalStore();

    const { ideVersion, webVersion } = useGlobal.global;
    if (webVersion) {
        showToast('update_system')
    }
    if (ideVersion) {
        const { send } = useCommunication()
        const { talkxVersion } = useGlobal.editer;
        const key = ideVersion.required == 1 ? 'update_is_required' : 'update_is_available';
        const params = { lastVersion: ideVersion.version, installVersion: talkxVersion }
        send(key, params)
    }

    document.body.classList[useGlobal.isGray ? 'add' : 'remove']('gray')
}

export const watchFun = debance(_watchFun, 300)

export const initUptate = () => {
    const useGlobal = useGlobalStore();

    closeWatch('update_system', async (confirm) => {
        if (confirm) { reload() }
    })

    watch([() => useGlobal.global], watchFun)
}
