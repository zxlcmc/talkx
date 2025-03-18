<template>
    <div class="list">
        <div class="item" v-for="(item, i) in props.list" :key="i">
            <div class="top flex_b">
                <div class="type">{{ typeName(item) }}</div>
                <div class="number">{{ item.value }}</div>
            </div>
            <div class="bottom flex_b">
                <div class="time">{{ timeStr(item.createTime) }}</div>
                <div class="ext">{{ item.billTitle }}</div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { timeStr } from '@/common/utils'
const props = defineProps(['list', 'tabs'])

const typeName = (item) => {
    const obj = props.tabs.find(t => t.type == item.type)
    return obj ? obj.name : ''
} 
</script>

<style lang="scss" scoped>
.list {
    .item {
        height: 64px;
        padding: 10px;
        margin-bottom: 1px;
        box-sizing: border-box;
        background-color: var(--tp_footer_bgcolor);

        .top {
            /* font-size: 15px; */
        }

        .bottom {
            color: #777777;
            font-size: 0.9rem;
        }
    }
}
</style>