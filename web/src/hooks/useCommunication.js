import { useChatStore } from '@/store'
import { PRODUCT_TYPE } from '@/common/config'
import { v4 as uuidv4 } from 'uuid'

export const separate_symbol = '-^-'

const assemble = (startText, endText, data) => {
    const { range: { start, end }, fileName, selectedText } = data
    const codeType = fileName.split('.')[1]
    const extra = { fileName, } // line: start.line + '-' + end.line
    const extraStr = ' ' + Object.entries(extra).map(([key, value]) => `${key}:${value}`).join(separate_symbol)
    return startText
        + '\n```' + codeType + extraStr + '\n'
        + selectedText
        + '\n```\n'
        + endText
}

// 功能区
const featureConfig = [
    {
        key: 'explain-this-code', text: "解释代码", icon: "icon-question",
        format: (data) => {
            const startText = '解释代码：', endText = '';
            return assemble(startText, endText, data)
        }
    },
    {
        key: 'language-translation', text: "中英翻译", icon: "icon-tubiao-fanyi", line: true,
        format: (data) => {
            const startText = '', endText = '';
            return data.selectedText//assemble(startText, endText, data)
        }
    },
    {
        key: 'check-for-performance-issues', text: "性能检查", icon: "icon-xingnengtongji",
        format: (data) => {
            const startText = '检查代码的性能：', endText = '';
            return assemble(startText, endText, data)
        }
    },
    {
        key: 'check-for-security-issues', text: "安全检查", icon: "icon-anquanbaozhang",
        format: (data) => {
            const startText = '检查代码的安全性：', endText = '';
            return assemble(startText, endText, data)
        }
    },
    {
        key: 'check-for-style-issues', text: "样式检查", icon: "icon-ic_style", hide: true,
        format: (data) => {
            const startText = '检查代码的样式及规范：', endText = '';
            return assemble(startText, endText, data)
        }
    },
    {
        key: 'optimize-this-code', text: "优化并改进", icon: "icon-dkw_tisheng", line: true,
        format: (data) => {
            const startText = '优化代码，提高代码的可读性、安全和性能等：', endText = '';
            return assemble(startText, endText, data)
        }
    },
    {
        key: 'improve-readability', text: "提高可读性", icon: "icon-daima", hide: true,
        format: (data) => {
            const startText = '提高代码的可读性：', endText = '';
            return assemble(startText, endText, data)
        }
    },
    {
        key: 'clean-code', text: "清理代码", icon: "icon-clean", hide: true,
        format: (data) => {
            const startText = '清理无效的代码：', endText = '';
            return assemble(startText, endText, data)
        }
    },
    {
        key: 'generate-comment-for-this-code', text: "生成注释", icon: "icon-zhibiaozhushibiaozhu2",
        format: (data) => {
            const startText = '根据提供的代码生成注释：', endText = '';
            return assemble(startText, endText, data)
        }
    },
    {
        key: 'generate-unit-test', text: "生成测试用例", icon: "icon-ceshi",
        format: (data) => {
            const startText = '为提供的代码生成单元测试：', endText = '';
            return assemble(startText, endText, data)
        }
    },
    {
        key: 'generate-echarts', text: "生成图表", icon: "icon-tubiao", line: true,
        format: (data) => {
            const startText = '', endText = '';
            return assemble(startText, endText, data)
        }
    },
    {
        key: 'insert-code-to-chat', text: "插入代码到聊天框", icon: "icon-code1",
        format: (data) => {
            const startText = '', endText = '';
            return { assemble: assemble(startText, endText, data), text: data.selectedText }
        }
    },
    {
        key: 'show-message-dialog', text: "接收编辑器信息", icon: "icon-code1", hide: true,
        format: (data) => data
    },
]

