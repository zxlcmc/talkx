<template>
  <div class="process_loading">
    <div class="circle" v-for="i in 6" :key="i"></div>
  </div>
</template>

<script setup></script>

<style lang="scss" scoped>
.process_loading {
  height: 100px;
  text-align: center;
  .circle {
    border-radius: 100vmax;
    display: inline-block;
    height: 40vmin;
    opacity: 0.5;
    width: 40vmin;

    animation: pulse-1 800ms infinite ease-in-out alternate;
    margin: 0 calc(0vw - 20vmin);
    position: absolute;
    transform: scale(0);
  }
  $color: #ff9800, #ffc107, #ffeb3b, #cddc39, #8bc34a, #4caf50;
  @each $c in $color {
    $i: index($color, $c);
    .circle:nth-of-type(#{$i + 1}) {
      background: $c;
      animation-delay: #{$i * 100}ms;
    }
  }
}
@keyframes pulse-1 {
  25% {
    transform: scale(0);
  }
  100% {
    transform: scale(1);
  }
}
</style>
