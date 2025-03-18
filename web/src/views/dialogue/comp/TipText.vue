<template>
  <div class="tip_text"> 
    <template v-for="(item, i) in tipList" :key="i">
      <div
        class="tip_item"
        :class="{
          enter: tipIndex == i,
          outer: isOuter(item, i),
        }"
        @transitionend="() => (item.outer = false)"
      >
        {{ item.text }}
      </div>
    </template>
  </div>
</template>

<script setup>
import tipTexts from "../compositions/buttom/tipTexts";
const { tipIndex, tipList, isOuter } = tipTexts();
</script>

<style lang="scss" scoped>
.tip_text {
  height: 20px;
  position: absolute;
  bottom: 17px;
  right: 35px;
  width: 200px;
  color: #7a8084;
  font-size: var(--tp_size);
  .tip_item {
    position: absolute;
    top: 0;
    right: 0;
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
