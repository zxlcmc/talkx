<template>
  <div class="FileListComp">
    <div class="item" v-for="item in props.list" :key="item">
      <div class="other">
        <span class="iconfont icon-file"></span>
      </div>
      <span class="text">{{ item.name }}</span>
    </div>
  </div>
</template>

<script setup>
const props = defineProps({
  list: Array,
});
</script>

<style lang="scss">
.FileListComp {
  align-items: center;
  .item {
    height: 42px;
    padding: 5px;
    display: flex;
    max-width: 300px;
    box-sizing: border-box;
    background-color: var(--tp_d_filelist_bgColor);
    .other {
      width: 25px; 
      text-align: center;
      background-color: #ff5688;
      .iconfont {
        color: #fff;
      }
    }
    .text {
      margin-left: 4px;
      font-size: 12px;
      overflow: hidden;
      white-space: nowrap;
      display: inline-block;
      text-overflow: ellipsis;
      color: var(--tp_d_filelist_color);
      width: calc(100% - 42px);
    }
  }
}
</style>
