import axios from "axios";
import loadAjax from './loadAjax'
import { useLoginStore } from '@/store'
import { useToast } from '@/hooks/useToast'
import { useLoadingPress } from '@/hooks/useLoadingPress'
const { showToast } = useToast()
const { setShow } = useLoadingPress()
let showPressTimer;
export const baseURL = import.meta.env.DEV ? '/api' : import.meta.env.VITE_MODE_API_URL
const service = axios.create({
    withCredentials: true,
    baseURL,
    timeout: 30 * 1000
})

service.interceptors.request.use(config => {
    clearTimeout(showPressTimer)
    showPressTimer = setTimeout(() => setShow(true), 300)
    const useLogin = useLoginStore()
    if (useLogin.token) {
        config.headers['talkx-token'] = useLogin.token
    }
    if (config.type !== 'file' && (config.method === 'post' || config.method === 'put') && config.dataType !== 'json') {
        config.headers['Content-Type'] = 'application/json'
    }
    return config;
}, error => {
    clearTimeout(showPressTimer)
    return Promise.reject(error);
})


service.interceptors.response.use(
    response => { 
        clearTimeout(showPressTimer)
        setShow(false)  
        return  response.data;
    },
    error => {
        clearTimeout(showPressTimer)
        setShow(false)
        console.log('response--error', error);
        // 超时提示
        if (error.response && error.response.status == 403) {
            const useLogin = useLoginStore()
            useLogin.nullify()
            showToast('login', error.response.data)
        }
        return Promise.reject(error);
    }
);

const ajax = (options) => {
    const { url, method, data } = options
    const useLogin = useLoginStore()
    const headers = {}
    const params = ['get', 'GET', 'urlencoded'].includes(method) ? options.params : options.data;
    if (useLogin.token) {
        headers['talkx-token'] = useLogin.token
    }
    return loadAjax({
        url: baseURL + url,
        method,
        headers,
        params: params || {}
    })
}

export default service
// export default ajax


export const errHander = err => {
    const statusText = { 
        400: '参数错误或者缺少参数',
        500: '服务器错误',
    }
    let errMsg = err.response.data || statusText[err.response.status] //|| '修改失败'
    if(err.response.status === 403) {
        errMsg = ""   
    }
    return { err:true,  errMsg }
}
