import { defineStore } from 'pinia'
import { v4 as uuidv4 } from 'uuid'
import { isMobile } from '@/common/utils'
import { PRODUCT_TYPE, ROLE_TYPE } from '@/common/config'
import { friendList } from '@/api/user'
import { saveSessionMsg } from '@/api/chat'
import { useCommunication } from "@/hooks/useCommunication";

const key = 'talkx_plugin_web_chat'

const noFMsg = true

const defaultSetting = {
    play: false,
    sending: false, // 是否回答中
    chats: [], // {uid：xxx, message}
    locaChats: [],
    currUid: '',
    currFriend: '', // 当前朋友 currChat
    productId: PRODUCT_TYPE.Web,//Vs_Code // IntelliJ_Idea, // 产品环境 0 Web (default) 1 DingTalk 2 MicroProgram 3 IntelliJ_Idea 4 Vs_Code
    friends: [],
    friendsChatMap: {}, // 不同类型对应的当前会话id
}

export const useChatStore = defineStore('chat', {
    state: () => {
        return { ...defaultSetting }
    },
    actions: {
        async getSession({ sessionId, friendId }) {
            const sessbj = this.locaChats.find(c => c.friendId == friendId && c.uid == sessionId) || { message: [] }
            // 本地数据不实行分页，因为本地数据有限
            return { current: Infinity, records: sessbj.message, total: sessbj.message.length }
        },
        async uploadLocaSession() { // 上传所有本地会话
            // 过滤一些数据 
            const promiseArr = this.locaChats.map(async ({ productId, title, friendId, message }, i) => {
                const saveMsgList = message.map(({ content, role, createTime }) => ({ content, role, createTime }))
                const params = { productId, title, friendId, saveMsgList }
                return await new Promise(async (resolve) => {
                    await saveSessionMsg(params)
                    resolve({ success: true, i })
                }).catch(() => ({ success: false }))
            })
            const list = await Promise.all(promiseArr)
            for (let j = 0; j < this.locaChats.length; j++) {
                const { success } = list[j]
                success && this.locaChats.splice(j, 1)
            }
        },
        deleteLocaSession(sessionId) {
            const cindex = this.locaChats.findIndex(lc => lc.uid == sessionId)
            this.locaChats.splice(cindex, 1)
        },
        createLocaSession({ _uid, title, friendId, message }) { // 创建本地会话
            const uid = _uid || uuidv4()
            const obj = { uid, productId: this.productId, title, friendId, loca: true, message: message || [] }
            this.locaChats.push(obj)
            return { id: uid }
        },
        saveLocaMsg({ content, role, sessionId, status }) {
            try {
                const newObj = { content, role, status, sessionId, id: uuidv4(), createTime: new Date() }
                const currLocaChats = this.locaChats.find(lc => lc.uid == sessionId)
                if (currLocaChats) { currLocaChats.message.push(newObj) }
                else { // 处于清理缓存的情况，登录状态丢失, 重新创建一个本地缓存
                    const title = content.slice(0, 32).trim();
                    this.createLocaSession({ _uid: sessionId, title, friendId: this.currFriend, message: [newObj] })
                }
                return newObj // { content: "", createTime: "", id: , role: "", sessionId: "" }
            } catch (e) {
                console.log('saveLocaMsg-err', e);
            }
        },
        deleteLocaMsg({ messageId }) {
            const currLocaChats = this.locaChats.find(lc => lc.uid == this.getCurrUid)
            if (!currLocaChats) return
            const cmIndex = currLocaChats.message.findIndex(m => m.id == messageId)
            currLocaChats.message.splice(cmIndex, 1)
        },
        setFriend(fid) {
            let IDEDefaultFid
            // 当在IDE环境添加新会话时，默认的好友是 编程助手
            // if (!fid && !this.isWeb) {
            //     const f = this.friends.find(f => f.roleType === ROLE_TYPE.coding_assistant)
            //     if (f) IDEDefaultFid = f.id
            // }
            this.currFriend = fid || IDEDefaultFid || this.currFriend;
            const fuid = this.friendsChatMap[this.currFriend]
            this.triggerChat(fuid)
        },
        async setfriends() {
            this.friends = await friendList()
            if (!this.currFriend) {
                this.setFriend(this.friends[0].id)
            }
        },
        addFriends(obj) {
            const f = this.friends.find((c) => c.id == obj.id);
            if (f) { return }
            this.friends.push(obj)
        },
        setText(payload) {
            this.message = payload
        },
        setSending(payload) {
            this.sending = payload
        },
        triggerChat(uid) { // 切换会话
            this.currUid = uid
            this.friendsChatMap[this.currFriend] = this.currUid
        },
        addMsg(payload) { // 添加记录到当前会话  
            this.currChat.message.push(payload)
            // if (this.currChat.message.length > 32) {
            //     this.currChat.message.shift()
            // } 
        },
        removeChat(uid) {
            const index = this.chats.findIndex(c => uid === c.uid)
            this.chats.splice(index, 1)
        },
        removeChatMsgEnd() {
            return this.removeChatMsg(this.currMsg.length - 1)
        },
        removeChatMsg(i) { // 清楚某一条消息
            const obj = this.chats.find(c => this.currUid === c.uid)
            const removeObj = obj.message.splice(i, 1)
            return removeObj
        },
        removeMsg(msg) { // 删除某个会话 
            const { sessionId, id } = msg
            const chat = this.chats.find(c => c.uid === sessionId)
            const index = chat.message.findIndex(m => m.id == id)
            return chat.message.splice(index, 1)
        },
        updataMsg({ updataObj, index }) { // 更新某一条会话记录
            const obj = this.chats.find(c => this.currUid === c.uid)
            if (!obj) return
            Object.assign(obj.message[index], updataObj)
        },
        changeUid(uid) { // 切换uid, 用接口返回的id 
            const obj = this.chats.find(c => this.currUid === c.uid)
            if (!obj) return
            obj.uid = uid
            delete obj.temp
            this.triggerChat(uid)
        },
        changeProductId(productId) {
            this.productId = productId
            try {
                document.body.classList[this.isWeb ? 'remove' : 'add']('IDE')
                document.body.classList[this.isWeb ? 'add' : 'remove']('WEB')
            } catch (e) { }
        },
        unshiftChat(uid, message) { // 往前面插入内容
            let obj = this.chats.find(c => uid === c.uid)
            if (obj) {
                obj.message = [...this.getFriendMsg(), ...message, ...this.currPuseMsg]
                // obj.message.unshift(...message) 
            } else {
                this.updateChat(uid, message)
            }
        },
        updateChat(uid, message) { // 更新会话  
            let obj = this.chats.find(c => uid === c.uid)
            if (obj) {
                obj.message = [...this.getFriendMsg(), ...message]
            } else {
                this.chats.push({ uid, message })
            }
        },
        updateChatInfo(uid, info = {}) {
            const obj = this.chats.find(c => uid === c.uid)
            obj && Object.assign(obj, info, { message: obj.message })
        },
        clearCurrChat() { // 清空当前会话记录
            this.currChat.message = []
        },
        currChatLastAnswer() { // 当前会话最后一条回答
            const obj = this.chats.find(c => this.currUid === c.uid)
            return obj.message[obj.message.length - 1]
        },
        getFriend(roleType) {
            return this.friends.find(f => f.roleType == roleType)
        },
        getFriendMsg() {
            let message = []
            if (this.isWeb && !noFMsg) {
                const content = this.currFriendInfo.welcome
                message = [
                    { welcome: true, role: "user", content: "" },
                    { welcome: true, role: "assistant", content }
                ]
            }
            return message
        },
        loginOutClear() {
            Object.assign(this, {
                locaChats: [],
                chats: [],
                currUid: ""
            })
        },
        newTopicMsg() {
            const friend = this.getFriend(ROLE_TYPE.coding_assistant)
            let msg = []
            if (friend && !this.isWeb && !noFMsg) {
                msg = [
                    { welcome: true, newTopic: true, role: "user", content: "" },
                    { welcome: true, newTopic: true, role: "assistant", content: friend.welcome }
                ]
            }
            return msg
        }
    },
    getters: {
        isPlay: state => state.play,
        getCurrUid() {
            return this.friendsChatMap[this.currFriend] || this.currUid || this.currChat.uid
        },
        currFriendInfo() {
            let finfo = this.friends.find(c => c.id == this.currFriend)
            if (!finfo) {
                finfo = this.friends[0]
                // this.setFriend(finfo.id)
            }
            return finfo || {}
        },
        isWeb() { // h5
            return this.productId == PRODUCT_TYPE.Web && !isMobile()
        },
        getSending() {
            return this.sending
        },
        currMsg() {
            return this.currChat.message
        },
        currPuseMsg() {
            return this.currChat.message.filter(m => !m.welcome).map(m => ({ ...m }))
        },
        currChat() {
            if (!this.currUid) {
                this.currUid = 'temp'//uuidv4()
                const ids = []
                for (let i = 0; i < this.chats.length; i++) {
                    if (!this.chats[i].message.length || this.chats[i].temp) {
                        ids.push(this.chats[i].uid)
                    }
                }
                ids.forEach(id => {
                    const index = this.chats.findIndex(c => c.uid === id)
                    this.chats.splice(index, 1)
                })
                const obj = { uid: this.currUid, fid: this.currFriend, temp: true, message: this.getFriendMsg() }
                this.chats.push(obj)
                return obj
            }
            const chat = this.chats.find(c => c.uid === this.currUid) || { message: [] }
            const isNewTopicMsg = chat.message.find(f => f.newTopic)
            if (chat.isNewTopic && !isNewTopicMsg) { // 把新会话的头部内容添加进去   
                chat.message.unshift(...this.newTopicMsg())
            }
            return chat
        },
        avater() {
            const f = this.friends.find(f => this.currFriend == f.friendId)
            return f ? f.avatar : ''
        }
    },
    persist: {
        key: 'talkx_plugin_web_chat'
    }
})