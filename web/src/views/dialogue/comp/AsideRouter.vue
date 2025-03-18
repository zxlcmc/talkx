<template>
  <div class="AsideRouter" v-if="show" @click="bodyClick">
    <div class="aside" @click.stop :class="[asideStatus, fixed]">
      <router-view />
    </div>
  </div>
</template>

<script setup>
import { computed, inject, nextTick, onUnmounted, ref, watchEffect } from "vue";
import { useRoute, useRouter } from "vue-router";
import { routerBack, delay } from "@/common/utils";
const route = useRoute();
const router = useRouter();
const resize = inject("resize");
const asideStatus = ref("");
const fixed = computed(() => route.meta.fixed || "left");
const show = computed(() => route.meta.asideRouter && !resize.smallRef.value);
const stop = watchEffect(() => {
  if (show.value) {
    setTimeout(() => (asideStatus.value = "show"));
  }
});
const bodyClick = async (e) => {
  //   routerBack(router, { name: "dialogue" });
  asideStatus.value = "";
  await delay(300);
  if (window[route.meta.variableName]) return; // 当事件是 路由守卫触发的，阻断 router.push， 防止出现无限循环
  router.push({ name: "dialogue" });
};

onUnmounted(stop);
</script>

<style lang="scss">
.AsideRouter {
  position: fixed;
  width: 100vw;
  height: 100vh;
  z-index: 20;
  background-color: rgba($color: #000000, $alpha: 0.3);
  .aside {
    width: 375px;
    height: 100%;
    transition: transform 0.3s;
    &.left {
      transform: translateX(-100%);
      &.show {
        transform: translateX(0);
      }
    }
    &.right {
      transform: translateX(100vw);
      &.show {
        transform: translateX(calc(100vw - 100%));
      }
    }
  }
}
</style>
