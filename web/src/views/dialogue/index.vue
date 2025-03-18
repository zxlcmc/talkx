<script setup>
import Buttom from "./comp/buttom.vue";
import UserRow from "./comp/UserRow.vue";
import NewTopic from "./comp/NewTopic.vue";
// import Recorder from "./comp/Recorder.vue";
import VerbalChat from "./comp/VerbalChat.vue";
import FriendList from "./comp/FriendList.vue";
import AsideRouter from "./comp/AsideRouter.vue";
import NewDialogue from "./comp/NewDialogue.vue";
import MessageBody from "./comp/MessageBody.vue";
import ContentRouter from "./comp/ContentRouter.vue";
import NewDialoguePc from "./comp/NewDialoguePc.vue";
import Header from "@/views/chat/comp/header.vue";

import { useMessage } from "naive-ui";
import { Icon } from "@vicons/utils";
import { MdAdd } from "@vicons/ionicons4";
import {
  computed,
  nextTick,
  onMounted,
  onUnmounted,
  ref,
  watch,
  reactive,
  getCurrentInstance,
  watchEffect,
  inject,
  onActivated,
} from "vue";
import { useScroll } from "@/hooks/useScroll";
import { useRouter, useRoute } from "vue-router";
import { fetchChatAPIProcess, shareMsg } from "@/api/chat";

import { delay } from "@/common/utils";
import { copyToClip } from "@/common/copy";
import { reload } from "@/common/ideUtils";
import { useToast } from "@/hooks/useToast";
import { ROLE_TYPE } from "@/common/config";
import { isMobile } from "@/common/utils";
import { commomandHander } from "@/common/commomand";
import { useScrollBottom } from "@/hooks/useScrollBottom";
import { useCommunication } from "@/hooks/useCommunication";
import "./compositions/chatAudio";
import { moveAnimate, moveColorBar } from "./compositions/moveAnimate";
import { fileList, close as fileClear } from "./compositions/buttom/fileList";
import chatLogic from "./chat";
const cxt = getCurrentInstance();
const bus = cxt.appContext.config.globalProperties.$bus;
const resize = inject("resize");
const {
  historyRef,
  addMsgGrounp,
  useChat,
  useLogin,
  isNew,
  newChatType,
  messageList,
  saveChatMsg,
  getChatMsg,
  delChatMsg,
  createChat,
  delChat,
  newType1Data,
  isNewTopic,
  cellect,
} = chatLogic;

const { receive, send, clearReceive } = useCommunication();
const { showToast, closeWatch, clearWatch, uid } = useToast();

let temp_insert_code = ""; // 插入代码临时保存
let isSaveEnd = false; // 在保存回答中, 防止出现chat完成时 点击的停止回话
let needSaveQuize = { need: false };

const route = useRoute();
const router = useRouter();
const message = useMessage();
const buttomC = ref(null);
const loading = ref(false);
const vShow = ref(true);
let noMoreData = ref(false);
const showShareArea = ref(false);
const sending = ref(useChat.sending);
const initSize = 50; //  测试数值，实际数值 50
const totalRef = ref(0);
const params = reactive({ current: 1, size: initSize });
const { scrollRef, scrollToBottom, scrollToBottomIfAtBottom } = useScroll();
const { scolllDom, scroll, addScrollFun } = useScrollBottom({
  listDom: scrollRef,
  type: "top",
});

const bodyClick = () => (showShareArea.value = false);
// setTimeout(()=>{
//   useChat.setSending(true);
//   console.log('回答状态');
// }, 1)

window.useChat = useChat
watch(sending, () => {
  useChat.setSending(sending.value);
  scrollToBottom();
  if (!sending.value) {
    console.log("回答完成，更新算力信息");
    useLogin.getUser(true);
  }
});

const getFoucs = () => {
  if (showVerbal.value && !["dialogue"].includes(route.name)) return;
  nextTick(() => buttomC.value && buttomC.value.textareaUp(true, true));
};

const voiceChat = computed(() => {
  return useChat.friends.find((f) => f.voiceChat) || {};
});

