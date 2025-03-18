<template>
  <div class="tabs_wrapper" ref="wrapper">
    <div
      class="tab_item item"
      v-for="item in rows.tabs"
      :key="item"
      :class="{ active: props.tIndex == item.id }"
      @click="itemClick(item)"
    >
      {{ item.label }}
      <span v-if="item.count"> ({{ item.count }})</span>
    </div>
    <div class="tab_item item tab_list" v-if="rows.tabs.length > 5">
      <!-- <div class="text" @mouseover="showList" @click.stop="showList">
        <span class="iconfont icon-option-horizontal"></span>
      </div>
      <div class="list_wrapper" v-show="listShow">
        <div
          class="tab_li item"
          v-for="(item, i) in rows.list"
          :key="i"
          :class="{ active: props.tIndex == item.id }"
          @click="itemClick(item)"
        >
          {{ item.label }}
        </div>
      </div> -->

      <n-popover
        placement="bottom"
        :show="listShow"
        :style="{
          padding: '4px',
          'border-radius': '0px',
          'background-color': 'var(--tp_userList_bgcolor)',
        }"
      >
        <template #trigger>
          <div
            class="text"
            @mouseover="(e) => (listShow = true)"
            @click.stop="(e) => (listShow = true)"
          >
            <span class="iconfont icon-option-horizontal"></span>
          </div>
        </template>
        <div class="list_wrapper" :class="{ small: resize.smallRef.value }">
          <div
            class="tab_li item"
            v-for="(item, i) in rows.list"
            :key="i"
            :class="{ active: props.tIndex == item.id }"
            @click="itemClick(item)"
          >
            {{ item.label }}
            <span v-if="item.count"> ({{ item.count }})</span>
          </div>
        </div>
      </n-popover>
    </div>
  </div>
</template>

<script setup>
import { NPopover } from "naive-ui";
import { computed, inject, onMounted, onUnmounted, ref } from "vue";

const emit = defineEmits(["tabChange"]);
const props = defineProps({
  data: Array,
  tIndex: Number | String,
  width: {
    type: Number,
    default: 100,
  },
});

const n = ref(7);
const wrapper = ref(null);
const listShow = ref(false);
const resize = inject("resize");
const computedN = () => {
  if (!wrapper.value) return;
  const w = parseInt(window.getComputedStyle(wrapper.value)["width"]);
  let len = Math.floor(w / props.width); // 左右留一点空隙
  if (!resize.smallRef.value) {
    len = len - 1;
  }
  n.value = props.data.length > len ? len - 1 : len;
};
resize.change(computedN);
const itemW = computed(() => props.width + "px");
const rows = computed(() => {
  return {
    tabs: props.data.slice(0, n.value),
    list: props.data.slice(n.value),
  };
});
const bodyClick = () => {
  listShow.value = false;
};

const itemClick = (item) => {
  emit("tabChange", item);
  listShow.value = false;
};

defineExpose({ bodyClick });

onMounted(() => {
  computedN();
  document.addEventListener("click", bodyClick);
});
onUnmounted(() => {
  document.removeEventListener("click", bodyClick);
});
</script>

<style lang="scss" scoped>
.item {
  width: v-bind(itemW);
  min-width: 100px;
  cursor: pointer;
  line-height: 30px;
  text-align: center;

  color: var(--tp_FriendCard_color);
  background-color: var(--tp_FriendCard_bg_color);

  &:hover {
    color: var(--tp_FriendCard_hover_color);
    background: var(--tp_FriendCard_hover_bg_color);
  }
  &.active {
    color: var(--tp_FriendCard_active_color);
    background: var(--tp_FriendCard_active_bg_color);
  }
}

.list_wrapper {
  &.small {
    max-height: 300px;
    overflow-y: auto;
  }
}

.tabs_wrapper {
  display: flex;
  justify-content: center;

  .tab_item {
    margin-left: 1px;
    &.tab_list {
      &:hover {
        .list_wrapper {
          display: block;
        }
      }
      position: relative;
      .list_wrapper {
        padding: 3px;
        background-color: var(--tp_item_bg_color);
        display: none;
        z-index: 1;
        position: absolute;
        top: 100%;
        left: -2px;
      }
    }
  }
}
</style>
