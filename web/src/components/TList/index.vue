<template>
    <div class="list">
        <div class="item flex_b" v-for="(item, i) in list " :key="item.id || i" :class="{ tag: item.tag }"
            @click="rightClick(item)">
            <div class="label">
                {{ item.text }}
                <div v-if="item.dot" class="dot"></div>
            </div>
            <div class="right" v-if="!item.tag">
                <span>{{ item.rightText }}</span>
                <span v-if="!item.noicon" class="iconfont icon-xiangyoujiantou"></span>
            </div>
        </div>
    </div>
</template>

<script setup>
const props = defineProps(['list'])
const emit = defineEmits(['rightClick'])
const rightClick = (item) => emit('rightClick', item) 
</script>

<style lang="scss" scoped>
.list {
    .item {
        cursor: pointer;
        padding: 0 10px;
        line-height: 50px;
        border-bottom: 1px solid transparent;
        background-color: var(--tp_footer_bgcolor);
        margin-bottom: 1px;

        .label {
            position: relative;

            .dot {
                position: absolute;
                top: 15px;
                right: -5px;
                width: 4px;
                height: 4px;
                border-radius: 50%;
                background-color: red;
            }
        }

        .right {
            .iconfont {
                margin-left: 10px;
                vertical-align: -1px;
            }
        }

        &.tag {
            cursor: auto;
            line-height: 45px;
            color: #A2A2A2;
            background: transparent;
        }
    }
}</style>