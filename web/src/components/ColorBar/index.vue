<template>
  <div class="colorbar flex_b">
    <div
      class="color_item"
      v-for="color in colors"
      :key="color"
      @click="itemClick(color)"
      :style="{ 'background-color': color }"
    ></div>
    <div class="picker_wrapper">
      <n-color-picker
        size="small"
        :value="props.value"
        @update:value="itemClick"
      />
    </div>
  </div>
</template>

<script setup>
import { NColorPicker } from "naive-ui";
import { onMounted } from "vue";
const props = defineProps(["value"]);
const emit = defineEmits(["update:value"]);

const colors = [
  "#ff515b",
  "#ff7500",
  "#ffcb00",
  "#85d700",
  "#00cb95",
  "#00cadd",
  "#0095ff",
  "#8c9dff",
  "#ff71a2",
];

onMounted(() => {
  if (!props.value) {
    emit("update:value", colors[0]);
  }
});
const itemClick = (color) => {
  emit("update:value", color);
};
</script>

<style lang="scss" scoped>
.colorbar {
  $size: 20px;
  .color_item {
    width: $size;
    height: $size;
    cursor: pointer;
    border-radius: 5px;
  }
  .picker_wrapper {
    width: 30px;
    height: $size;
    &::v-deep(.n-color-picker) {
      height: $size;
      .n-color-picker-trigger__value {
        display: none;
      }
    }
  }
}
</style>
