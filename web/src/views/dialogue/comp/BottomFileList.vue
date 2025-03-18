<template>
  <div
    class="BottomFileList"
    v-if="fileList.length"
    :class="{ small: resize.smallRef.value }"
  >
    <div class="ul flex">
      <div
        class="item"
        v-for="item in fileList"
        :key="item.url"
        :class="item.type"
      >
        <img v-if="item.type == 'img'" :src="item.url" alt="" />
        <div class="other" v-else>
          <span class="iconfont icon-file"></span>
        </div>
        <span class="text">{{ item.name }}</span>
        <div class="close" @click="close(item)">
          <span class="iconfont icon-close1"></span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { inject } from "vue";
import { fileList, infoList } from "../compositions/buttom/fileList";
const resize = inject("resize");
const close = (item) => {
  const index = infoList.value.findIndex((c) => c.id == item.id);
  infoList.value.splice(index, 1);
};
</script>

<style lang="scss" scoped>
.BottomFileList {
  position: relative;
  z-index: 2;
  margin-bottom: -56px;
  box-sizing: border-box;
  height: calc(42px + 7px + 6px + 1px);
  border-bottom: 0.5px solid var(--tp_bordecolor);
  .ul {
    position: absolute;
    left: 0;
    bottom: 0;
    height: 80px;
    padding-top: calc(80px - 48px);
    box-sizing: border-box;
    overflow-x: auto;
    overflow-y: hidden;
    width: 100%;
    padding-left: 6px;
    &::-webkit-scrollbar {
      height: 3px;
      width: 0;
    }
  }
  .item {
    padding: 5px;
    height: 42px;
    min-width: 150px;
    display: flex;
    margin-right: 5px;
    position: relative;
    align-items: center;
    box-sizing: border-box;
    background-color: var(--tp_d_filelist_bgColor);
    &.img {
      min-width: 42px;
      .text{
        display: none;
      }
    }
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
    img {
      width: 32px;
      height: 32px;
    }

    .close {
      display: none;
      position: absolute;
      z-index: 1;
      top: -9px;
      right: -9px;
      width: 18px;
      height: 18px;
      color: #fff;
      border-radius: 50%;
      cursor: pointer;
      text-align: center;
      line-height: 18px;
      background: #868793;
      border: 1px solid #fff;
      .iconfont {
        font-size: 14px;
      }
    }

    &:hover {
      .close {
        display: block;
      }
    }
  }
  &.small {
    .item {
      .close {
        display: block;
      }
    }
  }
}
</style>
