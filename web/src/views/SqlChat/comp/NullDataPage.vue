<template>
  <div class="NullDataPage">
    <div class="title">{{ info.name }}</div>
    <div class="intro">{{ info.welcome }}</div>
    <!-- TODO 展示快捷区 -->

    <div class="stars" :class="{ small: resize.smallRef.value }">
      <!-- pc 直接展示所有数据 -->
      <!-- samll <3 展示全部 -->
      <!-- samll >=3 滚动 -->
      <div
        class="flex"
        v-if="!stars.length"
        :class="{ small: resize.smallRef.value }"
      >
        <div
          class="border hover_bg"
          v-for="(item, i) in conversationStart"
          :key="i"
          @click="starClick(item)"
        >
          {{ item }}
        </div>
      </div>

      <div class="flex" v-else>
        <div class="border line2">
          <ListMove :list="stars" v-slot="{ text }">
            <div
              class="border"
              v-for="s in text"
              :key="s"
              @click="starClick(s)"
            >
              {{ s }}
            </div>
          </ListMove>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { inject, ref, computed } from "vue";
import { useSchemaStore } from "@/store";
import ListMove from "@/components/ListMove/index.vue";

const emits = defineEmits(["send"]);
const resize = inject("resize");
const useSchema = useSchemaStore();
const conversationStart = computed(
  () => useSchema.curr?.conversationStart || []
);
const info = ref({
  name: "专门为SQL而设计",
  welcome: "选择表结构，说出你的查询需求，我就会生成可执行的SQL。",
});

const stars = computed(() => {
  const mlist = [];
  if (!conversationStart.value) return mlist;
  const len = conversationStart.value.length;
  const showList = !resize.smallRef.value || len <= 2;

  if (!showList) {
    for (let i = 0; i < 4; i++) {
      const text =
        conversationStart.value[i] || conversationStart.value[i / len];
      const j = Math.floor(i / 2);
      if (i % 2 == 0) {
        mlist[j] = [];
      }
      mlist[j].push(text);
    }
  }
  return mlist;
});

const starClick = (text) => {
  emits("send", { text });
};
</script>

<style lang="scss" scoped>
.NullDataPage {
  height: 100%;
  text-align: center;
  padding-top: 50px;
  box-sizing: border-box;
  position: relative;
  color: var(--tp_textcolor);
  .title {
    color: var(--tp_toast_color);
    font-size: 1.5rem;
  }
  .intro {
    width: 80%;
    font-size: 18px;
    margin: 20px auto;
    word-wrap: break-word;
    color: var(--tp_textcolor);
  }
  .stars {
    position: absolute;
    left: 50%;
    bottom: -10px;
    width: calc(100%);
    transform: translateX(-50%);

    &.small {
      bottom: 10px;
    }

    .lines {
      box-sizing: border-box;
      padding: 0 12px;
      margin: auto;
      width: 100%;
      max-width: var(--msg_content-width);
    }
    .starItem {
      text-align: left;
      cursor: pointer;
      line-height: 30px;
      margin-bottom: 10px;
      border-bottom: 1px solid var(--tp_bordecolor);
      &:hover {
        background-color: var(--tp_nd_hover_bg_color);
      }
    }

    .flex {
      flex-wrap: wrap;
      box-sizing: border-box;
      padding: 0 10px;
      margin: auto;
      width: 100%;
      max-width: var(--msg_content-width);
      & > div {
        width: 45%;
        display: inline-block;
        margin-top: 10px;
        &:nth-child(2n) {
          margin-left: 10%;
        }
      }

      .border {
        padding: 2px 10px;
        cursor: pointer;
        text-align: left;
        line-height: 32px;
        box-sizing: border-box;
        border: 1px solid var(--tp_bordecolor);
        overflow: hidden;
        text-wrap: nowrap;
        text-overflow: ellipsis;
      }

      .line2 {
        width: 100%;
        padding: 0;
        .border {
          margin-left: 0;
          height: 36px;
          &:nth-child(2n) {
            margin-top: 10px;
          }
          &:hover {
            background-color: var(--tp_nd_hover_bg_color);
          }
        }
        border: none;
        height: 82px;
        overflow: hidden;
      }

      &.small {
        & > div {
          width: 100%;
          &:nth-child(2n) {
            margin-left: 0;
          }
        }
      }
    }
  }
}
</style>
