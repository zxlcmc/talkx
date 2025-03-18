import { isWeb } from '@/common/env'
import { useCommunication } from "@/hooks/useCommunication";

window.__talkx__ = {};


/** 打开外部连接方法 */
const openUrl = (url) => {
    const { send } = useCommunication(); 
    isWeb() ? window.open(url) : send("open-external-web-page", url);
};
window.__talkx__ = { openUrl };