<script setup>
import rings from "@/assets/svg/rings.svg";
import rings_light from "@/assets/svg/rings_light.svg";
import TextComp from "./TextComp.vue";
import FileListComp from "./FileListComp.vue";
import SimpleGallery from "@/components/SimpleGallery/index.vue";

import { Icon } from "@vicons/utils";
import { useMessage } from "naive-ui";
import {
  computed,
  inject,
  onMounted,
  onUnmounted,
  ref,
  watch,
  watchEffect,
} from "vue";
import { IosSync, MdAlbums, MdCreate } from "@vicons/ionicons4";

import { copyToClip } from "@/common/copy";
import { useToast } from "@/hooks/useToast";
import { getImgWH } from "@/common/utils";
import { useChatStore, useLoginStore } from "@/store";
import { default_user_avatar, IMG_TYPES } from "@/common/config";
import chatAudio from "../compositions/chatAudio";
const { getMsgAudio } = chatAudio;
const resize = inject("resize");
const message = useMessage();
const useChat = useChatStore();
const useLogin = useLoginStore();
const props = defineProps(["sending", "item", "end", "type"]);
const emit = defineEmits(["change", "update:sending", "delmsg"]);
const { showToast, closeWatch, clearWatch } = useToast();

const loadingIcon = computed(() =>
  useLogin.theme == "dark" ? rings : rings_light
);
const currFriend = computed(() => useChat.currFriendInfo);
const user = computed(() => useLogin.user || { name: "我" });
const quize = computed(() => props.item.chat[0]?.content);
// const quizeId = computed(() => props.item.chat[0]?.id);
// const answerId = computed(() => props.item.chat[1]?.id);
const quizeChart = computed(() => props.item.chat[0] || {});
const answerChart = computed(() => props.item.chat[1] || {});
const answer = computed(() => answerChart.value.content);
const welcome = computed(() => props.item.chat[0]?.welcome);
const answer500 = computed(() => answerChart.value.status == 500);
const share = computed(() => props.type === "share");
const quizeTitle = computed(() => {
  if (share.value) return "提问";
  return (useChat.isWeb ? user.value.name : "提问") || "提问2";
});
const answerTitle = computed(() => {
  if (share.value) return "回答";
  return (useChat.isWeb ? currFriend.value.name : "回答") || "回答";
});
const isPlay = computed(() => answerChart.value.play);
const pc = computed(() => !resize.smallRef.value);
const quizeAvater = computed(() => {
  if (useLogin.user) {
    return useLogin.user.avatar;
  }
  return default_user_avatar;
});
const answerAvater = computed(() => useChat.avater);

const imgs = ref([]);
const others = ref([]);

let isStop = false;
const computedImgs = () => {
  imgs.value = [];
  others.value = [];
  const attachments = quizeChart.value.attachments || [];
  attachments.forEach(async (obj) => {
    const { mimeType, url: src } = obj;
    const img = IMG_TYPES.includes(mimeType);
    if (img) {
      const { width, height } = await getImgWH(src);
      const params = {
        width,
        height,
        src,
        thumbnailURL: src + "?image_process=resize,w_150",
      };
      if (isStop) return;
      imgs.value.push(params);
    } else {
      others.value.push(obj);
    }
  });
};

const answerPlay = ref(false);
const playAudio = async () => {
  const audio = getMsgAudio(answerChart.value.id, (data) => {
    answerPlay.value = data.play;
  }).control;
  if (!audio.data.play) {
    answerPlay.value = true;
    console.log("语音文案:", answerChart.value.id);
    await audio.play(answer.value).catch((err) => {
      console.log("err", err);
    });
  } else {
    audio.stop();
  }
  // answerPlay.value = false;
  // TODO 每一个回答都有自己的播放状态
  // TODO 播放中 其他操作 （重新回答, 删除回答， 提问， 等等）
  // 先只通过点击控制播放暂停
};
const copyQuize = () => emit("change", { type: "copyQuize", item: props.item }); // 复制提问到输入框
const reanswer = () => emit("change", { type: "reanswer", item: props.item }); // 重新回答
const copyAnswer = async () => {
  // 复制答案
  await copyToClip(answer.value);
  message.success("复制成功！");
};
const collect = (e) => emit("change", { type: "collect", item: props.item, e });

let delMsgData = {};
onMounted(() => {
  computedImgs();
  closeWatch("del", (confirm) => {
    if (confirm) {
      emit("delmsg", delMsgData);
    }
  });
});
onUnmounted(() => {
  isStop = true;
  clearWatch();
});

