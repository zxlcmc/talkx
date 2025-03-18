class Bus {
    constructor() {
        this.listeners = {}
    }
    // 添加监听
    $on(eventName, handle) {
        if (!this.listeners[eventName]) return;
        this.listeners[eventName].delete(handle)
    }
    // 取消监听
    $off(eventName, handle) {
        if (!this.listeners[eventName]) return;
        this.listeners[eventName].delete(handle)
    }
    // 触发事件
    $emit(eventName, ...agrs) {
        if (!this.listeners[eventName]) return;
        for (const event of this.listeners[eventName]) {
            event(...agrs)
        }
    }
}

export default new Bus()