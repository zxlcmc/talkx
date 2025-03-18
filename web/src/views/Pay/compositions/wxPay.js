import { wxPayErrMsgMap } from './initData'

const invoke = async (res) => {
    return new Promise((resolve, reject) => {
        try {
            console.log(';唤起支付',res, WeixinJSBridge);
            WeixinJSBridge.invoke(
                "getBrandWCPayRequest",
                {
                    appId: res.appId, // 公众号名称，由商户传入
                    timeStamp: res.timeStamp, // 时间戳，自1970年以来的秒数
                    nonceStr: res.nonceStr, // 随机串
                    package: res.package,
                    signType: "MD5", // 微信签名方式：
                    paySign: res.paySign, // 微信签名
                },
                (res) => {
                    console.log(';res', res);
                    const rcode = wxPayErrMsgMap[res.err_msg];
                    // const rcode = wxPayErrMsgMap['get_brand_wcpay_request:ok'];
                    if (rcode) {
                        resolve(rcode)
                    } else {
                        console.error("wx - invoke - result:", res);
                    }
                }
            );
        } catch (e) {
            console.log('wx - invoke - err:', err);
        }
    })
}


const getUrl = () => { 
    const urlObj = new URL(window.location.href)
    urlObj.host = import.meta.env.VITE_WX_PAY_HOST
    const redirect_uri = encodeURIComponent(urlObj.toString()) 
    return redirect_uri
}

const redirectUrl = () => {
    const redirect_uri = getUrl()
    const url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx6ef283c4c49c2ea3&redirect_uri=" + redirect_uri + "&response_type=code&scope=snsapi_base&wechat_redirect=" + redirect_uri + "#wechat_redirect";
    window.location.href = url
}

export default {
    invoke,
    getUrl,
    redirectUrl
}