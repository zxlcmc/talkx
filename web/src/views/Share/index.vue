<script setup>
// import csText from './cs.md?raw'
import { NPopover } from "naive-ui";
import { useRoute } from "vue-router";
import { ref, computed, onMounted, onUnmounted, reactive, nextTick, getCurrentInstance } from "vue";
import Header from "@/views/chat/comp/header.vue";
import MessageBody from "@/views/dialogue/comp/MessageBody.vue";
import { useLoginStore } from "@/store";
import { getSession } from "@/api/chat";
import { isPhone } from "@/common/utils";
import { useScrollBottom } from "@/hooks/useScrollBottom";
 
const list = ref([]);
const route = useRoute();
const loading = ref(false);
let noMoreData = ref(false);
const useLogin = useLoginStore();
const instance = getCurrentInstance()
const params = reactive({ current: 1, size: 50 });

console.log('instance', instance.root)
const scrollRef = ref();
const { scolllDom, wrapperH, scroll, addScrollFun } = useScrollBottom({
  listDom: scrollRef,
  type: "top",
});
const width = ref("100%");
const messageList = computed(() => {
  var arr = [],
    index = -1;
  const mArr = list.value;
  let prev = "";
  mArr.forEach((obj, j) => {
    if (
      (mArr[prev] && mArr[prev].role == obj.role) ||
      !mArr[prev] ||
      obj.role === "user"
    ) {
      index++;
      if (!arr[index]) {
        arr[index] = { chat: [] };
      }
    }
    prev = j;
    arr[index].chat.push({ ...obj, __index: j });
  });
  // 数据补全
  arr = arr.map((c) => {
    if (!c.chat.find((c) => c.role == "user")) {
      c.chat.unshift({
        id: Math.random(),
        role: "user",
        sessionId: "",
        content: "",
      });
    }
    if (!c.chat.find((c) => c.role == "assistant")) {
      c.chat.push({
        id: Math.random(),
        role: "assistant",
        sessionId: "",
        content: "",
      });
    }
    return c;
  });
  return arr;
});
const getData = async () => {
  if (loading.value) return;
  loading.value = true;
  try {
    const { current, pages, records, total } = await getSession({
      sessionId: route.params.uid,
      ...params,
    });
    const first = list.value[0];
    let firstTop = 0;
    if (first) {
      const dom = document.querySelector(`[msg-id="${first.id}"]`);
      firstTop = dom.offsetTop;
    }  
    // records[0].content = csText // 测试
    // console.log('records', records);

    list.value.unshift(...records.reverse());
    noMoreData.value = current >= pages || list.value.length >= total;
    if (first) {
      nextTick(() => {
        // 滚动到当前数据位置, 获取消息id, 滚动到对应位置
        const dom = document.querySelector(`[msg-id="${first.id}"]`); 
        scrollRef.value.scrollTop =
          scrollRef.value.scrollTop + (dom.offsetTop - firstTop);
      });
    }
  } catch (err) {
    console.log("err", err);
  } finally {
    loading.value = false;
  }
}; 

const resize = () => {
  width.value = isPhone() ? "100%" : "600px";
};

const scrollBottom = () => { 
  if (loading.value || noMoreData.value) return;
  params.current++;
  getData();
};

onMounted(async () => {
  addScrollFun(scrollBottom);
  window.addEventListener("resize", resize);
  resize();
   
  await getData();
  nextTick(()=>{ 
    if(scrollRef.value){ 
      scrollRef.value.scrollTop = scrollRef.value.scrollHeight; 
    }
  })
});
onUnmounted(() => window.removeEventListener("resize", resize));

const freeUse = () => {
  window.open(useLogin.getShareUrl(route.params.inviteCode));
};
</script>

<template>
  <div class="share">
    <div class="warpper tpage" :style="{ width }">
      <Header>
        <template v-slot:right>
          <div class="right">
            <span @click="freeUse">免费使用</span>
            <NPopover trigger="hover" raw>
              <template #trigger>
                <span class="iconfont icon-info-circle"></span>
              </template>
              <div class="right_tip">
                此页面为用户与AI对话内容的分享，仅供参考，不代表任何个人、机构或公司观点。对话不构成专业建议，并概不负责对话信息带来的损失。对话内容可能随时变更、修改或删除。使用者需谨慎参考、寻求专业意见。
              </div>
            </NPopover>
          </div>
        </template>
      </Header>
      <div class="content">
        <div
          v-if="messageList.length"
          class="message_wrapper"
          id="scrollRef"
          ref="scrollRef"
          @scroll.passive="scroll"
        >
          <div class="message_ul" ref="scolllDom">
            <MessageBody
              v-for="(msg, i) in messageList"
              :key="i"
              :item="msg"
              :sending="false"
              type="share"
              :end="i == messageList.length - 1"
            />
            <!-- <div v-if="noMoreData" class="noMoreData">没有更多了</div> -->
          </div>
        </div>
        <div class="loading" v-if="loading">正在加载话题消息</div>
        <div v-else-if="!loading && !messageList.length.length" class="nolink">
          <span class="iconfont icon-lianjieshixiao"></span>
          <div>抱歉，你当前浏览的话题已失效或不存在。</div>
        </div>
      </div>
    </div>
  </div>
</template>

<style lang="scss">
.right_tip {
  padding: 10px;
  max-width: 300px;
  border-radius: 5px;
  color: var(--tp_share_statement_color);
  background-color: var(--tp_share_bg_color);
}
</style>
<style scoped lang="scss">
.share {
  height: 100%;
  /* background-color: var(--tp_share_bg_color); */

  .warpper {
    height: 100%;
    width: 100%;
    max-width: 600px;
    margin: 0 auto;
    .right {
      .iconfont {
        cursor: pointer;
        margin-left: 5px;
        vertical-align: -1px;
      }
    }
  }

  .content {
    overflow: hidden;
    height: calc(100% - 50px);
    background-color: var(--tp_bgcolor);

    .message_wrapper {
      height: 100%;
      max-height: 100%;
      overflow-y: auto;
      box-sizing: border-box;
      .message_ul {
        .noMoreData {
          color: #7a8084;
          line-height: 40px;
          text-align: center;
        }
      }
    }
    .loading {
      padding-top: 50px;
    }
    .nolink {
      padding-top: 50px;
      text-align: center;

      .iconfont {
        font-size: 80px;
      }

      div {
        margin-top: 40px;
      }
    }
  }
}
</style>
