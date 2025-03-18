<template>
  <div
    class="SchemaList"
    @scroll.passive="listScroll"
    :style="{ height: `${52 + list.length * 60 + 2}px` }"
  >
    <div class="title flex_b">
      <div class="text">我的表结构</div>
    </div>
    <div class="flist" ref="flistRef">
      <div class="list_wrapper">
        <div
          class="fitem flex_b"
          v-for="(item, i) in list"
          :key="item"
          @click="itemClick(item)"
          :class="{
            active: useSchema.current == item.id,
            showItem: item.showItem,
          }"
        >
          <div class="flex_sx_center">
            <div class="name">
              <span v-if="item.top" class="tdot"></span>
              {{ item.name }}
            </div>
          </div>
          <div
            class="right"
            @click.stop="clickoutside(item, i)"
            :style="{ opacity: 1 }"
          >
            <NDropdown
              :trigger="'click'"
              placement="right"
              :options="getOptions(item)"
              @select="rowClick($event, item)"
            >
              <div class="clickWrapper">
                <span class="iconfont icon-option-vertical"></span>
              </div>
            </NDropdown>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useMessage, NDropdown } from "naive-ui";
import { computed, onMounted } from "vue";
import { useSchemaStore } from "@/store";
import { useToast } from "@/hooks/useToast";
import { delSchema, saveSchema } from "@/api/schema";
import { useRouter } from "vue-router";
const router = useRouter();
const message = useMessage();
const useSchema = useSchemaStore();
const { showToast, closeWatch, clearWatch } = useToast();

let selectItem;
let isClick = false;
const list = computed(() =>
  useSchema.list.map((obj) => ({ ...obj, showItem: false }))
);
const getOptions = (item) => {
  const arr = [];
  arr.push({ label: "编辑", key: "edit" });
  arr.push({ label: "删除", key: "del" });
  return arr;
};

const itemClick = async (row) => {
  if (isClick) return;
  isClick = true;
  useSchema.setCurr(row.id);
  isClick = false;
};

// 列表滚动时，隐藏选项
let clickRightIndex;
const listScroll = () => {
  if (clickRightIndex !== null) {
    const list = document.querySelectorAll(".clickWrapper")[clickRightIndex];
    list && list.click();
  }
  clickRightIndex = null;
};

const clickoutside = (item, i) => {
  clickRightIndex = i;
};

const del = async () => {
  if (!selectItem) return;
  const res = await delSchema({ id: selectItem.id });
  if (res.errMsg) {
    message.error(res.errMsg);
    return;
  }
  message.success("删除成功");
  await useSchema.getList();
  isClick = false;
  selectItem = "";
};

const rowClick = async (type, item) => {
  if (isClick) return;
  isClick = true;
  selectItem = item;
  switch (type) {
    case "edit": // 编辑
      router.push({
        name: "editSchema",
        query: { id: selectItem.id },
      });
      isClick = false;
      break;
    case "del": // 删除
      window.__talkx_event_stop = true;
      showToast("del", "你正在删除这个表结构，确定要这样做吗？");
      break;
  }
};

onMounted(() => {
  closeWatch("del", (confirm) => {
    if (confirm) {
      del();
    } else {
      isClick = false;
    }
  });
});
</script>

<style lang="scss" scoped>
.SchemaList {
  width: 206px;
  height: 100%;
  overflow-y: auto;
  height: 500px;
  max-height: 500px;
  .title {
    color: var(--tp_textcolor);
    height: 52px;
    line-height: 52px;
    padding-left: 10px;
  }

  .flist {
    position: relative;
    overflow-y: auto;
    height: calc(100% - 52px);

    .list_wrapper {
      .fitem {
        cursor: pointer;
        height: 60px;
        color: var(--tp_aside_color);
        padding: 0 10px;
        &.showItem {
          transform: scale(0);
          transform-origin: left center;
          animation: showItem 0.3s forwards;
          @keyframes showItem {
            0% {
              transform: scale(0);
            }
            100% {
              transform: scale(1);
            }
          }
        }

        .name {
          width: 154px;
          @include oneOverFlow();
          .tdot {
            width: 0;
            height: 0;
            position: relative;
            top: -3px;
            display: inline-block;
            border-radius: 50%;
            border: 1px solid var(--tp_aside_color);
          }
          margin-left: 10px;
        }

        .right {
          opacity: 0;
          width: 30px;
          max-width: 30px;
          text-align: right;
          line-height: 60px;
          position: relative;
          transition: opacity 0.3s;
          .iconfont {
            font-size: 20px;
          }
        }

        &:hover {
          color: var(--tp_aside_hover_color);
          background: var(--tp_aside_hover_bg_color);
          .right {
            opacity: 1 !important;
          }
        }

        &.active {
          color: var(--tp_aside_hover_color);
          background: var(--tp_aside_hover_bg_color);
        }
      }
    }
  }
}
</style>
