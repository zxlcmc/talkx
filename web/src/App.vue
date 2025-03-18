<script setup>
import Toast from "@/components/Toast/index.vue";
import LoadingPress from "@/components/LoadingPress/index.vue";
import MessageModel from "@/components/MessageModel/index.vue";
import { useRoute, useRouter } from "vue-router";
import {
  onMounted,
  onUnmounted,
  provide,
  getCurrentInstance,
  reactive,
  watchEffect,
} from "vue";
import useResize from "@/hooks/useResize";
import { useToast } from "@/hooks/useToast";
import { getEnvironment } from "@/common/env";
import { useLoadingPress } from "@/hooks/useLoadingPress";
import { useChatStore, useLoginStore } from "@/store";
import { NConfigProvider, NMessageProvider, NDialogProvider } from "naive-ui";
import "@/compositions/initAttr";
import {
  modelObj,
  pinnedNotify,
  closeMessageModel,
} from "@/compositions/notify";
import { initGuide } from "@/compositions/initGuide";
import { initEditer } from "@/compositions/initEditor";
import { initUptate } from "@/compositions/initUptate";
const toast = useToast("App");
const { toastRef, closeToast } = toast;
const { showRef } = useLoadingPress();

const route = useRoute();
const router = useRouter();
const resize = useResize();
const useChat = useChatStore();
const useLogin = useLoginStore();
provide("resize", resize);
const cxt = getCurrentInstance();
const bus = cxt.appContext.config.globalProperties.$bus;

// window.talkxEditorInfo = {"productName":"Visual Studio Code 1.83.1","talkxVersion":"0.1.0","webVersion":"0.0.2","language":"zh-cn","fontSize":13,"fontFamily":"Menlo, Monaco, 'Courier New', monospace","theme":"GitHub Dark Colorblind (Beta)"};

let sharePage = false;
if (!location.hash.startsWith("#/share")) {
  useChat.setfriends();
  pinnedNotify();
} else {
  const root = document.querySelector("#app");
  root.classList.add("share");
}
useChat.changeProductId(getEnvironment());

const onUnmountedFuns = [];
const onUnmountedFun = (fun) => onUnmountedFuns.push(fun);
onMounted(() => {
  initEditer({ route, router, toast, bus, onUnmountedFun });
  useLogin.dot({ mt: 1 });
  initUptate();
  initGuide(resize, route, router);
});

onUnmounted(() => {
  onUnmountedFuns.forEach((fun) => fun());
});

console.log(
  "%cTalkX 使交流变得无限可能",
  "background: linear-gradient(to right, red, orange, indigo, violet); color: white; padding: 5px;"
);
</script>
<template>
  <NConfigProvider style="height: 100%">
    <NMessageProvider>
      <NDialogProvider>
        <router-view />
        <Toast
          v-model:show="toastRef.show"
          v-bind="toastRef"
          @close="closeToast"
        />
        <LoadingPress v-model:show="showRef.show" />
        <MessageModel
          v-model:show="modelObj.show"
          :msg="modelObj.msg"
          @close="closeMessageModel"
        />
      </NDialogProvider>
    </NMessageProvider>
  </NConfigProvider>
</template>

<style scoped></style>