watch(
  () => route.query.fid,
  (val) => val && getFoucs() // 在没有fid的情况下不聚焦，应为在iphone 14pro 上会出现 聚焦光标会显示在最上层
);

watch(
  () => route.query.id,
  (val, old) => {
    if (route.query.id) {
      totalRef.value = 0;
      if (["temp"].includes(val)) {
        addNew(route.query.nType ? 1 : 0);
      } else {
        initData();
      }
    }
  }
);

// 弃用
const showVerbal = computed(() => {
  return false; //isMobile() && useChat.currFriend == voiceChat.value.id;
});

let controller = new AbortController();
const cursor = computed(() => (sending.value ? "no-drop" : "pointer"));

// IDE环境指定好友id
const IDEDefaultFriendsId = () => 1;

// 创建新的会话
const addNew = (type = 0) => {
  console.log("addNew", type);
  if (sending.value || !["dialogue", "d_history"].includes(route.name)) return;
  newChatType.value = type;
  const next = (center = () => {}) => {
    useChat.triggerChat("");
    center();
    let fid = useChat.currFriend;
    // 处理上一次修改 IDE默认好友4 问题
    if (resize.IDERef.value) {
      fid = IDEDefaultFriendsId(); //useChat.friends[0].id;
      useChat.setFriend(fid);
    }
    const query = { id: useChat.getCurrUid, fid };
    router.replace({ query });
    // console.log("replace", { id: useChat.getCurrUid, fid });
    setTimeout(() => getFoucs(), 200);
  };
  if (newChatType.value == 1) {
    if (isNewTopic.value) return; // 已经是新话题，不在添加
    isNewTopic.value = true;
    // 刷新情况下 新话题变成了新会话-不影响
    // moveAnimate(scrollRef, historyRef).then(({ recover }) => {
    moveColorBar(scrollRef).then(({ recover }) => {
      next(() => {});
      useChat.updateChatInfo(useChat.currUid, { isNewTopic: true });
      if (!useChat.isWeb) {
        newType1Data.value.forEach(({ chat }) =>
          chat.forEach((msg) => useChat.addMsg(msg))
        );
      }
      console.log("dddd");
      // 数据替换
      recover();
      newChatType.value = 0;
    });
  } else {
    next();
  }
};

let isreload = false;
const reloadPage = async (isRelaod) => {
  const { reload: qreload, id, fid } = route.query;
  if ((qreload || isRelaod) && !isreload) {
    isreload = true;
    const query = { id, fid };
    await router.replace({ query });
    setTimeout(() => {
      reload();
      console.log("刷新了");
    }, 10);
  }
};

// 当连接上出现 reload 就刷新
watch(
  () => route.query.reload,
  () => {
    reloadPage();
  }
);

const eventBus = async ({ type, payload }) => {
  if (type === "logout") {
    addNew();
  } else if (type === "getData") {
    await initData(payload);
  }
};
const friendChange = () => {
  isNewTopic.value = false;
  scrollToBottom();
};

const getData = async ({ type, noScroll, query = {} } = {}) => {
  if (loading.value || !["dialogue"].includes(route.name)) return;
  loading.value = true;
  console.log("ddd", query.id, route.query.id, useChat.getCurrUid);
  const sessionId = query.id || route.query.id || useChat.getCurrUid;
  let friendId = query.fid || route.query.fid || useChat.currFriendInfo.id;
  useChat.changeUid(sessionId);
  useChat.setFriend(friendId);
  // console.log("params", type, params);
  const { current, pages, records, total, noData } = await getChatMsg({
    sessionId,
    friendId,
    ...params,
  });
  totalRef.value = total + 2;
  if ((records.length <= 0 && params.current == 0) || noData) {
    loading.value = false;
    return addNew();
  }
  if (type == "unshift") {
    const first = useChat.currPuseMsg[0];
    let firstTop = 0;
    if (first) {
      const dom = document.querySelector(`[msg-id="${first.id}"]`);
      firstTop = dom.offsetTop;
    }
    useChat.unshiftChat(sessionId, records.reverse());
    if (first) {
      nextTick(() => {
        // 滚动到当前数据位置, 获取消息id, 滚动到对应位置
        const dom = document.querySelector(`[msg-id="${first.id}"]`);
        scrollRef.value.scrollTop =
          scrollRef.value.scrollTop + (dom.offsetTop - firstTop);
      });
    }
  } else {
    useChat.updateChat(sessionId, records.reverse());
  }
  noMoreData.value =
    current >= pages || useChat.currChat.message.length >= total;
  if (!noScroll && !["unshift"].includes(type)) {
    await scrollToBottom();
  }
  loading.value = false;
};

