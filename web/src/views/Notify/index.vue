<template>
  <div class="notify tpage">
    <div class="topbar flex_b">
      <div class="flex_sx_center back hover_bg" tabindex="0" @click="back">
        <span class="iconfont icon-rollback" style="font-size: 20px"></span>
        <span class="text">返回</span>
      </div>
      <div class="hover_light" tabindex="0"></div>
    </div>
    <div class="list_wrapper" @scroll.passive="scroll" ref="listDom">
      <div ref="scolllDom">
        <div class="list">
          <div
            class="item"
            v-for="(item, i) in list"
            :key="i"
            :class="{ read: item.read }"
            @click="itemClick(item)"
          >
            <div class="title">{{ item.title }}</div>
            <div class="time">{{ timeStr(item.createTime) }}</div>
          </div>
        </div>
        <div v-if="noMoreData" class="noMoreData">没有更多了</div>
      </div>
    </div>
    <MessageModel
      v-model:show="modelObj.show"
      :msg="modelObj.msg"
      @close="closeMessageModel"
    />
  </div>
</template>

<script setup>
import { useRouter } from "vue-router";
import { onMounted, reactive, ref } from "vue";
import { timeStr } from "@/common/utils";
import { reading, notifyList } from "@/api/notify";
import { useScrollBottom } from "@/hooks/useScrollBottom";
import MessageModel from "@/components/MessageModel/index.vue";
const { listDom, scolllDom, wrapperH, scroll, addScrollFun } =
  useScrollBottom();

const list = ref([]);
const router = useRouter();
const loading = ref(false);
const noMoreData = ref(false);
const params = reactive({ size: 10, current: 1 });
const modelObj = reactive({ show: false, msg: {} });
const getData = async () => {
  if (loading.value) return;
  loading.value = true;
  const { current, pages, records, total } = await notifyList(params);
  list.value.push(...records);
  noMoreData.value = current >= pages || list.value.length >= total;
  loading.value = false;
};

const scrollBottom = () => {
  if (loading.value || noMoreData.value) return;
  params.current++;
  getData();
};

const initData = () => {
  params.current = 1;
  noMoreData.value = false;
  list.value = [];
  getData();
};
const back = () => router.back();
const itemClick = (item) => {
  modelObj.msg = item;
  modelObj.show = true;
};
const closeMessageModel = async (msg) => {
  await reading({ notificationId: msg.id }).catch(()=>{});
  msg.read = true;
  // initData(); 
};

onMounted(async () => {
  addScrollFun(scrollBottom);
  params.size = Math.max(Math.floor((wrapperH.value / 62) * 1.5), 10);
  getData();
});
</script>

<style lang="scss" scoped>
.notify {
  height: 100%;
  color: var(--tp_notify_color);
  background-color: var(--tp_dl_bg_color);
  .list_wrapper {
    overflow-y: auto;
    height: calc(100% - 52px);
    .list {
      .item {
        height: 61px;
        cursor: pointer;
        line-height: 25px;
        padding: 5px 10px;
        box-sizing: border-box;
        border-top: 1px solid var(--tp_bordecolor);
        background-color: var(--tp_footer_bgcolor);
        .title {
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
          font-weight: bold;
        }
        .time {
          color: #777777;
          font-size: 0.9rem;
        }
        &.read {
          .title {
            color: #777777;
            font-weight: 400;
          }
        }
        &:hover {
          background-color: var(--tp_foucs_bg_color);
        }
      }
    }
    .noMoreData {
      color: #777777;
      font-size: 0.9rem;
      text-align: center;
      line-height: 40px;
    }
  }
}
</style>
