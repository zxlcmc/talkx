
import { reactive } from 'vue'
import { v4 as uuidv4 } from 'uuid'
const toastRef = reactive({ show: false, type: '', text: "", curr: '', maskclosable: true, escclose: true })
let cbs = {}

export function useToast(name) {
    const types = ['feature', 'login', 'del', 'nodel', 'logout', 'auto', 'copy', 'update_system']
    const uid = name || uuidv4()
    cbs[uid] = {}
    const showToast = (type, text = "", closeText = '', confirmText = '', others={}) => {
        if (!types.includes(type)) {
            return new Error('请填写正确的type', types.join(','))
        }
        toastRef.show = true
        toastRef.type = type 
        toastRef.text = text
        toastRef.closeText = closeText
        toastRef.confirmText = confirmText
        toastRef.maskclosable = !['update_system'].includes(type)
        toastRef.escclose = !['update_system'].includes(type)
        toastRef.curr = uid; // 当前激活的
        toastRef.others = others
    }
    const hideToast = () => {
        toastRef.show = false
        toastRef.type = ''
        toastRef.text = ''
        toastRef.closeText = ''
        toastRef.confirmText = ''
        toastRef.maskclosable = true
        toastRef.escclose = true
        toastRef.curr = ''
        toastRef.others = {}
    }

    const closeWatch = (type, cb = () => { }) => {
        const attrName = type
        if (!cbs[uid][attrName]) { cbs[uid][attrName] = [] }
        if (cbs[uid][attrName].includes(cb)) return
        cbs[uid][attrName].push(cb)
    }

    // 只能触发对应uid的key
    const closeToast = ({ type, confirm }) => {
        Object.entries(cbs).forEach(([uid, obj]) => {
            obj && Object.entries(obj).forEach(([key, values]) => {
                if (type === key && toastRef.curr == uid) {
                    values.forEach(c => c(confirm))
                }
            })
        })
    }

    const clearWatch = () => {
        types.forEach((type) => {
            if (cbs[uid][type]) { cbs[uid][type] = [] }
        })
    }

    return {
        toastRef,
        uid,
        showToast,
        hideToast,
        closeToast,
        closeWatch,
        clearWatch
    }
}