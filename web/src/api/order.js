import server from './require'


/**
 * 创建订单
 * @returns 
 */
export const createOrder = async (data) => {
    return server({
        url: '/order/create',
        method: "POST",
        data
    })
}
 

/**
 * 支付订单
 * @returns 
 */
export const payOrder = async (data) => {
    const codeObj = {
        // 400: '支付失败', 
        500: '服务器错误',
    }
    return server({
        url: '/order/prepay',
        method: "POST",
        data
    }).catch(res => {
        const msg = codeObj[res.response.status] || res.data
        return { err: true, msg }
    })
}

/**
 * 查询订单
 * @returns 
 */
export const queryOrder = async (params) => {
    const codeObj = {
        400: '请求参数有误',
        404: '订单不存在',
        500: '服务器错误',
    }
    return server({
        url: '/order/query',
        method: "GET",
        params
    }).catch(res => {
        const msg = codeObj[res.response.status] || res.message
        return { err: true, msg }
    })
}