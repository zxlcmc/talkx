

export const delay = (time = 1000) => {
    return new Promise(resolve => setTimeout(resolve, time))
}

export const isZFB = () => !!/AlipayClient/.test(window.navigator.userAgent)
export const isWX = () => !!/MicroMessenger/.test(window.navigator.userAgent)

export const isIOS = () => !!navigator.userAgent.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/)
export const isMac = () => !!/macintosh|mac os x/i.test(navigator.userAgent);
export const isMobile = () => !!/Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent);

export const dot = (params = {}, callback = () => { }) => {
    const url = 'https://openlog.bianxianmao.com/talkx/s.gif'
    const dParams = {
        // ip:"", 
        // ua:navigator.userAgent,
        // time: +new Date()
    }
    Object.assign(dParams, params)
    const parstr = Object.entries(dParams).map(([key, value = ""]) => key + '=' + value).join('&')
    const src = url + '?' + parstr
    const img = new Image(1, 1);
    img.onload = callback
    img.onerror = callback
    img.src = src;
}

export const strByteLen = (str) => {
    return str.split("").reduce((n, c) => n += c.charCodeAt() > 255 ? 2 : 1, 0)
}

export function random(min, max) {
    return Math.floor(Math.random() * (max + 1 - min) + min)
}

export function isSmall() {
    const w = window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth;
    return w < 600
}

export function isPhone() {
    const userAgent = navigator.userAgent.toLowerCase();
    return userAgent.match(/(iphone|ipod|ipad|android|firefox)/) || isSmall()
}



/**
 * 将日期对象转化为一个预设格式的字符串
 * @param date 一个日期对象
 * @param fmt 预期格式
 * @returns 返回格式化后的字符串
 */
export function format(date, fmt) {
    var o = {
        // 月份
        'M+': date.getMonth() + 1,
        // 日
        'd+': date.getDate(),
        // 小时
        'h+': date.getHours(),
        // 分
        'm+': date.getMinutes(),
        // 秒
        's+': date.getSeconds(),
        // 季度
        'q+': Math.floor((date.getMonth() + 3) / 3),
        // 毫秒 
        'S': date.getMilliseconds()
    };
    if (/(y+)/.test(fmt)) {
        fmt = fmt.replace(RegExp.$1, (date.getFullYear() + '').substr(4 - RegExp.$1.length));
    }
    for (var k in o) {
        if (new RegExp('(' + k + ')').test(fmt)) {
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (('00' + o[k]).substr(('' + o[k]).length)));
        }
    }
    return fmt;
}


/**
 * 日期显示  今天 11:03  昨天 10:04 09月08日 10:08 
 * @param {*} time 
 */
export const timeStr = (time) => {
    const currTime = format(new Date(), 'yyyy-MM-dd')
    const time2 = format(new Date(time), 'yyyy-MM-dd')
    const diffDay = (new Date(currTime).getTime() - new Date(time2).getTime()) / (24 * 60 * 60 * 1000)
    const obj = { 0: '今天 hh:mm', 1: '昨天 hh:mm', 2: '前天 hh:mm', default: 'MM月dd日 hh:mm' }
    const currYear = +format(new Date(), 'yyyy')
    const timeYear = +format(new Date(time), 'yyyy')
    const diffYear = timeYear - currYear != 0;
    const yearStr = diffYear ? 'yyyy年' : '';
    // 今天 昨天 前天 x月x日
    return format(new Date(time), obj[diffDay] || yearStr + obj.default)
}

/** 是否整数 */
export function isInteger(num) {
    return Math.floor(num) === num;
}


/**防抖处理 **/
export function debance(fun, delay = 300) {
    let time = null
    return (...args) => {
        clearTimeout(time)
        time = setTimeout(() => {
            fun(...args)
        }, delay)
    }
}

/** 获取链接上的参数 */
export function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]); return null;
}


/**
 * router.back 返回没有历史记录处理
 * @param {*} router 
 * @param {*} pushSpare 没有历史记录的跳转
 */
