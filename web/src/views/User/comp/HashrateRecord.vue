<template>
  <div class="hashrate_record">
    <div class="tabs flex_b">
      <div
        class="tab-item"
        v-for="item in tabs"
        :key="item.type"
        :class="{ active: params.type == item.type }"
        @click="tabChange(item.type)"
      >
        {{ item.name }}
      </div>
    </div>
    <div class="list_wrapper" @scroll.passive="scroll" ref="listDom">
      <div ref="scolllDom">
        <RecordList :list="list" :tabs="tabs" />
        <div v-if="noMoreData" class="noMoreData">没有更多了</div>
      </div>
      <div class="loading" v-if="loading && list.length > params.size">
        loading......
      </div>
    </div>
  </div>
</template>

<script setup>
import RecordList from "./RecordList.vue";
import { searchCoin } from "@/api/hashrate";
import { delay, debance } from "@/common/utils";
import { useScrollBottom } from "@/hooks/useScrollBottom";
import { computed, onMounted, onUnmounted, reactive, ref, watch } from "vue";
const { listDom, scolllDom, wrapperH, scroll, addScrollFun } =
  useScrollBottom();
const tabs = [
  { type: "", name: "全部" },
  { type: 1, name: "消耗" },
  { type: 2, name: "充值" },
  { type: 3, name: "奖励" },
];
const list = ref([]);
const loading = ref(false);
const params = reactive({ type: "", current: 1, size: 10 });

let noMoreData = ref(false);
const getData = async () => {
  if (loading.value) return;
  loading.value = true;
  const { total, size, current, pages, records } = await searchCoin(params);
  noMoreData.value = current >= pages || list.value.length >= total;
  list.value.push(...records);
  loading.value = false;
};

const tabChange = (e) => {
  if (loading.value) return;
  params.type = e;
  params.current = 1;
  noMoreData.value = false;
  list.value = [];
  getData();
};

const scrollBottom = () => {
  if (loading.value || noMoreData.value) return;
  params.current++;
  getData();
};

onMounted(() => {
  addScrollFun(scrollBottom);
  params.size = Math.max(Math.floor((wrapperH.value / 65) * 1.5), 10);
  getData();
});

watch(
  () => wrapperH.value,
  () => {
    // console.log('容器高度发生变化，重新计算每页的数据条数，如果数据条数大于当前的数据条数，重新拉取数据');
    // 容器高度发生变化
    // params.XXX
  }
);
</script>

<style lang="scss" scoped>
.hashrate_record {
  height: calc(100% - 51px);

  .tabs {
    .tab-item {
      flex: 1;
      height: 34px;
      padding: 10px 0;
      cursor: pointer;
      text-align: center;
      box-sizing: border-box;
      border: 1px solid var(--tp_dl_bg_color);
      background-color: var(--tp_footer_bgcolor);
      line-height: 10px;

      &.active {
        background-color: var(--tp_dl_bg_color);
      }
    }
  }

  .list_wrapper {
    overflow-y: auto;
    height: calc(100% - 35px);

    .noMoreData {
      line-height: 64px;
      text-align: center;
      color: #7a8084;
    }
  }
}
</style>
