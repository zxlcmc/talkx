<template>
  <div class="agreement tpage">
    <div class="topbar flex_b flex_sx_center">
      <div class="flex_sx_center back hover_bg" tabindex="0" @click="back">
        <span class="iconfont icon-rollback"></span>
        <span class="text">返回</span>
      </div>
      <div class="right"></div>
    </div>

    <div class="xy_content">
      <div class="xy_title">{{ title }}</div>
      <div class="markdown-body" v-html="text"></div>
    </div>
  </div>
</template>

<script setup>
import MarkdownIt from "markdown-it";
import mila from "markdown-it-link-attributes";
import mdKatex from "@traptitech/markdown-it-katex";
import { computed, ref, watchEffect, inject } from "vue";
import { useRoute, useRouter } from "vue-router";
import { routerBack } from "@/common/utils";

const route = useRoute();
const router = useRouter();
const resize = inject("resize");
const argeementText = ref("");
const mdFiles = import.meta.glob("@/assets/agreement/*.md", { query: "raw" });

const listArr = [
  { type: "user", text: "用户协议" },
  { type: "privacy", text: "隐私条款" },
  { type: "content", text: "内容安全协议" },
  { type: "pay", text: "支付协议" },
];

const title = computed(() => {
  const obj = listArr.find((c) => route.query.type == c.type);
  return obj ? obj.text : "";
});

const back = () => {
  // router.back();
  // router.push({ name: "userEdit", query: { type: 'aboutus' } })
  const params = { name: "userEdit", query: { type: "aboutus" } };
  if (!resize.smallRef.value) {
    params.name = "d_userEdit";
  }
  routerBack(router, params);
};

watchEffect(() => {
  Object.keys(mdFiles).find((key) => {
    if (key.includes(route.query.type + ".md")) {
      mdFiles[key]().then((res) => (argeementText.value = res.default));
      return true;
    }
  });
});

const mdi = new MarkdownIt({
  html: true,
  linkify: false,
});

mdi.use(mila, { attrs: { target: "_blank", rel: "noopener" } });
mdi.use(mdKatex, {
  blockClass: "katexmath-block rounded-md p-[10px]",
  errorColor: " #cc0000",
});

const text = computed(() => mdi.render(argeementText.value));
</script>

<style lang="scss" scoped>
.agreement {
  height: 100%;
  background: var(--tp_dl_bg_color);
  .xy_content {
    padding: 20px;
    box-sizing: border-box;
    overflow-y: auto;
    height: calc(100% - 50px);

    .xy_title {
      font-size: 25px;
      font-weight: bold;
      text-align: center;
      margin-top: -25px;
      margin-bottom: 25px;
    }

    .markdown-body {
      background-color: inherit;
    }
  }
}
</style>
