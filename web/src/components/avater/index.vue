<script setup>
import { useChatStore } from "@/store";
import { computed } from "vue";
const useChat = useChatStore();
const talk = computed(() => useChat.sending);

const {size, text} = defineProps({
  size: { type: String, default: "28px" },
  text: { type: String, default: "TalkX" },
});
</script>

<template>
  <!-- <div class="avater" :class="talk ? 'talk' : ''"></div> -->
  <div class="avater_wrapper flex_sx_center"> 
    <img :style="{width: size, height: size}" src="@/assets/talkx.svg" alt="" />
    <span>{{text}}</span>
  </div>
</template>

<style lang="scss" scoped>
.avater {
  /* width: v-bind(size);
  height: v-bind(size); */
  background-size: 100% 100%;
  background-image: url(@/assets/avater_close.png);

  animation: normal 5.5s infinite;
  @keyframes normal {
    0%,
    90%,
    100% {
      background-image: url(@/assets/avater_close.png);
    }
    95% {
      background-image: url(@/assets/avater_eye.png);
    }
  }
  &.talk {
    animation: open 0.5s steps(1) infinite;
    @keyframes open {
      0%,
      100% {
        background-image: url(@/assets/avater_close.png);
      }
      50% {
        background-image: url(@/assets/avater_open.png);
      }
    }
  }
}

.avater_wrapper { 
  span {
    line-height: v-bind(size);
    font-size: 16px;
    margin-left: 2px;
    color: var(--tp_avatar_color);
  }
}
</style>
