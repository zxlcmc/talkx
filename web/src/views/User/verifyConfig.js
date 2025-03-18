
export const verConfig = {
    name: {
        placeholder: "请输入名称", verity: [
            { type: 'empty', message: "请输入名称" },
            { type: 'length', max: 10, min: 2, message: '字符长度不能超过10位或者小于2位' }]
    },
    email: {
        placeholder: "请输入邮箱", verity: [
            { type: 'empty', message: "请输入邮箱" },
            { type: 'emailRule', message: "请输入正确的邮箱" }]
    },
    ApiKey: { placeholder: "请输入ApiKey", verity: [] }, //{ type: 'empty', message: "请输入ApiKey" },
    proxy_base_url: { placeholder: "请输入代理地址", verity: [] },  
    phone: {
        placeholder: "请输入手机号",
        verity: [
            { type: 'empty', message: "请输入手机号" },
            { type: 'phoneRule', message: '请输入正确的手机号' }]
    },
    code: {
        placeholder: "", verity: [{ type: 'empty', message: "请输入验证码" }]
    },
}
