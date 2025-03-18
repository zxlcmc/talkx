<template>
  <div class="breath" :class="classNames">
    <p
      v-for="i in 8"
      :key="i"
      :class="{ scatter }"
      :style="{ 'transition-duration': scatterTransitionTime + 's' }"
    ></p>
  </div>
</template>

<script setup>
import { computed, ref } from "vue";
import { delay } from "@/common/utils";

let next = "";
const classNames = ref([next]);
const scatter = ref(false);
const props = defineProps(["type"]); // loading noraml

// 散开动画
let timer;
let stopRA = () => {};
const pRotateAnimate = () => {
  const stop = () => clearInterval(timer);
  stop();
  timer = setInterval(() => {
    scatter.value = !scatter.value;
  }, 2500);
  return stop;
};
const scatterTransitionTime = ref(2.5);
const loadScatter = async () => {
  await delay(10);
  scatterTransitionTime.value = 2.5; // 过渡时间
  scatter.value = true;
  stopRA = pRotateAnimate();
};
const noScatter = async () => {
  stopRA();
  scatter.value = false;
  scatterTransitionTime.value = 0.3;
  await delay(300);
};

const change = async (type) => {
  if (next == type) return;
  if (next == "noraml") {
    await noScatter();
  } else if (next == "loading") {
    classNames.value = [""];
    await delay(300);
  }
  if (type == "noraml") {
    loadScatter();
  } else if (type == "loading") {
    classNames.value = ["loading"];
  } else if (type == "") {
    classNames.value = [""];
  }
  next = type;
};

// change("noraml");
defineExpose({ change });
</script>

<style lang="scss" scoped>
.breath {
  width: 100px;
  height: 100px;
  position: relative;
  border-radius: 50%;
  background-color: rgba(255, 255, 255, 0);
  transition: background-color 0.3s;
  $end: 1;

  &:before {
    opacity: 0;
    transition: opacity #{$end}s;
    position: absolute;
    content: "";
    left: 0;
    top: 0;
    width: 100%;
    height: 100%;
    border-radius: 100%;
    border-bottom: 0 solid #ffffff05;
    box-shadow: 0 -10px 20px 20px #ffffff40 inset,
      0 -5px 15px 10px #ffffff50 inset, 0 -2px 5px #ffffff80 inset,
      0 -3px 2px #ffffffbb inset, 0 2px 0px #ffffff, 0 2px 3px #ffffff,
      0 5px 5px #ffffff90, 0 10px 15px #ffffff60, 0 10px 20px 20px #ffffff40;
    filter: blur(3px);
    animation: 2s rotate linear infinite;
  }

  &.loading {
    padding: 0;
    border-radius: 50%;
    /* background: linear-gradient(
      165deg,
      rgba(255, 255, 255, 1) 0%,
      rgb(220, 220, 220) 40%,
      rgb(170, 170, 170) 98%,
      rgb(10, 10, 10) 100%
    ); */
    background-color: #e4ffff;
    &:before {
      opacity: 1;
    }
    p {
      opacity: 0;
    }
  }

  p {
    transform: none;
    /* transition: transform 5s cubic-bezier(0.76, 0.02, 0.52, 0.98); */
  }
  /* p {
      animation: run 5s cubic-bezier(0.76, 0.02, 0.52, 0.98) infinite
        alternate-reverse;
    } */

  &.noraml {
    p {
      opacity: 1;
    }
  }

  .scatter {
    &:nth-of-type(1) {
      transform: rotate(180deg) translate(0px, -50px);
    }
    &:nth-of-type(2) {
      transform: rotate(180deg) translate(35px, -36px);
    }
    &:nth-of-type(3) {
      transform: rotate(180deg) translate(50px, 0px);
    }
    &:nth-of-type(4) {
      transform: rotate(180deg) translate(35px, 35px);
    }
    &:nth-of-type(5) {
      transform: rotate(180deg) translate(0px, 50px);
    }
    &:nth-of-type(6) {
      transform: rotate(180deg) translate(-36px, 35px);
    }
    &:nth-of-type(7) {
      transform: rotate(180deg) translate(-50px, 0px);
    }
    &:nth-of-type(8) {
      transform: rotate(180deg) translate(-36px, -36px);
    }
  }

  p {
    opacity: 1;
    margin: 0;
    width: inherit;
    height: inherit;
    border-radius: 50%;
    position: absolute;
    mix-blend-mode: screen;
    box-shadow: 0 0 2px 2px rgba(0, 0, 0, 0.15),
      inset 0 0 0 1px rgba(255, 255, 255, 0.1);
    transform-origin: 50% 50%;
    transition: all 0.2s;
  }

  p:nth-of-type(1) {
    background: rgba(89, 220, 213, 0.7);
  }
  p:nth-of-type(2) {
    background: rgba(89, 220, 219, 0.7);
  }
  p:nth-of-type(3) {
    background: rgba(89, 215, 220, 0.7);
  }
  p:nth-of-type(4) {
    background: rgba(89, 208, 220, 0.7);
  }
  p:nth-of-type(5) {
    background: rgba(89, 202, 220, 0.7);
  }
  p:nth-of-type(6) {
    background: rgba(89, 195, 220, 0.7);
  }
  p:nth-of-type(7) {
    background: rgba(89, 189, 220, 0.7);
  }
  p:nth-of-type(8) {
    background: rgba(89, 182, 220, 0.7);
  }

  @keyframes run {
    to {
      transform: none;
    }
  }

  @keyframes rotate {
    100% {
      transform: rotate(360deg);
    }
  }
}
</style>
