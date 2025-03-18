import { computed, reactive } from 'vue'
import { verify } from '@/common/verify'
import { verConfig } from './verifyConfig.js'
import { sign, checkCode, checkPhone } from '@/api/index'


/**
 * 手机号修改逻辑
 * @param {*} type 
 * @param {*} message 
 * @returns 
 */
export default function (type, message) {

    const sendCode = async (value) => {
        const { err, errMsg } = await sign(value)
        if (err) {
            message.error(errMsg)
            return
        }
        message.success('验证码发送成功')
        return true
    }

    const verCode = async (phoneNum, code) => {
        const { err, errMsg } = await checkCode({ phoneNum, code })
        if (err) {
            message.error(errMsg)
            return
        }
        return true
    }

    const verifyPhone = async (phoneNum) => {
        const { err, errMsg } = await checkPhone({ phoneNum })
        if (err) {
            message.error(errMsg)
            return
        }
        return true
    }

    const phoneObj = reactive({
        step: 0,
        config: [
            { rightText: '下一步', disabled: true, value: "", obj: verConfig.phone, info: () => `你正在更换手机号码，我们将向下方的手机发送一条验证码。` },
            {
                rightText: '下一步', value: "", obj: verConfig.code, info: () => {
                    const v = phoneObj.config[phoneObj.step - 1].value
                    return `我们已经向 ${v} 发送了一个六位数的验证码。该码即将过期。`
                }
            },
            { rightText: '下一步', value: "", obj: verConfig.phone, info: () => `请输入新的手机号码，我们将向这个手机发送一条验证码。` },
            {
                rightText: '完成', value: "", obj: verConfig.code, info: () => {
                    const v = phoneObj.config[phoneObj.step - 1].value
                    return `我们已经向 ${v} 发送了一个六位数的验证码。该码即将过期。`
                }
            },
        ],
    })
    const isPhone = computed(() => type == 'phone')
    const currPhone = computed(() => phoneObj.config[phoneObj.step] || { info: () => { } })

    const phoneOver = async (value) => {
        let result = { all: false, result: false }
        const errMsg = await verify(value, currPhone.value.obj.verity)
        if (errMsg) {
            message.warning(errMsg)
            return result
        }
        phoneObj.config[phoneObj.step].value = value
        if ([0, 2].includes(phoneObj.step)) {
            // 校验手机号是否被使用
            if (
                [2].includes(phoneObj.step) && !await verifyPhone(value) || // 校验手机号
                !await sendCode(value) // 发送短信
            ) { return result }
        } else {
            const phoneNum = phoneObj.config[phoneObj.step - 1].value;
            if (!await verCode(phoneNum, value)) {
                return result
            }
            result.phoneNum = phoneNum
        }
        phoneObj.step++
        Object.assign(result, { result: true, all: phoneObj.step >= phoneObj.config.length })
        return result
    }

    const phonePrev = (n = 1) => {
        if (phoneObj.step - n >= 0) {
            phoneObj.step -= n
        } else {
            return true // 处于第一阶段
        }
    }

    return { isPhone, phoneObj, currPhone, phonePrev, phoneOver }
}