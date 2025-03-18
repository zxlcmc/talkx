<script setup>
import avater from "@/components/avater/index.vue";
import UserList from "./UserList.vue";
import { useRouter } from "vue-router";
import { Icon } from "@vicons/utils";
import { NPopover, useMessage } from "naive-ui";
import {
  computed,
  onMounted,
  onUnmounted,
  ref,
  getCurrentInstance,
  inject,
  watchEffect,
} from "vue";
import { useToast } from "@/hooks/useToast";
import { PRODUCT_TYPE } from "@/common/config";
import {
  useLoginStore,
  useChatStore,
  themeObj,
  useHomeRecordStore,
} from "@/store";
import { useCommunication } from "@/hooks/useCommunication";

const cxt = getCurrentInstance();
const bus = cxt.appContext.config.globalProperties.$bus;

const props = defineProps({
  hideRight: {
    default: false,
  },
});
const { showToast, closeWatch, clearWatch } = useToast();
const { receive, send, clearReceive } = useCommunication();
const useChat = useChatStore();
const cursor = computed(() => (useChat.sending ? "no-drop" : "pointer"));

const select = ref("");
const router = useRouter();
const showItems = ref(false);
const showMlist = ref(true);
const message = useMessage();
const useLogin = useLoginStore();
const resize = inject("resize");
const homeRecordStore = useHomeRecordStore();
 
const routerName = computed(() => router.currentRoute.value.name);
watchEffect(() => {
  select.value = routerName.value;
});

const IDERef = computed(() => resize.IDERef.value);
const isMoreDot = computed(() => !homeRecordStore.get("header_more"));

const hlist = computed(() => {
  const list = [{ label: "对话", value: "dialogue" }];
  if (IDERef.value) {
    list.push({ label: "SQL", value: "sqlchat", beta: true });
  }
  return list;
});

const showList = () => {
  if (useChat.sending) return;
  homeRecordStore.set({ key: "header_more", value: true });
  showItems.value = !showItems.value;
};
const toUser = () =>
  router.push({ name: "user", query: { origin: routerName.value } });
const toCoins = () =>
  router.push({ name: "userEdit", query: { type: "my_hashrate" } });
const tItemClick = (item) => {
  if (item.value == "sqlchat" && !useLogin.isLogin) {
    message.warning("请登录");
    return;
  }
  select.value = item.value;
  router.push({ name: item.value });
};
const bodyClick = () => (showItems.value = false);
const controllist = (show) => {
  showMlist.value = show;
};
onMounted(() => {
  bus.on("controllist", controllist);
  closeWatch("logout", async (confirm) => {
    if (confirm) {
      const currRouteName = router.currentRoute.value.name;
      // 清除登陆信息并刷新页面
      await useLogin.logOut();
      useChat.loginOutClear();
      if (currRouteName == "dialogue") {
        bus.emit("dialogue_event", { type: "logout" });
      } else {
        // 当前路由不是会话页跳转会话页
        router.push({ name: "dialogue", query: { type: "add" } });
      }
    }
  });
  document.addEventListener("click", bodyClick);
});

onUnmounted(() => {
  clearWatch();
  clearReceive();
  bus.off("controllist", controllist);
  document.removeEventListener("click", bodyClick);
});
</script>

<template>
  <div class="chat-header" :class="{ IDERef }" v-if="!resize.webWideRef.value" >
    <div class="left flex">
      <avater :size="IDERef ? '20px' : '28px'" text="" />
      <div class="headerlist flex" v-if="IDERef">
        <div
          class="h-item"
          v-for="item in hlist"
          :key="item.value"
          @click="tItemClick(item)"
          :class="{ select: select == item.value && item.value == routerName }"
        >
          {{ item.label }}
          <img
            v-if="item.beta"
            class="beta"
            src="@/assets/svg/beta.svg"
            alt=""
          />
        </div>
      </div>
      <div class="user_info flex" v-if="useLogin.user && IDERef">
        <div class="coins" @click="toCoins">🧄 {{ useLogin.user.coins }}</div>
        <img
          class="user_avater"
          @click="toUser"
          :src="useLogin.user.avatar"
          alt=""
        />
      </div>
    </div>
    <div class="right" v-if="!props.hideRight">
      <slot name="right">
        <n-popover
          placement="left-start"
          :show="showItems"
          :style="{
            padding: '4px',
            'border-radius': '0px',
            'background-color': 'var(--tp_userList_bgcolor)',
          }"
        >
          <template #trigger>
            <div>
              <div
                v-if="showMlist"
                class="MdList hover_light hover_color"
                tabindex="0"
                @click.stop="showList"
                :style="{ cursor }"
              >
                <span class="iconfont icon-option-horizontal"></span>
                <div v-if="isMoreDot" class="dot"></div>
              </div>
            </div>
          </template>
          <div class="listwrapper">
            <UserList :show="true" @update:show="(e) => (showItems = e)" />
          </div>
        </n-popover>
      </slot>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.chat-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 10px;
  height: 52px;
  background-color: var(--tp_headerbgcolor);
  &.IDERef {
    height: 36px;
    border-bottom: 2px solid var(--tp_headerbgcolor);
    .avater_wrapper {
      img {
        width: 20px;
        height: 20px;
      }
    }
  }
  & > .left {
    flex: 1;
    .headerlist {
      flex: 1;
      padding: 0 5px;
      color: var(--tp_header_list_color);
      .h-item {
        cursor: pointer;
        line-height: 36px;
        margin: 0 15px;
        padding-top: 2px;
        position: relative;
        border-bottom: 2px solid transparent;
        &.select {
          border-bottom-color: #007acc;
        }
        .beta {
          position: absolute;
          top: 2px;
          right: -16px;
          width: 16px;
          height: 16px;
        }
      }
    }
    .user_info {
      align-items: center;
      .coins {
        cursor: pointer;
        white-space: nowrap;
        font-weight: lighter;
      }
      min-width: 20px;
      color: var(--tp_textcolor);
      img.user_avater {
        width: 20px;
        height: 20px;
        margin: 0 10px;
        cursor: pointer;
        border-radius: 50%;
      }
    }
  }
  & > .right {
    cursor: pointer;
    position: relative;

    .xicon {
      color: var(--tp_textcolor);
    }

    .MdList {
      width: 30px;
      height: 30px;
      text-align: center;
      line-height: 28px;
      position: relative;

      .iconfont {
        color: var(--tp_textcolor);
        font-size: 28px;
      }

      img {
        width: 28px;
        height: 24px;
      }
    }

    .listwrapper {
      width: 150px;
      position: absolute;
      top: 0;
      right: 30px;
      z-index: 10;
    }
  }
}
</style>
