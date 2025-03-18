
import { computed, ref, inject } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ROLE_TYPE } from '@/common/config'
import { useChatStore, useLoginStore } from '@/store'
import { getSession, deleteSession, craeteSession, saveMsg, deleteMsg } from '@/api/chat'
import { favoritesMsg } from '@/api/favorites'
import { domMove } from '@/common/utils'

const useChat = useChatStore()
const useLogin = useLoginStore()


// 新会话类型 0：新会话 1：新话题
const newChatType = ref(0)
const historyRef = ref(null)

// 当前是否新话题
const isNewTopic = ref(false)

// 消息是否使用本地缓存
const useLoca = computed(() => useLogin.isLogin)
// 是否新会话
const isNew = computed(() => {
    let list = useChat.currChat.message
    if (useChat.isWeb) {
        list = list.filter(m => m.sessionId)
    }
    return list.length <= 0
})


// 数据补全
const dataComplement = (arr) => {
    return arr.map(c => {
        if (!c.chat.find(c => c.role == "user")) { c.chat.unshift({ id: Math.random(), role: "user", sessionId: "", content: "" },) }
        if (!c.chat.find(c => c.role == "assistant")) { c.chat.push({ id: Math.random(), role: "assistant", sessionId: "", content: "" },) }
        return c
    })
}

// 聊天数据分割 
const dataPartition = data => {
    var arr = [], index = -1, prev = '';
    data.forEach((obj, j) => {
        if (data[prev] && data[prev].role == obj.role || !data[prev] || obj.role === "user") {
            index++
            if (!arr[index]) { arr[index] = { chat: [] } }
        }
        prev = j
        // arr[index].id = j + '-' + useChat.getCurrUid + '';
        arr[index].chat.push({ ...obj, __index: j })
    })
    arr.forEach((obj, i) => { 
        // 就算删除一个消息 也要保持 id一致, 为了保证尽可能的不刷新子组件 
        // 取值useChat.getCurrUid原因，防止在回到消息的时候还没有消息id,相加得到NaN，导致key是NaN，导致提问一直刷新
        let suff = obj.chat[0]?.sessionId || obj.chat[1]?.sessionId || useChat.getCurrUid
        obj.id = suff + i  //useChat.getCurrUid + i//obj.chat[0]?.id + obj.chat[1]?.id
    })
    return arr
}

const messageList = computed(() => { 
    let arr = dataPartition(useChat.currChat.message)
    // 数据补全
    arr = dataComplement(arr) 
    return arr
})

// 新话题消息
const newType1Data = computed(() => {
    let arr = dataPartition(useChat.isWeb ? useChat.getFriendMsg() : useChat.newTopicMsg())
    arr = dataComplement(arr)
    return arr
})


/**
 * 获取当前会话消息
 * @param {*} params 
 * @returns 
 */
const getChatMsg = async (params) => {
    let data =  useLoca.value ? await getSession(params) : await useChat.getSession(params).then(({ records, ...others }) => ({ records: records.reverse(), ...others }))
    
//     data.records[0].content = `<think>
// AAAAAA
// <think>55555</think>

// <think>
// 666666
// </think>

// BBBBBB 
// </think>
// others
//     `
    return data
    }

/**
 * 创建会话
 * @param {*} params 
 * @returns 
 */
const createChat = async (params) => {
    return useLoca.value ? await craeteSession(params) : useChat.createLocaSession(params)
}

/**
 * 删除会话
 * @param {*} params 
 */
const delChat = async () => {
    useLoca.value ? await deleteSession(useChat.getCurrUid) : useChat.deleteLocaSession(useChat.getCurrUid);
    useChat.removeChat(useChat.getCurrUid)
}

/**
 * 保存回答消息
 * @param {*} params 
 */
const saveChatMsg = async (params) => {
    if (useLoca.value) {
        let resendindex = 0
        const resend = async (err) => {
            if (resendindex >= 3) return
            resendindex++
            if (err.code === 'ERR_NETWORK') {
                return await saveMsg(params).catch(async err => await resend(err))
            }
        }
        // 会莫名的失败 net::ERR_CONNECTION_RESET， 尝试重新发送
        return await saveMsg(params).catch(async err => {
            console.log('save-assistant-err', err)
            return await resend(err)
        })
    } else {
        return useChat.saveLocaMsg(params)
    }
}

/**
 * 删除消息
 * @param {*} params 
 */
const delChatMsg = async (params) => {
    useLoca.value ? await deleteMsg(params) : useChat.deleteLocaMsg(params);
}

/**
 * 添加临时消息组
 * @param {*} userData 
 * @param {*} assistantData 
 */
const addMsgGrounp = (userData = {}, assistantData = {}) => {
    useChat.addMsg({ role: "user", content: "", ...userData })
    useChat.addMsg({ role: "assistant", content: "", loading: true, ...assistantData })
}



/**
 * 收藏回答
 * @param {*} item 
 * @param {*} e 
 */
const cellect = async (item, e) => {
    await favoritesMsg({ messageId: item.chat[1].id })
    const targetDom = document.querySelector('.MdList') || document.querySelector('.user_row')
    const cdom = e.target
    domMove(cdom, targetDom) 
}

const exportObj = {
    historyRef,
    useChat, useLogin, useLoca, isNew, newChatType, messageList,
    delChat, createChat, cellect,
    getChatMsg, saveChatMsg, delChatMsg,
    addMsgGrounp,
    newType1Data, isNewTopic
}


export default exportObj