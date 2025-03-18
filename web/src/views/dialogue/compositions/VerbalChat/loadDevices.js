import { AudioContext, OfflineAudioContext } from 'standardized-audio-context';


/**
 * 是否支持录音
 */
const recordingSupport = () => {
    let scope = navigator.mediaDevices || {};
    if (!scope.getUserMedia) {
        scope = navigator
        scope.getUserMedia || (scope.getUserMedia = scope.webkitGetUserMedia || scope.mozGetUserMedia || scope.msGetUserMedia);
    }

    if (!scope.getUserMedia) {
        return false
    }
    return scope
}

let streams = []
let audioCtxs = []
let mediaPermissions = []


// 获取麦克风权限
export const getUserMediaPermission = () => {
    const mediaPermission = new Promise((resolve, reject) => {
        const mediaDevices = recordingSupport()
        if (mediaDevices.getUserMedia) {
            let constraints = { audio: true }
            mediaDevices.getUserMedia(constraints).then(resolve, reject);
        } else { reject({ err: true, _msg: '不支持的浏览器' }) }
    })
    mediaPermissions.push(mediaPermission)
    return mediaPermission
}

export function checkMime() {
    var types = [
        "audio/mpeg",
        "audio/webm",
        "audio/mp4",
        "audio/wav",
        "audio/ogg",
        "audio/flac",
        "audio/m4a",
        "audio/mp3",
        "audio/mpga",
        "audio/oga",
    ];
    let first;
    for (var i in types) {
        // 判断当前浏览器支持哪种
        let supported = MediaRecorder.isTypeSupported(types[i]);
        if (supported && !first) {
            console.log("Is " + types[i] + " supported? " + (supported ? "Yes!" : "Nope :("));
            first = types[i];
        }
    }

    return first;
}

// navigator.mediaDevices.enumerateDevices() // 获取设备信息

/**
 * 释放资源
 */
export const devicesDispose = () => {
    streams.forEach(({ stream, mediaRecorder, destroy }) => {
        stream.getTracks().forEach(track => track.stop());
        destroy()
    })

    audioCtxs.forEach((audioCtx) => audioCtx.close())

    // mediaPermissions.forEach((mp) => { })
    mediaPermissions.length = 0; // 去除引用
}

/**
 * 判断是否有麦克风授权 兼容性不行 不使用
 * @returns 
 */
export const checkMicrophonePermission = async () => {
    if (navigator.permissions) {
        return navigator.permissions.query({ name: "microphone" }).then((result) => {
            console.log('granted', result.state);
            if (result.state === "granted") {
                return true
            } else if (result.state === "prompt") {
                return false
            }
            return false
        })
    } else {
        // 浏览器不支持处理
    }
}

export const getAudioContext = window.AudioContext ||
    window.webkitAudioContext ||
    window.mozAudioContext ||
    window.msAudioContext;


export default function loadDevices(options = {}) {
    const { MediaPermission = getUserMediaPermission(), readover = () => { }, change = () => { }, stop = () => { }, error = () => { } } = options
    let analyser;
    let mediaRecorder;
    let dataArray;
    let audioChunks = [];

    try {
        let stopdraw = false;
        const draw = () => {
            if (stopdraw) return
            requestAnimationFrame(draw);
            analyser.getByteTimeDomainData(dataArray);
            change(dataArray);
        };

        const stopDraw = () => stopdraw = true

        let mimeType = checkMime();

        MediaPermission.then((stream) => {

            console.log('---MediaPermission--');
            // 获取音频数据
            const audioContext = new AudioContext();
            const source = audioContext.createMediaStreamSource(stream);

            audioCtxs.push(audioContext)

            // 释放音频流
            const releaseAudioStream = () => stream.getAudioTracks().forEach((track) => track.stop()); // 停止录音

            // 创建记录器
            mediaRecorder = new MediaRecorder(stream, { mimeType });

            const destroy = () => {
                stopDraw()
                releaseAudioStream()
                audioContext.close()
            }
            streams.push({ stream, mediaRecorder, destroy })

            // 音频数据发生变化时收集音频片段，用于合成音频文件
            mediaRecorder.addEventListener("dataavailable", (event) => {
                console.log("mediaRecorder-dataavailable:", event);
                audioChunks.push(event.data);
            });

            // 监听音频开始录制
            mediaRecorder.addEventListener('start', () => {
                console.timeEnd('调用开始录制')
                console.log("mediaRecorder-start:");
                audioChunks = []
            })

            // 音频录制结束回调
            mediaRecorder.addEventListener("stop", () => {
                const audioBlob = new Blob(audioChunks, { type: "audio/mp4" }); // wav webm mp4  

                stop(audioBlob);

                // 销毁录音占用 
                destroy()

                // 清空 chunks 以便下一次录音 
                audioChunks = []
            });


            // 通过AnalyserNode对象的getByteTimeDomainData方法来获取音频数据的波形形式：
            // 获取音频时间和频率数据
            analyser = audioContext.createAnalyser();

            // 定义长度
            analyser.fftSize = 2048; // 可以调整这个值来改变细节
            const bufferLength = analyser.frequencyBinCount;
            dataArray = new Uint8Array(bufferLength);

            // 合并流数据
            source.connect(analyser);

            audioContext.suspend()
            readover({
                start: () => {
                    draw()
                    audioContext.resume()
                    console.time('调用开始录制')
                    mediaRecorder.start()
                },
                stop: () => {
                    audioContext.suspend()
                    mediaRecorder.stop()
                },
                destroy
            })
        }).catch((err) => {
            console.log("stream-errr", err);
            if (err == 'Requested device not found') {
                return error({ _msg: '未找到录音设备' })
            }
            error(err)
        });
    } catch (err) {
        console.log("mediaDevices-errr", err);
        if (err.message.includes('MediaRecorder')) {
            err._msg = '不支持的浏览器'
        }
        error(err)
    }

    return
}