const delMessage = async (i) => {
  delMsgData = props.item.chat[i];
  showToast("del", "你正在删除这条消息，确定要这样做吗？");
};
</script>

<template>
  <div class="MessageBody" :class="{ pc: !share && pc }">
    <div class="msg_item quize" v-if="quize">
      <div class="flex_b title_row">
        <div class="left">
          <img class="avater" v-if="pc && !share" :src="quizeAvater" alt="" />
          <span> {{ quizeTitle }} </span>
        </div>
        <div class="icon" v-show="!props.sending && !share">
          <span
            class="hover_light iconfont icon-delete"
            @click="delMessage(0)"
            title="删除提问"
          ></span>
          <span
            class="hover_light iconfont icon-edit"
            @click="copyQuize"
            title="编辑提问"
          ></span>
        </div>
      </div>
      <div class="msg_body">
        <div class="text_wrapper" :msg-id="quizeChart.id">
          <FileListComp v-if="others.length" :list="others" />
          <SimpleGallery
            v-if="imgs.length"
            :galleryID="'gallery' + quizeChart.id"
            :images="imgs"
          />
          <TextComp
            :text="quize"
            :chat="props.item.chat"
            type="quize"
            :mtype="props.type"
          />
        </div>
      </div>
    </div>
    <div
      class="msg_item answer"
      v-if="answer || answerChart.loading"
      :class="{ status_500: answer500 }"
    >
      <div class="flex_b title_row">
        <div class="left">
          <img class="avater" v-if="pc && !share" :src="answerAvater" alt="" />
          <span>{{ answerTitle }}</span>
        </div>
        <div class="icon" v-show="!welcome && !props.sending">
          <template v-if="!share">
            <span
              class="iconfont icon-jushoucang"
              @click="collect"
              title="收藏"
            ></span>
            <span
              v-if="currFriend.voiceChat"
              class="hover_light iconfont"
              :class="!answerPlay ? 'icon-fayinlianxi' : 'icon-bizui'"
              @click="playAudio"
              title="语音播放"
            ></span>
            <span
              class="hover_light iconfont icon-delete"
              @click="delMessage(1)"
              title="删除回答"
            ></span>
            <span
              v-if="end"
              class="hover_light iconfont icon-refresh"
              @click="reanswer"
              title="重新回答"
            ></span>
          </template>
          <span
            class="hover_light iconfont icon-file-copy"
            @click="copyAnswer"
            title="复制回答"
          ></span>
        </div>
      </div>
      <div class="msg_body">
        <div
          class="text_wrapper"
          :msg-id="answerChart.id"
          v-if="!answerChart.loading"
        >
          <TextComp
            :text="answer"
            :chat="props.item.chat"
            type="answer"
            :cursor="end"
            :mtype="props.type"
          />
        </div>
        <div class="text_loading flex" v-show="answerChart.loading">
          <img style="width: 26px" class="loading_svg" :src="loadingIcon" />
        </div>
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.MessageBody {
  margin: 0 10px 10px;
  padding-bottom: 3px; // 5px
  padding-top: 5px;

  &.pc {
    /* color: var(--pc_message_answer_color); */
    --tp_quize_bgcolor: transparent;
    --tp_answer_bgcolor: transparent;
    --tp_message_quize_color: var(--pc_message_quize_color);
    --tp_message_answer_color: var(--pc_message_answer_color);
    .msg_item {
      .msg_body {
        padding-left: 34px;
        .text_wrapper {
          padding: 5px 0;
          .TextComp {
            padding: 2px 0;
          }
        }
      }
      .title_row {
        .icon {
          opacity: 0;
        }
      }
    }
  }

  .iconfont,
  .xicon {
    cursor: pointer;
    color: var(--tp_textcolor);
    margin-left: 5px;
  }

  .msg_item {
    .title_row {
      height: 26px;
      line-height: 25px;

      .left {
        display: flex;
        align-items: center;
        font-size: 15px;

        .avater {
          width: 24px;
          height: 24px;
          margin-right: 10px;
          border-radius: 50%;
        }
      }
      .icon {
        opacity: 1;
        transition: opacity 0.2s;
      }
    }

    &.quize {
      .text_wrapper {
        background: var(--tp_quize_bgcolor);
      }
    }

    &.answer {
      margin-top: 10px;

      .text_wrapper {
        background: var(--tp_answer_bgcolor);
      }
    }

    &:hover {
      .title_row {
        .icon {
          opacity: 1;
        }
      }
    }
  }

  .text_wrapper {
    margin: 5px 0 0;
    line-height: 30px;
    padding: 5px;
  }
}
</style>
