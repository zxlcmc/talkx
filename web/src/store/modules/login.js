import { defineStore } from 'pinia'
import { ss } from '@/common/storage'
import { virtual_version } from '@/common/config'
import { login, logout } from '@/api/index'
import { getUser, userUpdate } from '@/api/user'
import { dot } from '@/common/utils'
import { useGlobalStore } from "./global";

const key = 'talkx_plugin_web_login'

const defaultUser = {
    avatar: 'https://plugin-web.talkx.cn/images/2023/07/26/84e9ed96f2e842bbba8b2e6c5ae8a415.png'
}

const defaultSetting = {
    loginStatus: false,
    token: '',
    phone: '',
    user: null,
    theme: 'dark'
}

// 数据存储不统一的原因，防止用户本来是登录，让用户要重新登录

export const themeObj = {
    dark: { next: 'light', name: '暗色' },
    light: { next: 'dark', name: '亮色' }
}


export const useLoginStore = defineStore('login', {
    state: () => {
        return {
            ...defaultSetting,
            ...ss.get(key)
        }
    },
    // persist:{ key },
    actions: {
        async dot(params) {
            const { id, model } = await this.getUser()
            params.user_id = id
            if (params.mt == 2) { params.model = model }
            const useGlobal = useGlobalStore()
            dot({
                talkx_ver: useGlobal.editer.talkxVersion,
                web_ver: virtual_version,
                ...params
            })
        },
        async getUser(isUpdata = false) {
            if (!this.$state.loginStatus) {
                return defaultUser
            }
            if (isUpdata || !this.$state.user) {
                this.$state.user = await getUser()
            }
            this.recordState()
            return this.$state.user
        },
        async setUser(params) {
            const result = await userUpdate(params)
            await this.getUser(true)
            return result
        },
        setTheme(theme) {
            this.$state.theme = theme || this.$state.theme || defaultSetting.theme
            const root = document.documentElement
            root.classList.remove(themeObj[this.$state.theme].next)
            root.classList.add(this.$state.theme)
            this.recordState()
        },
        async login({ phoneNum, code, inviteCode }) {
            const { token, errMsg } = await login({ phoneNum, code, inviteCode }).catch(errMsg => {
                return { errMsg: errMsg.response.data }
            })
            let params = { err: false }
            if (!errMsg) {
                this.$state.token = token
                this.$state.loginStatus = true
                this.$state.phone = phoneNum
                this.getUser(true)
                this.recordState()
            } else {
                params = { err: true, errMsg }
            }
            return params
        },
        async logOut() {
            try {
                await logout()
                this.nullify()
                this.recordState()
            } catch (err) {
                console.error('退出失败', err)
            }
        },
        recordState() {
            ss.set(key, this.$state)
        },
        nullify() {
            this.$state.loginStatus = false
            this.$state.phone = ''
            this.$state.user = ''
            this.$state.token = ''
            this.recordState()
        },
        getShareUrl(inviteCode = this.inviteCode) {
            const url = new URL(location.href)
            url.hash = '#/dialogue?inviteCode=' + inviteCode
            return url.toString()
        }
    },
    getters: {
        isLogin: (state) => state.$state.loginStatus,
        vtoken: (state) => state.$state.token,
        nextThemeName: (state) => themeObj[themeObj[state.theme].next].name,
        inviteCode: (state) => state.user ? state.user.inviteCode : "",
        coins: (state) => state.user ? state.user.coins : "",
    }
}) 