<template>
  <div class="Favorites tpage">
    <div class="top_bar flex_b flex_sx_center">
      <div class="flex_sx_center back hover_bg" tabindex="0" @click="back">
        <span class="iconfont icon-rollback" style="font-size: 20px"></span>
        <span class="text" style="margin-left: 5px">返回</span>
      </div>
    </div>
    <div class="content" @scroll.passive="scroll" ref="listDom">
      <div ref="scolllDom">
        <div class="item" v-for="item in list" :key="item.id">
          <MessageBody :data="item" @control="control" />
        </div>
        <div class="noData" v-if="!list.length">目前没有历史话题</div>
        <div v-else-if="noMoreData" class="noMoreData">没有更多了</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from "vue-router";
import { onMounted, ref, reactive, onUnmounted } from "vue";
import { useToast } from "@/hooks/useToast";
import { favoritesList, delFavorites } from "@/api/favorites";
import { useScrollBottom } from "@/hooks/useScrollBottom";
import MessageBody from "./comp/MessageBody.vue"; 
import { useMessage } from "naive-ui";
import { routerBack } from "@/common/utils";
const message = useMessage()
const { showToast, closeWatch, clearWatch } = useToast();

const list = ref([]);
let currData;
let isClick = false;
const loading = ref(false);
const router = useRouter();
const noMoreData = ref(false);
const params = reactive({ current: 1, size: 50 });
const { listDom, scolllDom, scroll, addScrollFun } = useScrollBottom();

const getData = async () => {
  if (loading.value) return;
  loading.value = true;
  const { current, pages, records, total } = await favoritesList(params);
  list.value.push(...records.map((r) => ({ ...r, open: false })));
  noMoreData.value = current >= pages || list.value.length >= total;
  loading.value = false;
};
const back = () => routerBack(router, { name: "dialogue" }); ;

const deleteMsg = async () => { 
  await delFavorites({ id: currData.id });
  message.success("删除成功");
  const index = list.value.findIndex(i => i.id === currData.id)
  list.value.splice(index, 1)
  currData = ''
};

const control = async ({ type, data }) => {
  if (isClick) return;
  isClick = true;
  switch (type) {
    case "delete":
      currData = data;
      showToast("del", "你要取消收藏这条消息，确定吗？");
      break;
    default:
      break;
  }
  isClick = false;
};
const scrollBottom = () => {
  console.log("scrollBottom");
  if (loading.value || noMoreData.value) return;
  params.current++;
  getData();
}; 

getData();
onMounted(() => {
  addScrollFun(scrollBottom);
  closeWatch("del", (confirm) => {
    if (confirm) {
      deleteMsg();
    }
  });
});

onUnmounted(() => {
  clearWatch();
});
</script>

<style lang="scss" scoped>
.Favorites {
  height: 100%;
  background: var(--tp_dl_bg_color);

  .top_bar {
    padding: 10px;
    background-color: var(--tp_footer_bgcolor);

    .back {
      cursor: pointer;
      padding: 0 10px 0 5px;
    }
  }

  .content {
    overflow-y: auto;
    height: calc(100% - 52px);
    .noMoreData,
    .noData {
      color: #7a8084;
      line-height: 40px;
      text-align: center;
    }
  }
}
</style>