// 可发送消息类型
const sendConfig = [
    {
        key: 'execute-code-template', text: "代码模板",
        paramsHander: executeTemplateKey => {
            return { "key": "execute-code-template", "data": { executeTemplateKey } }
        },
    },
    {
        key: 'open-external-web-page', text: "打开外部浏览器的页面",
        paramsHander: url => {
            return { "key": "open-external-web-page", "data": { url } }
        },
    },
    {
        key: 'insert-code', text: "插入代码",
        paramsHander: code => {
            return { "key": "insert-code", "data": { code } }
        },
    },
    {
        key: 'open-ai-response-in-diff-view', text: "比较",
        paramsHander: ({ editer, code }) => {
            console.log('_editer__paramsHander', { editer, code });
            try {
                const { fileName, filePath, selectedText, range } = editer
                const { start, end } = range
                const params = {
                    "key": "open-ai-response-in-diff-view",
                    "data": {
                        fileName,
                        filePath,
                        selectedText,
                        newText: code,
                        uniqueDiffId: uuidv4(),
                        selectionCordinates: {
                            "start": {
                                "line": Number(start.line) + 1,
                                "column": start.column
                            },
                            "end": {
                                "line": Number(end.line) + 1,
                                "column": start.column
                            }
                        }
                    }
                }
                console.log('_比较_params__', params);
                return params
            } catch (err) {
                console.error('open-ai-response-in-diff-view err', err)
            }
        }
    },
    {
        key: 'query-editor-info', text: "获取编辑器信息",
        paramsHander: () => ({ "key": "query-editor-info", "data": {} })
    },
    {
        key: 'notification', text: "触发IDE的消息通知",
        paramsHander: (data) => {
            // {"title": "通知标题","subTitle": "副标题","content": "消息内容",
            //     "type": "information" information、warning、error
            // }
            return {
                "key": "notification", data
            }
        }
    },
    {
        key: 'update_is_available', text: "可以更新",
        paramsHander: (data) => {
            // {  "lastVersion": "0.0.2",  "installVersion": "0.0.1"  }
            return { "key": "update_is_available", data }
        }
    },
    {
        key: 'update_is_required', text: "强制更新",
        paramsHander: (data) => {
            // {  "lastVersion": "0.0.2",  "installVersion": "0.0.1"  }
            return { "key": "update_is_required", data }
        }
    },
    {
        key: 'talkx-reload', text: "刷新插件",
        paramsHander: (data = {}) => {
            return { "key": "reload", data }
        }
    },
    {
        key: 'write-to-file', text: "写入文件",
        paramsHander: (data = {}) => { //{ fileName append content}
            return { "key": "write-to-file", data: Object.assign({ append: false }, data) }
        }
    },
    {
        key: 'read-from-file', text: "读取文件",
        paramsHander: (data = {}) => { // fileName 
            return { "key": "read-from-file", data }
        }
    }
]


let load = false
const hbucbs = []
const hbuilderMessage = (e) => {
    if (load) return
    try {
        hbuilderx.onDidReceiveMessage((e) => {
            let { key, data } = e
            if (key) { console.log('--hbuilder-message-key--', e); }
            const keys = featureConfig.map(f => f.key)
            if (!keys.includes(key)) return
            data = JSON.parse(data)
            hbucbs.forEach(c => {
                const config = featureConfig.find(f => f.key === key)
                const prompt = config.format(data)
                c({ key, data, prompt, productId: PRODUCT_TYPE.HBuilder })
            })
        });
        load = true
    } catch (err) {
        console.error('hbuilderx-message-err:', err)
    }
}
const hbuilder = () => {
    window.removeEventListener('load', hbuilderMessage)
    window.addEventListener('load', hbuilderMessage)
    hbuilderMessage()
    const receive = (cb = () => { }) => { hbucbs.push(cb) };

    const send = (type, payload, onSuccess, onFailure) => {
        const config = sendConfig.find(s => s.key === type)
        if (!config) return
        const request = config.paramsHander(payload)
        console.log('hbuilder', type, request);
        try {
            hbuilderx.postMessage(JSON.stringify(request));
        } catch (err) {
            console.error('hbuilderx-send-err:', err)
        }
    }

    const clearReceive = () => { hbucbs.length = 0 }
    return { send, receive, clearReceive, featureConfig, productId: PRODUCT_TYPE.HBuilder }
}


const VScbs = []
// window._vscodeApi = null
const getVscodeApi = () => {
    if (window._vscodeApi && !window._vscodeApi.__temp) return window._vscodeApi
    try {
        if (acquireVsCodeApi) {
            window._vscodeApi = acquireVsCodeApi()
        } else {
            window._vscodeApi = { postMessage: () => { }, __temp: true }
        }
    } catch (err) {
        console.log('getVscodeApi-error', err);
        window._vscodeApi = { postMessage: () => { }, __temp: true }
    } finally {
        return window._vscodeApi
    }
}
const vscodeMessage = (e) => {
    let { key, data } = e.data
    if (key) {
        console.log('--vsocde-message-key--', e.data);
    }
    const keys = featureConfig.map(f => f.key)
    if (!keys.includes(key)) return
    data = JSON.parse(data)
    VScbs.forEach(c => {
        const config = featureConfig.find(f => f.key === key)
        const prompt = config.format(data)
        c({ key, data, prompt, productId: PRODUCT_TYPE.IntelliJ_Idea })
    })
}
const vscode = () => {
    getVscodeApi()
    window.removeEventListener('message', vscodeMessage)
    window.addEventListener('message', vscodeMessage)

    const receive = (cb = () => { }) => { VScbs.push(cb) }

    const send = (type, payload, onSuccess, onFailure) => {
        const config = sendConfig.find(s => s.key === type)
        if (!config) return
        const request = config.paramsHander(payload)
        console.log('vscode', window._vscodeApi, type, request);
        window._vscodeApi.postMessage(JSON.stringify({
            request,
            onSuccess: function (args) {
                console.log('--onSuccess-vscode');
                onSuccess(args)
            },
            onFailure: function (code, ex) {
                onFailure(arguments)
            }
        }));
    }

    const clearReceive = () => { VScbs.length = 0 }

    return { send, receive, clearReceive, featureConfig, productId: PRODUCT_TYPE.Vs_Code}
}

