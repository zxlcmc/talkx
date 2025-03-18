<template>
  <div
    class="FriendList"
    @scroll.passive="listScroll"
    :class="{ small: resize.smallRef.value }"
    :style="{
      height: resize.smallRef.value ? 52 + list.length * 60 + 2 + 'px' : '100%',
    }"
  >
    <div class="title flex_b">
      <div class="text">我的AI</div>
      <div class="right flex">
        <template v-if="!resize.smallRef.value">
          <div @click="addFirend">
            <span
              class="iconfont icon-user-add hover_bg3"
              :class="{ active: addActive && !currentFriendId2 }"
            ></span>
          </div>
          <div @click="toFriendsPlaza">
            <span
              class="iconfont icon-fenleiorguangchangorqitatianchong hover_bg3"
              :class="{ active: toActive }"
            ></span>
          </div>
        </template>
      </div>
    </div>
    <div class="flist" ref="flistRef">
      <div class="list_wrapper">
        <div
          class="fitem flex_b"
          v-for="(item, i) in list"
          :key="item"
          @click="itemClick(item)"
          :class="{
            active: (currentUserFriendId && item.userFriendId == currentUserFriendId) || item.friendId == currentFriendId || addActive && item.friendId == currentFriendId2,
            showItem: item.showItem
          }"
        >
          <div class="flex_sx_center">
            <div class="icon">
              <img
                :src="item.avatar || default_friend_avatar"
                :alt="item.name"
              />
            </div>
            <div class="name">
              <span v-if="item.top" class="tdot"></span>
              {{ item.name }}
            </div>
          </div>
          <div
            class="right"
            v-if="!useChat.sending && useLogin.isLogin"
            @click.stop="clickoutside(item, i)"
            :style="{ opacity: resize.smallRef.value ? 1 : 0 }"
          >
            <NDropdown
              :trigger="!resize.smallRef.value ? 'hover' : 'click'"
              placement="right"
              :options="getOptions(item)"
              @select="rowClick($event, item)"
            >
              <div class="clickWrapper">
                <span class="iconfont icon-option-vertical"></span>
              </div>
            </NDropdown>
          </div>
        </div>
      </div>
    </div>
    <div class="san"></div>
  </div>
</template>

<script setup>
import {
  ref,
  getCurrentInstance,
  computed,
  onMounted,
  onUnmounted,
  inject,
  nextTick,
} from "vue";
import { useMessage, NDropdown } from "naive-ui";
import { useRoute, useRouter } from "vue-router";
import { delay } from "@/common/utils";
import { sessionList } from "@/api/chat";
import { useToast } from "@/hooks/useToast";
import { topFriend, deleteFriend } from "@/api/user";
import { useChatStore, useLoginStore } from "@/store";
import { default_friend_avatar } from "@/common/config";
import { runGuideOver } from "@/compositions/initGuide";

const route = useRoute();
const router = useRouter();
const flistRef = ref(null);
const message = useMessage();
const useChat = useChatStore();
const resize = inject("resize");
const useLogin = useLoginStore();
const cxt = getCurrentInstance();
const { showToast, closeWatch, clearWatch } = useToast();
const bus = cxt.appContext.config.globalProperties.$bus;
const list = ref([]);

const dialogue = computed(() => route.name == "dialogue");
const toActive = computed(() => route.name == "d_friendsPlaza");
const addActive = computed(() => route.name == "d_friendsPlazaEdit");
const currentUserFriendId = computed(() => route.query.userFriendId);
const currentFriendId = computed(() => route.query.fid);
const currentFriendId2 = computed(() => route.query.id);

const getData = async (set) => {
  if (set) {
    await useChat.setfriends();
  }
  list.value = useChat.friends.map((obj) => ({ ...obj, showItem: false }));
};
const getOptions = (item) => {
  const arr = [{ label: item.top ? "取消置顶" : "置顶", key: "top" }];
  const ordinary = item.friendType == 1;
  if (ordinary) {
    // 普通AI类型才有 编辑
    arr.push({ label: "编辑", key: "edit" });
    arr.push({ label: "智体", key: "device" });
  }
  if (item.roleType !== "0") {
    arr.push({ label: "删除", key: "del" });
  }
  return arr;
};

const addFirend = () => router.push({ name: "d_friendsPlazaEdit" });
const toFriendsPlaza = () => router.push({ name: "d_friendsPlaza" });

// 列表滚动时，隐藏选项
let clickRightIndex;
const listScroll = () => {
  if (clickRightIndex !== null) {
    const list = document.querySelectorAll(".clickWrapper")[clickRightIndex];
    list && list.click();
  }
  clickRightIndex = null;
};

const clickoutside = (item, i) => {
  clickRightIndex = i;
};

const scrollBottom = () => {
  nextTick(() => {
    if (flistRef.value) {
      flistRef.value.scrollTop = window.innerHeight;
    }
  });
};