const initData = async (args) => {
  // 处理上一次修改 IDE默认好友4 问题
  if (resize.IDERef.value) {
    let fid = IDEDefaultFriendsId(); //useChat.friends[0].id;
    useChat.setFriend(fid);
    router.replace({
      query: { id: useChat.getCurrUid, fid },
    });
  }

  params.current = 1;
  params.size = totalRef.value || initSize;
  await getData({ ...args, type: "new" });
  params.size = initSize;
};

useChat.setFriend(route.query.fid);

const scrollTop = () => {
  if (loading.value || noMoreData.value) return;
  params.current++;
  getData({ type: "unshift" });
};

const ideMsg = async (params) => {
  const { key, data, prompt } = params;
  if (sending.value) return;
  console.log("receive", key, prompt);
  if (["show-message-dialog"].includes(key)) {
    sending.value = false;
    showToast("feature", data.message);
    return;
  }
  // 插入代码到输入框
  if (["insert-code-to-chat"].includes(key)) {
    let { assemble, text } = prompt;
    // if (useChat.message) { text = useChat.message + '\n' + text }
    // if ( buttomC.value ) { buttomC.value.setText("") }
    if (buttomC.value) {
      temp_insert_code = "```\n" + text + "\n```\n";
      buttomC.value.showInsetArea(assemble, () => {
        temp_insert_code = "";
      });
      setTimeout(() => getFoucs(), 300);
    }
    sending.value = false;
    return;
  }
  sending.value = true;
  addMsgGrounp({ content: prompt });
  const { isUpDate } = await answerFun({ prompt, key, isFeature: true });
  // 更新编辑器消息最后一个回答
  if (isUpDate) {
    // 出来未登录情况下，和回退
    useChat.updataMsg({
      updataObj: { editer: data },
      index: useChat.currChat.message.length - 1,
    });
  }
  console.log("ide 发送消息 完成");
  sending.value = false;
};

let shareUrl = ref("");
onMounted(async () => {
  if (route.query.reload) {
    reloadPage();
    return;
  }

  const id = route.query.id || useChat.getCurrUid;
  const type = route.query.type;
  const nType = route.query.nType;
  if (route.query.inviteCode) {
    localStorage.setItem("dialogue_inviteCode", route.query.inviteCode);
  }
  if (type === "add" || !id || id == "temp") {
    addNew(nType ? 1 : 0);
  } else if (id) {
    useChat.triggerChat(id);
    useChat.getCurrUid !== "temp" && initData();
  }
  sending.value = false;
  scrollToBottom();
  addScrollFun(scrollTop);
  bus.on("ide_message", ideMsg);
  bus.on("dialogue_event", eventBus);
  bus.on("friendChange", friendChange);
  document.addEventListener("click", bodyClick);

  // 等待聊天界面加载完成之后在添加容器阴影
  const root = document.querySelector("#app");
  root.classList.add("shadow");
});

onUnmounted(() => {
  isNewTopic.value = false;
  clearWatch();
  bus.off("ide_message", ideMsg);
  bus.off("dialogue_event", eventBus);
  bus.off("friendChange", friendChange);
  document.removeEventListener("click", bodyClick);
  if (sending.value) {
    controller.abort();
  }
});

const upDateQuery = async (center = () => {}) => {
  const query = {
    id: useChat.getCurrUid,
    fid: route.query.fid || useChat.currFriend,
  };
  await router.replace({ query });

  // await initData();
  scrollToBottom();

  await center();
  setTimeout(() => {
    getFoucs();
  }, 300);
};

