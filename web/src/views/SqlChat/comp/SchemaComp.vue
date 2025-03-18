<template>
  <n-popover
    placement="top-start"
    :show="show"
    :style="{
      padding: '4px',
      'border-radius': '0px',
      'background-color': 'var(--tp_userList_bgcolor)',
    }"
  >
    <template #trigger>
      <div class="currSchema flex" @click.stop="showList">
        <!-- <div><span class="iconfont icon-schema"></span></div> -->
        <img src="@/assets/svg/schema.svg" style="width: 16px" alt="" />
        <template v-if="curr">
          <div class="name">{{ curr.name }}</div>
          <div><span class="iconfont icon-xiangyoujiantou"></span></div>
        </template>
        <div v-else class="name noCurr">请先新建表结构</div>
      </div>
    </template>
    <div class="list_wrapper">
      <div class="san"></div>
      <div class="listWrraper">
        <SchemaList />
      </div>
    </div>
  </n-popover>
</template>

<script setup>
import { NPopover } from "naive-ui";
import { useSchemaStore } from "@/store";
import SchemaList from "./SchemaList.vue";
import { computed, ref, onMounted, onUnmounted } from "vue";
const show = ref(false);
const useSchema = useSchemaStore();
const curr = computed(() => useSchema.curr);
const showList = () => {
  if (!curr.value) return;
  show.value = !show.value;
};

const bodyClick = () => {
  if (window.__talkx_event_stop) {
    window.__talkx_event_stop = false;
    return;
  }
  show.value = false;
};

onMounted(() => {
  document.addEventListener("click", bodyClick);
});
onUnmounted(() => {
  document.removeEventListener("click", bodyClick);
});
</script>

<style lang="scss" scoped>
.currSchema {
  cursor: pointer;
  color: var(--tp_textcolor);
  line-height: 1;
  align-items: center;
  height: 100%; 
  .name {  
    margin: 0 5px;
    @include oneOverFlow();
    &.noCurr {
      color: rgba($color: gray, $alpha: 0.6);
    }
  }
  .iconfont {
    color: inherit;
    font-size: 12px;
  }
}
</style>
