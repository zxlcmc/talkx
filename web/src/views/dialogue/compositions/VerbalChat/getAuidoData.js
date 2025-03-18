
// import { getAudioContext } from "./loadDevices";
import { AudioContext } from 'standardized-audio-context';

const clearArr = []

export const stopAudio = () => { clearArr.forEach(f => f()) }

/**
 * 获取音频文件频谱数据 
 */
export function getAuidoData() {
    let analyser, dataArray;
    let loadOver = false;
    var audioCtx = new AudioContext({
        latencyHint: "playback"
    }); // 创建和播放需要间隔一端时间
    analyser = audioCtx.createAnalyser();
    analyser.fftSize = 2048;

    const destroy = () => { audioCtx.close() }

    let stop = false;
    const cbs = []
    const run = () => {
        if (stop) return;
        requestAnimationFrame(() => {
            var bufferLength = analyser.frequencyBinCount;
            dataArray = new Uint8Array(bufferLength);
            analyser.getByteFrequencyData(dataArray);
            cbs.forEach(f => f(dataArray))
            run()
        });
    };

    const load = (url, data, onended = () => { }) => { 
        let audioBuffer;

        // 通过fetch 请求音频获取 arrayBuffer 类型数据
        const getData = () => {
            // 方法一 使用fetch请求
            return fetch(url)
                .then((res) => res.arrayBuffer())
                .then((arrayBuffer) => {
                    return new Promise((resolve, reject) => {
                        // 兼容低版本的iphone中 audioCtx.decodeAudioData 返回的不是一个promise,必须通过回调函数
                        audioCtx.decodeAudioData(arrayBuffer, resolve, err => {
                            console.log('decodeAudioData-er', err);
                            reject(err)
                        })
                    })
                })
                .then((buffer) => (audioBuffer = buffer));

            // 方法二 把接口返回blob转 arrayBuffer 
            // ios ajax返回的二进制数据没有 arrayBuffer 方法
            // return data
            //     .arrayBuffer()
            //     .then((arrayBuffer) => audioCtx.decodeAudioData(arrayBuffer))
            //     .then((buffer) => (audioBuffer = buffer));
        };
        getData().then(function () {
            // 创建音频源
            const source = audioCtx.createBufferSource();
            source.buffer = audioBuffer;
            source.connect(analyser);
            analyser.connect(audioCtx.destination);

            // 创建音量节点
            // const gainNode = audioCtx.createGain();
            // gainNode.gain.value = 1 // 设置声音大小

            console.log('source.start'); 
            source.start(); // 开始播放
            run();
            source.onended = () => {
                console.log('source.onended');
                stop = true
                onended()
            }; // 播放完成
            loadOver = true;

            clearArr.push(() => { source.stop() })
        }).catch(err => {
            console.log('getdata-err', err);
        });
    };

    const change = (cb = () => { }) => cbs.push(cb)

    return { load, change, destroy };
}


