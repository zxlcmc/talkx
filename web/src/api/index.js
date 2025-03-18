import server, { baseURL } from './require'

export const upload_img_url = baseURL + '/upload/img'



/**
 * 上传图片
 * @param {*}   
 * @returns 
 */
export const uploadImg = async (formdata) => {
    return server({
        url: '/upload/img',
        method: "POST",
        type:'file',
        'Content-type':'multipart/form-data;', 
        data: formdata
    })
}
/**
 * 上传文件
 * @param {*}   
 * @returns 
 */
export const uploadFile = async (formdata) => {
    return server({
        url: '/upload/file',
        method: "POST",
        type:'file',
        'Content-type':'multipart/form-data;', 
        data: formdata
    })
}

/**
 * 获取验证码
 * @param {*} phoneNum 
 * @returns 
 */
export const sign = async (phoneNum) => {
    return server({
        url: '/user/send_code',
        method: "POST",
        data: { phoneNum }
    }).catch(err => {
        if (err.code === "ERR_BAD_REQUEST") { // 操作过于频繁
            return { err: true, errMsg: err.response.data }
        }
        return { err: true }
    })
}

/**
 * 校验验证码
 * @param {*}  
 * @returns 
 */
export const checkCode = async (data) => {
    return server({
        url: '/user/check_code',
        method: "POST",
        data
    }).catch(err => {
        if (err.code === "ERR_BAD_REQUEST") {  
            return { err: true, errMsg: err.response.data }
        }
        return { err: true }
    })
}
/**
 * 校验手机号
 * @param {*}  
 * @returns 
 */
export const checkPhone = async (data) => {
    return server({
        url: '/user/info/check_phone',
        method: "POST",
        data
    }).catch(err => {
        if (err.code === "ERR_BAD_REQUEST") {  
            return { err: true, errMsg: err.response.data }
        }
        return { err: true }
    })
}

/**
 * 登陆
 * @param {*} phoneNum 
 * @param {*} code 
 * @returns 
 */
export const login = (data) => {
    return server({
        url: '/user/login',
        method: "POST",
        data
    })
}

/**
 * 退出登录
 * @param {*} phoneNum 
 * @param {*} code 
 * @returns 
 */
export const logout = () => {
    return server({
        url: '/user/logout',
        method: "POST"
    })
}


/**
 * 保持活跃
 * @param {*} params  
 * @returns 
 */
export const keepAlive = (params) => { 
    return server({
        url: '/index/keep_alive',
        method: "GET",
        params
    })
    // return Promise.resolve({
    //     "intro": [
    //         "你好！我是「TalkX」，一个基于NLP技术实现的智能聊天机器人。我的目标是使交流变得无限可能。",
    //         "你可以跟我聊任何你感兴趣的话题，无论是关于生活、工作、娱乐、教育和科技等等。",
    //         "我可以回答各种问题，提供信息、建议和解决方案，帮助人们更好地理解世界和解决问题。",
    //         "我会尽力提供最准确、最相关的答案，但不能保证完全正确，因为我的回答是基于我所学习到的知识和语言模型生成算法。",
    //         "如果你想在某个领域得到更专业的帮助并解决相关问题，可以通过普通版本中的「AI」进行交流探讨。",
    //         "我目前无法保证我所提供的信息是最新或最全面的，所以建议在做出任何决策之前，仍然需要自己进行深入的研究和核实。"
    //     ],
    //     "notification": {
    //         "id": 23,
    //         "title": "TalkX用户满意度调查",
    //         "type": 1,
    //         "pinned": 1,
    //         "content": "<iframe src='https://www.wjx.cn/vm/Q2TFEKz.aspx?width=750&source=iframe&s=t' width='100%' height='650' frameborder='0' style='overflow:auto'></iframe>",
    //         "read": 1,
    //         "createTime": "2023-10-16T14:56:41"
    //     },
    //     "ideVersion": {
    //         "id": 3,
    //         "productType": 2,
    //         "version": "0.0.13",
    //         "required": 0,
    //         "releaseTime": "2023-10-24T09:17:16"
    //     },
    //     "webVersion": {
    //         "id": 2,
    //         "productType": 1,
    //         "version": "0.0.2",
    //         "required": 0,
    //         "releaseTime": "2023-10-23T18:02:06"
    //     }
    // })
}
