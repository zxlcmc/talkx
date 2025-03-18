<template>
  <canvas class="VerbalCanvas" ref="canvasRef"></canvas>
</template>

<script setup>
import { onMounted, ref, watch } from "vue";

let points = []; // 点集合
let ratio = Math.max(window.devicePixelRatio, 2);

/**
 * 粒子移动处理
 *
 * 上声波
 * 中球旋转
 * 下声波
 */
const canvasRef = ref();

/** 绘制曲线 */
const drawWave = (ctx, canvas, type, data) => {
  const waveH = 150; // 波区域高度
  const obj = {
    top: 0,
    center: canvas.height / 2,
    bottom: canvas.height - waveH,
  };
  const initY = obj[type];
  const dataArray = data || [];

  ctx.fillStyle = "rgba(200, 200, 200, 0)";
  ctx.fillRect(0, 0, canvas.width, canvas.height);

  ctx.lineWidth = 1;
  ctx.strokeStyle = "#0077FF"; //"rgb(0, 0, 0)";

  ctx.clearRect(0, 0, canvas.width, canvas.height);
  ctx.beginPath();

  const sliceWidth = (canvas.width * 1.0) / dataArray.length;
  let x = 0;

  for (let i = 0; i < dataArray.length; i++) {
    const v = dataArray[i] / 128.0;
    // const y = (v * canvas.height) / 2 ;
    const y = (v * waveH) / 2 + initY;

    if (i === 0) {
      ctx.moveTo(x, y);
    } else {
      ctx.lineTo(x, y);
    }

    x += sliceWidth;
  }

  // ctx.lineTo(canvas.width, canvas.height / 2);
  ctx.lineTo(canvas.width, waveH / 2) + initY;
  ctx.stroke();
};

/** 绘制球 */
const drawBall = (options) => {};

/** 绘制音频环 */
const drawLoop = (ctx, canvas, type, data) => {
  ctx.clearRect(0, 0, canvas.width, canvas.height);
  const cX = canvas.width / 2;
  const cY = canvas.height / 2;
  const r = 100;
  const basel = Math.floor(data.length / 360);
  for (var i = 0; i < 360; i++) {
    var value = (data[i * basel] / 60) * 8; //8;    // 模拟数据 value = Math.random() * 100
    ctx.beginPath();
    ctx.lineWidth = 2;
    ctx.strokeStyle = "#08a3ef";
    ctx.moveTo(cX, cY);
    //R * cos (PI/180*一次旋转的角度数) ,-R * sin (PI/180*一次旋转的角度数)
    ctx.lineTo(
      Math.cos(((i * 1) / 180) * Math.PI) * (r + value) + cX,
      -Math.sin(((i * 1) / 180) * Math.PI) * (r + value) + cY
    );
    ctx.stroke();
  }
  //画一个小圆，将线条覆盖
  ctx.beginPath();
  ctx.lineWidth = 1;
  ctx.arc(cX, cY, r, 0, 2 * Math.PI, false);
  ctx.fillStyle = "#000";
  ctx.stroke();

  ctx.fill();
};

/** 绘制圆 */
const drawCircle = (ctx, canvas, type, data) => {
  ctx.clearRect(0, 0, canvas.width, canvas.height);
  const cX = canvas.width / 2;
  const cY = canvas.height / 2;
  const r = 100;
  const colors = ["#C1F7F9", "#A6ECEF", "#7ED6D6", "#409A9A"]; // 数值越大，颜色越深
  const getColor = (v) => {
    let i = 0;
    if (v >= 110) {
      i = 3;
    } else if (v >= 90) {
      i = 2;
    } else if (v >= 70) {
      i = 1;
    }
    return colors[i];
  };
  const vValue = 128;
  for (var i = 0; i < data.length; i += 4) {
    const v = (data[i] + data[i + 1] + data[i + 2] + data[i + 3]) / 4; // 范围[50-130]  128=50
    const r = 50 + (v - 128) * (50 / 128);

    // 模拟数据
    // for (var i = 0; i < 254; i += 4) {
    //   const r = 120//Math.random() * 130;

    ctx.beginPath();
    ctx.lineWidth = 1; //* ratio;
    ctx.arc(cX, cY, r * ratio, 0, 2 * Math.PI, false);
    ctx.strokeStyle = getColor(r); //"#C1F7F9";
    ctx.stroke();
  }
};

let ctx, canvas;
const draw = ({ type, data }) => {
  if (!canvasRef.value) return;
  canvas = canvasRef.value;
  canvas.height = parseFloat(getComputedStyle(canvas)["height"]) * ratio;
  canvas.width = parseFloat(getComputedStyle(canvas)["width"]) * ratio;
  ctx = canvas.getContext("2d");
  ctx.imageSmoothingEnabled = true; // 抗锯齿

  // ctx.scale(1, 1);
  // ctx.translate(0.5, 0.5);

  // drawWave(ctx, canvas, type, data);
  // drawLoop(ctx, canvas, type, data);
  drawCircle(ctx, canvas, type, data);
};

const clear = () => {
  try {
    if (ctx) {
      ctx.clearRect(0, 0, canvas.width, canvas.height);
    }
  } catch (er) {
    console.log("er", er);
  }
};

defineExpose({ draw, clear });
</script>

<style lang="scss">
.VerbalCanvas {
  width: 100%;
  height: 100%;
}
</style>
