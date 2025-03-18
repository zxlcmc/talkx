
import { ref, computed } from "vue"
import { useChatStore, useLoginStore, useSchemaStore } from '@/store'
import { getSession, deleteSession, craeteSession, saveMsg, deleteMsg } from '@/api/chat'
import { favoritesMsg } from '@/api/favorites'
import { domMove } from '@/common/utils'

const useChat = useChatStore()
const useLogin = useLoginStore()
const useSchema = useSchemaStore()

const messages = ref([])
const sending = ref(false)

const clearMesages = () => {
    messages.value = []
}

const updateAnwser = (obj) => {
    const lastAnswer = messages.value[messages.value.length - 1]
    if (!lastAnswer || lastAnswer.role !== 'assistant') return
    Object.assign(lastAnswer, obj)
}

const updateMessage = (obj, i) => {
    const data = messages.value[i]
    if (!data) return
    Object.assign(data, obj)
}

// 是否新会话
const isNew = computed(() => messages.value.length <= 0)


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
    arr.forEach(obj => {
        obj.id = useChat.getCurrUid + obj.chat[0]?.id + obj.chat[1]?.id
    })
    return arr
}

const messageList = computed(() => {
    let arr = dataPartition(messages.value)
    // 数据补全
    arr = dataComplement(arr)
    return arr
})

const getSendMessageData = () => {
    let list = messages.value.filter(
        (m) => !([500].includes(m.status))
    );
    const end = list[list.length - 1];
    // 如果最后一条是空的, 且是添加的空回答，就不添加到参数里面
    if (!end.id && !end.content) {
        list.pop();
    }
    list = list.map(({ role, content, attachments }) => ({
        role,
        content,
        attachments,
    }));
    // 排除数据，不能超过32条
    if (list.length > 32) {
        list.splice(0, 32 - list.length);
    }

    return list
}

const removeAnswer = () => {
    let i = messages.value.length - 1
    const lastAnswer = messages.value[i]
    if (!lastAnswer || lastAnswer.role !== 'assistant') return
    return messages.value.splice(i, 1)
}

/**
 * 删除消息
 * @param {*} params 
 */
const delChatMsg = async (params) => {
    await deleteMsg(params)
    const index = messages.value.findIndex(m => m.id == params.messageId)
    index >= 0 && messages.value.splice(index, 1)
}


/**
 * 删除会话
 * @param {*} params 
 */
const delChat = async (uid) => {
    await deleteSession(uid)
}


/**
 * 获取当前会话消息
 * @param {*} params 
 * @returns 
 */
const getChatMsg = async (params) => {
    return await getSession(params)
}

/**
 * 创建会话
 * @param {*} params 
 * @returns 
 */
const createChat = async (params) => {
    return await craeteSession(params)
}

/**
 * 保存回答消息
 * @param {*} params 
 */
const saveChatMsg = async (params) => {
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
}
/**
 * 添加临时消息组
 * @param {*} userData 
 * @param {*} assistantData 
 */
const addMsgGrounp = (userData = {}, assistantData = {}) => {
    messages.value.push({ role: "user", content: "", ...userData })
    messages.value.push({ role: "assistant", content: "", loading: true, ...assistantData })
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


export {
    useChat,
    useLogin,
    useSchema,

    messages,
    sending,
    isNew,
    messageList,


    cellect,
    delChat,
    getChatMsg,
    delChatMsg,
    createChat,
    saveChatMsg,
    addMsgGrounp,
    clearMesages,
    updateAnwser,
    updateMessage,
    removeAnswer,
    getSendMessageData,
}