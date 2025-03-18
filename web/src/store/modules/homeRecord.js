import { defineStore } from 'pinia'


/**
 * 引导记录
 */
export const useHomeRecordStore = defineStore('home_record', {
    state: () => ({
        records: {
            invite_rewards: false,
            my_hashrate: false,
            header_more: false,
            header_more_login: false,
            header_notify: false,
        }
    }),
    actions: {
        get(key) {
            return true //this.records[key]
        },
        set({ key, value = true }) {
            this.records[key] = value
        }
    },
    persist: {
        key: 'talkx_plugin_web_home_record'
    }
})