const saveEnd = async () => {
  if (isSaveEnd) return;
  isSaveEnd = true;
  const index = useChat.currMsg.length - 1;
  const end = useChat.currMsg[index];
  if (end && end.role == "assistant") {
    const { content, role, sessionId = useChat.getCurrUid, status } = end;
    const params = { content, role, sessionId, status };
    useChat.updataMsg({ updataObj: { loading: false }, index });
    // await saveChatMsg(params);
    await saveChatMsg(params).then((res) => {
      useChat.updataMsg({ updataObj: res, index });
    });
  }
  await upDateQuery(async () => {
    // 更新echart回答，回答完成之后显示图表
    const answer = useChat.currChatLastAnswer();
    if (answer) {
      const isEcharts = answer.content.includes("```echarts");
      if (isEcharts) {
        useChat.updataMsg({
          updataObj: { showEchart: true },
          index: useChat.currMsg.length - 1,
        });
      }
    }
  });
  isSaveEnd = false;
};

const stop = () => {
  if (!sending.value) return;
  const next = async () => {
    if (needSaveQuize.need) {
      await saveQuize(); // 保证先保存提问
    }
    saveEnd();
  };
  next();
  controller.abort();
  sending.value = false;
};

// 回归提问
const regressionQuize = () => {
  useChat.removeChatMsgEnd();
  const quize = useChat.removeChatMsgEnd();
  buttomC.value && buttomC.value.setText(quize[0].content);
};

// 功能操作
const featureHandler = async (type, text) => {
  const [, sendKey, featureKey] = type.split(":");
  // 中英翻译处理, 有内容，直接发送消息
  if (
    ["language-translation", "generate-echarts"].includes(featureKey) &&
    useChat.message
  ) {
    sending.value = true;
    addMsgGrounp({ content: useChat.message });
    // useChat.addMsg({ role: 'user', content: useChat.message })
    // useChat.addMsg({ role: "assistant", content: '' })
    buttomC.value && buttomC.value.setText("");
    await answerFun({ prompt: useChat.message, key: featureKey });
    sending.value = false;
    return;
  }
  const sendErr = (code, message) => {
    sending.value = false;
    showToast("feature", message);
  };
  send(sendKey, featureKey, () => {}, sendErr);
};

const saveQuize = async () => {
  const { noSaveUser } = needSaveQuize;
  needSaveQuize = { need: false };
  const index = useChat.currMsg.length - 2;
  const lastQu = useChat.currMsg[index];
  if (lastQu && lastQu.role == "user" && !noSaveUser) {
    const {
      content,
      role,
      sessionId = useChat.getCurrUid,
      attachments,
    } = lastQu;
    const params = { content, role, sessionId, attachments };
    // await saveChatMsg(params);
    await saveChatMsg(params).then((res) => {
      useChat.updataMsg({ updataObj: res, index });
    });
  }
};

