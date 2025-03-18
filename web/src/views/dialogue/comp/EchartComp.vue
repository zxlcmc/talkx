<template>
  <div class="eachart_wrapper" ref="main"></div>
</template>

<script setup>
import { onMounted, ref } from "vue";
import * as echarts from "echarts";
const main = ref();
const props = defineProps(["options"]);

const render = () => {
  if (!main.value) {
    return;
  }
  const myChart = echarts.init(main.value);
  try {
    myChart.setOption(JSON.parse(props.options));
  } catch (err) {}
};
onMounted(() => {
  render();
});
</script>

<style lang="scss" scoped>
.eachart_wrapper {
  width: 100%;
  height: 400px;
  overflow: auto;
  padding-top: 10px;
}
</style>
