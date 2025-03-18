
import { isMobile } from "@/common/utils";
import { PRODUCT_TYPE } from '@/common/config'

export const isVscode = () => {
    if (window.talkxEditorInfo) {
        return window.talkxEditorInfo.productName.includes('Visual Studio Code') && window.navigator.userAgent.includes('Code/')
    }
    return false
}
export const isHbuilder = () => {
    if (window.talkxEditorInfo) {
        return window.talkxEditorInfo.productName.includes('HBuilder')
    }
    return false
}

export const isIDE = () => {
    const search = window.location.search
    return search.includes('?productName=IntelliJ+IDEA+')
}
export const isPyCharm = () => {
    // ?productName=PyCharm+2022.1.3
    const search = window.location.search
    return search.includes('?productName=PyCharm')
}
export const isWebStorm = () => {
    const search = window.location.search
    return search.includes('?productName=WebStorm')
}
export const isAndroidStudio = () => {
    const search = window.location.search
    return search.includes('?productName=Android+Studio')
}
export const isGoLand = () => {
    const search = window.location.search
    return search.includes('?productName=GoLand')
}
export const isMindStudio = () => {
    const search = window.location.search
    return search.includes('?productName=MindStudio')
}
export const isRider = () => {
    const search = window.location.search
    return search.includes('?productName=JetBrains+Rider')
}
export const isPhpStorm = () => {
    const search = window.location.search
    return search.includes('?productName=PhpStorm')
}
export const isFleet = () => {
    const search = window.location.search
    return search.includes('?productName=Fleet')
}

export const getEnvironment = () => {
    if (isVscode()) { return PRODUCT_TYPE.Vs_Code }
    if (isIDE()) { return PRODUCT_TYPE.IntelliJ_Idea }
    if (isFleet()) { return PRODUCT_TYPE.Fleet }
    if (isRider()) { return PRODUCT_TYPE.Rider }
    if (isPyCharm()) { return PRODUCT_TYPE.PyCharm }
    if (isWebStorm()) { return PRODUCT_TYPE.WebStorm }
    if (isPhpStorm()) { return PRODUCT_TYPE.PhpStorm }
    if (isGoLand()) { return PRODUCT_TYPE.GoLand }
    if (isMindStudio()) { return PRODUCT_TYPE.MindStudio }
    if (isHbuilder()) { return PRODUCT_TYPE.HBuilder }
    if (isAndroidStudio()) { return PRODUCT_TYPE.Android_Studio }
    return PRODUCT_TYPE.Web
}

// 非IDE 环境
export const isWeb = () => getEnvironment() == PRODUCT_TYPE.Web
export const isPC = () => isWeb() && !isMobile()
export const isWebMobile = () => isWeb() && isMobile()
export const isWebSmall = () => isWeb() && window.innerWidth < 600 