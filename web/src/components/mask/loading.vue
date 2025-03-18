<script setup>
import { NSpin } from 'naive-ui'
import { propsAttrs } from './props'
import Mask from './index.vue'
 
const props = defineProps({
    ...propsAttrs,
    text: {
        type: String,
        default: '正在处理，请稍后'
    }
}) 
</script>

<template>
    <Mask v-bind="$props">
        <div class="loading_mask">
            <!-- <NSpin size="large" /> -->
            <div class="text">{{props.text}}</div>
        </div>
    </Mask>
</template>

<style lang="scss" scoped> .loading_mask { 
    position: absolute;
    bottom: 0;
    left: 0;
    width: 100%;
    display: flex; 
    flex-direction: column;
    justify-content: center; 
    align-items: center;
    height: 60px;
    background-color: #3c3f41;
    .text{
        margin-top: 10px;
        line-height: 60px;
        color: var(--tp_textcolor);
    }
 }
</style>