export function routerBack(router, pushSpare) {
    if (history.length <= 2 && pushSpare) {
        router.push(pushSpare)
    } else {
        router.back()
    }
}


/**
 * base64 转 file
 * @param {*} url 
 * @param {*} filename 
 */
export const base64ToFile = (url, filename = "file") => {
    let arr = url.split(',')
    let mime = arr[0].match(/:(.*?);/)[1]
    let suffix = mime.split('/')[1]
    let bstr = atob(arr[1])
    let n = bstr.length
    let u8arr = new Uint8Array(n)
    while (n--) {
        u8arr[n] = bstr.charCodeAt(n)
    }
    return new File([u8arr], `${filename}.${suffix}`, {
        type: mime
    })
}

/**
 * 获取图片尺寸
 * @param {*} src 
 */
const imgMap = new Map()
export const getImgWH = (src) => {
    if (imgMap.has(src)) {
        return imgMap.get(src)
    }
    return new Promise((resolve) => {
        const img = new Image()
        img.onload = () => {
            const { width, height } = img
            resolve({ width, height })
            imgMap.set(src, { width, height })
        }
        img.src = src
    })
}


export const domMove = (start, end) => {
    const span = document.createElement('span')
    const { width, height, top, left } = start.getBoundingClientRect()
    const { width: tw, height: th, top: tt, left: tf } = end.getBoundingClientRect()
    span.className = 'iconfont icon-jushoucang'
    span.style.color = 'red'
    span.style.position = 'fixed'
    span.style.left = left + 'px'
    span.style.top = top + 'px'
    span.style.width = width + 'px'
    span.style.height = height + 'px'
    document.body.appendChild(span)

    const c1 = { x: left + width / 2, y: top + height / 2 }
    const c2 = { x: tf + tw / 2, y: tt + th / 2 }

    span.style.zIndex = '10'
    span.style.transition = 'transform 1s '
    span.style.transform = `translate(0px,0px)`

    setTimeout(() => {
        setTimeout(() => {
            span.remove()
        }, 1000)
        span.style.transform = `translate(${c2.x - c1.x}px,${c2.y - c1.y}px)`
    }, 100)
}



/**
 * 优化gpt输出数学公式显示
 * 行内公式
 * \\( f(x) = x^n \\) ===> $\( f(x) = x^n \)$
 * 块级公式
 * \\[
 * f(x) = x^n
 * \\]
 * ===>
 * $$
 * f(x) = x^n
 * $$
 * @param {string} s 
 */
export const optimizeKatex = (s) => {

    // s = `\\( f(x) = x^n \\) $\( f(x) = x^n \)$`
    // s = `即 \\( b^n \\)，其中 \\( b \\) 代表底数  $\( b^n \)$ `
    // s = `\\[
    // f(x) = x^n
    // \\]
    // $$
    // f(x) = x^n
    // $$
    // `  
    // s = `\\( f(x) = x^n \\)
    // sss
    // \\( 2^3 = 2 \\times 2 \\times 2 = 8 \\)
    // `
    // s= `\\[
    // ( 2^3 = 2 \\times 2 \\times 2 = 8 \\)
    // \\]`
    // s= `$$
    // f(x) = x^n \\times 2
    // $$
    // `

    // 匹配  \\( f(x) = x^n \\)  
    s = s.replaceAll(/\\\(.*?\\\)/g, v => {
        // v = v.replaceAll('\\(', '(') 
        // v = v.replaceAll('\\)', ')')  
        v = v.replace(/\\\(|\\\)/g, (m) => m === '\\(' ? '(' : ')');
        return `$${v}$`
    })


    // 匹配  \\\[(.*?)\\\]
    s = s.replaceAll(/\\\[(.*?)\\\]/gs, v => {
        // v = v.replaceAll('\\[','')
        // v = v.replaceAll('\\]','')
        v = v.replaceAll(/\\\[|\\\]/g, '')
        return `$$${v}$$`
    })
    return s
}