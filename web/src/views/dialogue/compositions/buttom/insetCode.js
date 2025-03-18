import { reactive } from 'vue'

/**
 * 插入代码区域逻辑
 * @returns 
 */
export default function insetCode() {
    const inset = reactive({ show: false, text: "", cbs: [] });

    const insetClose = () => {
        inset.cbs.forEach((c) => c());
        inset.text = "";
        inset.cbs = [];
    };
    const showInsetArea = (text, cb = () => { }) => {
        inset.show = true;
        inset.text = text;
        inset.cbs.push(cb);
    };
    const hideInsetArea = () => {
        inset.show = false;
        inset.text = "";
        inset.cbs = [];
    };

    return { inset, hideInsetArea, showInsetArea, insetClose }
}