const IDEAcbs = []
const IEDMessage = (e) => {
    let { key, data } = e.data
    if (key) { console.log('--ide-message-key--', e.data); }
    const keys = featureConfig.map(f => f.key)
    if (!keys.includes(key)) return
    data = JSON.parse(data)
    IDEAcbs.forEach(c => {
        const config = featureConfig.find(f => f.key === key)
        const prompt = config.format(data)
        c({ key, data, prompt, productId: PRODUCT_TYPE.IntelliJ_Idea })
    })
}
const IDEA = () => {
    const send = (type, payload, onSuccess = () => { }, onFailure = () => { }) => {
        const config = sendConfig.find(s => s.key === type)
        if (!config) return
        const request = config.paramsHander(payload)
        console.log('request', type, request);
        try {
            sendDataToJava && sendDataToJava({
                request: JSON.stringify(request),
                onSuccess: function (args) {
                    // console.log('onSuccess', args); // string
                    onSuccess(args)
                },
                onFailure: function (code, message) {
                    // console.log('onFailure', code, message);
                    onFailure(code, message)
                }
            });
        } catch (err) { }
    }

    window.removeEventListener('message', IEDMessage)
    window.addEventListener('message', IEDMessage)

    // window.sendX = function () {
    //     let { key, data } = {
    //         "key": "optimize-this-code", //"explain-this-code",
    //         "data": {
    //             "range": {
    //                 "start": {
    //                     "line": 70,
    //                     "column": 0,
    //                     "text": ""
    //                 },
    //                 "end": {
    //                     "line": 74,
    //                     "column": 45,
    //                     "text": "        openApiRequest.setMessages(messages);"
    //                 }
    //             },
    //             "selectedText": "        OpenApiRequest openApiRequest = prompt.getRequest();\n        List<Message> messages;\n        messages = addSystemPromptMessage2FirstIndexIfExists(prompt, chatRequest);\n        messages = truncateMessageList(messages, prompt.getMessageContextSize());\n        openApiRequest.setMessages(messages);",
    //             "userSelectedText": true,
    //             "cloudFile": "n",
    //             "isDiffView": "n",
    //             "fileName": "ChatGptChatServiceImpl.java",
    //             "filePath": "/Users/huxiao/Documents/github/talkx/src/main/java/org/bigmouth/gpt/openai/service/ChatGptChatServiceImpl.java",
    //             "isGitRepository": false,
    //             "initialGitFileStatus": "local"
    //         }
    //     }

    //     // 模拟生成图表
    //     // let { key, data } = { "key": "generate-echarts", "data": JSON.parse("{\"range\":{\"start\":{\"line\":0,\"column\":0,\"text\":\"\"},\"end\":{\"line\":12,\"column\":15,\"text\":\"2022年12月\\t549291\"}},\"selectedText\":\"月份\\t支出金额\\n2022年1月\\t548582\\n2022年2月\\t484939\\n2022年3月\\t769054\\n2022年4月\\t643352\\n2022年5月\\t669089\\n2022年6月\\t749881\\n2022年7月\\t624908\\n2022年8月\\t950257\\n2022年9月\\t740968\\n2022年10月\\t530375\\n2022年11月\\t582426\\n2022年12月\\t549291\",\"userSelectedText\":true,\"cloudFile\":\"n\",\"isDiffView\":\"n\",\"fileName\":\"data.cvs\",\"filePath\":\"/Users/huxiao/Documents/github/gpt-exp/src/main/resources/data.cvs\",\"isGitRepository\":false,\"initialGitFileStatus\":\"local\",\"location\":\"talkx\"}") }
    //     // console.log('iedcbs', IDEAcbs);
    //     IDEAcbs.forEach(c => {
    //         const config = featureConfig.find(f => f.key === key)
    //         const prompt = config.format(data)
    //         c({ key, data, prompt, productId: PRODUCT_TYPE.IntelliJ_Idea })
    //     })
    // }

    const receive = (cb = () => { }) => {
        IDEAcbs.push(cb)
    }

    const clearReceive = () => {
        IDEAcbs.length = 0
    }

    return { send, receive, featureConfig, clearReceive, productId: PRODUCT_TYPE.IntelliJ_Idea }
}

/**
 * 通信
 */
export function useCommunication() {
    const useChat = useChatStore() 
    switch (useChat.productId) {
        case PRODUCT_TYPE.PyCharm:
        case PRODUCT_TYPE.IntelliJ_Idea:
        case PRODUCT_TYPE.Android_Studio:
        case PRODUCT_TYPE.WebStorm:
        case PRODUCT_TYPE.GoLand:
        case PRODUCT_TYPE.MindStudio:
        case PRODUCT_TYPE.Rider:
        case PRODUCT_TYPE.PhpStorm:
        case PRODUCT_TYPE.Fleet:
            return IDEA()
        case PRODUCT_TYPE.Vs_Code:
            return vscode()
        case PRODUCT_TYPE.HBuilder:
            return hbuilder()
        default:
            return { mismatching: true, send: () => { }, receive: () => { }, clearReceive: () => { }, featureConfig: [] }
    }
}