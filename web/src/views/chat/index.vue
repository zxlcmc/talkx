<script setup>
import Header from "./comp/header.vue";
import { computed, inject } from "vue";
const resize = inject("resize");
const showHeader = computed(() => resize.smallRef.value || resize.IDERef.value);
const height = computed(() =>
  !showHeader.value ? "100%" : `calc(100% - ${resize.IDERef.value ? 36 : 50}px)`
);
</script>

<template>
  <div class="chat">
    <Header v-if="showHeader" />
    <div class="content">
      <router-view />
    </div>
  </div>
</template>

<style scoped lang="scss">
.chat {
  height: 100%;
}
.content {
  overflow: hidden;
  height: v-bind(height);
  background-color: var(--tp_bgcolor);
}
</style>
