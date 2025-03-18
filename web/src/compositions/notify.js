import { reactive, watchEffect } from "vue";
import { reading, pinned } from "@/api/notify";
import { useGlobalStore } from "@/store";


export const modelObj = reactive({ show: false, msg: {} });

export const pinnedNotify = async () => {
    const useGlobal = useGlobalStore()
    watchEffect(() => {
        const notification = useGlobal.global.notification 
        if (notification && notification.read == '0') { 
            modelObj.show = true;
            modelObj.msg = notification;
        }
    }) 
};

export const closeMessageModel = async (msg) => {
    const useGlobal = useGlobalStore()
    useGlobal.readNotify()
    await reading({ notificationId: msg.id });
};