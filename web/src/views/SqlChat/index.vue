<template>
  <div class="sql-chat tpage flex">
    <Header @addNew="addNew" @del="delCurrMsg" @share="share" />
    <div class="center">
      <div
        class="message_wrapper"
        id="scrollRef"
        ref="scrollRef"
        @scroll.passive="scroll"
      >
        <div
          ref="scolllDom"
          v-if="!isNew"
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
        </div>
        <NullDataPage @send="buttomSend" v-if="isNew && isLoad" />
      </div>
    </div>
    <Bottom
      ref="buttomC"
      @stop="stop"
      @send="buttomSend"
      @keydownEvent="buttomKeyDownEvent"
    />
  </div>
</template>

<script setup>
// 只在IDE下支持
import Bottom from "./comp/Bottom.vue";
import Header from "./comp/Header.vue";
import NullDataPage from "./comp/NullDataPage.vue";
import { useMessage } from "naive-ui";
import { copyToClip } from "@/common/copy";
import { reload } from "@/common/ideUtils";
import { useToast } from "@/hooks/useToast";
import { useScroll } from "@/hooks/useScroll";
import { useRoute, useRouter } from "vue-router";
import { commomandHander } from "@/common/commomand";
import { useScrollBottom } from "@/hooks/useScrollBottom";
import { ref, reactive, computed, onMounted, nextTick, onUnmounted } from "vue";
import { fetchChatAPIProcess, shareMsg } from "@/api/chat";
import MessageBody from "@/views/dialogue/comp/MessageBody.vue";
import {
  cellect,
  delChat,
  delChatMsg,
  getChatMsg,
  createChat,
  addMsgGrounp,
  isNew,
  sending,
  messages,
  useChat,
  useLogin,
  useSchema,
  saveChatMsg,
  messageList,
  clearMesages,
  updateAnwser,
  removeAnswer,
  updateMessage,
  getSendMessageData,
} from "./compositions/chat";
const route = useRoute();
const router = useRouter();
const message = useMessage();
const buttomC = ref(null);
const loading = ref(false);
let noMoreData = ref(false);
const initSize = 50;
let sessionId;

const isLoad = ref(false);
let shareUrl = ref("");
let needSaveQuize = { need: false };
let temp_insert_code = "";
let controller = new AbortController();
const params = reactive({ current: 1, size: initSize });
const { showToast, closeWatch, clearWatch } = useToast();
const { scrollRef, scrollToBottom, scrollToBottomIfAtBottom } = useScroll();
const { scolllDom, scroll, addScrollFun } = useScrollBottom({
  listDom: scrollRef,
  type: "top",
});
const id = computed(() => route.query.id);
const type = computed(() => route.query.type);

const getData = async ({ type, noScroll } = {}) => {
  if (loading.value) return;
  loading.value = true;
  sessionId = id.value || useSchema.sessionId;
  useSchema.setSessionId(sessionId);
  if (!id.value) {
    router.replace({ query: { id: sessionId } });
  }
  const { current, pages, records, total } = await getChatMsg({
    sessionId,
    ...params,
  });
  if (type == "unshift") {
    const first = messages.value[0];
    let firstTop = 0;
    if (first) {
      const dom = document.querySelector(`[msg-id="${first.id}"]`);
      firstTop = dom.offsetTop;
    }
    messages.value.unshift(...records.reverse());
    if (first) {
      nextTick(() => {
        // 滚动到当前数据位置, 获取消息id, 滚动到对应位置
        const dom = document.querySelector(`[msg-id="${first.id}"]`);
        scrollRef.value.scrollTop =
          scrollRef.value.scrollTop + (dom.offsetTop - firstTop);
      });
    }
  } else {
    messages.value.push(...records.reverse());
  }

  noMoreData.value = current >= pages;
  if (!noScroll && !["unshift"].includes(type)) {
    await scrollToBottom();
  }
  isLoad.value = true;
  loading.value = false;
};

const initData = async () => {
  params.current = 1;
  params.size = initSize;
  await getData({ type: "new" });
};

// 新会话
const addNew = () => {
  clearMesages();
  isLoad.value = true;
  useSchema.setSessionId("");
  params.current = 1;
  params.size = initSize;
  router.replace({ query: { id: "", type: "add" } });
};

// 删除当前会话
const delCurrMsg = async () => {
  delBefore();
};

