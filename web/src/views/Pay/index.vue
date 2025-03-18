<template>
  <div class="pay tpage">
    <div class="topbar flex_b flex_sx_center">
      <div>
        <div
          class="flex_sx_center back hover_bg"
          tabindex="0"
          @click="back"
          v-if="!isWXZFB"
        >
          <span class="iconfont icon-rollback" style="font-size: 20px"></span>
          <span class="text">{{ leftText }}</span>
        </div>
      </div>
    </div>
    <div class="pay_content">
      <div class="top_area">
        <!-- 在web模式下  -->
        <div class="pay_status" v-if="showPayStatus">
          <span
            class="iconfont"
            :class="payObj.icon"
            :style="{ color: payObj.color }"
          ></span>
          <div class="pay_text">{{ payObj.text }}</div>
        </div>
        <div class="ewm_area" v-if="showEwmArea">
          <div class="text1">{{ ewmTxt }}</div>
          <div class="text2">请在 {{ timeStr }} 内完成支付</div>
          <img
            v-show="
              !isWXZFB &&
              ewmImg &&
              [payStatusEnum.unpaid, payStatusEnum.progress].includes(payStatus)
            "
            class="ewm_img"
            :src="ewmImg"
          />
        </div>
      </div>
      <TList :list="list" />
      <div
        class="btn_wrapper"
        v-if="
          isWXZFB &&
          [payStatusEnum.unpaid, payStatusEnum.progress].includes(payStatus)
        "
      >
        <NButton block type="info" class="btn" @click="toPay">立即支付</NButton>
      </div>
    </div>
  </div>
</template>

<script setup>
import { NButton, useMessage } from "naive-ui";
import { useRouter, useRoute } from "vue-router";
import { computed, onMounted, onUnmounted, ref, getCurrentInstance } from "vue";
import TList from "@/components/TList/index.vue";
import { useCountDown } from "@/hooks/useCountDown";
import { createOrder, queryOrder, payOrder } from "@/api/order";
import {
  format,
  isZFB,
  isWX,
  getQueryString,
  routerBack,
} from "@/common/utils";
import zfbPay from "./compositions/zfbPay";
import wxPay from "./compositions/wxPay";
import { scanPay } from "./compositions/scanPay";
import {
  list,
  upDataList,
  payStatusObj,
  payStatusEnum,
  wxPayErrMsgMap,
} from "./compositions/initData";

const cxt = getCurrentInstance();
const bus = cxt.appContext.config.globalProperties.$bus;

const route = useRoute();
const router = useRouter();
const message = useMessage();
const timelen = ref(30 * 60);

/**
 * 是否处于微信支付宝环境
 *
 * 处于
 * 1. 显示支付按钮, 可调起微信支付宝支付
 *
 * 不处于
 * 2. 展示二维码
 */

const back = () => {
  routerBack(router, { name: "userEdit", query: { type: "my_hashrate" } });
};

let poll;
let wxCode;
let orderId;
let isClick = false;
const orderInfo = ref({});
const payStatus = ref("");
const payObj = computed(() => payStatusObj[payStatus.value]);
const isWXZFB = computed(() => isZFB() || isWX());
const ewmImg = computed(() => orderInfo.value.orderPageQrcode);
const timeStr = computed(() =>
  format(
    new Date(+new Date("2020/12/2 00:00:00") + timelen.value * 1000),
    "mm分ss秒"
  )
);
const leftText = computed(() => {
  return [payStatusEnum.fail].includes(payStatus.value) ? "取消" : "返回";
});

const ewmTxt = computed(() => {
  // 当处于微信环境 且状态为待支付
  if (
    (isWX() &&
      [payStatusEnum.unpaid, payStatusEnum.progress].includes(
        payStatus.value
      )) ||
    (!isWXZFB.value && [payStatusEnum.progress].includes(payStatus.value))
  ) {
    return payObj.value.text;
  } 
  return "请使用微信扫码支付";
});

