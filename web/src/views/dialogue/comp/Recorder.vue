<template>
  <div class="vue-Recorder-core">
    <div>
      <button @click="recOpen">打开录音,请求权限</button>
      <button @click="recClose">关闭录音,释放资源</button>
    </div>

    <button @click="recStart">录制</button>
    <button @click="recStop" style="margin-right: 80px">停止</button>
    <div>
      <button @click="recPlayLast">播放</button>
      <button @click="recUploadLast">上传</button>
    </div>

    <div class="mainBox">
      <div
        style="
          height: 150px;
          width: 100%;
          border: 1px solid #ccc;
          box-sizing: border-box;
          display: inline-block;
          vertical-align: bottom;
        "
        class="ctrlProcessWave"
      ></div>
      <div
        style="
          height: 40px;
          width: 300px;
          display: inline-block;
          background: #999;
          position: relative;
          vertical-align: bottom;
        "
      >
        <div
          class="ctrlProcessX"
          style="height: 40px; background: #0b1; position: absolute"
          :style="{ width: powerLevel + '%' }"
        ></div>
        <div
          class="ctrlProcessT"
          style="padding-left: 50px; line-height: 40px; position: relative"
        >
          {{ duration + "/" + powerLevel }}
        </div>
      </div>
    </div>

    <div class="">
      <audio ref="audioRef"></audio>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import Recorder from "recorder-core";

//需要使用到的音频格式编码引擎的js文件统统加载进来，这些引擎文件会比较大
import "recorder-core/src/engine/mp3";
import "recorder-core/src/engine/mp3-engine";

// import "recorder-core/src/engine/wav";
//可选的扩展
import "recorder-core/src/extensions/waveview";

import { useChatStore } from "@/store";
import { textToVoice, voiceToText } from "@/api/voice";

const emit = defineEmits(["send"]);

const useChat = useChatStore();

const audioRef = ref();
const duration = ref(1);
const powerLevel = ref(10);
const logs = ref([]);

const config = {
  type: "mp3",
  bitRate: 16, // 比特率 越大音质越好
  sampleRate: 16000, // 采样率, 越大细节越丰富
  rec: 0,

  recOpenDialogShow: 0,
};

let rWave;
let rec;
let recLogLast;

const recOpen = () => {
  rec = Recorder({
    type: config.type,
    bitRate: config.bitRate,
    sampleRate: config.sampleRate,
    onProcess: function (buffers, powerLevel, duration, sampleRate) {
      try {
        duration.value = duration;
        powerLevel.value = powerLevel;
        rWave.input(buffers[buffers.length - 1], powerLevel, sampleRate); // 有错误
      } catch (e) {
        console.log("onProcess-err", e);
      }
    },
  });

  rec.open(
    function () {
      rWave = Recorder.WaveView({ elem: ".ctrlProcessWave" });
      console.log("open-success", rWave);
    },
    () => {
      console.log("open-err");
    }
  );
};

const recClose = () => rec.close();

const recStart = () => {
  if (!Recorder.IsOpen()) {
    console.log("未打开录音");
    return;
  }
  rec.start();
};
const recStop = () => {
  if (!Recorder.IsOpen()) {
    console.log("未打开录音");
    return;
  }

  rec.stop(
    function (blob, duration) {
      const obj = {
        idx: logs.value.length,
        blob: blob,
        duration: duration,
        rec: rec,
      };
      console.log("已录制:", obj);
      if (obj && obj.blob) {
        recLogLast = obj;
      }
      logs.value.unshift(obj);
    },
    function (s) {
      console.log("录音失败：" + s, 1);
    }
  );
};

const recplay = (idx) => {
  var o = logs.value[logs.value.length - idx - 1];

  const audio = audioRef.value;
  audio.controls = true;
  audio.src = (window.URL || webkitURL).createObjectURL(o.blob);
  audio.play();
};
const recPlayLast = () => {
  if (!recLogLast) {
    console.log("请先录音，然后停止后再播放");
    return;
  }
  recplay(recLogLast.idx);
};
const recUploadLast = async () => {
  if (!recLogLast) {
    console.log("请先录音，然后停止后再播放");
    return;
  }

  try {
    const blob = recLogLast.blob;
    const formData = new FormData();
    formData.append("file", blob);
    const text = await voiceToText(formData);
    console.log("text", text);

    const result = await new Promise((c) =>
      emit("send", { type: "text", text: text, over: c })
    );
    // 回答异常处理
    if ([403].includes(result.response.status)) {
      process.value = false;
      return;
    }
    const index = useChat.currMsg.length - 1;
    const end = useChat.currMsg[index];
    console.log("chat 响应完成", end);

    const data = await textToVoice({
      tts1: { input: end.content, speed: "1.0" },
    });

    const blobData = new Blob([data], { type: "audio/mpeg" });
    const audio = audioRef.value;
    audio.controls = true;
    audio.src = (window.URL || webkitURL).createObjectURL(blobData);
    audio.play();
  } catch (e) {
    console.log("recUploadLast-err", e);
  }
};
</script>

<style lang="scss">
.vue-Recorder-core {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  z-index: 10;
  background-color: rgba($color: #000000, $alpha: 0.5);
  button {
    color: #fff;
  }
}
</style>
