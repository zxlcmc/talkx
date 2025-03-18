<template>
  <div class="schat-bottom">
    <div class="top flex_b">
      <div class="left"><SchemaComp /></div>
      <div class="right flex">
        <div class="right_item aiItem" @click="addSchema">
          <span class="iconfont icon-add-rectangle hover_bg3"></span>
          <span class="text">新建表结构</span>
        </div>
      </div>
    </div>
    <div class="input_area">
      <NInput
        type="textarea"
        ref="textareaRef"
        v-model:value="text"
        placeholder=""
        :disabled="sending"
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
      <div
        class="stop flex_sx_center"
        tabindex="0"
        @click="stop"
        v-show="sending"
      >
        <Icon size="20" color="#fff">
          <MdRemoveCircle />
        </Icon>
        <span class="text">停止回答</span>
      </div>
      <TipText />
    </div>
  </div>
</template>

<script setup>
import { Icon } from "@vicons/utils";
import { useRouter } from "vue-router";
import { useSchemaStore } from "@/store";
import SchemaComp from "./SchemaComp.vue";
import { NInput } from "naive-ui";
import { isMobile } from "@/common/utils";
import { inputStyle } from "@/common/style";
import { sending } from "../compositions/chat.js";
import { IosPaperPlane, IosUndo, MdRemoveCircle } from "@vicons/ionicons4";
import { ref, computed, onMounted, watchEffect, nextTick, inject } from "vue";
import inputHeightChange from "@/views/dialogue/compositions/buttom/inputHeightChange";
import TipText from "@/views/dialogue/comp/TipText.vue";
const text = ref("");
const router = useRouter();
const textareaRef = ref(null);
const resize = inject("resize");
const useSchema = useSchemaStore();
const inputFocusStatus = ref(false);
const emit = defineEmits(["send", "stop", "back", "keydownEvent"]);
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
const cursor = computed(() => (sending.value ? "no-drop" : "pointer"));

useSchema.getList();
inputHeightChange(textareaRef, text);
const addSchema = () => router.push({ name: "editSchema" });

const textareaUp = async (up, focus) => {
  await nextTick();
  if (!textareaRef.value) return;
  focus && textareaRef.value.focus();
};

watchEffect(() => {
  useSchema.setText(text.value);
  if (text.value) {
    textareaUp(!!text.value, true);
  }
});

let feed = false;
const send = (e) => {
  if (feed) {
    feed = false;
    return false;
  }
  e.preventDefault();
  if (sending.value || !text.value || !text.value.trim()) return;
  emit("send", { type: "text", text: text.value });
  text.value = "";
};

const enter = (e) => {
  if (isMobile()) {
    return;
  }
  send(e);
};

const inputFocus = () => {
  inputFocusStatus.value = true;
  textareaUp(true);
};
const inputBlur = () => {
  inputFocusStatus.value = false;
  textareaUp(!!text.value);
};

const inputKeydown = function (e) {
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
const getText = () => text.value;
const setText = (val) => (text.value = val);

defineExpose({
  //   open,
  setText,
  getText,
  textareaUp,
});

onMounted(() => {
  textareaUp(!!text.value, true);
});
</script>

<style lang="scss" scoped>
$maxH: 200px;
.schat-bottom {
  height: 164px;
  padding: 0 10px;
  box-sizing: border-box;
  color: var(--tp_textcolor);
  background-color: var(--tp_footer_bgcolor);
  border-top: 1px solid var(--tp_bordecolor);
  .top {
    padding: 10px 5px;
    .left {
      width: 50%;
    }
    .right {
      .right_item {
        cursor: pointer;
        white-space: nowrap;
        .iconfont {
          font-size: 14px;
          color: var(--tp_textcolor);
        }
        .text {
          margin-left: 5px;
          color: var(--tp_textcolor);
        }
      }
    }
  }

  .input_area {
    padding-bottom: 10px;
    box-sizing: border-box;
    margin: 0 auto;
    position: relative;

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
  &::v-deep(.tip_text) {
    right: 0;
    bottom: -17px;
  }
}
</style>