const answerFun = async ({ prompt, key, isFeature, noSaveUser }) => {
  controller = new AbortController();

  scrollToBottom();
  setTimeout(() => scrollToBottom());

  if (useChat.currChat.temp) {
    try {
      const title = prompt.slice(0, 32).trim();
      const { id } = await createChat({
        title,
        friendId: useChat.currFriendInfo.id,
      });
      useChat.changeUid(id);
    } catch (err) {
      console.log("创建新话题错误", err);
      return;
    }
  }
  isNewTopic.value = false;

  needSaveQuize.need = true; // 需要保存提问
  needSaveQuize.noSaveUser = noSaveUser;

  // 如果有文件信息
  if (fileList.value.length) {
    const endIndex = useChat.currMsg.length - 2;
    const lastQu = useChat.currMsg[endIndex];
    lastQu.attachments = fileList.value.map(({ url, name, suffix, size }) => {
      return { name, mimeType: suffix, size, url };
    });
    fileClear();
    useChat.updataMsg({ updataObj: lastQu, index: endIndex });
  }

  return new Promise((resolve, reject) => {
    const sessionId = useChat.currChat.temp ? "" : useChat.getCurrUid;
    const productId = useChat.productId;
    // 排除问候语，和异常回答

    let messages = useChat.currChat.message.filter(
      (m) => !(m.welcome || [500].includes(m.status))
    );
    const lastQu = useChat.currMsg[useChat.currMsg.length - 2];
    const end = messages[messages.length - 1];
    // 如果最后一条是空的, 且是添加的空回答，就不添加到参数里面
    if (!end.id && !end.content) {
      messages.pop();
    }
    messages = messages.map(({ role, content, attachments }) => ({
      role,
      content,
      attachments,
    }));
    // 排除数据，不能超过32条
    if (messages.length > 32) {
      messages.splice(0, 32 - messages.length);
    }
    // 如果是通过功能区提问的，只传递提问消息
    if (isFeature) {
      messages = [lastQu];
    }
    let roleType = key || ROLE_TYPE.coding_assistant;
    // if (productId == PRODUCT_TYPE.universal) { roleType = ROLE_TYPE.universal }
    if (resize.webRef.value) {
      roleType = useChat.currFriendInfo.roleType;
    }
    useLogin.dot({ mt: 2, product_id: productId, role_type: roleType });

    const config = {
      options: { roleType: roleType, productId, sessionId, messages },
      signal: controller.signal,
      controller,
      interruptClear: () => {},
      onDownloadProgress: (response) => {
        try {
          let isUpDate = true;
          let content = useChat.currChatLastAnswer().content;
          const body = response.body || response._bodyBlob.stream();
          const reader = body.getReader();
          async function process() {
            try {
              const { done, value } = await reader.read();
              if (done) {
                let saveQuizePromise = async () => {};
                if (response.status === 403) {
                  regressionQuize();
                  showToast("login", content);
                  isUpDate = false;
                } else {
                  if (response.status == 400) {
                    message.error(content);
                    saveQuizePromise = saveQuize();
                    useChat.removeChatMsgEnd();
                    await saveQuizePromise;
                    await upDateQuery();
                  } else if (response.status == 500) {
                    useChat.updataMsg({
                      updataObj: { status: 500 },
                      index: useChat.currChat.message.length - 1,
                    });
                    await saveQuize();
                    await saveEnd();
                  } else {
                    await saveQuize();
                    await saveEnd();
                  }
                }
                resolve({ isUpDate, response });
                return;
              }
              content += new TextDecoder("utf-8").decode(value);
              if (response.status !== 400) {
                useChat.updataMsg({
                  updataObj: { content, loading: false },
                  index: useChat.currChat.message.length - 1,
                });
                scrollToBottomIfAtBottom();
              }
              process();
            } catch (err) {
              console.log("process-err", err);
              resolve(false);
            } finally {
            }
          }
          process();
        } catch (err) {
          console.log("onDownloadProgress-err", err);
          if (err.message == "BodyStreamBuffer was aborted") {
            console.log("回答中-中断");
            config.interruptClear();
          }
          reject(err);
        }
      },
      timeOut: async () => {
        // 保存提问，并把提问内容放到输入框中
        message.error("请求超时");
        useChat.updataMsg({
          updataObj: { loading: false },
          index: useChat.currChat.message.length - 1,
        });
        await saveQuize();
        await upDateQuery(() => {
          const quize = useChat.currMsg[useChat.currMsg.length - 1];
          buttomC.value && buttomC.value.setText(quize.content);
        });
        sending.value = false;
      },
    };
    fetchChatAPIProcess(config).catch((e) => {
      if (e.message == "The user aborted a request.") {
        console.log("未回答-中断");
        config.interruptClear();
      }
      return Promise.reject(e);
    });
  });
};

const buttomControl = ({ type, value }) => {
  if (type === "verbal") {
    vShow.value = value;
  }
};

