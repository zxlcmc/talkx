<template>
  <div class="proxyUrl">
    <span class="model_look" @click="proxeSkip"> [点击这里] </span
    >查看代理设置说明。
  </div>
</template>

<script setup>
import { proxy_base_url } from "@/common/config";
const openUrl = window.__talkx__.openUrl;
const proxeSkip = () => {
  openUrl(proxy_base_url);
};
</script>

<style lang="scss" scoped>
.proxyUrl {
  padding: 20px;
  .model_look {
    cursor: pointer;
    color: var(--tp_model_look_color);
  }
}
</style>
