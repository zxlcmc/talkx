
import server from './require'


/**
 * 收藏消息  
 * @returns 
 */
export const favoritesMsg = async (data) => {
    return server({
        url: '/favorites/save_msg',
        method: "POST",
        data
    })
}

/**
 * 收藏消息  
 * @returns 
 */
export const favoritesList = async (params) => {
    return server({
        url: '/favorites/list',
        method: "GET",
        params
    })
}

/**
 * 删除消息  
 * @returns 
 */
export const delFavorites = async (data) => {
    return server({
        url: '/favorites/delete_msg',
        method: "POST",
        data
    })
}