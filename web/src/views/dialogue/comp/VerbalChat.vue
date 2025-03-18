<template>
  <div class="VerbalChat tpage">
    <div class="vCWrapper" @click="cs">
      <ProcessLoading2 class="processLoading2" ref="plRef" />
      <VerbalCanvas class="VerbalCanvas" ref="verCanvas" />
      <!-- <ProcessLoading class="processLoading" v-if="process && !answer" /> -->
      <div class="top_beta">「语音版<sup>beta</sup>」</div>
    </div>
    <div class="footer">
      <div class="guanji_wrapper" :class="{ active: !guanji }">
        <!-- @click="btnClick" -->
        <div
          class="guanji"
          @touchstart.prevent="touchstart"
          @touchend.prevent="touchend"
        >
          <span class="iconfont icon-audio-fill"></span>
        </div>
        <div class="guanji_dot"></div>
      </div>
      <!-- <NButton class="open no-select" @click="btnClick">
        <span class="btn_text">{{ btnText }}</span>  
      </NButton> -->
      <!-- <NButton
        class="open no-select"
        @touchstart.prevent="touchstart"
        @touchend.prevent="touchend"
      >
        <span class="btn_text">{{ btnText }}</span>
      </NButton> -->
      <div class="tipText" v-if="tipText">{{ tipText }}</div>
      <div class="beta" v-if="beta">长按开始说话，松开结束说话</div>
    </div>

    <div class="close" @click="close">
      <span class="iconfont icon-close"></span>
    </div>
  </div>
</template>

<script setup>
import { NButton } from "naive-ui";
import { computed, onMounted, onUnmounted, ref, watch } from "vue";
import { random } from "@/common/utils";
import { useChatStore, useLoginStore } from "@/store";
import { textToVoice, voiceToText } from "@/api/voice";
import VerbalCanvas from "@/components/VerbalCanvas/index.vue";
import ProcessLoading from "@/components/ProcessLoading/index.vue";
import ProcessLoading2 from "@/components/ProcessLoading2/index.vue";
import {
  getAuidoData,
  stopAudio,
} from "../compositions/VerbalChat/getAuidoData";
import loadDevices, {
  checkMime,
  devicesDispose,
  getUserMediaPermission,
} from "../compositions/VerbalChat/loadDevices";
import clear from "naive-ui/es/_internal/clear";

const emit = defineEmits(["send", "update:show"]);
const props = defineProps(["show"]);

const beta = ref(true);
const guanji = ref(true);
const tipText = ref("");
const speak = ref(false); // 状态：平静 说
const answer = ref(false); // 状态：播放回答
const process = ref(false); // 处理中
const verCanvas = ref();
const plRef = ref();
const useChat = useChatStore();

let closePage = false; // 页面是否关闭
let MediaPermission;
let upWave;

const btnText = computed(() => {
  if (!speak.value && !process.value) return "点击说话";
  if (speak.value) return "我说完了";
  if (answer.value) return "回答中";
  if (process.value) return "响应中";
});

const close = () => {
  closePage = true;
  emit("update:show", false);
};

let mediaRecorder, initAudio;
const draw = ({ type, data }) => {
  verCanvas.value && verCanvas.value.draw({ type, data });
};

const claerCanvas = () => verCanvas.value && verCanvas.value.clear();

const uploadAudio = async (blob) => {
  if (process.value || closePage) return;
  // 绘制中间部分
  process.value = true;
  try {
    plRef.value?.change("loading");
    claerCanvas();
    upWave = getAuidoData(); // 还不能放到一开始, 只能等录音停止 且 可能需要一段时间初始化 iphone7
    // 上传音频数据 转换文本
    const formData = new FormData();
    formData.append("file", blob); // , "recording.wav"
    const text = await voiceToText(formData).catch((err) => {
      console.log("err", err);
      return "";
    });

    console.log("text", text);
    if (!text) {
      upWave.destroy();
      tipText.value = "无法识别";
      process.value = false;
      plRef.value?.change("noraml");
      return;
    }

    // 然后调用chat 接口
    const result = await new Promise((c) =>
      emit("send", { type: "text", text, over: c })
    );
    // 回答异常处理
    if ([403].includes(result.response.status)) {
      upWave.destroy();
      process.value = false;
      plRef.value?.change("noraml");
      return;
    }

    const index = useChat.currMsg.length - 1;
    const end = useChat.currMsg[index];

    console.log("chat 响应完成", end);
    // 转换为语音 播放
    const params = { tts1: { input: end.content, speed: "1.0" } };
    const bdata = await textToVoice(params).catch((err) => {
      console.log("err", err);
      return "";
    });

    if (!bdata) {
      upWave.destroy();
      tipText.value = "出了点问题";
      process.value = false;
      plRef.value?.change("noraml");
      return;
    }

    plRef.value?.change("");
    answer.value = true;

    console.log("语音 播放");
    const blobData = new Blob([bdata], { type: "audio/mpeg" });
    // const blobData = new Blob([bdata], { type: checkMime()});
    const url = window.URL.createObjectURL(blobData);
    upWave.change((data) => draw({ data })); // 绘制波纹
    await new Promise((c) => upWave.load(url, "", c)); 
    console.log("播放 完成");

    answer.value = false;
    // 绘制上声波
  } catch (err) {
    console.log("流程出现错误", err);
  }
  setTimeout(() => {
    claerCanvas(); 
    setTimeout(() => claerCanvas(), 10)
  }, 10);
  plRef.value?.change("noraml");
  process.value = false;
};

