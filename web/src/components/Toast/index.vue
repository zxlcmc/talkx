<script setup>
import { useRouter } from "vue-router";
import { copyToClip } from "@/common/copy";
import { NDrawer, NDrawerContent, NButton, useMessage } from "naive-ui";
import { computed, onUnmounted, watchEffect } from "vue";
const props = defineProps([
  "show",
  "type",
  "text",
  "closeText",
  "confirmText",
  "maskclosable",
  "escclose",
  "others",
]);
const emit = defineEmits(["update:show", "close"]);

const router = useRouter();
const message = useMessage();
const style = {
  "--n-text-color": "var(--n-text-color)",
  color: "var(--tp_textcolor)",
  "background-color": "var(--tp_bgcolor)",
};

const toLogin = () => {
  emit("update:show", false);
  const query = {};
  const inviteCode = localStorage.getItem("dialogue_inviteCode");
  if (inviteCode) {
    query["inviteCode"] = inviteCode;
  }
  router.push({ name: "login", query });
};

const close = (confirm) => {
  emit("update:show", false);
  emit("close", { type: props.type, confirm });
};

const enterFun = (e) => {
  if (e.keyCode === 13) {
    // enter 确认
    props.type === "login" ? toLogin() : close(true);
  }
};

const removeWatch = () => {
  document.removeEventListener("keydown", enterFun);
};

const watchEnter = () => {
  removeWatch();
  document.addEventListener("keydown", enterFun);
};

const stop = watchEffect(() => {
  if (props.show) {
    watchEnter();
  } else {
    removeWatch();
  }
});

const height = computed(() => {
  return props.type === "copy" ? "150px" : "106px";
});

onUnmounted(stop);
</script>

<template>
  <NDrawer
    :style="style"
    :show="props.show"
    @update:show="close(false)"
    :width="502"
    :height="height"
    :mask-closable="props.maskclosable"
    :close-on-esc="props.escclose"
    placement="bottom"
  >
    <NDrawerContent>
      <div class="wrapper">
        <template v-if="props.type === 'feature'">
          <div class="desc" @click="close(false)">
            {{
              props.text || "请先选择一些代码，然后点击快捷键以获取所需的输出。"
            }}
          </div>
          <div class="footer">
            <NButton
              type="error"
              style="background-color: var(--tp_btn_error)"
              @click="close(true)"
              >好的</NButton
            >
          </div>
        </template>
        <template v-else-if="props.type === 'login'">
          <div class="desc">{{ props.text || "需要登录才能继续使用。" }}</div>
          <div class="footer">
            <NButton class="close_btn" text @click="close(false)">取消</NButton>
            <NButton
              type="error"
              style="background-color: var(--tp_btn_error)"
              @click="toLogin"
              >去登录</NButton
            >
          </div>
        </template>
        <template v-else-if="props.type === 'logout'">
          <div class="desc">
            {{
              props.text ||
              "无登录状态下产生的话题记录不会保存，你确定要这样做吗？"
            }}
          </div>
          <div class="footer">
            <NButton class="close_btn" text @click="close(false)">取消</NButton>
            <NButton
              type="error"
              style="background-color: var(--tp_btn_error)"
              @click="close(true)"
              >确定</NButton
            >
          </div>
        </template>
        <template v-else-if="props.type === 'del'">
          <div class="desc">
            {{ props.text || "您正在删除话题记录，确定要这样做吗？" }}
          </div>
          <div class="footer">
            <NButton class="close_btn" text @click="close(false)">取消</NButton>
            <NButton
              type="error"
              style="background-color: var(--tp_btn_error)"
              @click="close(true)"
            >
              确认
            </NButton>
          </div>
        </template>
        <template v-else-if="props.type === 'nodel'">
          <div class="desc">
            {{ props.text || "不能删除当前话题，因为正在使用。" }}
          </div>
          <div class="footer">
            <NButton
              type="error"
              style="background-color: var(--tp_btn_error)"
              @click="close(true)"
              >{{ props.confirmText || "好的" }}</NButton
            >
          </div>
        </template>
        <template v-else-if="props.type === 'auto'">
          <div class="desc">{{ props.text }}</div>
          <div class="footer">
            <NButton
              v-if="props.closeText"
              class="close_btn"
              text
              @click="close(false)"
              >{{ props.closeText }}
            </NButton>
            <NButton
              v-if="props.confirmText"
              type="error"
              style="background-color: var(--tp_btn_error)"
              @click="close(true)"
              >{{ props.confirmText }}</NButton
            >
          </div>
        </template>
        <template v-else-if="props.type === 'update_system'">
          <div class="desc">
            {{
              props.text ||
              "太棒了！为了给你带来更好的产品体验，我们又发布了新版本。"
            }}
          </div>
          <div class="footer">
            <NButton
              type="error"
              style="background-color: var(--tp_btn_error)"
              @click="close(true)"
              >{{ props.confirmText || "立即更新" }}</NButton
            >
          </div>
        </template>
        <template v-else-if="props.type === 'copy'">
          <div class="desc">
            <div>分享链接</div>
            {{ props.text }}
          </div>
          <div class="footer">
            <NButton
              type="error"
              style="background-color: var(--tp_btn_error)"
              @click="close(true)"
              >{{ props.confirmText || "确认" }}</NButton
            >
          </div>
        </template>
      </div>
    </NDrawerContent>
  </NDrawer>
</template>

<style lang="scss" scoped>
.wrapper {
  height: 100%;
  position: relative;
  padding-bottom: 34px;
  box-sizing: border-box;
}
.desc {
  font-size: var(--tp_size);
  color: var(--tp_textcolor);
  min-height: 40px;
}

.footer {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 34px;
  display: flex;
  justify-content: flex-end;

  button {
    width: 100px;
    /* color: #fff; */
    margin-left: 15px;
  }

  button.close_btn,
  button.close_btn:not(.n-button--disabled):focus {
    color: var(--tp_toast_color);
  }

  button:focus,
  button:focus-visible {
    outline: none;
    color: #fff;
  }
}
</style>
