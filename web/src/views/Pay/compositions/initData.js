import { ref } from 'vue'
import { format } from "@/common/utils";

export const list = ref([
    { text: "商品名称", key: "productName", rightText: "-", noicon: true },
    { text: "订单编号", key: "orderId", rightText: "-", noicon: true },
    { text: "下单时间", key: "createTime", rightText: "-", noicon: true },
    { text: "订单金额", key: "totalPrice", rightText: "-", noicon: true },
]);

export const upDataList = (data) => {
    list.value.forEach((o) => {
        let value = data[o.key];
        if (!value) return;
        if (o.key == "createTime") {
            value = format(new Date(value), "yyyy-MM-dd hh:mm:ss");
        } else if (o.key == "totalPrice") {
            value = "￥" + value.toFixed(2);
        }
        o.rightText = value;
    });
}

export const orderStatus = {

}

export const payStatusEnum = {
    unpaid: 0, // 待支付
    progress: 1, // 支付中
    success: 2, // 支付成功
    close: 3, // 已关闭 
    fail: 4, // 支付失败

    cancel: 98, // 支付取消 
    noOrder: 100
}

export const payStatusObj = {
    [payStatusEnum.unpaid]: { color: "", icon: "icon-info-circle", text: "请支付" },
    [payStatusEnum.success]: { color: "#16aa68", icon: "icon-zhifuchenggong", text: "支付成功" },
    [payStatusEnum.fail]: { color: "#f4420a", icon: "icon-zhifushibai", text: "支付失败" },
    [payStatusEnum.progress]: { color: "", icon: "icon-info-circle", text: "正在支付" },
    [payStatusEnum.close]: { color: "", icon: "icon-info-circle", text: "订单已关闭" },

    [payStatusEnum.cancel]: { color: "#f4420a", icon: "icon-zhifushibai", text: "支付取消" }, 
    [payStatusEnum.noOrder]: { color: "", icon: "icon-info-circle", text: "订单不存在" },
}


export const wxPayErrMsgMap = {
    'get_brand_wcpay_request:ok': payStatusEnum.success,
    'get_brand_wcpay_request:cancel': payStatusEnum.progress,
    'get_brand_wcpay_request:fail': payStatusEnum.fail
}