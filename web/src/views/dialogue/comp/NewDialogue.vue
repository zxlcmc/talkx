<script setup>
// import { intro } from "@/api/chat";
import { delay } from "@/common/utils";
import { useLoginStore, useGlobalStore } from "@/store";
import { ref, computed, onMounted, watch, watchEffect } from "vue";

const texts = ref("");
const props = defineProps(["sending"]);
const emit = defineEmits(["send", "update:sending"]);
const useLogin = useLoginStore();
const useGlobal = useGlobalStore();
const isLogin = computed(() => useLogin.isLogin);
const list = [
  // { key: '1', text: '“用 SpringMVC 写一个 event/stream 响应格式的示例”' },
  // { key: '2', text: '“生成一个简单的 REST API 示例代码”' },
  // { key: '3', text: '“如何设置 git 的全局变量”' },
  // { key: '4', text: '“使用阿里云提供的CLI，写一个OSS的上传示例”' },
  { key: "1", text: "你好！TalkX，请问你可以提供哪些帮助？" },
  { key: "2", text: "用 Python 写一个爬取 www.talkx.cn 页面标题的示例代码。" },
  { key: "3", text: "出现了java.lang.NoSuchMethodError，是什么原因？" },
];

const itemClick = (item) => {
  if (props.sending) return;
  emit("update:sending", true);
  emit("send", { type: "newDialogue", text: item.text });
};

let writerStop = () => {};
watch(
  () => useGlobal.intro,
  () => {
    typewriter();
  }
);

const typewriter = async () => {
  writerStop();
  const data = useGlobal.intro; //await intro()
  let i = 0;
  const max = data.length;
  let stop = false;
  const writer = async () => {
    const text = data[i];
    if (!text && stop) return;
    texts.value = "";
    for (let j = 0; j < text.length; j++) {
      if (stop) return;
      texts.value += text[j];
      await delay(50);
    }
    await delay(4000);
    i++;
    i = i % max;
    writer();
  };
  writer();
  writerStop = () => {
    stop = true;
  };
}; 

onMounted(() => {
  typewriter();
});
</script>

<template>
  <div class="NewDialogue">
    <div class="nologin" v-if="!isLogin">
      当前未登录，虽然可以正常使用，但是话题记录不会保存。建议点击右上角菜单登录后再使用。
    </div>
    <!-- <div class="top">
            <Icon size="55">
                <MdHelpCircle />
            </Icon>
            <div class="desc">请问您在IDE中选择的代码模板是哪个？您需要我帮您解决什么技术问题吗？</div>
        </div> -->
    <div class="text_wrapper">
      <div class="texts">
        <span class="text">{{ texts }}</span>
        <span class="cursor"></span>
      </div>
    </div>
    <div class="info">
      <p>试一下:</p>
      <div
        class="btn_div"
        v-for="(item, i) in list"
        :key="i"
        @click="itemClick(item)"
      >
        "{{ item.text }}"
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.NewDialogue {
  padding: 0 10px;
  font-size: var(--tp_size);
  color: #7a8084;
  position: relative;
  height: 100%;

  .nologin {
    padding: 10px;
    margin-bottom: 30px;
    color: #555555;
    background-color: #ffcc99;
  }

  /* .top {
        display: flex;
        align-items: center;

        .desc {
            margin-left: 15px;
            flex: 1;
        }
    } */

  .text_wrapper {
    padding: 20px 10px 0;

    .texts {
      line-height: 1.5;
      text-align: center;

      margin: auto;

      .text {
        font-size: 22px;
        color: var(--tp_textcolor);
      }

      .cursor {
        display: inline-block;
        width: 0;
        vertical-align: -4px;
        height: 22px;
        margin-left: 5px;
        border-left: 2px var(--tp_textcolor) solid;
        animation: cursor 1s steps(1) infinite;
      }

      @keyframes cursor {
        0%,
        100% {
          opacity: 1;
        }

        50% {
          opacity: 0;
        }
      }
    }
  }

  .info {
    position: absolute;
    bottom: 0;
    left: 0;
    width: 100%;
    padding: 0 10px;
    box-sizing: border-box;

    .btn_div {
      cursor: pointer;
      line-height: 30px;
      margin-bottom: 10px;
      border-bottom: 1px solid var(--tp_bordecolor);

      &:hover {
        background-color: var(--tp_nd_hover_bg_color);
      }
    }
  }
}
</style>
