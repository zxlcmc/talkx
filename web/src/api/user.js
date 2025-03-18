import server from './require'

const errHander = err => {
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

const errHander2 = err => {
    if (err.response.status == 404) {
        return { exist: false }
    }
    return { errMsg: err.response.data || '服务器错误' }
}


/**
 * 检验用户是否存在
 * @returns 
 */
export const checkUser = async (data) => {
    return server({
        url: '/user/check_exists_user',
        method: "POST",
        data
    }).then(() => ({ exist: true }), errHander2)
}

/**
 * 检查邀请码是否存在
 * @returns 
 */
export const checkInviteCode = async (data) => {
    return server({
        url: '/user/check_exists_invite_code',
        method: "POST",
        data
    }).then(() => ({ exist: true }), errHander2)
}



/**
 * 修改用户信息
 * @returns 
 */
export const userUpdate = async (data) => {
    return server({
        url: '/user/info/update',
        method: "POST",
        data
    }).catch(errHander)
}

/**
 * 获取用户信息
 * @returns 
 */
export const getUser = async () => { 
    return server({
        url: '/user/info/get',
        method: "GET",
    })
}

/**
 * 模型列表
 * @returns 
 */
export const modelList = async () => {
    return server({
        url: '/user/info/list_model',
        method: "GET",
    })
}

/**
 * 提交用户反馈
 * @returns 
 */
export const feedback = async (data) => {
    return server({
        url: '/feedback/submit',
        method: "POST",
        data
    }).catch(errHander)
}


/**
 * 好友列表
 * @returns 
 */
export const friendList = async (params) => {
    return server({
        url: '/friend/list',
        method: "GET",
        params
    })
}

/**
 * 好友广场
 * @returns 
 */
export const friendplaza = async (params) => {
    return server({
        url: '/friend/plaza',
        method: "GET",
        params
    })
}
/**
 * 添加好友
 * @returns 
 */
export const friendFollow = async (data) => {
    return server({
        url: '/friend/follow',
        method: "POST",
        data
    }).catch(errHander)
}

/**
 * 新建好友
 * @returns 
 */
export const createFriend = async (data) => {
    return server({
        url: '/friend/create',
        method: "POST",
        data
    }).catch(errHander)
}

/**
 * 修改好友信息
 * @returns 
 */
export const updateFriend = async (data) => {
    return server({
        url: '/friend/update',
        method: "POST",
        data
    }).catch(errHander)
}

/**
 * 修改好友记忆配置
 * @param {*} data 
 * @returns 
 */
export const updateMemoryConfig = async (data) => {
    return server({
        url: '/friend/update_memory_config',
        method: "POST",
        data
    }).catch(errHander)
}

/**
 * 删除好友
 * @returns 
 */
export const deleteFriend = async (data) => {
    return server({
        url: '/friend/delete',
        method: "POST",
        data
    }).catch(errHander)
}

/**
 * 好友置顶
 * @returns 
 */
export const topFriend = async (data) => {
    return server({
        url: '/friend/set_top',
        method: "POST",
        data
    }).catch(errHander)
}

/**
 * 查询用户好友
 * @returns 
 */
export const queryFriend = async (params) => {
    return server({
        url: '/friend/query',
        method: "GET",
        params
    })
}

/**
 * 查询用户好友
 * @returns 
 */
export const queryFriendByUserFriendId = async (params) => {
    return server({
        url: '/friend/query_by_user_friend_id',
        method: "GET",
        params
    })
}