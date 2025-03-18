<script setup>
import { propsAttrs } from './props'
const props = defineProps(propsAttrs)
const style = {
    backgroundColor: props.bgColor
}
</script>

<template>
    <div class="mask" :style="style">
        <slot></slot>
    </div>
</template>

<style scoped>
.mask {
    position: fixed;
    top: 0;
    left: 0;
    bottom: 0;
    right: 0;
    z-index: 1000;
}
</style>
