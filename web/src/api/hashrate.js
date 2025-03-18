import server from './require'


/**
 * 邀请新用户注册可获得奖励蒜粒数量。
 * @returns 
 */
export const getinviteReward = async () => {
    return server({
        url: '/user/get_invite_reward',
        method: "GET",
        params: {}
    })
}

/**
 * 获取蒜粒目录。
 * @returns 
 */
export const coinCatalog = async () => {
    const config = {
        url: '/user/coin_catalog',
        method: "GET",
        params: {}
    }
    return server(config)
    
    // .catch(err => {
    //     if (err.response.status == 0) {
    //         return server(config)
    //     } else {
    //         return Promise.reject(err)
    //     }
    // })
}

/**
 * 查询蒜粒记录
 * @returns 
 */
export const searchCoin = async (params) => {
    return server({
        url: '/coin_bill/search',
        method: "GET",
        params
    })
}
