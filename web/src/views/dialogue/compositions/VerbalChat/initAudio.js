

export default function audioInit() {
    const audio = document.createElement("audio");
    audio.setAttribute("controls", true);
    audio.style.display = "none";
    document.body.appendChild(audio);

    let init = false;
    const iosInitPlayAudio = () => {
        console.log("初始化播放");
        if (init) return;
        init = true;
        audio.volume = 0;
        audio.src =
            "https://dev-test-space.oss-cn-hangzhou.aliyuncs.com/dist01/IATest/private/T/T260/music/gold.mp3";
        audio.addEventListener('canplay', () => {
            audio.volume = 0;
            audio.play(); 
        })
    };

    const play = (src, end = () => { }) => {
        audio.src = src;
        audio.load();
        audio.addEventListener("ended", () => end());
        audio.volume = 1;
        audio.play();
        audio.volume = 1;
    };

    return { audio, iosInitPlayAudio, play };
};