import { textToVoice } from "@/api/voice";

const getUrl = async (text) => {
    // const params = { tts1: { input: text, speed: "1.0" } };
    // const bdata = await textToVoice(params).catch((err) => {
    //     console.log("err", err);
    //     return "";
    // });
    // const blobData = new Blob([bdata], { type: "audio/mpeg" });
    // const url = window.URL.createObjectURL(blobData);
    // return url
    const params = {
        'tts1.model': "tts-1",
        'tts1.input': text,
        'tts1.responseFormat': 'mp3'
    };
    const querString = Object.entries(params).map(([key, value]) => `${key}=${value}`).join("&")
    return `${import.meta.env.VITE_MODE_API_URL}/voice/text_to_speech_stream?${querString}`;

    // return textToVoicestream(params)
}

// const audio = document.createElement("audio");
// audio.setAttribute("controls", true);
// audio.setAttribute("autoplay", true);
// audio.setAttribute("preload", 'preload');
// audio.style.display = "none";
// document.body.appendChild(audio);


class AudioComp {
    constructor() {
        // const audio = new Audio();
        // this.audio = audio
    }

    async play(url) {
        return new Promise((ended, reject) => {
            this.audio = new Audio();
            this.audio.addEventListener('ended', ended)
            this.audio.addEventListener('error', (e) => {
                console.log('audio-err', e);
                reject(e)
            })
            this.audio.src = url// 'https://api-test.talkx.cn/voice/text_to_speech_stream?tts1.model=tts-1-1106&tts1.input=11月21日，外交部发言人毛宁主持例行记者会。有记者提问，据俄新社报道，阿根廷当选总统米莱外事顾问蒙迪诺20日接受采访时，针对是否会支持并鼓励对华贸易提问应询称，将停止与中国政府的合作。中方对此有何回应？“据我了解，你提到的内容与俄新社网站公布的采访内容有出入。”毛宁说。&tts1.responseFormat=mp3';  
            this.audio.play()
            })
    }

    pause() {
        this.audio?.pause()
    }

    close() {
        this.pause()
    }
}
const playMap = new Map()

/**
 * 音频维护播放
 * 维护一个列表，可播放，导出一个状态
 * 
 */
function useAudioList() {

    const audioObj = new AudioComp()

    const cFuns = {}

    const playChange = (id) => {
        cFuns[id].forEach(f => f())
    }

    function stopAll() {
        for (const [key, value] of playMap) {
            stop(key)
        }
    }

    function add(id) {
        console.log('addd:', id, playMap, playMap.has(id));
        if (!playMap.has(id)) {
            playMap.set(id, { play: false, url: "" })
            cFuns[id] = []
        }
    }
    window.playMap = playMap
    async function play(id, text) { 
        add(id)
        stopAll()
        const obj = playMap.get(id)
        obj.play = true
        playChange(id)
        if (!obj.url) {
            const url = await getUrl(text)
            obj.url = url
        }
        await audioObj.play(obj.url)
        obj.play = false
        playChange(id)
    }

    async function stop(id) {
        if (!playMap.has(id)) { return }
        const obj = playMap.get(id)
        audioObj.pause()
        obj.play = false
        playChange(id)
    }

    const getControl = (id, change) => {
        let data = playMap.get(id)
        if (!data) { add(id); data = playMap.get(id) }
        cFuns[id].push(() => change(data))
        return {
            play: async (...args) => await play(id, ...args).catch(e => {
                data.play = false
                playChange(id)
                throw new Error(e)
            }),
            stop: async (...args) => await stop(id, ...args),
            data
        }
    }

    return { getControl, play, stop, stopAll }
}

const list = useAudioList()
export default () => list