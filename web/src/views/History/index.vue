<script setup>
import {
  IosUndo,
  MdAdd,
  IosRefresh,
  IosTrash,
  MdMore,
} from "@vicons/ionicons4";
import { useMessage } from "naive-ui";
import { useRoute, useRouter } from "vue-router";
import {
  computed,
  onMounted,
  onUnmounted,
  ref,
  reactive,
  getCurrentInstance,
} from "vue";
import { Icon } from "@vicons/utils";
import { useChatStore } from "@/store";
import { useToast } from "@/hooks/useToast";
import { debance, routerBack } from "@/common/utils";
import { useScrollBottom } from "@/hooks/useScrollBottom";
import { sessionList, deleteSession } from "@/api/chat";

const uid = "history";
const route = useRoute();
const loading = ref(false);
const router = useRouter();
const message = useMessage();
const useChat = useChatStore();
const cxt = getCurrentInstance();
const params = reactive({ current: 1, size: 10 });
const bus = cxt.appContext.config.globalProperties.$bus;
const { showToast, closeWatch, clearWatch } = useToast(uid);
const { listDom, scolllDom, wrapperH, scroll, addScrollFun } =
  useScrollBottom();

const list = ref([]);
const noMoreData = ref(false);
const type = computed(() => route.query.type);
const getData = async () => {
  if (loading.value) return;
  loading.value = true;
  if (type.value === "sql") {
    params.friendId = 2;
  }
  const { current, pages, records, total } = await sessionList(params);
  list.value.push(...records.map((r) => ({ ...r, open: false })));
  noMoreData.value = current >= pages || list.value.length >= total;
  loading.value = false;
};

const bodyClick = () => {
  list.value.forEach((f) => (f.open = false));
};

const scrollBottom = () => {
  if (loading.value || noMoreData.value) return;
  params.current++;
  getData();
};

onMounted(async () => {
  addScrollFun(scrollBottom);
  params.size = Math.max(Math.floor((wrapperH.value / 41) * 1.5), 10);
  document.addEventListener("click", bodyClick);
  getData();
});

onUnmounted(() => {
  clearWatch();
  document.removeEventListener("click", bodyClick);
});

const back = () => router.back();
const addNew = () => {
  const params = { name: "dialogue", query: { type: "add" } };
  if (type.value === "sql") {
    params.name = "sqlchat";
  }
  router.push(params); // nType: 1
};

const open = (item) => {
  list.value.forEach((l) => {
    l.open = item.id === l.id ? !l.open : false;
  });
};
let selectItem = {};
const del = (item) => {
  if (useChat.getCurrUid === item.id) {
    showToast("nodel");
  } else {
    selectItem = item;
    showToast("del");
  }
};

const change = (item) => {
  if (type.value === "sql") {
    router.push({ name: "sqlchat", query: { id: item.id } });
  } else {
    router.push({
      name: "dialogue",
      query: { id: item.id, fid: item.friendId },
    });
    bus.emit("friendChange");
  }
};

closeWatch("del", async (confirm) => {
  if (confirm && selectItem.id) {
    try {
      await deleteSession(selectItem.id);
      message.success("删除成功");
      // getData();
      const index = list.value.findIndex((t) => t.id == selectItem.id);
      if (list.value[index]) {
        list.value.splice(index, 1);
      }
      selectItem = {};
    } catch (e) {
      message.success("删除失败");
    }
  }
});
</script>

<template>
  <div class="history tpage">
    <div class="top_bar flex_b">
      <div class="flex_sx_center back hover_bg" tabindex="0" @click="back">
        <span class="iconfont icon-rollback" style="font-size: 20px"></span>
        <span class="text">返回</span>
      </div>
      <div class="add hover_light" tabindex="0" @click="addNew">
        <Icon size="30">
          <MdAdd />
        </Icon>
      </div>
    </div>
    <div class="list_wrapper" @scroll.passive="scroll" ref="listDom">
      <div ref="scolllDom">
        <div
          class="item flex_b flex_sx_center"
          v-for="(item, i) in list"
          :key="i"
          @click="change(item)"
        >
          <div class="text">{{ item.title }}</div>
          <div class="right hover_color" @click.stop="open(item)">
            <Icon size="30">
              <MdMore />
            </Icon>
            <div class="list" v-if="item.open">
              <div class="fitem" @click.stop="del(item)">删除</div>
            </div>
          </div>
        </div>
        <div class="noData" v-if="!list.length">目前没有历史话题</div>
        <div v-else-if="noMoreData" class="noMoreData">没有更多了</div>
        <div class="loading" v-if="loading && list.length > params.size">
          loading......
        </div>
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.history {
  height: 100%;
  background: var(--tp_dl_bg_color);
  .top_bar {
    background: var(--tp_topbar_bgcolor);
    padding: 10px;

    .back {
      cursor: pointer;
      padding: 0 10px 0 5px;
    }

    .text {
      margin-left: 5px;
    }

    .add {
      cursor: pointer;
      border-radius: 2px;
      text-align: center;
      width: 30px;
      height: 30px;
      color: var(--tp_addBtn_color);
      background-color: var(--tp_addBtn_bg_color);
    }
  }

  .list_wrapper {
    overflow-y: auto;
    height: calc(100% - 52px);

    .item {
      cursor: pointer;
      padding: 5px 10px;
      border-bottom: 1px solid var(--tp_bordecolor);

      &:hover {
        background-color: var(--tp_foucs_bg_color);
      }

      .text {
        flex: 1;
        overflow: hidden;
        white-space: nowrap;
        text-overflow: ellipsis;
      }

      .right {
        position: relative;
        cursor: pointer;
        width: 30px;
        height: 30px;

        .list {
          user-select: none;
          position: absolute;
          right: 18px;
          top: 20px;
          background-color: var(--tp_headerlistbgcolor);
          width: 47px;
          /* display: none; */

          .fitem {
            padding: 5px 10px;
          }
        }
      }
    }

    .noData {
      color: #7a8084;
      padding-top: 50px;
      text-align: center;
    }

    .noMoreData {
      color: #7a8084;
      line-height: 41px;
      text-align: center;
    }
  }
}
</style>
