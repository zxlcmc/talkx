<template>
  <div class="feature_row">
    <template v-for="(item, i) in props.list" :key="i">
      <div
        v-if="!item.hide"
        class="feature_item hover_light"
        tabindex="0"
        @click="itemClick(item)"
        :style="{ cursor }"
      >
        <n-tooltip trigger="hover">
          <template #trigger>
            <div class="icon">
              <span class="iconfont" :class="item.icon"></span>
            </div>
          </template>
          {{ item.text }}
        </n-tooltip>
      </div>
      <div class="division" v-if="item.line"></div>
    </template>
    <!-- <div class="feature_item hover_light">
      <n-dropdown
        v-model:show="dShow"
        :options="options"
        placement="top"
        trigger="click"
        @clickoutside="clickoutside"
        @select="itemSelect"
      >
        <span
          style="cursor: pointer"
          class="iconfont icon-option-horizontal"
        ></span>
      </n-dropdown>
    </div> -->
  </div>
</template>

<script setup>
import { useRouter } from "vue-router";
import { inject, onMounted, onUnmounted, ref } from "vue";
import { NTooltip, NPopover, NDropdown, useMessage } from "naive-ui";
import { useToast } from "@/hooks/useToast";
import { friendList, deleteFriend } from "@/api/user";
const router = useRouter();
const message = useMessage();
const showPopover = ref(false);
const resize = inject("resize");
const props = defineProps(["list", "cursor"]);
const emit = defineEmits(["itemClick"]);
const { showToast, closeWatch, clearWatch } = useToast();

const itemClick = (item) => {  
  emit("itemClick", item);
};

const dShow = ref(false);
const options = ref([]);
let selectItem;
const itemSelect = (key, options) => {
  console.log("itemSelect", options);
  if (key == "add") {
    router.push({ name: "d_friendsPlazaEdit" });
  } else if (key === "edit") {
    router.push({
      name: "d_friendsPlazaEdit",
      query: { id: options.data.friendId },
    });
  } else if (key === "del") {
    selectItem = options.data;
    showToast("del", "你正在删除这个AI，确定要这样做吗？");
  }
};

const rowClick = (obj) => { 
  dShow.value = false; 
  itemClick({ ...props.list[0], friend: obj }); 
};

const getData = async () => {
  const res = await friendList({ productType: 1 });
  const data = res.map((obj) => ({
    label: obj.name,
    key: "item",
    props: {
      onClick: () => rowClick(obj),
    },
    children: [
      { label: "编辑", key: "edit", data: obj },
      { label: "删除", key: "del", data: obj },
    ],
  }));
  options.value = data;
  if (data.length) {
    options.value.push({ type: "divider", key: "divider" });
  }
  options.value.push({ label: "新建", key: "add" });  
};

const clickoutside = () => {
  console.log("clickoutside");
};

const del = async () => {
  if (!selectItem) return;
  const res = await deleteFriend({ friendId: selectItem.friendId });
  if (res.errMsg) {
    message.error(res.errMsg);
    return;
  }
  message.success("删除成功");
  getData();
  isClick = false;
};

onMounted(() => {
  getData();
  closeWatch("del", (confirm) => {
    if (confirm) {
      del();
    }
  });
});
onUnmounted(() => clearWatch());
</script>

<style lang="scss" scoped>
.feature_row {
  display: flex;
  align-items: center;

  .division {
    width: 0;
    height: 15px;
    border-left: 1px solid var(--tp_textcolor);
    margin-right: 10px;
    margin-top: -1px;
  }

  .feature_item {
    margin-right: 10px;

    .icon {
      font-size: 16px;
    }
  }
}
</style>
