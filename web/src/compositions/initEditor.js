import { useCommunication } from "@/hooks/useCommunication";
import { useChatStore, useLoginStore, useGlobalStore } from "@/store";
import { ideInitStorage } from '@/store/plugins/ideStorage';
let useChat;
let useLogin;

let defaultEditerInfo = {
    productName: "IntelliJ IDEA 2022.1.4",
    talkxVersion: "",
    keepAliveTime: undefined,
    fontSize: 13,
    fontFamily: ".SFNSText",
    theme: "",
    // pageData: {
    // //     login: JSON.stringify({"loginStatus":true,"token":"20dcda7ddc0b4b57bc749a5ae7549359","phone":"13723829301","user":{"id":2,"email":"3460797110@qq.com","phoneNum":"13723829301","name":"豆腐干大锅","model":"gpt-3.5-turbo","modelIcon":"https://plugin-web.talkx.cn/images/model/gpt3_5.png","apiKey":"","avatar":"https://plugin-web.talkx.cn/images/2023/07/26/de790d0b4a0a4530a5e625c8c91c346e.png","inviteCode":"E7UTmp","inviteUrl":"http://yy.hangzhouyiyao.cn/talkx-plugin-web/index.html#/?inviteCode=E7UTmp","coins":900},"theme":"dark","sending":false})
    //     login: JSON.stringify({lll:3,"loginStatus":true,"token":"f3a4866a0689449e81fc5e72a152fba9","phone":"13723829301","user":{"id":2,"email":"3460797110@qq.com","phoneNum":"13723829301","name":"豆腐干大锅","model":"gpt-3.5-turbo","modelIcon":"https://plugin-web.talkx.cn/images/model/gpt3_5.png","apiKey":"","avatar":"https://plugin-web.talkx.cn/images/2023/07/26/de790d0b4a0a4530a5e625c8c91c346e.png","inviteCode":"E7UTmp","inviteUrl":"http://yy.hangzhouyiyao.cn/talkx-plugin-web/index.html#/?inviteCode=E7UTmp","coins":900},"theme":"dark"})
    // }
};

const setStyle = (styles, { noData } = {}) => {
    const { fontSize, fontFamily, theme = 'dark' } = styles;
    const style = document.getElementById("root_style") || document.createElement("style");
    style.setAttribute("id", "root_style");
    const styleContent = `
      :root{
        --tp_size: ${fontSize}px;
        font-family: ${fontFamily};
      }
    `;
    style.innerHTML = styleContent;
    document.head.appendChild(style);
    const themeObj = {
        dark: ["Dark", "dark"],
        light: ["light", "Light"],
    };
    const text = theme.toLocaleLowerCase();
    let themeStr = "";
    Object.entries(themeObj).forEach(([key, values]) => {
        if (values.includes(text)) {
            themeStr = key;
        }
    });
    useLogin.setTheme(themeStr);
    !noData && useLogin.getUser(true);
};

export const initEditer = async ({ route, toast, router, bus, onUnmountedFun = () => { } }) => {
    useChat = useChatStore();
    useLogin = useLoginStore();
    const { showToast } = toast
    const useGlobal = useGlobalStore();
    const { receive, send, clearReceive, mismatching } = useCommunication();
    const next = (options) => {
        // pc端，没有外来数据

        console.log('useChat', useChat, options);
        setStyle(defaultEditerInfo, options);
        useGlobal.setEditer(defaultEditerInfo)
    }
    const receiveFun = () => {
        receive(async (params = {}) => {
            if (!["dialogue"].includes(route.name)) {
                showToast('auto', '抱歉，不能执行此操作，请进入对话页面后重试。', '', '确认')
                return
            }
            bus.emit('ide_message', params)
        })
    }
    if (window.talkxEditorInfo) {
        Object.assign(defaultEditerInfo, window.talkxEditorInfo);
        await ideInitStorage(window.talkxEditorInfo.pageData)
        receiveFun()
        next()
    } else {
        await ideInitStorage()
        next({ noData: true })
        send("query-editor-info", "", (res) => {
            Object.assign(defaultEditerInfo, JSON.parse(res));
            next()
        });
        if (mismatching) { next() } else {
            receiveFun()
        }
    }

    onUnmountedFun(() => {
        clearReceive()
    })
}