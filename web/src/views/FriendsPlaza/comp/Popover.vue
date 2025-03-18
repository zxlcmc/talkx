<template>
  <NPopover trigger="hover" raw>
    <template #trigger>
      <span class="iconfont icon-wenhao"></span>
    </template>
    <div class="right_tip">{{ props.text }}</div>
  </NPopover>
</template>

<script setup>
import { NPopover } from "naive-ui";
const props = defineProps(["text"]);
</script>

<style lang="scss" scoped>
.right_tip {
  padding: 10px;
  max-width: 300px;
  border-radius: 5px;
  color: var(--tp_share_statement_color);
  background-color: var(--tp_share_bg_color);
}
</style>
