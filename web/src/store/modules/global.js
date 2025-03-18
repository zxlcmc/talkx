import { defineStore } from 'pinia'
import { keepAlive } from '@/api/index'
import { random, delay, getQueryString } from '@/common/utils'
import { virtual_version, PRODUCT_TYPE } from '@/common/config'
import { useChatStore } from './chat'

// 1 Web、2 JetBrains、3 VS Code、4 HBuilderX
const pType = { web: 1, JetBrains: 2, Vs_Code: 3, HBuilderX: 4 }
const productTypeMap = {
    [PRODUCT_TYPE.Web]: pType.web,
    [PRODUCT_TYPE.Vs_Code]: pType.Vs_Code,
    [PRODUCT_TYPE.HBuilder]: pType.HBuilderX,

    [PRODUCT_TYPE.DingTalk]: pType.JetBrains,
    [PRODUCT_TYPE.MicroProgram]: pType.JetBrains,
    [PRODUCT_TYPE.IntelliJ_Idea]: pType.JetBrains,
    [PRODUCT_TYPE.PyCharm]: pType.JetBrains,
    [PRODUCT_TYPE.Android_Studio]: pType.JetBrains,
    [PRODUCT_TYPE.WebStorm]: pType.JetBrains,
    [PRODUCT_TYPE.GoLand]: pType.JetBrains,
    [PRODUCT_TYPE.MindStudio]: pType.JetBrains,
}

const loadKeepAlive = (store, cb = () => { }) => {
    const { talkxVersion = getQueryString('talkxVersion'), keepAliveTime = getQueryString('keepAliveTime') } = store.editer;
    const request = async () => {
        const useChat = useChatStore()
        const productType = productTypeMap[useChat.productId]
        const webParams = { webProductType: productTypeMap[PRODUCT_TYPE.Web], webVersion: virtual_version }
        const ideParams = { ideProductType: productType, ideVersion: talkxVersion }
        const data = await keepAlive(Object.assign(webParams, !useChat.isWeb ? ideParams : {}))
        cb(data)
    }
    const run = async () => {
        const time = keepAliveTime || random(5 * 60, 10 * 60)
        console.log('time', time);
        await delay(time * 1000)
        request()
        run()
    }
    run()
    request()
}

/**
 * 全局信息
 */
export const useGlobalStore = defineStore('talkx_global', {
    state: () => ({
        global: {
            friendPlaza: {}
        },
        editer: {},
        guide: {
            type: 'web',
            mobile: {
                over: false,
                progress: 0,
                guidance: false,
            },
            web: {
                over: false,
                progress: 0,
                guidance: false,
            }
        }
    }),
    actions: {
        keepAlive(cb) {
            loadKeepAlive(this, (data) => {
                this.global = data
                cb()
            })
        },
        readNotify() {
            if (this.global.notification) {
                this.global.notification.read = 1
            }
        },
        setEditer(data) {
            this.editer = data
        },
        setGuideStatus(status) {
            this.guide[this.guide.type].guidance = status
        },
        setGuideProgress(progress) {
            this.guide[this.guide.type].progress = progress
        },
        setGuideOver() {
            this.guide[this.guide.type].over = true
        },
        setGuideType(type) {
            this.guide.type = type
        }
    },
    getters: {
        intro: state => state.global.intro || [],
        tags: state => state.global.friendPlaza.newTags || [], 
        isGray: state => state.global.gray == 1,
        plugin: state => state.global.plugin,
        guideOver: state => state.guide[state.guide.type].over,
        guidance: state => state.guide[state.guide.type].guidance, 
    },
    persist: {
        key: 'talkx_plugin_web_talkx_global'
    }
})