<script setup>
import { Icon } from "@vicons/utils";
import { useRouter, useRoute } from "vue-router";
import { NInput } from "naive-ui";
import { IosPaperPlane, IosUndo, MdRemoveCircle } from "@vicons/ionicons4";
import {
  computed,
  onMounted,
  onUnmounted,
  onBeforeUnmount,
  ref,
  watchEffect,
  nextTick,
  reactive,
  inject,
} from "vue";
import { isMobile } from "@/common/utils";
import { inputStyle } from "@/common/style";
import { useChatStore, useLoginStore } from "@/store";
import { runGuideOver } from "@/compositions/initGuide";
import { useCommunication } from "@/hooks/useCommunication";
import TipText from "./TipText.vue";
import InsetPup from "./InsetPupComp.vue";
import FriendList from "./FirendComp.vue";
import FeatureRow from "./FeatureRow.vue";
import ModelComp from "./ModelComp.vue";
import UploadIcon from "./UploadIcon.vue";
import BottomFileList from "./BottomFileList.vue";
import { fileList } from "../compositions/buttom/fileList";
import insetCode from "../compositions/buttom/insetCode";
import inputHeightChange from "../compositions/buttom/inputHeightChange";
import chatLogic from "../chat";
const style = {
  ...inputStyle,
  fontSize: "14px",
  "padding-bottom": "25px",
  "--n-text-color": "var(--n-text-color)",
  "--n-border": "1px solid var(--tp_bordecolor)",
  "--n-border-disabled": "1px solid var(--tp_bordecolor)",
  "--n-border-hover": "1px solid var(--tp_focus_bordecolor)",
  "--n-border-focus": "1px solid var(--tp_focus_bordecolor)",
  "--n-caret-color": "var(--tp_focus_bordecolor)",
};

const resize = inject("resize");
const route = useRoute();
const router = useRouter();
const useChat = useChatStore();
const useLogin = useLoginStore();
const { featureConfig } = useCommunication();
const text = ref("");
const open = ref(false);
const inputFocusStatus = ref(false);
const textareaRef = ref(null);
const props = defineProps(["sending", "showVerbal", "vShow"]);
const emit = defineEmits([
  "send",
  "stop",
  "back",
  "keydownEvent",
  "update:sending",
]);

inputHeightChange(textareaRef, text);
const { inset, hideInsetArea, showInsetArea, insetClose } = insetCode();

const curr = computed(() => useChat.currFriendInfo);
const toActive = computed(() => route.name == "d_friendsPlaza");
const addActive = computed(() => route.name == "d_friendsPlazaEdit");
const existKey = computed(() => useLogin.user?.apiKey);

const cursor = computed(() => (props.sending ? "no-drop" : "pointer"));
const featureList = computed(() =>
  featureConfig.map((f) => ({ ...f, icon: f.icon }))
);

// 当前模型 allowUpload=true 或者 好友GPTs:friendType=2
const showUpload = computed(
  () => useLogin.user?.allowUpload || curr.value.friendType == 2
);
watchEffect(() => {
  useChat.setText(text.value);
  if (text.value) {
    textareaUp(!!text.value, true);
  }
});

const addFirend = () => router.push({ name: "d_friendsPlazaEdit" });
const toFriendsPlaza = () => router.push({ name: "d_friendsPlaza" });

const setKey = () => {
  router.push({ name: "userEdit", query: { type: "api_key" } });
};

const textareaUp = async (up, focus) => {
  await nextTick();
  if (!textareaRef.value || (props.showVerbal && props.vShow)) return;
  // const { wrapper, textEa } = getInputWrapper();
  // const wrapper = textareaRef.value.$el.querySelector(".n-input-wrapper");
  // wrapper.style.height = up ? '80px' : '62px'
  // textEa.focus()
  focus && textareaRef.value.focus();
};

const inputFocus = () => {
  inputFocusStatus.value = true;
  textareaUp(true);
};
const inputBlur = () => {
  inputFocusStatus.value = false;
  textareaUp(!!text.value);
};

// 功能区点击
let isClick = false;
const itemClick = (item) => {
  console.log("bottom-itemClick", item);
  if (props.sending || isClick) return;
  isClick = true;
  // emit('update:sending', true)
  emit("send", { type: "feature:execute-code-template:" + item.key, text: "" });
  setTimeout(() => (isClick = false));
};

const setVerbal = () => {
  emit("control", { type: "verbal", value: true });
};

let feed = false;
// const shiftEnter = (e) => (feed = true);
const send = (e) => {
  if (feed) {
    feed = false;
    return false;
  }
  e.preventDefault();
  if (props.sending || !text.value || !text.value.trim()) return;
  emit("update:sending", true);
  emit("send", { type: "text", text: text.value });
  text.value = "";
};