/** 打开录音 */
const openRecord = () => {
  return new Promise((resolve, reject) => {
    mediaRecorder?.destroy()
    loadDevices({
      MediaPermission,
      readover: (r) => {
        mediaRecorder = r;
        console.log("准备");
        resolve(true);
      },
      change: (dataArray) => {
        if (speak.value) {
          draw({ data: dataArray });
        }
      },
      stop: (blob) => uploadAudio(blob),
      error: reject,
    });
  });
};

// 提问
const quizeFun = () => {
  beta.value = false;
  if (answer.value || process.value || speak.value) return; // 排除 播放回答中，响应中， 提问中
  console.log("开始录制");
  guanji.value = false;
  tipText.value = "";
  plRef.value?.change("");
  speak.value = true;
  mediaRecorder && mediaRecorder.start();
};
// 回答
const answerFun = () => {
  guanji.value = true;
  console.log("停止录制");
  speak.value = false;
  mediaRecorder && mediaRecorder.stop();
};

let ducTime = 60 * 1000;
let permissionTime = 1000; // 授权时间
let durationTimer;
let isTouchstart = false;
const touchstart = async () => {
  console.log("touchstart");
  if (isTouchstart) return; // 防止重复触发
  isTouchstart = true;
  // 判断权限
  let t = Date.now();
  const result = await openRecord().catch((err) => {
    console.log("打开权限-err", err);
    tipText.value = err._msg || "请重新授权";
    isTouchstart = false;
    return false;
  });
  if (!result || !isTouchstart) return;
  tipText.value = "";
  const dt = Date.now() - t;
  console.log("---dt---", dt);
  if (dt > permissionTime) {
    isTouchstart = false;
    console.log("仅授权");
    mediaRecorder?.destroy()
    return;
  }
  quizeFun();
  // 最长录音时长限制
  clearTimeout(durationTimer);
  durationTimer = setTimeout(() => {
    console.log(ducTime + "ms时长到了, 自动回答");
    touchend();
  }, ducTime);
};
const touchend = () => {
  console.log("touchend");
  isTouchstart = false;
  clearTimeout(durationTimer);
  answerFun();
};

// 如果手势抬起 还没授权完毕，就是 仅授权，谈起弹窗，不会触发 touchEnd 事件
// 是否已授权, 如果获取流超过一段时间就是获取权限，否则就是，开始录音

// TODO 录制时长限制

// const btnClick = async () => {
//   beta.value = false;
//   if (answer.value || process.value) return;
//   if (!speak.value) {
//     console.log("打开权限-按下");
//     uplift = false;
//     let t = Date.now();
//     const result = await openRecord().catch((err) => {
//       console.log("打开权限-err", err);
//       tipText.value = err._msg || "请重新授权";
//       return false;
//     });
//     const dt = Date.now() - t;
//     console.log("授权时间dt:", dt + "ms");
//     if (!result) return;
//     guanji.value = false;
//     console.log("开始录制");
//     tipText.value = "";
//     plRef.value?.change("");
//     speak.value = true;
//     mediaRecorder && mediaRecorder.start();
//   } else {
//     console.log("uplift 抬起");
//     if (uplift) return;
//     uplift = true;
//     // 排除授权阶段抬起
//     guanji.value = true;
//     console.log("停止录制");
//     speak.value = false;
//     mediaRecorder && mediaRecorder.stop();
//   }
// };

const cs = async () => {
  return;
  const data = await textToVoice({
    tts1: { input: "你好吗" + random(10, 99), speed: "1.0" },
  });
  // 绘制上声波
  // 把接口返回的二进制音频数据转url
  const blobData = new Blob([data], { type: "audio/mpeg" });
  const url = window.URL.createObjectURL(blobData);
  console.log("url", url);
  // const upWave = getAuidoData();
  plRef.value?.change("");

  upWave.change((data) => draw({ data }));
  setTimeout(() => {
    upWave.load(url, data);
    claerCanvas();
  }, 3000);
};

