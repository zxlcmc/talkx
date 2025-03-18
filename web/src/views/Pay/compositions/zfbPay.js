



const redey = (callback = () => { }) => {
    function ready(callback) {
        if (window.AlipayJSBridge) {
            callback && callback();
        } else {
            document.addEventListener('AlipayJSBridgeReady', callback, false);
        }
    }
    ready(function () {
        alert('bridge ready');
        callback()
    });

}

export default {
    redey
}