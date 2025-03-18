import { virtual_version } from './config'
import { isIDE, isVscode, isPyCharm, isHbuilder, isWebStorm, isAndroidStudio, isRider, isPhpStorm, isFleet } from '@/common/env'


// 刷新指令
export const ENV_RELOAD = ['TALKX_RELOAD']
// 获取信息指令
export const ENV_CONSTS = ['TALKX_ENV_INFO_']

export const getEnvironmentInfo = () => {
    return '```js \n' +
        `{
    isIDE: ${isIDE()},
    isRider: ${isRider()},
    isFleet: ${isFleet()},
    isVscode: ${isVscode()},
    isPyCharm: ${isPyCharm()},
    isHbuilder: ${isHbuilder()},
    isWebStorm: ${isWebStorm()},
    isPhpStorm: ${isPhpStorm()},
    isAndroidStudio: ${isAndroidStudio()},
    search: ${window.location.search}

    virtual_version: ${virtual_version},

} \n` + '```'
}


const commomandArr = [
    { keys: ['TALKX_RELOAD'], type: 'reload' },
    { keys: ['TALKX_SHOW_VERSION'], type: 'show_version' },
    { keys: ['TALKX_ENV_INFO_'], type: 'info', extraFun: getEnvironmentInfo }
]

export const commomandHander = (str, rules) => {
    for (let i = 0; i < commomandArr.length; i++) {
        const { keys, type, extraFun = () => { } } = commomandArr[i];
        const isCommomand = keys.includes(str.trim())
        if (isCommomand) {
            const ruleObj = rules.find(r => r.type === type)
            ruleObj.handler(extraFun())
            return true
        }
    }
}