onMounted(() => {
  // MediaPermission = getUserMediaPermission();
  // draw({ type: "bottom", data: [] });
  closePage = false;
  plRef.value?.change("noraml");
});

onUnmounted(() => {
  // TODO 当在响应中的时候 关闭语音窗口处理
  upWave?.destroy();
  stopAudio(); // 停止通过AudioContext还在播放的语音
  devicesDispose(); // 释放录音占用
});
</script>

<style lang="scss" scoped>
.VerbalChat {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  z-index: 101;
  background-color: rgba($color: #000000, $alpha: 1);
  $bh: 240px;

  .vCWrapper {
    position: relative;
    height: calc(100% - $bh);
    
    .VerbalCanvas {
      position: relative;
      z-index: 1;
    }

    .processLoading {
      position: absolute;
      width: 100%;
      bottom: 50%;
      transform: translateY(50%);
    }

    .processLoading2 {
      position: absolute;
      top: 50%;
      left: 50%;
      transform: translate(-50%, -50%);
    }

    .top_beta{
      position: absolute;
      left: 0;
      top: 20px;
      width: 100%;
      color: #888;
      line-height: 24px;
      text-align: center;
    }
  }
  .footer {
    height: $bh;
    text-align: center;
    box-sizing: border-box;
    padding-top: 10px;

    .guanji_wrapper {
      position: relative;
      .guanji {
        position: relative;
        top: -3px;
        font-family: FontAwesome;
        width: 50px;
        height: 50px;
        margin: 0 auto;
        cursor: pointer;
        border-radius: 50%;
        text-decoration: none;
        background-color: rgb(83, 87, 93);
        text-shadow: 0px 1px 1px rgba(250, 250, 250, 0.1);
        box-shadow: 0px 3px 0px 0px rgb(34, 34, 34),
          0px 7px 10px 0px rgb(17, 17, 17),
          inset 0px 1px 1px 0px rgba(250, 250, 250, 0.2),
          inset 0px -12px 35px 0px rgba(0, 0, 0, 0.5);
        transition: text-shadow 350ms;
        .iconfont {
          color: rgb(37, 37, 37);
          font-size: 24px;
          line-height: 50px;
          transition: color 350ms ease;
        }
      }

      .guanji_dot {
        position: relative;
        top: -80px;
        width: 8px;
        height: 8px;
        margin: 10px auto 0;
        border-radius: 4px;
        background-color: rgb(226, 0, 0);
        box-shadow: inset 0px 1px 0px 0px rgba(250, 250, 250, 0.5),
          0px 0px 3px 2px rgba(226, 0, 0, 0.5);
      }
      &.active {
        .guanji {
          top: 0px;
          box-shadow: 0px 0px 0px 0px rgb(34, 34, 34),
            0px 3px 7px 0px rgb(17, 17, 17),
            inset 0px 1px 1px 0px rgba(250, 250, 250, 0.2),
            inset 0px -10px 35px 5px rgba(0, 0, 0, 0.5);
          .iconfont {
            color: #fff;
          }
        }
        .guanji_dot {
          box-shadow: inset 0px 1px 0px 0px rgba(250, 250, 250, 0.5),
            0px 0px 3px 2px rgba(135, 187, 83, 0.5);
          background-color: rgb(135, 187, 83);
        }
      }
    }

    .beta {
      color: #888;
      line-height: 24px;
      text-align: center;
    }

    .tipText {
      padding: 5px;
      color: red;
      text-align: center;
    }
  }

  .close {
    color: #fff;
    position: absolute;
    top: 20px;
    right: 20px;
    z-index: 102;
    cursor: pointer;
    .iconfont {
      font-size: 26px;
    }
  }
  .open {
    /* -webkit-touch-callout: none; */
    /* & > ::v-deep(.n-button__content) { */
    .btn_text {
      color: #fff;
      pointer-events: none;
      -webkit-touch-callout: none;
      -webkit-user-select: none;
      -khtml-user-select: none;
      -moz-user-select: none;
      -ms-user-select: none;
      user-select: none;
    }

    /* .btn_text_div {
      width: 100px;
      height: 20px;
      background-position-y: -115px;
      background-size: 50px 50px;
      background-image: url(https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fitem%2F201712%2F18%2F20171218171246_mB5nY.thumb.1000_0.jpeg&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1702541371&t=e2159939d8b867a337c3ed9cce88b57d);
    } */
  }
}
</style>
