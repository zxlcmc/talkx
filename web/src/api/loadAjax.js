


/**
 * ajax封装
 * @paraams options 参数对象
 *    method 请求参数 默认get
 *    url 请求路径
 *    asyncs 是否异步
 *    params 参数
 *    dataType 参数格式 urlencoded json
 *      
*/
export default function ajax(options) {

    // 默认配置
    let defaultObj = {
        method: "GET",
        asyncs: true,
        headers: {},
        params: {},
        dataType: 'json'
    }

    // 配置对象 
    Object.assign(defaultObj, options)
    
    return new Promise((resolve, reject) => {
        // 处理method小写 method.toLocaleUpperCase()
        let { dataType, method, url, asyncs, params, headers, methods = method.toLocaleUpperCase() } = defaultObj

        // 处理query参数
        let queryString = ''
        if (methods == 'GET' || dataType == 'urlencoded') {
            Object.keys(params).forEach(key => {
                queryString += `${key}=${params[key]}&`
            })
            if (queryString) {
                queryString = queryString.substring(0, queryString.length - 1)   // 去除最后的&              
                url += '?' + queryString // 拼接url
            }
        }

        // 1.创建xhr 对象
        let xhr = window.XMLHttpRequest ? new XMLHttpRequest() : new ActiveXObject('Microsoft.XMLHTTP')

     
        // 2.请求初始化
        xhr.open(methods, url, asyncs)

        Object.entries(headers).forEach(([key, value])=>{
            xhr.setRequestHeader(key, value)
        })


        // 3 请求监听  可以写在发送请求后面（在异步请求的情况下）
        xhr.onreadystatechange = function () {

            // 如果请求没有完成，直接结束
            if (xhr.readyState !== 4) return

            // 如果响应状态在[200,300)之间代表成功，否则失败
            const { status, response, statusText } = xhr
            if (status >= 200 && status <= 299) {

                // 获取响应数据类型  要在请求已接收xhr.readyState=2 才能获取 
                // application/json;charset=utf-8 application/html;charset=utf-8 ....
                let cType = xhr.getResponseHeader('Content-type')

                // 响应数据
                let data = response
                if (cType.includes('application/json')) { // json格式处理
                    data = JSON.parse(data)
                } 
                // data数据 status状态码 statusText状态文本信息
                resolve(data)
            } else {
                reject(new Error("xhr error status is " + status))
            }
        }

        // 或者
        // xhr.onload = function(){
        //     // 如果响应状态在[200,300)之间代表成功，否则失败
        //     const {status,response,statusText} = xhr
        //     if(status >= 200 && status <= 299){
        //         逻辑处理
        //     }else{
        //         reject(new Error("xhr error status is "+status))
        //     }
        // }

        xhr.onerror = function () {
            reject(new Error("网络中断，无法发送Ajax请求"))
        }

        // 4 发送请求
        if (methods === 'GET' || methods === 'DELETE') {
            xhr.send()
        } else if (methods === 'POST' || methods === 'PUT') {
            let contentType = 'json'
            let data = JSON.stringify(params)
            // 设置数据请求的数据格式
            if (dataType === 'urlencoded') {
                contentType = 'x-www-from-urlencoded'
                data = queryString
            }
            xhr.setRequestHeader('Content-Type', `application/${contentType};charset=utf-8`) //告诉服务器请求体的格式是
            xhr.send(data)// 发送json格式请求体参数
        }
    })
} 

