<template>
  <div class="schat-header">
    <div class="top_bar flex_b flex_sx_center">
      <div class="flex">
        <div class="del hover_bg" tabindex="0" @click="del" :style="{ cursor }">
          <span class="iconfont icon-delete" v-show="!isNew"></span>
        </div>
        <div
          v-if="useLogin.isLogin"
          class="share hover_bg"
          tabindex="1"
          :style="{ cursor }"
          @click.stop="showShareArea = !showShareArea"
        >
          <span class="iconfont icon-share" v-show="!isNew"></span>
          <div class="list" v-if="showShareArea"> 
            <div class="item" @click="share">分享链接</div>
          </div>
        </div>
      </div>
      <div class="right flex">
        <div
          ref="historyRef"
          class="history flex_sx_center hover_bg"
          tabindex="0"
          :style="{ cursor }"
          @click="toHistory"
        >
          <span class="iconfont icon-history"></span>
          <span class="text">历史话题</span>
        </div>
        <div
          class="add hover_light2"
          tabindex="0"
          :class="{ isNewTopic: false }"
          v-show="!isNew"
          @click="addNew"
          :style="{ cursor }"
        >
          <Icon size="30">
            <MdAdd />
          </Icon>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
/**
 * 状态
 * 新会话
 * 回答中
 * 有会话记录
 */
import { Icon } from "@vicons/utils";
import { MdAdd } from "@vicons/ionicons4";
import { useRoute, useRouter } from "vue-router";
import { onMounted, computed, onUnmounted, ref } from "vue";
import { isNew, useLogin, sending } from "../compositions/chat";

const route = useRoute();
const router = useRouter();
const showShareArea = ref(false);
const emits = defineEmits(["addNew", "del", "share"]);

const cursor = computed(() => (sending.value ? "no-drop" : "pointer"));

const bodyClick = () => (showShareArea.value = false);
const del = () => emits("del");
const share = () => emits("share");
const addNew = () => emits("addNew");
const toHistory = () => {
  router.push({ name: "history", query: { type: "sql" } });
};

onMounted(() => {
  document.addEventListener("click", bodyClick);
});
onUnmounted(() => {
  document.removeEventListener("click", bodyClick);
});
</script>

<style lang="scss" scoped>
.schat-header {
  height: 52px;
  padding: 10px;
  align-items: center;
  align-content: center;
  box-sizing: border-box;
  color: var(--tp_textcolor);
  background: var(--tp_topbar_bgcolor);

  .share,
  .del {
    display: flex;
    padding: 0 2px;
    cursor: pointer;
    margin-right: 5px;
    align-items: center;

    &:focus {
      background-color: var(--tp_foucs_bg_color);
    }

    .icon-share,
    .icon-delete {
      font-size: 20px;
    }
  }
  .share {
    position: relative;

    .list {
      z-index: 10;
      position: absolute;
      top: calc(100% - 5px);
      left: calc(100% - 5px);
      user-select: none;
      position: absolute;
      color: var(--tp_textcolor);
      font-size: var(--tp_size);
      background-color: var(--tp_headerlistbgcolor);

      .item {
        height: 35px;
        line-height: 25px;
        padding: 5px 10px;
        position: relative;
        white-space: nowrap;
        box-sizing: border-box;

        &:hover {
          color: var(--tp_aside_hover_color);
          background: var(--tp_aside_hover_bg_color);
        }
      }
    }
  }

  .right {
    .history {
      cursor: pointer;

      padding: 0px 5px 0;

      .xicon {
        margin-top: -2px;
      }

      .icon-history {
        font-size: 20px;
      }

      .text {
        margin-left: 5px;
        vertical-align: top;
      }
    }

    .add {
      margin-left: 10px;
      cursor: pointer;
      border-radius: 2px;
      text-align: center;
      width: 30px;
      height: 30px;
      color: var(--tp_addBtn_color);
      background-color: var(--tp_addBtn_bg_color);
      &.isNewTopic {
        cursor: no-drop !important;
      }
    }
  }
}
</style>
