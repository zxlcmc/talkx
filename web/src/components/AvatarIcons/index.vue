<template>
  <div class="AvatarIcons flex">
    <div
      class="icon_w"
      v-for="(icon, i) in iconNames"
      :key="icon"
      :style="style"
      @click="itemClick(icon, i)"
    >
      <span class="aicon" :class="icon"></span>
    </div>
  </div>
</template>

<script setup>
import { computed, nextTick, onMounted, ref } from "vue";
import styles from "@/assets/font/avatarIcon.scss?raw";

const reg = /icon-[a-zA-Z0-9]{2,8}/g;
const regText = /content: "\\[a-zA-Z0-9]{4}/g; 
const iconNames = ref(styles.match(reg));
const iconText = styles.match(regText) 
  
const props = defineProps(["value", "bgColor"]);
const emit = defineEmits(["update:value", "font"]);  

const style = computed(() => {
  return {
    "background-color": props.bgColor || "#0095ff",
  };
});
onMounted(() => {
  if (!props.value) {
    // nextTick(()=> itemClick(iconNames.value[0], 0))
    setTimeout(()=> itemClick(iconNames.value[0], 0), 100)
  }
});
const itemClick = (icon, i) => {
  const text = iconText[i].slice(11);
  const family = "aicon";
  emit("update:value", icon);
  emit("font", { text, family });
};
</script>

<style lang="scss" scoped>
.AvatarIcons {
  flex-wrap: wrap;
  .icon_w {
    $size: 30px;
    width: $size;
    height: $size;
    line-height: $size;
    text-align: center;
    border-radius: 50%;
    margin: 2px;
    .aicon {
      cursor: pointer;
      color: #fff;
      font-size: 20px;
    }
  }
}
</style>
