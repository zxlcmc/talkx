<template>
  <div class="user_row">
    <!-- <div
      class="user flex_sx_center"
      @click.stop="userClick"
      :class="{ active: show }"
    >
      <img class="avater" :src="user.avatar || default_user_avatar" />
      <div class="name" :title="user.name">{{ user.name || '未登录' }}</div>
    </div>
    <div class="listWrapper" v-show="show">
      <UserList v-model:show="show" />
    </div> -->
    <n-popover
      :show-arrow="false"
      placement="top-end"
      :show="show"
      :style="{
        padding: '4px',
        'border-radius': '0px',
        'background-color': 'var(--tp_userList_bgcolor)',
      }"
    >
      <template #trigger>
        <div
          class="user flex_sx_center"
          @click.stop="userClick"
          :class="{ active: show }"
        >
          <img class="avater" :src="user.avatar || default_user_avatar" />
          <div class="name" :title="user.name">{{ user.name || "未登录" }}</div>
        </div>
      </template>
      <div class="listWrapper">
        <UserList :show="true" @update:show="(e) => (show = e)" />
      </div>
    </n-popover>
  </div>
</template>

<script setup>
import UserList from "@/views/chat/comp/UserList.vue";
import { useLoginStore, useChatStore } from "@/store";
import { default_user_avatar } from "@/common/config";
import { computed, onMounted, onUnmounted, ref } from "vue";
import { NPopover } from "naive-ui";
const show = ref(false); 
const useChat = useChatStore();
const useLogin = useLoginStore();
const user = computed(() => useLogin.user || {});
const bodyClick = () => (show.value = false);
const userClick = () => (show.value = !show.value);
onMounted(() => document.addEventListener("click", bodyClick));
onUnmounted(() => document.removeEventListener("click", bodyClick));
</script>

<style lang="scss">
.user_row {
  position: relative;
  .user {
    height: 60px;
    padding: 0 10px;
    cursor: pointer;
    .avater {
      width: 32px;
      height: 32px;
      border-radius: 50%;
    }

    .name {
      width: 164px;
      margin-left: 10px;
      overflow: hidden;
      text-wrap: nowrap;
      text-overflow: ellipsis;
    }
    &.active,
    &:hover {
      color: var(--tp_aside_hover_color);
      background: var(--tp_aside_hover_bg_color);
    }
  }
  .listWrapper {
    width: 150px;
    position: absolute;
    bottom: 100%;
    right: 0;
    z-index: 10;
  }
}
</style>
