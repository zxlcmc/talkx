<template>
  <div class="FriendList" ref="wrapper">
    <div
      class="item"
      v-for="item in props.data"
      :key="item"
      :style="itemStyle"
      @click="itemClick(item)"
    >
      <FriendCard :data="item" @collect="collect(item)" />
    </div>
  </div>
</template>

<script setup>
import { inject, nextTick, onMounted, ref } from "vue";
import FriendCard from "./FriendCard.vue";
const resize = inject("resize");
const props = defineProps(["data"]);
const emit = defineEmits(["followed", "itemClick"]);
const itemStyle = ref({ width: "150px" });
const wrapper = ref(null);
const min = 150;
const max = 180;
// 150 -180
const computedW = () => {
  if (!wrapper.value) return;
  const w = parseInt(window.getComputedStyle(wrapper.value)["width"]);
  if (resize.smallRef.value) {
    return (itemStyle.value.width = (w - 20) / 2 + "px");
  }
  const minLen = Math.floor(w / min);
  const maxLen = Math.floor(w / max);
  let len = Math.max(minLen, maxLen);
  len = len > 5 ? 5 : len;
  const baseW = (w - 5 * 2 * len) / len;
  itemStyle.value.width = baseW + "px";
};
resize.change(() => {
  nextTick(() => computedW());
});
const collect = (item) => emit("followed", item);
const itemClick = (item) => emit("itemClick", item);

onMounted(() => {
  computedW();
});

defineExpose({ computedW });
</script>

<style lang="scss" scoped>
.FriendList {
  width: 100%;
  display: flex;
  flex-wrap: wrap;
  .item {
    margin: 5px;
    &.driver-active-element{
      .collect{
        pointer-events: none;
      }
    }
  }
}
</style>