const friendFollow = async (item) => {
  console.log("friendFollow", item);
  // 选中+滚动
  if (item.__type == "edit") {
    const index = list.value.findIndex((t) => item.id == t.id);
    list.value[index] = item;
    itemClick(item);
    scrollBottom();
  } else if (item.__type == "add") {
    const obj = { ...item, showItem: true };
    list.value.push(obj);
    // 等待一会
    setTimeout(() => {
      itemClick(obj);
      scrollBottom();
    }, 100);
    obj.showItem = false;
    await delay(300);
  } else {
    // 收藏
    const obj = { ...item, showItem: true };
    list.value.push(obj);
    scrollBottom();
    obj.showItem = false;
    nextTick(() => {
      if (flistRef.value) {
        flistRef.value.scrollTop = window.innerHeight;
      }
    });
    await delay(300);
  }
  await getData(true);
};

onMounted(() => {
  const resflesh = true; //resize.smallRef.value ? true : !useChat.friends.length;
  getData(resflesh);
  bus.on("friendFollow", friendFollow);
  closeWatch("del", (confirm) => {
    if (confirm) {
      del();
    } else {
      isClick = false;
    }
  });

  setTimeout(() => runGuideOver("FriendList"), 100);
});

let isClick = false;
const itemClick = async (row) => {
  if (isClick) return;
  isClick = true;
  useChat.setFriend(row.friendId);
  const query = { id: useChat.getCurrUid, fid: row.friendId };
  router.replace({ name: "dialogue", query });
  bus.emit("friendChange");
  isClick = false;
};

let selectItem = null;
const toTop = async () => {
  if (!selectItem) return;
  const isTop = +!selectItem.top;
  const res = await topFriend({ friendId: selectItem.friendId, isTop });
  if (res.errMsg) {
    message.error(res.errMsg);
    return;
  }
  message.success(isTop ? "置顶成功" : " 取消置顶成功");
  getData(true);
};

const del = async () => {
  if (!selectItem) return;
  const res = await deleteFriend({ friendId: selectItem.friendId });
  if (res.errMsg) {
    isClick = false;
    message.error(res.errMsg);
    return;
  }
  message.success("删除成功");
  getData(true);
  isClick = false;
  itemClick(list.value[0]);
};

const rowClick = async (type, item) => {
  if (isClick) return;
  isClick = true;
  selectItem = item;
  switch (type) {
    case "top": // 置顶
      await toTop();
      isClick = false;
      break;
    case "edit": // 编辑
      router.push({
        name: "d_friendsPlazaEdit",
        query: { id: selectItem.friendId },
      });
      isClick = false;
      break;
    case "device": // 智体
      router.push({
        name: "d_device",
        query: { id: selectItem.friendId, userFriendId: selectItem.userFriendId },
      });
      isClick = false;
      break;
    case "del": // 删除
      window.__talkx_event_stop = true;
      showToast(
        "del",
        "你正在删除这个AI，历史话题也会随之删除，确定要这样做吗？"
      );
      break;
  }
};

onUnmounted(() => {
  clearWatch();
  bus.off("friendFollow", friendFollow);
});
</script>

<style lang="scss" scoped>
.FriendList {
  width: 206px;
  height: 100%;
  overflow-y: auto;
  /* background: var(--tp_aside_bg_color); */
  &.small {
    height: 500px;
    max-height: 500px;
  }
  .title {
    color: var(--tp_textcolor);
    height: 52px;
    line-height: 52px;
    padding-left: 10px;
    .right {
      padding-right: 5px;
      & > div {
        margin: 0 2px;
        cursor: pointer;
        .iconfont {
          padding: 3px;
          border-radius: 3px;
          color: var(--tp_friend_icon_color);
        }
        .iconfont.active {
          color: var(--tp_friend_icon_active_color);
          background-color: var(--tp_friend_icon_active_bg_color);
        }
      }
    }
  }

  .flist {
    position: relative;
    overflow-y: auto;
    height: calc(100% - 52px);

    .list_wrapper {
      .fitem {
        cursor: pointer;
        height: 60px;
        color: var(--tp_aside_color);
        padding: 0 10px;

        &.showItem {
          transform: scale(0);
          transform-origin: left center;
          animation: showItem 0.3s forwards;
          @keyframes showItem {
            0% {
              transform: scale(0);
            }
            100% {
              transform: scale(1);
            }
          }
        }

        .icon,
        .icon img {
          width: 36px;
          height: 36px;
          min-width: 36px;
          overflow: hidden;
          border-radius: 50%;
        }

        .name {
          .tdot {
            width: 0;
            height: 0;
            position: relative;
            top: -3px;
            display: inline-block;
            border-radius: 50%;
            border: 1px solid var(--tp_aside_color);
          }
          margin-left: 10px;
        }

        .right {
          opacity: 0;
          width: 30px;
          max-width: 30px;
          text-align: right;
          line-height: 60px;
          position: relative;
          transition: opacity 0.3s;
          .iconfont {
            font-size: 20px;
          }
        }

        &:hover {
          color: var(--tp_aside_hover_color);
          background: var(--tp_aside_hover_bg_color);
          .right {
            opacity: 1 !important;
          }
        }

        &.active {
          color: var(--tp_aside_hover_color);
          background: var(--tp_aside_hover_bg_color);
        }
      }
    }
  }
}
</style>
