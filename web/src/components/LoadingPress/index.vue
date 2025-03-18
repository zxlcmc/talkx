<template>
    <div class="loadingPress" v-if="props.show"></div>
</template>

<script setup>
const props = defineProps(['show'])
const emit = defineEmits(['update:show'])


</script> 
<style lang="scss">
@keyframes press {
    0% {
        background-position: 0 0;
    }

    100% {
        background-position: 100% 0;
    }
}

.loadingPress {
    z-index: 100;
    pointer-events: none;
    position: fixed;
    left: 0;
    bottom: 0;
    width: 100%;
    height: 5px;
    background: linear-gradient(90deg, rgba(0, 122, 204, 0.2), rgba(0, 122, 204, 1), rgba(0, 122, 204, 0.2));
    background-size: 50% 100%;
    background-position: 0 0;
    animation: press 1s linear infinite;
}
</style>