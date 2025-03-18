import server, { errHander } from './require'



/**
 * 我的表结构
 * @returns 
 */
export const schemaList = async (params) => {
    return server({
        url: '/table_schema/list',
        method: "GET",
        params
    })
}


/**
 * 删除表结构
 * @returns 
 */
export const delSchema = async (data) => {
    return server({
        url: '/table_schema/delete',
        method: "POST",
        data
    }).catch(errHander)
}


/**
 * 新建和修改表结构
 * @returns 
 */
export const saveSchema = async (data) => {
    return server({
        url: '/table_schema/save_or_update',
        method: "POST",
        data
    }).catch(errHander)
}