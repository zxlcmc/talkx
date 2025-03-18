<template>
  <TList :list="listArr" @rightClick="rightClick" />
</template>

<script setup>
import { inject } from "vue";
import { useRouter } from "vue-router";
import TList from "@/components/TList/index.vue";
const router = useRouter();
const resize = inject("resize");
const listArr = [
  { id: 1, type: "", text: "协议条款", tag: true },
  { id: 2, type: "user", text: "用户协议", rightText: "" },
  { id: 3, type: "privacy", text: "隐私条款", rightText: "" },
  { id: 4, type: "content", text: "内容安全协议", rightText: "" },
  { id: 5, type: "pay", text: "支付协议", rightText: "" },
];
const rightClick = ({ type, tag }) => {
  if (tag) return;
  let name = "agreement";
  if (!resize.smallRef.value) {
    name = "d_" + name;
  }
  router.push({ name, query: { type } });
};
</script>

<style lang="scss"></style>