const enter = (e) => {
  if (isMobile()) {
    return;
  }
  send(e);
};

const inputKeydown = function (e) {
  // e.stopPropagation();
  // e.preventDefault();
  feed = false;
  const { shiftKey, ctrlKey, altKey, key, keyCode } = e;
  // console.log(` ctrlKey:${ctrlKey}, altKey:${altKey}, key:${key}`, 'keyCode:',e.keyCode);
  // shift enter
  if (shiftKey && key == "Enter") {
    feed = true;
  }
  // ctrl shift D
  else if (ctrlKey && (shiftKey || altKey) && keyCode == 68) {
    emit("keydownEvent", { type: "delCurrChat" });
  }
  // ctrl shift N78=>c67
  else if (ctrlKey && (shiftKey || altKey) && keyCode == 67) {
    emit("keydownEvent", { type: "addNew1" });
  }
};

const stop = () => emit("stop");
const getText = (val) => text.value;
const setText = (val) => (text.value = val);
const back = () => !props.sending && emit("back");
defineExpose({
  open,
  setText,
  getText,
  textareaUp,
  showInsetArea,
  hideInsetArea,
});

const base = 100;
const listStyle = ref({
  "grid-template-columns": `repeat(auto-fill, ${base}px)`,
});
const resizeFun = () => {
  const wrapperW = window.innerWidth - 40;
  let pressrun = Math.floor(wrapperW / 100);
  const dlen = featureList.value.length;
  pressrun = pressrun > dlen ? dlen : pressrun;
  const w = wrapperW / pressrun;
  listStyle.value["grid-template-columns"] = `repeat(auto-fill, ${w}px)`;
};
resize.change(resizeFun);
onMounted(() => {
  textareaUp(!!text.value, true);
  resizeFun();

  setTimeout(() => {
    runGuideOver("bottom_area");
  }, 100);
});
</script>

<template>
  <div
    class="dialogue_buttom"
    :class="{
      pc: !resize.smallRef.value,
      small: resize.smallRef.value,
      noBottom:
        !curr.showModelSelect &&
        (resize.mobileRef.value || resize.smallRef.value),
    }"
  >
    <InsetPup
      v-model:show="inset.show"
      :text="inset.text"
      @close="insetClose"
    />
    <div class="top_row flex_b">
      <div>
        <FriendList />
        <FeatureRow
          v-if="featureConfig.length"
          :cursor="cursor"
          :list="featureConfig"
          @itemClick="itemClick"
        />
      </div>
      <div class="right flex">
        <!-- 注释 setKey -->
        <!-- <div class="right_item hover_light">
          <div tabindex="0" @click="setKey">
            <span class="iconfont icon-key" :class="{ exist: existKey }"></span>
          </div>
        </div> -->

        <template v-if="resize.webSmallRef.value">
          <div class="right_item aiItem" @click="addFirend">
            <span
              class="iconfont icon-user-add hover_bg3"
              :class="{ active: addActive }"
            ></span>
            <span class="text">创建AI</span>
          </div>
          <div class="right_item aiItem square" @click="toFriendsPlaza">
            <span
              class="iconfont icon-fenleiorguangchangorqitatianchong hover_bg3"
              :class="{ active: toActive }"
            ></span>
            <span class="text">AI广场</span>
          </div>
        </template>

        <!-- 语音弃用 -->
        <!-- <div class="right_item hover_light" v-if="props.showVerbal">
          <div tabindex="0" @click="setVerbal">
            <span class="iconfont icon-audio-fill"></span>
          </div>
        </div> -->
      </div>
    </div>
    <div class="input_area" :class="{ uploadFile: fileList.length > 0 }">
      <BottomFileList />
      <NInput
        type="textarea"
        ref="textareaRef"
        v-model:value="text"
        placeholder=""
        :disabled="props.sending"
        @keypress.enter="enter"
        @keydown="inputKeydown"
        :style="style"
        :autosize="{ minRows: 1, maxRows: 1 }"
        @focus="inputFocus"
        @blur="inputBlur"
      />
      <div
        class="send hover_bg2"
        tabindex="0"
        @click="send"
        :style="{ cursor }"
      >
        <Icon size="20">
          <IosPaperPlane />
        </Icon>
      </div>
      <!-- 返回弃用 -->
      <!-- <div class="back hover_bg" tabindex='0' @click="back" :style="{ cursor }">
        <Icon size="20">
          <IosUndo />
        </Icon>
      </div> -->
      <div
        class="stop flex_sx_center"
        tabindex="0"
        @click="stop"
        v-show="props.sending"
      >
        <Icon size="20" color="#fff">
          <MdRemoveCircle />
        </Icon>
        <span class="text">停止回答</span>
      </div>
      <TipText />
      <UploadIcon v-if="showUpload" />
      <ModelComp :showUpload="showUpload" />
    </div>
    <div class="bottom_tip" v-if="!resize.smallRef.value">
      TalkX 可能会犯错误，请检查重要信息。
    </div>
  </div>
