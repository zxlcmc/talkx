<template>
  <div class="model_list">
    <div class="head">
      <span>模型</span>
      <span>提问/回答</span>
      <div class="close" @click="close">
        <span class="iconfont icon-close"></span>
      </div>
    </div>
    <div class="tbody">
      <div class="list">
        <div
          class="item flex_sx_center"
          v-for="item in list"
          :key="item.id"
          :class="{
            disabled: !item.canSelection,
          }"
          @click="itemClick(item)"
        >
          <div class="select">
            <NCheckbox
              :checked="item.model == selected"
              :disabled="!item.canSelection"
            />
          </div>
          <div class="content flex">
            <div class="left">
              <div class="name">{{ item.model }}</div>
              <div class="consume">
                {{ item.inputCoins > 0 ? item.inputCoins : "免费" }}/{{
                  item.outputCoins > 0 ? item.outputCoins : "免费"
                }}
              </div>
            </div>
            <div class="cTags flex">
              <div
                class="tagItem"
                v-for="item in [...item.commentTags]"
                :key="item.name"
                :style="item.style"
              >
                {{ item.name }}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from "vue";
import { modelList } from "@/api/user";
import { NCheckbox, NTable, useMessage } from "naive-ui";
import { useLoginStore } from "@/store";
const props = defineProps(["show"]);
const emit = defineEmits(["update:show"]);
const message = useMessage();
const useLogin = useLoginStore();
const list = ref([]);

let isClick = false;
const selected = computed(() => useLogin.user?.model);
const close = () => {
  emit("update:show", false);
};
const itemClick = async (item) => {
  if (isClick || !item.canSelection) return;
  isClick = true;
  const { errMsg } = await useLogin.setUser({
    field: "model",
    value: item.model,
  });
  if (errMsg) {
    message.error(errMsg);
  } else {
    emit("update:show", false);
  }
  isClick = false;
};
onMounted(() => {
  modelList().then(
    (res) => {
      list.value = res.map((item) => {
        const { commentTags = [] } = item;
        return Object.assign({}, item, {
          commentTags: commentTags.map(({ color, bgColor, name }) => {
            return { name, style: { color, backgroundColor: bgColor } };
          }),
        });
      }) 
    }
  );
});
</script>

<style lang="scss" scoped>
.model_list {
  user-select: none;
  font-size: var(--tp_size);
  color: var(--tp_textcolor);
  background-color: var(--tp_userList_bgcolor);
  .head {
    position: relative;
    padding: 5px;
    span {
      &:first-child {
        margin: 0 10px 0 32px;
      }
    }
    .close {
      position: absolute;
      right: 0;
      top: 0;
      cursor: pointer;
      .iconfont {
        font-size: 20px;
      }
    }
  }
  .tbody {
    max-height: 408px; // 51 * 8
    overflow-y: auto;
    .list {
      .item {
        padding: 5px;
        cursor: pointer;
        font-size: var(--tp_size);
        border-top: 1px solid var(--tp_aside_border_color);
        &.disabled {
          opacity: 0.4;
          cursor: not-allowed;
        }
        .select {
          padding: 0 10px 0 5px;
        }
        .content {
          flex: 1;
          justify-content: space-between;
          .name {
            text-wrap: nowrap;
          }
          .consume {
            font-size: 0.9em;
          }
          .cTags {
            align-items: center;
            padding-left: 10px;
            .tagItem {
              padding: 0px 5px;
              border-radius: 3px;
              margin: 0 5px;
              font-size: 10px;
            }
          }
        }
        &:hover {
          color: var(--tp_aside_hover_color);
          background: var(--tp_userList_hover_bgcolor);
        }
      }
    }
  }
}
</style>
