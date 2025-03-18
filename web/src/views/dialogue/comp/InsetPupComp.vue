<template>
    <div class="inset_wrapper" v-if="props.show">
        <div class="title flex_b">
            <div>选择的代码</div>
            <div class="close"><span class="iconfont icon-close" @click="close"></span></div>
        </div>
        <div class="text_content"> 
            <TextComp type="quize" :text="props.text" mtype="share" />
        </div>
    </div>
</template>

<script setup>
import { ref } from 'vue'
import TextComp from './TextComp.vue'
const props = defineProps(['show', 'text'])
const emit = defineEmits(['update:show', 'close'])
const text = ref("")
const close = () => {
    emit('update:show', false)
    emit('close')
}
</script>

<style lang="scss" scoped> .inset_wrapper {
     padding: 10px;
     position: absolute;
     left: 10px;
     z-index: 1;
     bottom: calc(100% + 0px);
     box-sizing: border-box;
     width: calc(100% - 20px); 
     background-color: var(--tp_headerbgcolor);

     .title {
         align-items: center;

         .close {
             cursor: pointer;
         }
     }

     .text_content {
         overflow-y: auto;
         max-height: 225px;
     }
 }
</style>