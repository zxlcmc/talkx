<template>
  <div class="userPgee tpage">
    <div class="top_bar flex_b flex_sx_center">
      <div class="flex_sx_center back hover_bg" tabindex="0" @click="back">
        <span class="iconfont icon-rollback" style="font-size: 20px"></span>
        <span class="text" style="margin-left: 5px">返回</span>
      </div>
      <div class="flex_sx_center back hover_bg" tabindex="0" @click="loginOut">
        <span class="iconfont icon-logout" style="font-size: 20px"></span>
      </div>
    </div>
    <div class="content">
      <div class="avater">
        <Upload
          class="avater_upload"
          :imgs="avater"
          listType="text"
          :showFileList="false"
          :style="upLoadStyle"
          @success="successAvater"
        >
          <NUploadTrigger #="{ handleClick }" abstract>
            <img
              v-show="avater"
              class="avater_img"
              :src="avater"
              alt=""
              @click.stop="handleClick"
            />
          </NUploadTrigger>
        </Upload>
      </div>
      <TList :list="listArr" @rightClick="rightClick" />
    </div>
  </div>
</template>

<script setup>
import { NUploadTrigger } from "naive-ui";
import { useRoute, useRouter } from "vue-router";
import {
  computed,
  inject,
  onMounted,
  onUnmounted,
  reactive,
  ref,
  getCurrentInstance,
} from "vue";
import { useToast } from "@/hooks/useToast";
import { useLoginStore, useChatStore, useHomeRecordStore } from "@/store";
import TList from "@/components/TList/index.vue";
import Upload from "@/components/Upload/index.vue";

const resize = inject("resize");
const avater = ref("");
const route = useRoute();
const router = useRouter();
const useChat = useChatStore();
const useLogin = useLoginStore();
const homeRecordStore = useHomeRecordStore();
const cxt = getCurrentInstance();
const bus = cxt.appContext.config.globalProperties.$bus;
const { showToast, closeWatch, clearWatch } = useToast();
const listArr = reactive([
  { id: 1, type: "name", text: "名字", rightText: "未设置" },
  { id: 2, type: "phone", text: "手机号码", rightText: "" },
  { id: 3, type: "email", text: "电子邮箱", rightText: "未设置" },
  { id: 4, type: "faceback", text: "反馈与建议", rightText: "我有话想说" },
  { id: 5, type: "aboutus", text: "协议条款", rightText: "" },
  { id: 6, type: "", text: "蒜粒", tag: true },
  {
    id: 7,
    type: "invite_rewards",
    text: "邀请奖励",
    rightText: "",
    dot: computed(() => !homeRecordStore.get("invite_rewards")),
  },
  {
    id: 8,
    type: "my_hashrate",
    text: "我的蒜粒",
    rightText: "",
    dot: computed(() => !homeRecordStore.get("my_hashrate")),
  },
  { id: 9, type: "hashrate_record", text: "蒜粒记录", rightText: "" },
  { id: 10, type: "", text: "模型设置", tag: true },
  { id: 11, type: "model", text: "默认模型", rightText: "" },
  { id: 12, type: "", text: "代理设置", tag: true },
  { id: 13, type: "proxy_base_url", text: "代理地址", rightText: "" },
  { id: 14, type: "api_key", text: "ApiKey", rightText: "" },
]);
const upLoadStyle = {
  "--n-dragger-border": "0px",
  "--n-dragger-border-hover": "0px",
  "--n-item-text-color-error": "rgba(0,0,0,0)",
  "--n-item-text-color-success": "rgba(0,0,0,0)",
  "--n-item-border-image-card-error": "0px",
  "--n-dragger-color": "rgba(0,0,0,0)",
};

const back = () => {
  router.push({ name: route.query.origin || "dialogue" });
};
const rightClick = ({ type, tag }) => {
  if (!tag) {
    if (["invite_rewards", "my_hashrate"].includes(type)) {
      homeRecordStore.set({ key: type });
    }
    let name = "userEdit";
    if (!resize.smallRef.value) {
      name = "d_" + name;
    }
    router.push({ name, query: { type } });
  }
};

const setValue = (params) => {
  const keys = Object.keys(params);
  listArr.forEach((obj, i) => {
    if (keys.includes(obj.type)) {
      listArr[i].rightText = params[obj.type];
    }
    if (params.apiKey) {
      listArr.find((c) => c.type == "api_key").rightText = "已设置";
    }
    if (params.phoneNum) {
      listArr.find((c) => c.type == "phone").rightText = params.phoneNum;
    }
    if (params.inviteCode) {
      listArr.find((c) => c.type == "invite_rewards").rightText =
        params.inviteCode;
    }
    if (params.proxyBaseUrl) {
      listArr.find((c) => c.type == "proxy_base_url").rightText =
        params.proxyBaseUrl;
    }
    if (params.coins) {
      listArr.find((c) => c.type == "my_hashrate").rightText =
        params.coins + "粒";
    }
  });
};

const successAvater = ({ imgs, url }) => {
  avater.value = url;
  useLogin.setUser({ field: "avatar", value: url });
};

const loginOut = () => {
  showToast("logout");
};

onMounted(() => {
  if (!useLogin.loginStatus) {
    return router.push({ name: "dialogue" });
  }
  useLogin.getUser(true).then((res) => {
    setValue(res);
    avater.value = res.avatar;
  });

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
        router.push({ name: "dialogue", query: { type: "add", reload: 1 } });
      }
    }
  });
});

onUnmounted(() => {
  clearWatch();
});
</script>

<style lang="scss">
.userPgee {
  height: 100%;
  overflow-y: auto;
  background: var(--tp_dl_bg_color);

  .top_bar {
    padding: 10px;
    background-color: var(--tp_footer_bgcolor);

    .back {
      cursor: pointer;
      padding: 0 10px 0 5px;
    }
  }

  .avater {
    height: 70px;
    padding: 30px 0;
    text-align: center;

    .avater_upload {
      width: auto;
      display: inline-block;
    }

    .avater_img {
      cursor: pointer;
      width: 64px;
      height: 64px;
      border-radius: 50%;
    }
  }

  .content {
    overflow-y: auto;
    padding-bottom: 30px;
    box-sizing: border-box;
    height: calc(100% - 52px);
  }
}
</style>
