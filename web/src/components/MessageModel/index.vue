<template>
  <div class="messageModel flex_sx_center" v-if="show">
    <div class="content">
      <div class="header">
        <div class="title">{{ props.msg.title }}</div>
        <div class="time">{{ timeStr(props.msg.createTime) }}</div>
      </div>
      <div class="center" v-html="props.msg.content"></div>
      <div class="close" @click="close">
        <span class="iconfont icon-close hover_light"></span>
      </div>
    </div>
  </div>
</template>

<script setup>
import {} from "vue";
import { timeStr } from "@/common/utils";
const props = defineProps({
  show: Boolean,
  msg: Object,
});
const emit = defineEmits(["update:show", "close"]);

const close = () => { 
  emit("update:show", false);
  emit("close", props.msg);
};
</script>

<style lang="scss" scoped>
.messageModel {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  z-index: 100;
  background-color: rgba($color: #000000, $alpha: 0.3);
  .content {
    margin: auto;
    position: relative;
    box-sizing: border-box;
    /* width: calc(100% - 20px);
    height: calc(100% - 120px); */
    width: 80%;
    height: 90%;
    max-width: 1024px;
    color: var(--tp_invite_text_color);
    background-color: var(--tp_footer_bgcolor);
    .header {
      height: 60px;
      line-height: 30px;
      border-bottom: 1px solid var(--tp_bordecolor);
      margin: 10px;
      .time {
        color: #777777;
        font-size: 0.9rem;
      }
    }
    .center {
      padding: 10px;
      overflow-y: auto;
      box-sizing: border-box;
      height: calc(100% - 91px);
    }
    .close {
      cursor: pointer;
      position: absolute;
      top: 15px;
      right: 10px;
      width: 20px;
      height: 20px;
      .iconfont {
        color: var(--tp_textcolor);
        font-size: 18px;
        line-height: 21px;
      }
    }
  }
}

@media (max-width: 600px) {
  .messageModel {
    .content {
      width: calc(100% - 20px);
      height: calc(100% - 120px);
    }
  }
}
</style>
