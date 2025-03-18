<template>
  <div
    v-if="show"
    class="contentRouter"
    :class="{ mobile: resize.smallRef.value }"
    @click="back"
  >
    <div class="wrapper" :class="wrapperStatus">
      <div class="top">
        <span class="iconfont icon-jiantou_liebiaozhankai_o"></span>
      </div>
      <div class="conetnt" @click.stop="">
        <router-view />
      </div>
    </div>
  </div>
</template>

<script setup>
import { inject, ref, computed, watchEffect, onUnmounted, nextTick } from "vue";
import { useRoute, useRouter } from "vue-router";
import { routerBack, delay } from "@/common/utils";
const route = useRoute();
const router = useRouter();
const resize = inject("resize");
const show = computed(() => route.meta.contentRouter);
const wrapperStatus = ref("");
const stop = watchEffect(() => {
  if (show.value && resize.smallRef.value) {
    // nextTick(() => {wrapperStatus.value = "show" });
    setTimeout(() => (wrapperStatus.value = "show"));
  }
});

const back = async () => {
  wrapperStatus.value = "";
  await delay(300);
  if (window[route.meta.variableName]) return; // 当事件是 路由守卫触发的，阻断 router.push， 防止出现无限循环
  router.push({ name: "dialogue" });
};
onUnmounted(stop);
</script>

<style lang="scss" scoped>
.contentRouter {
  height: 100vh;
  .wrapper {
    height: 100%;
    .top {
      height: 0;
      overflow: hidden;
      text-align: center;
      line-height: 30px;
      background-color: var(--tp_dl_bg_color);
      .iconfont {
        font-size: 40px;
      }
    }
    .conetnt {
      height: 100%;
      background-color: var(--tp_dl_bg_color);
    }
  }

  &.mobile {
    background-color: rgba($color: #000000, $alpha: 0.5);
    z-index: 10;
    position: fixed;
    top: 0;
    left: 0;
    width: 100vw;
    height: 100vh;
    .wrapper {
      width: 100vw;
      height: 80%;
      position: absolute;
      left: 0;
      bottom: 0;
      transition: transform 0.3s;
      transform: translateY(100%);
      $topH: 30px;
      &.show {
        transform: translateY(0);
      }
      & > .top {
        height: $topH;
      }
      & > .conetnt {
        overflow-y: auto;
        height: calc(100% - $topH);
      }
    }
  }
}
</style>