const buttomSend = async ({ type, text, over = () => {} }) => {
  if (type.startsWith("feature")) {
    await featureHandler(type, text);
    return;
  }
  sending.value = true;
  if (temp_insert_code) {
    // 拼接插入代码文案
    text = temp_insert_code + text;
    temp_insert_code = "";
    if (buttomC.value) {
      buttomC.value.hideInsetArea();
    }
  }
  addMsgGrounp({ content: text });
  isNew.value = false;
  buttomC.value && buttomC.value.setText("");
  const rule = [
    {
      type: "reload",
      handler: () => reload(),
    },
    {
      type: "show_version",
      handler: () => {
        showToast("update_system");
        closeWatch("update_system", async (confirm) => {
          if (confirm) {
            reload();
          }
        });
      },
    },
    {
      type: "info",
      handler: (content) => {
        useChat.updataMsg({
          updataObj: { content, loading: false },
          index: useChat.currChat.message.length - 1,
        });
        scrollToBottomIfAtBottom();
      },
    },
  ];

  let result;
  // 特殊指令处理
  if (!commomandHander(text, rule)) {
    result = await answerFun({ prompt: text });
  }
  sending.value = false;
  over(result);
};

// 点击后将最后一次提问的内容覆盖到提问输入框中；
const buttomBack = async () => {
  const endSession = messageList.value[messageList.value.length - 1];
  if (endSession) {
    buttomC.value && buttomC.value.setText(endSession.chat[0].content);
  }
};

const toHistory = () => {
  if (sending.value) return;
  const params = { name: "history" };
  if (!resize.smallRef.value) {
    params.name = "d_history";
  }
  router.push(params);
};

const messageChange = async ({ type, item, e }) => {
  switch (type) {
    case "copyQuize":
      buttomC.value && buttomC.value.setText(item.chat[0].content);
      break;
    case "reanswer": // 重新回答
      sending.value = true;
      const text = item.chat[0].content;
      await delmsg(item.chat[1]);
      useChat.addMsg({ role: "assistant", content: "", loading: true });
      await answerFun({ prompt: text, noSaveUser: true });
      sending.value = false;
      break;
    case "collect":
      cellect(item, e);
      break;
  }
};

const delmsg = async (msg) => {
  const params = { messageId: msg.id };
  // await delChatMsg(params);
  // await initData({ noScroll: true });
  delChatMsg(params);
  useChat.removeMsg(msg);
};

// 删除当前会话,开一个新会话
closeWatch("del", async (confirm) => {
  if (confirm) {
    await delChat();
    addNew();
    buttomC.value && buttomC.value.setText("");
  }
});

const del = () => {
  if (sending.value) return;
  showToast("del");
};

const share = async () => {
  // 复制不能异步
  const res = await shareMsg({ sessionId: useChat.getCurrUid });
  shareUrl.value = res.shareUrl;
  showToast("copy", shareUrl.value, "", "", {
    successText: "分享链接复制成功！",
  });
};
const buttomKeyDownEvent = ({ type }) => {
  switch (type) {
    case "addNew1": // 打开新会话 样式1
      if (isNew.value) return;
      addNew(1);
      break;
    case "delCurrChat": // 删除当前会话
      if (isNew.value || isNewTopic.value) return;
      del();
      break;
    default:
      break;
  }
};
</script>

