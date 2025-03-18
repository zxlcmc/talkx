<template>
  <div class="my_hashrate">
    <div class="number">{{ useLogin.coins }}</div>
    <div class="list">
      <div
        class="item flex_b"
        v-for="(item, i) in list"
        :key="item.catalogId || i"
        :class="{ tag: item.tag }"
        @click="rowClick(item)"
      >
        <div class="label">
          <NCheckbox
            v-if="!item.tag"
            :checked="selectCata == item.catalogId"
            @update-checked="selectCata = item.catalogId"
          />
          <span class="number"
            >{{ item.coins }} {{ !item.tag ? "个蒜粒" : "" }}
          </span>
        </div>
        <div class="right" v-if="!item.tag">
          <div v-if="item.price == item.marketPrice">{{ item.price }}元</div>
          <div v-else class="flex_end">
            <div class="old_price">原价{{ item.marketPrice }}元</div>
            <div class="line"></div>
            <div class="limit_time">限时{{ item.price }}元</div>
          </div>
        </div>
      </div>
    </div>
    <div class="bottom">
      <NButton block type="info" class="btn" @click="createOrderFun">
        立即下单
      </NButton>
      <div class="annotation">
        <div>【注意事项】</div>
        <div>
          1、一个蒜粒约等于GPT-4新模型的7.5万个提问单词或者2.5万个回答单词；
        </div>
        <div>2、不同模型的计算额度请到个人中心-默认模型查看；</div>
        <div>3、虚拟产品不支持退款；</div>
        <div>
          4、蒜粒的售价会随着官方、市场及成本等多方面因素的变化随时调整，不另做通知。
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from "vue-router";
import { useToast } from "@/hooks/useToast";
import { NCheckbox, NButton, useMessage } from "naive-ui";
import { inject, onMounted, onUnmounted, ref } from "vue";
import { useLoginStore } from "@/store";
import { coinCatalog } from "@/api/hashrate";
import { createOrder, queryOrder } from "@/api/order";

const resize = inject("resize");
const router = useRouter();
const message = useMessage();
const useLogin = useLoginStore();
const { showToast, closeWatch, clearWatch } = useToast();

const list = ref([]);
const selectCata = ref("");
let order_id;

let isClick = false;
const createOrderFun = async () => {
  if (isClick) return;
  isClick = true;

  const { orderId } = await createOrder({ catalogId: selectCata.value }).catch(
    (err) => {
      message.error("订单创建失败");
      isClick = false;
      return {};
    }
  );
  if (!orderId) return;
  let name = "pay";
  if (!resize.smallRef.value) {
    name = "d_" + name;
  }
  router.push({ name, query: { order: orderId } });

  //   //  查询是否还有未支付的订单 先不处理
  //   showToast(
  //     "auto",
  //     "你还有未支付的订单，继续支付还是取消订单？",
  //     "取消订单",
  //     "去支付"
  //   );
};
const defaultData = [{ coins: "充值蒜粒", tag: true }];
const initData = async () => {
  try {
    useLogin.getUser(true);
  } catch (e) {
    console.log("获取蒜粒err", e);
  }
  const coins = await coinCatalog();
  selectCata.value = coins[0].catalogId;
  list.value = [...defaultData, ...coins];
};
initData();

onMounted(() => {
  closeWatch("auto", async (confirm) => {
    if (confirm) {
      router.push({ name: "pay", query: { order: order_id } });
    }
    isClick = false;
  });
});
onUnmounted(() => {
  clearWatch();
});

const rowClick = (item) => {
  if (!item.tag) {
    selectCata.value = item.catalogId;
  }
};
</script>

<style lang="scss" scoped>
.my_hashrate {
  overflow-y: auto;
  height: calc(100% - 52px);
  & > .number {
    text-align: center;
    padding: 40px 0;
    font-size: 38px;
    color: var(--tp_hashrate_color);
  }

  .list {
    .item {
      cursor: pointer;
      padding: 0 10px;
      line-height: 50px;
      margin-bottom: 1px;
      border-bottom: 1px solid transparent;
      background-color: var(--tp_footer_bgcolor);

      .label {
        .number {
          margin-left: 10px;
        }
      }

      .right {
        .flex_end {
          align-items: center;

          .old_price {
            text-decoration: line-through;
          }

          .line {
            width: 1px;
            height: 16px;
            margin: 0 10px;
            background-color: var(--tp_textcolor);
          }
        }
      }

      &.tag {
        cursor: auto;
        line-height: 45px;
        color: #a2a2a2;
        background: transparent;
      }
    }
  }

  .bottom {
    padding: 20px 10px;

    .info {
    }

    .annotation {
      padding-top: 10px;
      line-height: 20px;
    }
  }
}
</style>
