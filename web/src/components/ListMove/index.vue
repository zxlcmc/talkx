<template>
  <div class="list_wrapper">
    
    <div
      class="item"
      v-for="(item, i) in list"
      :key="i"
      :class="{
        enter: tipIndex == i,
        outer: isOuter(item, i),
      }"
      @click="itemClick(item)"
      @transitionend="() => (item.__outer = false)"
    >
      <slot :text="item.text">{{ item.text }} </slot>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, onUnmounted, ref, watch, watchEffect } from "vue";

const emit = defineEmits(["itemClick"]);
const props = defineProps({
  list: {
    type: Array,
    default: () => [],
  },
  height: {
    type: String,
    default: "30px",
  },
});

let timer;
const list = ref([]);
const tipIndex = ref(0);

const h = computed(() => props.height);

const _outer = (index) => {
  // 当前的上一个，
  // 然后是最后一个，那么第一个就是
  // 排除当前
  return (
    (tipIndex.value - 1 == index || index == list.value.length - 1) &&
    tipIndex.value !== index
  );
};
const isOuter = (item, index) => _outer(index) && item.__outer;
const next = () => {
  tipIndex.value = (tipIndex.value + 1) % list.value.length;
  list.value.forEach((item, i) => (item.__outer = _outer(i)));
};

const init = () => {
  clearInterval(timer);
  timer = setInterval(next, 3000);
};

watchEffect(() => {
  list.value = props.list.map((text) => ({ text, __outer: false }));
  init();
});

const itemClick = (item) => emit("itemClick", item);

onMounted(init);

onUnmounted(() => {
  clearInterval(timer);
});
</script>

<style lang="scss" scoped>
.list_wrapper {
  position: relative;
  height: v-bind(h);
  .item {
    text-align: left;
    height: v-bind(h);
    line-height: v-bind(h);
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    opacity: 0;
    transform: translateY(100%);
    transition: all 0.2s;
    &.enter {
      opacity: 1;
      transform: translateY(0);
    }
    &.outer {
      opacity: 0;
      transform: translateY(-100%);
    }
  }
}
</style>