</template>

<style lang="scss" scoped>
$maxH: 200px;

.dialogue_buttom {
  padding-top: 10px;
  position: relative;
  color: var(--tp_textcolor);
  background-color: var(--tp_footer_bgcolor);
  border-top: 1px solid var(--tp_bordecolor);

  &.pc {
    margin: 0 auto;
    width: 100%;
    max-width: var(--msg_content-width);
    border-top: 1px solid transparent;
    background-color: var(--tp_dl_bg_color);
    --tp_footer_bgcolor: transparent;
    /* --tp_focus_bordecolor: var(--pc_focus_bordecolor); */
  }

  &.small {
    padding-bottom: 30px;
    .tip_text {
      right: 0;
      bottom: -17px;
    }
    .model_wrapper {
      left: 0;
      bottom: -17px;
    }
  }

  &.noBottom {
    padding-bottom: 0px;
  }

  .bottom_tip {
    font-size: 12px;
    line-height: 30px;
    color: #6c6c6c;
    text-align: center;
    margin-top: -10px;
  }

  .xicon {
    color: var(--tp_textcolor);
  }

  .top_row {
    margin-top: -5px;
    padding: 0 10px 5px;

    .right {
      .right_item {
        cursor: pointer;
        margin-left: 10px;
        .iconfont {
          &.icon-key {
            &.exist {
              color: var(--tp_key_active_color);
            }
          }
        }
        &.aiItem {
          padding: 0 10px;
          line-height: 24px;
          margin-left: 0px;
          .iconfont {
            font-size: 14px;
          }
          .text {
            margin-left: 5px;
          }
        }
      }
    }
  }

  .input_area {
    padding-bottom: 10px;
    box-sizing: border-box;
    /* width: 100%; */
    width: calc(100% - 20px);
    margin: 0 auto;
    position: relative;

    &.uploadFile {
      .n-input.n-input--textarea {
        padding-top: 56px;
      }
    }

    &::v-deep(.n-input-wrapper) {
      min-height: 62px;
      max-height: $maxH;
      transition: height 0.3s;
      .n-input__textarea {
        .n-input__textarea-el {
          max-height: $maxH;
          height: auto;
        }
      }
    }

    .send {
      position: absolute;
      bottom: 15px;
      right: 5px;
      width: 25px;
      height: 25px;
      padding: 2.5px;
      box-sizing: border-box;
    }

    .back {
      position: absolute;
      bottom: 15px;
      left: 5px;
      width: 25px;
      height: 25px;
      padding: 2.5px;
      box-sizing: border-box;
    }

    .stop {
      cursor: pointer;
      color: #fafcff;
      justify-content: center;
      background-color: #595d60;
      width: 122px;
      height: 34px;
      position: absolute;
      top: -20px;
      left: 50%;
      z-index: 1;
      transform: translateX(-50%);
      border: 1px solid #a4a4a4;

      .text {
        margin-left: 5px;
      }
    }
  }

  .feature {
    font-size: var(--tp_size);
    border-top: 1px solid var(--tp_bordecolor);

    .feature_row {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 10px;
      line-height: 28px;

      .right {
        height: 28px;
        cursor: pointer;
        transition: transform 0.3s;

        .iconfont {
          font-size: 28px;
        }

        .xicon {
          vertical-align: bottom;
        }

        &.open {
          transform: rotate(180deg);
        }
      }
    }

    .feature_list {
      height: 70px;
      overflow: hidden;
      box-sizing: border-box;
      display: flex;
      transition: height 0.3s;
      padding: 0px 20px;
      display: grid;
      /* grid-template-columns: repeat(auto-fill, 100px); */
      transition: height 0.2s;

      /* 使用伪元素辅助左对齐 */
      &::after {
        content: "";
        flex: 1;
      }

      &.open {
        padding: 0px 20px;
        height: auto;
      }

      .feature_item {
        width: 90px;
        height: 70px;
        font-size: var(--tp_size);
        margin: 0 10px;
        text-align: center;

        .iconfont {
          font-size: 20px;
        }
      }
    }
  }
}
</style>
