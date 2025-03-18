<template>
    <div class="faceback">
        <div class="row flex_b">
            <div class="left">问题和意见</div>
            <div class="right">{{ form.content.length }}/200</div>
        </div>
        <div class="answer_area">
            <NInput type="textarea" :autosize="{ minRows: 5, maxRows: 5 }" ref="inputRef" maxlength="200" v-model:value="form.content" :style="style"
                placeholder="请填写10个字以上的问题描述以便我们提供更好的帮助" />
        </div>
        <div class="row flex_b">
            <div class="left">图片（选填，提供问题截图）</div>
            <div class="right">{{ imgNumber }}/4</div>
        </div>
        <div class="answer_area imgs">
            <Uplaod v-model:imgs="form.image" :max="4" :size="1024 * 1024 * 5" />
        </div>
        <div class="row flex_b">
            <div class="left">联系电话</div>
        </div>
        <div class="answer_area">
            <NInput maxlength="11" v-model:value="form.contactPhone" :style="style" placeholder="选填，便于我们与你联系" />
        </div>
    </div>
</template>

<script setup>
import { computed, reactive } from "vue";
import { NInput } from "naive-ui";
import { verify } from '@/common/verify'
import Uplaod from '@/components/Upload/index.vue'
const props = defineProps(['inputstyle']) 
const style = props.inputstyle
const form = reactive({ content: "", image: "", contactPhone: "" })

const rule = {
    content: [
        { type: 'empty', message: "请输入问题描述" },
        { type: 'length', max: 200, min: 10, message: '请填写10个字以上的问题描述以便我们提供更好的帮助' }
    ],
    contactPhone: [{ type: 'phoneRule', message: "请输入正确的手机号" }]
}
const imgNumber = computed(() => {
    const arr = form.image.split(',') 
    return arr.filter(c => c).length
}) 

const getForm = async () => {
    const arr = Object.entries(rule).map(([key, value]) => ({ key, rules: value }))
    for (let i = 0; i < arr.length; i++) {
        const { key, rules } = arr[i]
        const errMsg = await verify(form[key], rules)
        if (errMsg) {
            return { err: true, errMsg }
        }
    }
    return { err: false, data: form }
}

defineExpose({ getForm })
</script>

<style lang="scss">
.faceback {
    .row {
        line-height: 50px;
        padding: 0 10px;
    }

    .answer_area {
        background-color: var(--tp_footer_bgcolor);

        &.imgs {
            padding: 10px;
        }
    }
}
</style>