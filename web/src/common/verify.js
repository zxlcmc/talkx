import { strByteLen } from '@/common/utils'

// 不通过返回true
export const verifyFun = {
    empty: val => !val,
    nameRule: (val) => val.length > 10 || val.length < 2,
    emailRule: (val) => !/^[a-zA-Z0-9_-|.]+@[a-zA-Z0-9_-]+(.[a-zA-Z0-9_-]+)+$/.test(val),
    phoneRule: val => !!val && !/^(?:(?:\+|00)86)?1(?:(?:3[\d])|(?:4[5-79])|(?:5[0-35-9])|(?:6[5-7])|(?:7[0-8])|(?:8[\d])|(?:9[0-35-9]))\d{8}$/.test(val),
    length: (val, max, min) => val.length > max || val.length < min,
    betyLength: (val, max, min) => {
        const len = strByteLen(val) 
        return len > max || len < min
    },
    length: (val, max, min) => {
        const len = val.length
        return len > max || len < min
    }
}

export const verify = async (val, rules) => {
    return new Promise((resolve) => {
        for (let i = 0; i < rules.length; i++) {
            const { type, max, min, message } = rules[i]
            if (verifyFun[type] && verifyFun[type](val, max, min)) {
                return resolve(message)
            }
        }
        resolve()
    })
}