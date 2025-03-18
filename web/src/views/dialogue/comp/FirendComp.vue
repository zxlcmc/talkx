<template>
  <div class="userList" v-if="resize.webSmallRef.value ">
    <!-- <div class="currFriend" @click.stop="show = !show">
      <div class="avater">
        <img :src="curr.avatar || default_friend_avatar" />
      </div>
      <div class="name">{{ curr.name }}</div>
      <div><span class="iconfont icon-xiangyoujiantou"></span></div>
    </div>
    <div class="list_wrapper" v-show="show">
      <div class="san"></div>
      <div class="listWrraper">
        <FriendList />
      </div>
    </div> -->
    <n-popover
      placement="top-start"
      :show="show"
      :style="{
        padding: '4px',
        'border-radius': '0px',
        'background-color': 'var(--tp_userList_bgcolor)',
      }"
    >
      <template #trigger>
        <div class="currFriend" @click.stop="show = !show">
          <div class="avater">
            <img :src="curr.avatar || default_friend_avatar" />
          </div>
          <div class="name">{{ curr.name }}</div>
          <div><span class="iconfont icon-xiangyoujiantou"></span></div>
        </div>
      </template>
      <div class="list_wrapper">
        <div class="san"></div>
        <div class="listWrraper">
          <FriendList />
        </div>
      </div>
    </n-popover>
  </div>
</template>

<script setup>
import { NPopover } from "naive-ui";
import { useChatStore } from "@/store";
import FriendList from "./FriendList.vue";
import { default_friend_avatar } from "@/common/config";
import { computed, inject, onMounted, onUnmounted, ref } from "vue";
const useChat = useChatStore();
const curr = computed(() => useChat.currFriendInfo);
const show = ref(false);
const resize = inject("resize");

const bodyClick = () => {
  if (window.__talkx_event_stop) {
    window.__talkx_event_stop = false;
    return;
  }
  show.value = false;
};
onMounted(() => {
  document.addEventListener("click", bodyClick);
});
onUnmounted(() => {
  document.removeEventListener("click", bodyClick);
});
</script>

<style lang="scss" scoped>
.userList {
  /* display: none; */
  position: relative;

  .currFriend {
    position: relative;
    z-index: 1;
    top: 4px;
    display: inline-flex;
    cursor: pointer;
    color: var(--tp_textcolor);
    line-height: 1;
    padding: 0 0 10px;

    .avater {
      width: 12px;
      height: 12px;

      img {
        width: 100%;
        height: 100%;
      }
    }

    .name {
      margin: 0 5px;
    }

    .iconfont {
      font-size: 12px;
    }
  }

  .list_wrapper {
    position: absolute;
    bottom: 45px;
    left: 10px;
    width: 208px;
    height: 294px;
    box-sizing: border-box;
    border: 1px solid var(--tp_aside_border_color);

    .san {
      display: none;
      border: 1px solid var(--tp_aside_border_color);
      position: absolute;
      top: calc(100% - 8px);
      left: 10px;
      width: 13px;
      height: 13px;
      z-index: 1;
      background-color: var(--tp_aside_bg_color);
      transform: rotate(45deg);
    }

    .listWrraper {
      position: absolute;
      bottom: 0;
      left: 0;
      z-index: 2;
      height: 292px;
      overflow-y: auto;
    }
  }
}

@media (max-width: 600px) {
  .dialogue_buttom {
    .userList {
      display: block;
    }
  }
}
</style>
