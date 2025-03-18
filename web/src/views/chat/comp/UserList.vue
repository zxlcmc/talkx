<template>
  <div class="list" v-show="props.show">
    <div class="item flex_sx_center" @click="itemClick('theme')">
      <span class="iconfont icon-theme-light-dark"></span>
      <span class="text">切换{{ useLogin.nextThemeName }}主题</span>
    </div>
    <!-- ide 登录情况下 不显示 个人中心-->
    <div
      class="item flex_sx_center"
      @click="itemClick('login')"
      v-if="!(useLogin.isLogin && resize.IDERef.value)"
    >
      <Icon size="20">
        <MdContact />
      </Icon>
      <span class="text">{{ !isLogin ? "去登录" : "个人中心" }}</span>
      <div v-if="isLoginDot" class="dot"></div>
    </div>
    <div class="item flex_sx_center" @click="itemClick('collect')">
      <span class="iconfont icon-a-controlplatform"></span>
      <span class="text">收藏夹</span>
    </div>

    <div class="item flex_sx_center" @click="itemClick('notify')">
      <span class="iconfont icon-fuhao-tongzhi"></span>
      <span class="text">消息通知</span>
      <div v-if="isNotifyDot" class="dot"></div>
    </div>
    <div class="item flex_sx_center" @click="itemClick('us')">
      <Icon size="20">
        <IosMail />
      </Icon>
      <span class="text">联系我们</span>
    </div>
    <div
      v-if="resize.IDERef.value"
      class="item flex_sx_center"
      @click="itemClick('ordinary')"
    >
      <span class="iconfont icon-chrome"></span>
      <span class="text">TalkX 通用版</span>
    </div>
    <div class="item flex_sx_center" @click="itemClick('about')">
      <Icon size="20">
        <MdHelpCircle />
      </Icon>
      <span class="text">关于 TalkX</span>
    </div>
    <!-- <div
            class="item flex_sx_center"
            @click="itemClick('out')"
            v-if="isLogin"
          >
            <Icon size="20">
              <IosLogOut />
            </Icon>
            <span class="text">退出</span>
          </div> -->
  </div>
</template>

<script setup>
import { computed, inject } from "vue";
import { Icon } from "@vicons/utils";
import { useRouter } from "vue-router";
import {
  useLoginStore,
  useChatStore,
  themeObj,
  useHomeRecordStore,
} from "@/store";
import {
  MdList,
  MdContact,
  IosMail,
  MdHelpCircle,
  IosLogOut,
} from "@vicons/ionicons4";
import { us_url, aboout_url, ordinary_url } from "@/common/config";
const resize = inject("resize");
const props = defineProps(["show"]);
const emit = defineEmits(["update:show"]);
const router = useRouter();
const useChat = useChatStore();
const useLogin = useLoginStore();
const homeRecordStore = useHomeRecordStore();

const isLogin = computed(() => useLogin.isLogin);
const isLoginDot = computed(() => !homeRecordStore.get("header_more_login"));
const isNotifyDot = computed(() => !homeRecordStore.get("header_notify"));

const openUrl = window.__talkx__.openUrl;

let isClick = false;
const itemClick = async (type) => {
  if (isClick) return;
  isClick = true;
  switch (type) {
    case "notify":
      homeRecordStore.set({ key: "header_notify", value: true });
      router.push({ name: "notify" });
      break;
    case "theme":
      useLogin.setTheme(themeObj[useLogin.theme].next);
      break;
    case "collect":
      router.push({
        name: !resize.smallRef.value ? "d_favorites" : "favorites",
      });
      break;
    case "login":
      const inviteCode = localStorage.getItem("dialogue_inviteCode");
      homeRecordStore.set({ key: "header_more_login", value: true });
      const params = { name: !isLogin.value ? "login" : "user" };
      if (params.name == "login" && inviteCode) {
        params.query = { inviteCode };
      } else if (!resize.smallRef.value && params.name == "user") {
        params.name = "d_user";
      }
      router.push(params);
      break;
    case "us":
      openUrl(us_url);
      break;
    case "ordinary":
      openUrl(ordinary_url);
      break;
    case "about":
      openUrl(aboout_url);
      break;
    case "out":
      showToast("logout");
      break;
  }
  isClick = false;
  emit("update:show", false);
};
</script>

<style lang="scss" scoped>
.list {
  /* padding: 5px 0; */
  user-select: none;
  font-size: var(--tp_size);
  color: var(--tp_textcolor);
  background-color: var(--tp_userList_bgcolor);
  /* box-shadow: 1px 1px 5px; */

  .iconfont {
    font-size: 28px;
    color: var(--tp_textcolor);
  }

  .item {
    height: 35px;
    cursor: pointer;
    padding: 5px 10px;
    position: relative;
    box-sizing: border-box;

    &:hover {
      color: var(--tp_aside_hover_color);
      background: var(--tp_userList_hover_bgcolor);
    }

    .dot {
      right: 5px;
      /* right: auto; */
      /* left: 5px; */
    }

    .iconfont {
      font-size: 18px;
      margin-top: -4px;
      margin-right: 2px;
      display: inline-block;
    }

    .text {
      margin-left: 5px;
      vertical-align: top;
    }
  }
}
</style>
