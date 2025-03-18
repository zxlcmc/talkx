import pinia from "@/store";
import { debance } from '@/common/utils'
import { PRODUCT_TYPE } from '@/common/config'
import { useCommunication } from "@/hooks/useCommunication";
import * as storeModules from "@/store/modules/index";


/**
 * pinia 插件，当store变化时，将store写入IDE
 * @param {*} param0 
 * @returns 
 */
export default function ideStorage({ options, store, pinia }) {
    const { send, mismatching, productId } = useCommunication()
    if (!['login'].includes(store.$id)) return
    if (mismatching) return
    const subscribe = debance(() => {
        send('write-to-file', { fileName: store.$id, append: false, content: JSON.stringify(store.$state) })
    }, 100)
    store.$subscribe(subscribe)
}

/**
 * 初始化 读取IDE文件写入 store
 */
export const ideInitStorage = async (pageData) => {
    const { send, mismatching, productId } = useCommunication() 
    if (mismatching || [PRODUCT_TYPE.Vs_Code, PRODUCT_TYPE.HBuilder].includes(productId)) return
    const storeMaps = Object.values(storeModules).filter(s => s.$id && ['login'].includes(s.$id)).map(s => [s.$id, s()]) // 初始化所有仓库
    const write = (store, key, str) => {
        if (str) {
            try {
                store.$patch((state) => { Object.assign(state, JSON.parse(str)) })
            } catch (e) {
                console.log(`__read-from-file:${key}__ 赋值 err`, e);
            }
        }
    }
    await Promise.all(
        storeMaps.map(([key, store]) => {
            return new Promise((resolve) => {
                const next = (...args) => {
                    write(...args)
                    resolve()
                }
                // 如果可以获取locaStorage, 就不覆盖 
                const storage = localStorage.getItem(`talkx_plugin_web_` + key)
                if (storage) {
                    console.log(`${key}:talkx_plugin_web_${key}:有localStorage不读取文件`);
                    resolve()
                    return
                }
                if (pageData) {
                    const value = pageData[key]
                    value && next(store, key, value)
                } else {
                    send('read-from-file', { fileName: key }, (str) => next(store, key, str))
                }
            })

        })
    )
}