<template>
  <div
    class="dialogue tpage"
    :class="{ pc: !resize.smallRef.value, small: resize.smallRef.value }"
  >
    <div class="aside" v-if="!resize.smallRef.value">
      <Header :hideRight="!resize.smallRef.value" />
      <div
        class="friendList_wrapper"
        :style="{
          height: resize.webWideRef.value
            ? `calc(100% - 60px)`
            : `calc(100% - 52px - 60px)`,
        }"
      >
        <FriendList />
      </div>
      <UserRow />
    </div>
    <div class="main" :class="{ web: !resize.smallRef.value }">
      <ContentRouter />
      <template v-if="!(route.meta.contentRouter && !resize.smallRef.value)">
        <div class="d_content" :style="{ height: '1%' }">
          <div class="top_bar flex_b flex_sx_center">
            <div class="flex">
              <div
                class="del hover_bg"
                tabindex="0"
                @click="del"
                :style="{ cursor }"
              >
                <span
                  class="iconfont icon-delete"
                  v-show="!isNew && !isNewTopic"
                ></span>
              </div>
              <div
                v-if="useLogin.isLogin"
                class="share hover_bg"
                tabindex="1"
                :style="{ cursor }"
                @click.stop="showShareArea = !showShareArea"
              >
                <span
                  class="iconfont icon-share"
                  v-show="!isNew && !isNewTopic"
                ></span>
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
                :class="{ isNewTopic: !!isNewTopic }"
                v-show="!isNew"
                @click="addNew(1)"
                :style="{ cursor }"
              >
                <Icon size="30">
                  <MdAdd />
                </Icon>
              </div>
            </div>
          </div>
          <div class="msg_content_wrapper">
            <div
              class="message_wrapper"
              id="scrollRef"
              ref="scrollRef"
              @scroll.passive="scroll"
            >
              <div
                ref="scolllDom"
                class="message_ul"
                :style="{ height: isNew ? 'calc(100% - 20px)' : 'auto' }"
              >
                <MessageBody
                  v-for="(msg, i) in messageList"
                  :key="msg.id"
                  :item="msg"
                  v-model:sending="sending"
                  @change="messageChange"
                  @delmsg="delmsg"
                  :end="i == messageList.length - 1"
                />
                <!-- <NewDialogue
                  class="NewDialogue"
                  v-if="isNew && !useChat.isWeb"
                  v-model:sending="sending"
                  @send="buttomSend"
                /> -->
                <NewDialoguePc
                  class="NewDialogue"
                  v-if="isNew && !sending"
                  @send="buttomSend"
                />
              </div>
            </div>
            <div class="newTopic_wrapper">
              <NewTopic v-show="newChatType == 1" />
            </div>
          </div>
        </div>
        <Buttom
          ref="buttomC"
          :showVerbal="showVerbal"
          :vShow="vShow"
          v-model:sending="sending"
          @send="buttomSend"
          @control="buttomControl"
          @stop="stop"
          @back="buttomBack"
          @keydownEvent="buttomKeyDownEvent"
        />
      </template>
    </div>
    <verbal-chat
      @send="buttomSend"
      v-if="showVerbal && vShow"
      v-model:show="vShow"
    />
    <!-- <Recorder @send="buttomSend" v-if="showVerbal" /> -->

    <AsideRouter />
  </div>
</template>

<style lang="scss" scoped>
.dialogue {
  width: 100%;
  height: 100%;
  display: flex;
  overflow: hidden;

  &.pc {
    .main {
      .top_bar {
        background-color: transparent;
      }
    }
  }

  .aside {
    width: 206px;
    /* overflow: hidden; */
    transition: all 0.3s;

    .friendList_wrapper {
      overflow-y: auto;
    }
  }

  .main {
    display: flex;
    width: 100%;
    flex-direction: column;
    background-color: var(--tp_dl_bg_color);

    &.web {
      width: calc(100% - 206px);
    }
  }

  .d_content {
    flex: 1;
    background-color: var(--tp_dl_bg_color);

    .top_bar {
      height: 52px;
      padding: 10px;
      align-items: center;
      align-content: center;
      box-sizing: border-box;
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

    .msg_content_wrapper {
      height: calc(100% - 50px);
      max-height: calc(100% - 50px);
      & > div {
        .message_ul {
          margin: 0 auto;
          max-width: var(--msg_content-width);
        }
      }
      .newTopic_wrapper,
      .message_wrapper {
        position: relative;
        overflow-y: auto;
        box-sizing: border-box;
        height: 100%;
      }
      .message_wrapper {
        z-index: 1;
      }
      .newTopic_wrapper {
        position: relative;
        /* top: calc(-100% + 50px); */
        top: calc(-100%);
      }
    }
  }
}

@media (max-width: 600px) {
  .dialogue {
    .aside {
      width: 0;
    }

    .main {
      width: 100%;

      &.web {
        width: 100%;
      }
    }
  }
}
</style>