// 显示支付状态部分
const showPayStatus = computed(() => {
  // web 待支付状态 支付中  // Wx  待支付状态 支付中
  var p = ![payStatusEnum.unpaid, payStatusEnum.progress].includes(
    payStatus.value
  );
  return p && payObj.value;
});

const showEwmArea = computed(() => {
  var p = [payStatusEnum.unpaid, payStatusEnum.progress].includes(
    payStatus.value
  );
  return p;
});

const toPay = async () => {
  if (isClick) return;
  isClick = true;
  if (isWX()) {
    // wxCode
    const params = { orderId, payType: 2, clientType: 2, wxCode };
    const res = await payOrder(params);
    payStatus.value = await wxPay.invoke(res);
  } else if (isZFB()) {
    // 支付宝支付暂时搁置
    const params = { orderId, payType: 1, clientType: 5, wxCode };
    const res = await payOrder(params);
    // payStatus.value = await zfbPay.invoke(res);
  }
  isClick = false;
};

const orderQuery = async () => {
  const result = { err: false };
  orderInfo.value = await queryOrder({ orderId });
  if (orderInfo.value.err) {
    payStatus.value = payStatusEnum.noOrder;
    message.error(orderInfo.value.msg);
    result.err = true;
    return result;
  }
  timelen.value = orderInfo.value.timeoutForPay;
  payStatus.value = orderInfo.value.status;
  upDataList(orderInfo.value);
  return result;
};

const pollFun = () => {
  clearInterval(poll);
  // 只有当状态为 待支付 支付中 才轮询接口
  const isNext = () =>
    [payStatusEnum.unpaid, payStatusEnum.progress].includes(payStatus.value);
  if (!isNext()) return;
  poll = setInterval(async () => {
    if (!isNext()) {
      clearInterval(poll);
      return;
    }
    if (timelen.value <= 0) {
      clearInterval(poll);
      // 订单超时
      payStatus.value = payStatusEnum.close;
      cb();
      return;
    }
    // 查询订单状态
    await orderQuery();
  }, 1000);
};

onMounted(async () => {
  orderId = route.query.order;
  if (!orderId) return;

  wxPay.getUrl();
  // 获取订单信息
  const result = await orderQuery();
  if (result.err) return;
  console.log("orderInfo", isWXZFB.value, orderInfo);

  pollFun();

  if (!isWXZFB.value) {
  } else {
    bus.emit("controllist", false);
    // 唤起 支付宝或者微信支付

    if (isZFB()) {
      console.log("支付宝-支付初始化");
      // zfbPay.redey(() => {});
    }

    if (isWX()) {
      // 初始化获取 code
      wxCode = getQueryString("code");
      // console.log("wxCode", wxCode);
      // if (!wxCode) {
      //   wxPay.redirectUrl();
      //   return;
      // }
    }
  }
});
onUnmounted(() => {
  clearInterval(poll);
});
</script>

<style lang="scss" scoped>
.pay {
  height: 100%;
  overflow-y: auto;
  background: var(--tp_dl_bg_color);

  .pay_content {
    overflow-y: auto;
    padding-bottom: 20px;
    box-sizing: border-box;
    height: calc(100% - 53px);
  }

  .top_area {
    min-height: 240px;
    display: flex;
    text-align: center;
    flex-direction: column;
    justify-content: center;

    .pay_status {
      padding: 20px 0;
      text-align: center;

      .iconfont {
        display: block;
        font-size: 80px;
      }

      .pay_text {
        font-size: 28px;
      }
    }

    .ewm_area {
      .text1 {
        font-size: 22px;
        padding-top: 25px;
      }

      .text2 {
        margin: 5px 0;
      }

      .ewm_img {
        width: 185px;
        height: 185px;
        margin: 30px auto 40px;
      }
    }
  }

  .btn_wrapper {
    margin: 25px 10px 0;
  }
}
</style>
