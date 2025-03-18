import server from './require'
import { useLoginStore, useChatStore } from '@/store'
 
const fetchTimeout = 60 * 1000
export function fetchChatAPIProcess(params) {
    const data = JSON.stringify(params.options)
    const baseStore = useLoginStore()

    let fetchTimeOutTimer;
    clearTimeout(fetchTimeOutTimer)
    fetchTimeOutTimer = setTimeout(() => {
        params.controller.abort()
        params.timeOut()
        console.log('----fetch timeOut----')
    }, fetchTimeout)

    params.interruptClear = () => {
        console.log('interruptClear');
        clearTimeout(fetchTimeOutTimer)
    }
    // http://47.98.242.33:7443
    const baseUrl = import.meta.env.DEV ? '/_gpt' : import.meta.env.VITE_MODE_API_URL
    console.log('fetch open')
    console.time('fetch')
    return fetch(baseUrl + '/gpt/chat', {
        method: 'POST',
        signal: params.signal,
        body: data,
        headers: {
            'talkx-token': baseStore.token,
            'Content-Type': 'application/json;charset=utf-8',
        },
    }).then((response) => {
        console.timeEnd('fetch')
        console.log('fetch open success')
        clearTimeout(fetchTimeOutTimer)

        params.onDownloadProgress(response)
        return response
    })
}

/**
 * 删除消息
 * @returns 
 */
export const deleteMsg = (data) => {
    return server({
        url: '/session/delete_msg',
        method: "POST",
        data
    })
}

/**
 * 分享会话
 * @returns 
 */
export const shareMsg = (data) => {
    return server({
        url: '/session/share',
        method: "POST",
        data
    })
}


/**
 * 滚动信息列表 
 * @returns 
 */
export const intro = () => {
    return server({
        url: '/index/intro',
        method: "GET",
    })
}

/**
 * 创建会话
 * @param {*}   
 * @returns 
 */
export const craeteSession = (data) => {
    const useChat = useChatStore()
    return server({
        url: '/session/new',
        method: "POST",
        data: { productId: useChat.productId, ...data }
    })
}

/**
 * 获取会话列表
 * @param {*} productId 
 * @returns 
 */
export const sessionList = (params) => {
    const useChat = useChatStore()
    return server({
        url: '/session/list',
        method: "GET",
        params: { productId: useChat.productId, friendId: useChat.currFriend, ...params }
    })
}


/**
 * 获取会话详情
 * @param {*} sessionId 
 * @returns 
 */
export const getSession = (params) => {
    return server({
        url: '/session/get',
        method: "GET",
        params
    }).catch(err => {
        if (err.response.status == '404') { // 当会话ID不存在时
            return { current: 1, pages: 0, records: [], total: 0, noData: true }
        }
        throw new Error(err)
    })
}



/**
 * 删除会话
 * @param {*} sessionId 
 * @returns 
 */
export const deleteSession = (sessionId) => {
    return server({
        url: '/session/delete',
        method: "POST",
        data: { sessionId }
    })
}

/**
 * 保存消息
 * @param {*} data {sessionId, "role": "user", content:""} 
 * @returns 
 */
export const saveMsg = (data) => {
    return server({
        url: '/session/save_msg',
        method: "POST",
        data
    })
}
/**
 * 创建会话并保存消息
 * @param {*} data  
 * @returns 
 */
export const saveSessionMsg = (data) => {
    return server({
        url: '/session/new_session_with_message',
        method: "POST",
        data
    })
}
