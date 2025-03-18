<template>
  <div
    class="FriendsPlaza"
    :class="{ small: resize.smallRef.value }"
    @click="bodyClick"
  >
    <div class="content">
      <div class="title">AI广场</div>
      <div class="desc">精选高质量AI，使交流变得无限可能</div>
      <div class="rows">
        <Tabs
          ref="tabsRef"
          :data="tags"
          :tIndex="params.tag"
          @tabChange="tabChange"
        />
      </div>
      <div class="lists">
        <div class="list_wrapper" :class="{ show: listShow }">
          <FriendList ref="friendRef" :data="list" @followed="followed" />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import {
  computed,
  inject,
  onMounted,
  ref,
  getCurrentInstance,
  nextTick,
} from "vue";
import { useMessage } from "naive-ui";
import Tabs from "@/components/Tabs/index.vue";
import FriendList from "./comp/FriendList.vue";
import { delay } from "@/common/utils";
import { friendplaza, friendFollow } from "@/api/user";
import { useChatStore, useGlobalStore } from "@/store";
import { runGuideOver } from "@/compositions/initGuide";

const list = ref([]);
const listShow = ref(false);
const tabsRef = ref(null);
const friendRef = ref(null);
const message = useMessage();
const useChat = useChatStore();
const resize = inject("resize");
const useGlobal = useGlobalStore();
const cxt = getCurrentInstance();
const bus = cxt.appContext.config.globalProperties.$bus;

let isClick = false;
const params = ref({ tag: "" });
const getData = async (set) => {
  list.value = await friendplaza(params.value);
  if (friendRef.value) {
    // 此处是 在窄屏下 出现滚动条导致的宽度变小 导致计算错误 处理
    nextTick(() => {
      friendRef.value.computedW();
    });
  }
  listShow.value = true;
};
const bodyClick = () => {
  if (tabsRef.value) {
    tabsRef.value.bodyClick();
  }
};
const tags = computed(() => {
  let total = 0;
  const list = useGlobal.tags.map(({ count, name }) => {
    total += count;
    return { label: name, count, id: name };
  });
  return [{ label: "全部", count: total, id: "" }, ...list];
});
const tabChange = async (item) => {
  if (isClick) return;
  isClick = true;
  listShow.value = false;
  const tag = tags.value.find((t) => t.id == item.id);
  if (tag) {
    params.value.tag = tag.id;
    await delay(200); // 使用过渡效果
    await getData();
  }
  isClick = false;
};

// 添加好友
const followed = async (item) => {
  if (isClick || item.followed) return;
  isClick = true;
  const res = await friendFollow({ friendId: item.id });
  if (res.err) {
    isClick = false;
    res.errMsg && message.error(res.errMsg);
    return;
  }
  const obj = list.value.find((f) => f.id === item.id);
  obj.followed = 1;
  useChat.addFriends(res);
  bus.emit("friendFollow", res);
  isClick = false;
};

onMounted(async () => {
  await getData();
  setTimeout(() => runGuideOver("AI_Plaza"), 300);
});
</script>

<style lang="scss" scoped>
.FriendsPlaza {
  height: 100%;
  overflow-y: auto;
  text-align: center;
  color: var(--tp_avatar_color);
  background-color: var(--tp_dl_bg_color);
  &.small {
    .content {
      height: 100%;
      .title {
        padding: 20px 0 20px;
      }
      .lists {
        padding: 0;
        padding-bottom: 100px;
        box-sizing: border-box;
        height: calc(100% - 180px);
      }
      /* padding-bottom: constant(safe-area-inset-bottom);
        padding-bottom: env(safe-area-inset-bottom); */
    }
  }
  .content {
    margin: 0 auto;
    max-width: var(--msg_content-width);
    .title {
      font-size: 30px;
      font-weight: bold;
      padding: 50px 0 20px;
    }
    .desc {
      font-size: 1rem;
      color: var(--tp_aside_color);
    }
    .rows {
      margin: 20px 0 20px;
    }
    .lists {
      padding: 0 30px;
      overflow-y: auto;
      height: calc(100vh - 250px);
      .list_wrapper {
        opacity: 0;
        transition: opacity 0.2s linear;
        &.show {
          opacity: 1;
        }
      }
    }
  }
}
</style>
