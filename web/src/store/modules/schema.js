import { defineStore } from 'pinia'
import { schemaList, delSchema, saveSchema } from '@/api/schema'

export const useSchemaStore = defineStore('talkx_schema', {
    state: () => ({
        list: [],
        current: "",
        text: "",
        currId: ''
    }),
    actions: {
        setText(text) {
            this.text = text
        },
        setCurr(id) {
            this.current = id
        },
        setSessionId(id) {
            this.currId = id
        },
        async getList() {
            const data = await schemaList()
            this.list = data
            if (!this.current && data.length || !this.curr) {
                this.setCurr(data[0].id)
            }
        },
        getSchema(id) {
            return this.list.find(item => item.id == id)
        }
    },
    getters: {
        sessionId: state => state.currId,
        curr: state => state.list.find(item => item.id == state.current),
    },
    persist: {
        key: 'talkx_plugin_schema'
    }
})