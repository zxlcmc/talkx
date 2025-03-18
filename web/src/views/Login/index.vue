<script setup>
import { Icon } from "@vicons/utils";
import { onMounted, ref } from "vue";
import { useRouter, useRoute } from "vue-router";
import { IosInformationCircle } from "@vicons/ionicons4";
import { NInput, NButton, useMessage, NCheckbox } from "naive-ui";
import { sign } from "@/api/index";
import { checkUser } from "@/api/user";
import { inputStyle, checkboxStyle } from "@/common/style";
import { verifyFun } from "@/common/verify";

const route = useRoute();
const router = useRouter();
const message = useMessage();
const phone = ref("");
const isSend = ref(false);
const inputRef = ref(null);
const selectAgreement = ref(false);
const verityText = ref("请输入正确的手机号码");

onMounted(() => {
  inputRef.value && inputRef.value.focus();
});

const verityFun = (value = phone.value) => {
  if (!value) {
    verityText.value = "请输入正确的手机号码";
    return;
  }
  // const reg = /^(?:(?:\+|00)86)?1(?:(?:3[\d])|(?:4[5-79])|(?:5[0-35-9])|(?:6[5-7])|(?:7[0-8])|(?:8[\d])|(?:9[1589]))\d{8}$/
  // if (!reg.test(value)) {
  if (verifyFun.phoneRule(value)) {
    verityText.value = "请输入正确的手机号码";
    return;
  }
  verityText.value = "";
  return true;
};

const allowInput = (value) => {
  verityFun(value);
  return true;
};

const sendCode = async () => {
  const { err, errMsg } = await sign(phone.value);
  if (err) {
    return message.error(errMsg);
  }
  message.success("验证码发送成功");
};

const next = async () => {
  if (!selectAgreement.value) {
    message.warning("请仔细阅读协议并同意");
    return;
  }
  if (!verityFun() || isSend.value) return;
  isSend.value = true;
  const { exist, errMsg } = await checkUser({ phoneNum: phone.value });
  if (errMsg) {
    isSend.value = false;
    return message.error(errMsg);
  }
  const query = { phone: phone.value };
  if (!exist) {
    if (route.query.inviteCode) {
      query.inviteCode = route.query.inviteCode;
    }
    router.push({ name: "invite", query });
  } else {
    await sendCode();
    router.push({ name: "verify", query });
  }
  isSend.value = false;
};

const skipAgree = (type) => {
  router.push({ name: "agreement", query: { type } });
};
</script>

<template>
  <div class="login tpage">
    <!-- <img class="logo" src="@/assets/talkx.svg" alt="" /> -->
    <div class="title">欢迎使用 TalkX</div>
    <p class="desc">注册一个新的或登录你已经存在的账号。</p>
    <div class="row">
      <div class="row_title">使用你的手机号码注册或登录</div>
      <NInput
        ref="inputRef"
        v-model:value="phone"
        :style="inputStyle"
        type="phone"
        placeholder=""
        :allow-input="allowInput"
      />
      <div class="info" v-if="verityText">
        <Icon size="20" style="vertical-align: sub">
          <IosInformationCircle />
        </Icon>
        <span class="text">{{ verityText }}</span>
      </div>
    </div>
    <NButton block type="info" class="btn" @click="next">继续</NButton>
    <div class="Agreement">
      <div class="icon">
        <n-checkbox :style="checkboxStyle" v-model:checked="selectAgreement" />
      </div>
      <div class="text">
        我同意<span @click="skipAgree('user')">《用户协议》</span>、<span
          @click="skipAgree('privacy')"
          >《隐私条款》</span
        >和<span @click="skipAgree('content')">《内容安全协议》</span>
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.login {
  padding: 40px;

  .logo {
    width: 48px;
  }

  .title {
    font-size: 24px;
    font-weight: bold;
    margin: 20px 0;
  }

  .desc {
    margin: 20px 0 30px;
  }

  .row {
    .row_title {
      margin-bottom: 10px;
    }

    .info {
      margin-top: 10px;

      .text {
        margin-left: 5px;
        vertical-align: 1px;
      }
    }
  }

  .Agreement {
    display: flex;
    margin-top: 10px;
    line-height: 24px;
    .text {
      padding-left: 5px;
      span {
        cursor: pointer;
      }
    }
  }

  .btn {
    height: 36px;
    margin-top: 30px;
  }
}
</style>
