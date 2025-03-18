<template>
  <div class="NewDialoguePc">
    <div class="avatar">
      <img :src="friend.avatar || default_friend_avatar" alt="" />
    </div>
    <div class="title">{{ friend.name }}</div>
    <div class="intro">{{ friend.welcome }}</div>
    <div class="cTags">
      <div
        class="tagItem"
        v-for="item in commentTags"
        :key="item.name"
        :style="item.style"
      >
        {{ item.name }}
      </div>
    </div>
    <div class="stars" :class="{ small: resize.smallRef.value }">
      <!-- pc 直接展示所有数据 -->
      <!-- samll <3 展示全部 -->
      <!-- samll >=3 滚动 -->
      <div
        class="flex"
        v-if="!stars.length"
        :class="{ small: resize.smallRef.value }"
      >
        <div
          class="border hover_bg"
          v-for="(item, i) in friend.conversationStart"
          :key="i"
          @click="starClick(item)"
        >
          {{ item }}
        </div>
      </div>

      <div class="flex" v-else>
        <div class="border line2">
          <ListMove :list="stars" v-slot="{ text }">
            <div
              class="border"
              v-for="s in text"
              :key="s"
              @click="starClick(s)"
            >
              {{ s }}
            </div>
          </ListMove>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, inject, onMounted, ref } from "vue";
import { useChatStore, useGlobalStore } from "@/store";
import { ROLE_TYPE, default_friend_avatar } from "@/common/config";
import ListMove from "@/components/ListMove/index.vue";

const resize = inject("resize");
const useChat = useChatStore();
const useGlobal = useGlobalStore();
const emit = defineEmits(["send"]);

const friend = computed(() => {
  if (resize.IDERef.value) {
    return (
      useChat.friends
        .filter((f) => f)
        .find((f) => f.roleType === ROLE_TYPE.coding_assistant) ||
      useGlobal.plugin ||
      {}
    );
  }
  return useChat.currFriendInfo;
});

const stars = computed(() => {
  const mlist = [];
  if (!friend.value.conversationStart) return mlist;
  const len = friend.value.conversationStart.length;
  const showList = !resize.smallRef.value || len <= 2;

  if (!showList) {
    for (let i = 0; i < 4; i++) {
      const text =
        friend.value.conversationStart[i] ||
        friend.value.conversationStart[i / len];
      const j = Math.floor(i / 2);
      if (i % 2 == 0) {
        mlist[j] = [];
      }
      mlist[j].push(text);
    }
  }
  return mlist;
});

const commentTags = computed(() => {
  const list = friend.value.commentTags || [];
  return list.map(({ color, bgColor, name }) => {
    return { name, style: { color, backgroundColor: bgColor } };
  });
});

const starClick = (text) => {
  emit("send", { type: "newDialogue", text });
};
</script>

<style lang="scss" scoped>
.NewDialoguePc {
  height: 100%;
  width: 100%;
  text-align: center;
  position: relative;  
  
  .avatar {
    padding-top: 20%;
    img {
      width: 96px;
      height: 96px;
      border-radius: 20px;
    }
  }
  .title {
    color: var(--tp_toast_color);
    font-size: 1.5rem;
  }
  .intro {
    width: 80%;
    font-size: 18px;
    margin: 20px auto;
    word-wrap: break-word;
  }
  .cTags {
    display: flex;
    flex-wrap: wrap;
    justify-content: center;
    .tagItem {
      padding: 2px 15px;
      border-radius: 3px;
      margin: 0 5px;
    }
  }
  .stars {
    position: absolute;
    left: 50%;
    bottom: -10px;
    width: calc(100%);
    transform: translateX(-50%);

    &.small {
      bottom: 0;
    }

    .lines {
      box-sizing: border-box;
      padding: 0 12px;
      margin: auto;
      width: 100%;
      max-width: var(--msg_content-width);
    }
    .starItem {
      text-align: left;
      cursor: pointer;
      line-height: 30px;
      margin-bottom: 10px;
      border-bottom: 1px solid var(--tp_bordecolor);
      &:hover {
        background-color: var(--tp_nd_hover_bg_color);
      }
    }

    .flex {
      flex-wrap: wrap;
      box-sizing: border-box;
      padding: 0 10px;
      margin: auto;
      width: 100%;
      max-width: var(--msg_content-width);
      & > div {
        width: 45%;
        display: inline-block;
        margin-top: 10px;
        &:nth-child(2n) {
          margin-left: 10%;
        }
      }

      .border {
        padding: 2px 10px;
        cursor: pointer;
        text-align: left;
        line-height: 32px;
        box-sizing: border-box;
        border: 1px solid var(--tp_bordecolor);
        overflow: hidden;
        text-wrap: nowrap;
        text-overflow: ellipsis;
      }

      .line2 {
        width: 100%;
        padding: 0;
        .border {
          margin-left: 0;
          height: 36px;
          &:nth-child(2n) {
            margin-top: 10px;
          }
          &:hover {
            background-color: var(--tp_nd_hover_bg_color);
          }
        }
        border: none;
        height: 82px;
        overflow: hidden;
      }

      &.small {
        & > div {
          width: 100%;
          &:nth-child(2n) {
            margin-left: 0;
          }
        }
      }
    }
  }
}
</style>