const delBefore = () => {
  if (sending.value) return;
  showToast("del");
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

const buttomKeyDownEvent = ({ type }) => {
  switch (type) {
    case "addNew1": // 打开新会话 样式1
      if (isNew.value) return;
      addNew();
      break;
    case "delCurrChat": // 删除当前会话
      console.log("删除当前会话");
      if (isNew.value) return;
      delBefore();
      break;
    default:
      break;
  }
};

const share = async () => {
  // 复制不能异步
  const res = await shareMsg({ sessionId: useSchema.sessionId });
  shareUrl.value = res.shareUrl;
  showToast("copy", shareUrl.value);
};

const upDateQuery = async (center = () => {}) => {
  scrollToBottom();

  await center();
  setTimeout(getFoucs, 300);

  const id = messages.value.find((m) => m.sessionId).sessionId;
  router.replace({ query: { id } });
};

const scrollTop = () => {
  if (loading.value || noMoreData.value) return;
  params.current++;
  getData({ type: "unshift" });
};

const delmsg = async (msg, noTip) => {
  const { errMsg } = (await delChatMsg({ messageId: msg.id })) || {};
  if (errMsg) {
    message.error(errMsg);
  } else {
    !noTip && message.success("删除成功");
  }
};

const getFoucs = () => {
  nextTick(() => buttomC.value && buttomC.value.textareaUp(true, true));
};

const reloadPage = () => {
  console.log('reloadPage');
  setTimeout(() => {
    reload()
  }, 10);
};

let isSaveEnd = false;
const saveEnd = async () => {
  if (isSaveEnd) return;
  isSaveEnd = true;
  const end = messages.value[messages.value.length - 1];
  if (end && end.role == "assistant") {
    const { content, role, status } = end;
    const params = {
      content,
      role,
      sessionId: end.sessionId || sessionId,
      status,
    };
    updateAnwser({ loading: false });
    await saveChatMsg(params).then((res) => updateAnwser(res));
  }
  await upDateQuery(async () => {
    // 更新echart回答，回答完成之后显示图表
    const answer = useChat.currChatLastAnswer();
    if (answer) {
      const isEcharts = answer.content.includes("```echarts");
      if (isEcharts) {
        updateAnwser({ showEchart: true });
      }
    }
  });
  isSaveEnd = false;
};

const saveQuize = async () => {
  const { noSaveUser } = needSaveQuize;
  needSaveQuize = { need: false };
  let i = messages.value.length - 2;
  const lastQu = messages.value[i];
  if (lastQu && lastQu.role == "user" && !noSaveUser) {
    const { content, role, attachments } = lastQu;
    const params = {
      content,
      role,
      sessionId: lastQu.sessionId || sessionId,
      attachments,
    };
    await saveChatMsg(params).then((res) => updateMessage(res, i));
  }
};

// 删除当前会话,开一个新会话
closeWatch("del", async (confirm) => {
  if (confirm) {
    await delChat(useSchema.sessionId);
    message.success("删除成功");
    addNew();
    buttomC.value && buttomC.value.setText("");
  }
});

const answerFun = async ({ prompt, key, isFeature, noSaveUser }) => {
  controller = new AbortController();
  const roleType = "sql-assistant";
  const productId = useChat.productId;

  scrollToBottom();
  setTimeout(() => scrollToBottom());
  sessionId = id.value;
  if (!sessionId) {
    try {
      const title = prompt.slice(0, 32).trim();
      const { id } = await createChat({ title, friendId: 2 });
      router.replace({ query: { id } });
      sessionId = id;
      useSchema.setSessionId(sessionId);
    } catch (err) {
      console.log("创建新话题错误", err);
      return;
    }
  }
  needSaveQuize.need = true; // 需要保存提问
  needSaveQuize.noSaveUser = noSaveUser;

  return new Promise((resolve, reject) => {
    const mlist = getSendMessageData();
    useLogin.dot({ mt: 2, product_id: productId, role_type: roleType });

    const config = {
      options: {
        roleType,
        productId,
        sessionId,
        messages: mlist,
        tableSchemaId: useSchema.current,
      },
      signal: controller.signal,
      controller,
      interruptClear: () => {},
      onDownloadProgress: (response) => {
        try {
          let isUpDate = true;
          let content = messages.value[messages.value.length - 1].content;
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
                    // useChat.removeChatMsgEnd();
                    removeAnswer();
                    await saveQuizePromise;
                    await upDateQuery();
                  } else if (response.status == 500) {
                    updateAnwser({ status: 500 });
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
                updateAnwser({ content, loading: false });
                scrollToBottomIfAtBottom();
              }
              process();
            } catch (err) {
              console.log("process-err", err);
              if (err.message == "BodyStreamBuffer was aborted") {
                console.log("回答中-中断");
                config.interruptClear();
              }
              resolve(false);
            } finally {
            }
          }
          process();
        } catch (err) {
          console.log("onDownloadProgress-err", err);
          reject(err);
        }
      },
      timeOut: async () => {
        // 保存提问，并把提问内容放到输入框中
        message.error("请求超时");
        updateAnwser({ loading: false });
        await saveQuize();
        await upDateQuery(() => {
          const quize = messages.value[messages.value.length - 1];
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

const buttomSend = async ({ type, text, over = () => {} }) => {
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
      handler: () => reloadPage(),
    },
    {
      type: "show_version",
      handler: () => {
        showToast("update_system");
        closeWatch("update_system", async (confirm) => {
          if (confirm) {
            reloadPage();
          }
        });
      },
    },
    {
      type: "info",
      handler: (content) => {
        updateAnwser({ content, loading: false });
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

const messageChange = async ({ type, item, e }) => {
  switch (type) {
    case "copyQuize":
      buttomC.value && buttomC.value.setText(item.chat[0].content);
      break;
    case "reanswer": // 重新回答
      sending.value = true;
      const text = item.chat[0].content;
      await delmsg(item.chat[1], true);
      messages.value.push({ role: "assistant", content: "", loading: true });
      await answerFun({ prompt: text, noSaveUser: true });
      sending.value = false;
      break;
    case "collect":
      cellect(item, e);
      break;
  }
};

onMounted(async () => {
  if (route.query.reload) {
    reloadPage();
    return;
  }

  sessionId = id.value || useSchema.sessionId;
  clearMesages();
  if (
    ["add"].includes(type.value) ||
    !sessionId ||
    ["temp"].includes(sessionId)
  ) {
    addNew();
  } else if (sessionId) {
    getFoucs();
    initData();
  }

  scrollToBottom();
  sending.value = false;
  addScrollFun(scrollTop);
});

onUnmounted(() => {
  clearWatch();
  if (sending.value) {
    controller.abort();
  }
});
</script>

<style lang="scss" scoped>
.sql-chat {
  height: 100%;
  flex-direction: column;
  .center {
    flex: 1;
    overflow: hidden;
    max-height: calc(100% - 60px);
    .message_wrapper {
      height: 100%;
      overflow-y: auto;
      background-color: var(--tp_dl_bg_color);
    }
  }
}
</style>
