import server from './require'


/**
 * 获取最新的置顶消息  
 * @returns 
 */
export const pinned = async (params) => {
    return server({
        url: '/notification/select_pinned_one',
        method: "GET",
        params
    }) 
}
/**
 * 读取消息
 * {notificationId:1}
 * @returns 
 */
export const reading = async (data) => {
    return server({
        url: '/notification/reading',
        method: "POST",
        data
    }) 
}
/**
 * 获取最新的置顶消息
 * @returns 
 */
export const notifyList = async (params) => {
    return server({
        url: '/notification/list',
        method: "GET",
        params
    }) 
}
