<template>
  <div class="userEdit tpage">
    <div class="topbar flex_b flex_sx_center">
      <div class="flex_sx_center back hover_bg" tabindex="0" @click="back">
        <span v-if="(!isPhone || phoneObj.step == 0) && !hashrate" class="text"
          >取消</span
        >
        <template v-else>
          <span class="iconfont icon-rollback"></span>
          <span class="text">返回</span>
        </template>
      </div>
      <div class="right" v-if="!hashrate">
        <NButton block type="info" class="btn" @click="over">{{
          rightText
        }}</NButton>
      </div>
    </div>

    <NInput
      v-if="curr"
      ref="inputRef"
      v-model:value="text"
      :style="style"
      :placeholder="curr.placeholder"
    />

    <div class="phone_area" v-else-if="type === 'phone'">
      <div class="phone_info">{{ currPhone.info() }}</div>
      <NInput
        ref="inputRef"
        style="text-align: center"
        :disabled="isClick || currPhone.disabled"
        v-model:value="text"
        :style="style"
        :placeholder="currPhone.obj.placeholder"
      />
    </div>
    <Faceback
      ref="facebackRef"
      v-else-if="type === 'faceback'"
      :inputstyle="style"
    />
    <Model v-else-if="type === 'model'" v-model:selected="selectedModel" />
    <AboutUs v-else-if="type === 'aboutus'" />
    <MyHashrate v-else-if="type === 'my_hashrate'" />
    <InviteRewards v-else-if="type === 'invite_rewards'" />
    <HashrateRecord v-else-if="type === 'hashrate_record'" />

    <ProxyUrl v-if="type === 'proxy_base_url'" />
  </div>
</template>

<script setup>
import phoneVerify from "./phoneVerify.js";
import { verConfig } from "./verifyConfig.js";
import Model from "./comp/Model.vue";
import AboutUs from "./comp/AboutUs.vue";
import Faceback from "./comp/Faceback.vue";
import ProxyUrl from "./comp/ProxyUrl.vue";
import MyHashrate from "./comp/MyHashrate.vue";
import InviteRewards from "./comp/InviteRewards.vue";
import HashrateRecord from "./comp/HashrateRecord.vue";
import { feedback } from "@/api/user";
import { useLoginStore } from "@/store";
import { verify } from "@/common/verify";
import { inputStyle } from "@/common/style";
import { routerBack } from "@/common/utils";
import { useRoute, useRouter } from "vue-router";
import { NInput, NButton, useMessage } from "naive-ui";
import { computed, inject, nextTick, onMounted, ref } from "vue";
const route = useRoute();
const router = useRouter();
const message = useMessage();
const useLogin = useLoginStore();
const resize = inject("resize");

const type = computed(() => route.query.type);
const { isPhone, phonePrev, phoneObj, phoneOver, currPhone } = phoneVerify(
  type.value,
  message
);
const rightText = computed(() => {
  let text = "完成";
  if (isPhone.value) {
    text = currPhone.value.rightText;
  } else if (type.value === "faceback") {
    text = "提交";
  }
  return text;
});
const style = Object.assign(inputStyle, {
  fontSize: "var(--tp_size)",
  lineHeight: "50px",
  "--n-border": "none",
  "--n-border-hover": "none",
  "--n-border-focus": "none",
  backgroundColor: "var(--tp_footer_bgcolor)",
  "--n-border-radius": "0px",
  "--n-border-disabled": "none",
});

const text = ref("");
const inputRef = ref(null);
const facebackRef = ref(null);
const hashrate = computed(() =>
  ["aboutus", "invite_rewards", "my_hashrate", "hashrate_record"].includes(
    type.value
  )
);
const typeObj = {
  name: verConfig.name,
  email: verConfig.email,
  api_key: verConfig.ApiKey,
  proxy_base_url: verConfig.proxy_base_url,
};
const curr = computed(() => typeObj[type.value]);
const inputFocus = () =>
  nextTick(() => inputRef.value && inputRef.value.focus());
onMounted(() => inputFocus());

const selectedModel = ref("");
useLogin.getUser().then((res) => {
  if (curr.value) {
    let attr = type.value;
    const obj = {api_key:'apiKey', proxy_base_url: 'proxyBaseUrl'}
    // attr = attr === "api_key" ? "apiKey" : attr;
    attr = obj[attr] || attr;
    text.value = res[attr]; 
  } else if (isPhone.value && phoneObj.step == 0) {
    text.value = res.phoneNum;
  } else if (type.value === "model") {
    selectedModel.value = res.model;
  }
});

let isClick = ref(false);
const back = () => {
  if (isClick.value) return;
  isClick.value = true;
  if (isPhone.value) {
    const prevN = phoneObj.step == 2 ? 2 : 1;
    if (!phonePrev(prevN)) {
      if (phoneObj.step === 0) {
        text.value = currPhone.value.value;
      }
      isClick.value = false;
      return;
    }
  }
  let name = "user";
  if (!resize.smallRef.value) {
    name = "d_" + name;
  }
  routerBack(router, { name });
};

const errHnadler = (msg) => {
  isClick.value = false;
  message.warning(msg);
};

const over = async () => {
  if (isClick.value) return;
  isClick.value = true;
  // 校验  名称，邮箱，apikey, 代理地址
  if (curr.value) {
    const value = text.value.trim();
    const errMsg = await verify(value, curr.value.verity);
    if (errMsg) {
      return errHnadler(errMsg);
    }
    let res = await useLogin.setUser({ field: type.value, value });
    if (res.errMsg) {
      return errHnadler(res.errMsg);
    }
    // message.success('修改成功')
  } else if (isPhone.value) {
    const value = text.value.trim();
    const { all, result, phoneNum } = await phoneOver(value);
    if (!result) {
      return (isClick.value = false);
    } // 单流程验证失败
    if (result && !all) {
      // 单流程验证成功，进入下一个流程
      text.value = "";
      inputFocus();
      isClick.value = false;
      return;
    }
    let res = await useLogin.setUser({
      field: "phone_num",
      value: phoneNum,
      extra: value,
    });
    if (res.errMsg) {
      // phonePrev()
      return errHnadler(res.errMsg);
    }
    message.success("手机号修改成功");
  } else if (type.value === "model") {
    const { errMsg } = await useLogin.setUser({
      field: type.value,
      value: selectedModel.value,
    });
    if (errMsg) {
      return errHnadler(errMsg);
    }
    // message.success('模型切换成功')
  } else if (type.value === "faceback") {
    const { err, errMsg, data } = await facebackRef.value.getForm();
    if (errMsg) {
      return errHnadler(errMsg);
    }
    const res = await feedback(data);
    if (res.errMsg) {
      return errHnadler(res.errMsg);
    }
    message.success("反馈成功，感谢你的反馈");
  }
  isClick.value = false;

  let name = "user";
  if (!resize.smallRef.value) {
    name = "d_" + name;
  }
  router.push({ name });
};
</script>

<style lang="scss">
.userEdit {
  height: 100%;
  background: var(--tp_dl_bg_color);

  .phone_area {
    .phone_info {
      padding: 50px 30px;
    }
  }
}
</style>
