import { useChatStore } from '@/store'
import { PRODUCT_TYPE } from '@/common/config'
import { useCommunication } from '@/hooks/useCommunication'

/**
 * 重新加载插件
 */
export const reload = () => {
    const useChat = useChatStore()
    const { send } = useCommunication()
    const plugins = [  PRODUCT_TYPE.Vs_Code]
    const isSelectPlugin = plugins.includes(useChat.productId)
    if (isSelectPlugin) {
        send('talkx-reload')
    } else {
        window.location.reload(true)
    }
}