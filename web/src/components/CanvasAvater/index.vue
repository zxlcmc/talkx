<template>
  <canvas class="canvas" ref="canvasRef" :style="style"></canvas>
</template>

<script setup>
import { computed, onMounted, ref, watch } from "vue";
const props = defineProps({
  size: {
    typeof: Object,
    default: { width: 80, height: 80 },
  },
  bgColor: {
    type: String,
    default: "#50c7da",
  },
  type: {
    type: String,
    default: "text",
  },
  text: {
    type: String,
    default: "文字",
  },
  fontFamily: {
    type: String,
    default: "Arial",
  },
  offset: {
    typeof: Object,
    default: { x: 0, y: 0 },
  },
  fontSize: Number,
});

const style = computed(() => {
  const { width, height } = props.size;
  return {
    width: width + "px",
    height: height + "px",
  };
});
const canvasRef = ref(null);

let canvas, ctx;
const initCanvas = () => {
  if (!canvasRef.value || canvas) return;
  canvas = canvasRef.value;
  ctx = canvas.getContext("2d");
};
const scale = 2;
const draw = () => {
  if (!canvasRef.value) return;
  initCanvas();
  const { width, height } = props.size;
  canvas.width = width * scale;
  canvas.height = height * scale;
  const fontSize =
    props.fontSize ||
    Math.min(Math.floor((width - 10) / props.text.length), 30);
  ctx.fillStyle = props.bgColor;
  ctx.fillRect(0, 0, canvas.width, canvas.height);
  ctx.font = `${fontSize * scale}px ${props.fontFamily}`;
  ctx.textBaseline = "middle";
  ctx.textAlign = "center";
  ctx.fillStyle = "#fff";

  let text = props.text;
  if (props.type == "icon") {
    // 绘制icon
    if (text) {
      text = eval(
        ('("' + "&#x" + text).replace("&#x", "\\u").replace(";", "") + '")'
      );
    } else {
      text = "";
    }
  }
  const x = width / 2 + props.offset.x;
  const y = height / 2 + props.offset.y;
  ctx.fillText(text, x * scale, y * scale);
};

watch(props, draw);

const toUrl = () => canvas.toDataURL("image/png");

onMounted(() => draw());
defineExpose({ toUrl });
</script>

<style lang="scss" scoped>
.canvas {
  outline: 1px solid red;
}
</style>
