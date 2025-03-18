<template>
  <div class="TextComp answer" ref="textRef" :id="'TextComp' + props.data.id">
    <div class="markdown-body" v-html="text"></div>
  </div>
</template>

<script setup>
import hljs from "highlight.js";
import * as echarts from "echarts";
import { v4 as uuidv4 } from "uuid";
import MarkdownIt from "markdown-it";
import mila from "markdown-it-link-attributes";
import mdKatex from "@traptitech/markdown-it-katex";
import { copyToClip } from "@/common/copy";
import { getImgWH, optimizeKatex } from "@/common/utils";
import { useMessage } from "naive-ui";
import { ref, computed, onMounted, onUnmounted } from "vue";
import { useCommunication, separate_symbol } from "@/hooks/useCommunication";
import "photoswipe/style.css";
import PhotoSwipeLightbox from "photoswipe/lightbox";
import "photoswipe-dynamic-caption-plugin/photoswipe-dynamic-caption-plugin.css";
import PhotoSwipeDynamicCaption from "photoswipe-dynamic-caption-plugin";


const echartOptions = [];
const message = useMessage();
const props = defineProps({
  text: {
    type: String,
    default: "",
  },
  data: {
    type: Object,
    default: () => ({}),
  },
  cursor: Boolean,
});

const textRef = ref(null);

function highlightBlock(str, lang, extra, { code }) {
  const isEchartIcon = lang === "echarts";
  let quizeParams = {};
  if (extra) {
    extra.split(separate_symbol).reduce((prev, curr) => {
      const [key, value] = curr.split(":");
      prev[key] = value;
      return prev;
    }, quizeParams);
  }
  const style = `style="display:inline"`;
  let echartId = uuidv4();
  if (isEchartIcon) {
    echartOptions.push({ id: echartId, show: false, options: code });
  }

  const list = [];
  if (!quizeParams.fileName) {
    list.push(`<span class="code-block-header__lang">${lang || ""}</span>`);
  }
  list.push(
    `<span class="block code-block-header__copy iconfont icon-file-copy" title="复制"></span>`
  );
  if (isEchartIcon) {
    list.push(
      `<span ${style} class="block code-block-header__echart iconfont icon-chart-pie" eid="${echartId}" title="显示图表"></span>`
    );
  }
  if (quizeParams.fileName) {
    list.push(
      `<span class="code-block-header__fileName">${
        quizeParams.fileName || ""
      } ${quizeParams.line || ""}</span>`
    );
  }
  list.push(`<span class="code-block-header__unfold">隐藏</span>`);
  return `<pre class="code-block-wrapper">
		<div class="code-block-header answer"> 
           ${list.join("")}
        </div>
        <code class="hljs code-block-body ${lang}">${str}</code>
        ${
          isEchartIcon
            ? `<div class="code_echarts_wrapper" eid="${echartId}" ></div>`
            : ""
        } 
    </pre>`;
}

const mdi = new MarkdownIt({
  html: false, // 防止xss
  linkify: true,
  highlight(code, language, extra) {
    const validLang = !!(language && hljs.getLanguage(language));
    if (validLang) {
      const lang = language ?? "";
      return highlightBlock(
        hljs.highlight(code, { language: lang }).value,
        lang,
        extra,
        { code }
      );
    }
    const str = hljs.highlightAuto(code).value;
    return highlightBlock(str, language, undefined, { code });
  },
});

let isImage = false;
mdi.renderer.rules.image = function (tokens, idx, options, env, self) {
  // 获取图片的地址和描述
  const src = tokens[idx].attrs[tokens[idx].attrIndex("src")][1];
  const alt = tokens[idx].content;
  // 自定义渲染图片的方式
  // return `<img class="custom-img" src="${src}" alt="${alt}">`;
  isImage = true;
  let attrs = "";
  const obj = getImgWH(src);
  if (!(obj instanceof Promise)) {
    const { width, height } = obj;
    attrs = `data-pswp-width="${width}" data-pswp-height="${height}"`;
  }
  return `<div class="gallery__item" url="${src}">
      <a class="md_img" href="${src}" target="_blank" rel="noreferrer" ${attrs} >
      <img src="${src}?image_process=resize,w_150"  alt="${alt}" />
    </a>
  </div>`;
};

mdi.use(mila, { attrs: { target: "_blank", rel: "noopener" } });
mdi.use(mdKatex, {
  blockClass: "katexmath-block rounded-md p-[10px]",
  errorColor: " #cc0000",
});

// 处理没有经过 highlight 方法生成的 pre/code 标签
const preUnify = (str) => {
  const div = document.createElement("div");
  div.innerHTML = str;
  const pres = div.querySelectorAll("pre");
  Array.from(pres).forEach((pre) => {
    const isHand = pre.className.includes("code-block-wrapper");
    if (!isHand) {
      const codeDom = pre.querySelector("code");
      const pj = "```\n" + codeDom.innerText + "```";
      pre.outerHTML = mdi.render(pj);
    }
  });
  return div.innerHTML;
};

let showTimer
const text = computed(() => {
  echartOptions.length = 0;
  let value = props.text ?? "";
  value = optimizeKatex(value)
  const tag = "P_P_P_P";
  if (props.cursor) {
    value += tag;
  }
  if (!props.asRawText) {
    value = mdi.render(value);
  }
  value = value.replace(tag, `<span class="text_cursor"></span>`);
  value = preUnify(value);

  console.log('xxxxxx');
  clearTimeout(showTimer)
  showTimer = setTimeout(() => {
    // 最后一个在回答在中的时候，不渲染echart图表
    if (textRef.value) {
      const echartIcon = textRef.value.querySelectorAll(".code-block-header__echart");
      Array.from(echartIcon).forEach(d=>d && d.click())
    }
  }, 100);

  return value;
});

const copyEvent = function (e) {
  const btn = e.target;
  const code = btn.parentElement?.nextElementSibling?.textContent;
  if (code) {
    copyToClip(code.trim()).then(() => message.success("复制成功"));
  }
};

const unfoldEvent = function (e) {
  const btn = e.target;
  const codeDom = btn.parentElement?.nextElementSibling;
  const wrapper = btn.parentElement?.parentElement;
  if (codeDom) {
    // 隐藏 显示
    const show = btn.textContent == "显示";
    btn.textContent = show ? "隐藏" : "显示";
    codeDom.classList[show ? "remove" : "add"]("hide");
    wrapper.style.overflow = show ? "auto" : "hidden";
  }
};

const showEchart = (item, dom, hideChart) => {
  try {
    if (!item.chart) {
      item.chart = echarts.init(dom);
    }
    let config = Object.assign({ 
      grid: {
        left: '20',
        right: '20',
        bottom: '20',
        containLabel: true
      }
    }, eval("(" + item.options + ")")  )
    item.chart.setOption(config, true);
  } catch (err) {
    console.log("err", err);
    item.chart = null;
    console.log("options", item.options);
    hideChart();
  }
};

const echartControl = (e) => {
  const span = e.target;
  const eid = span.getAttribute("eid");
  const code = span.parentElement?.nextElementSibling;
  const wrapper = span.parentElement?.nextElementSibling?.nextElementSibling;
  const obj = echartOptions.find((o) => o.id == eid);
  const arr = [
    { icon: "icon-chart-pie", title: "显示图表" },
    { icon: "icon-code", title: "显示源码" },
  ];
  if (obj) {
    const common = () => {
      const { icon, title } = arr[+obj.show];
      const { icon: removeIcon } = arr[+!obj.show];
      span.classList.remove(removeIcon);
      span.classList.add(icon);
      span.setAttribute("title", title);
    };
    const hideChart = () => {
      obj.show = false;
      common();
      code.style.display = "block";
      wrapper.style.display = "none";
      obj.chart?.dispose();
      obj.chart = null;
      wrapper.innerHTML = "";
    };
    const showChart = () => {
      obj.show = true;
      common();
      code.style.display = "none";
      wrapper.style.display = "block";
      showEchart(obj, wrapper, () => {
        // hideChart();
        // span.remove();
      });
    };
    obj.show ? hideChart() : showChart();
  }
};

const hideCodeArea = async (e) => {
  await nextTick();
  const targets = document.querySelectorAll(
    ".MessageBody .quize .code-block-header__unfold"
  );
  Array.from(targets).forEach((target) => {
    target.textContent = "隐藏";
    unfoldEvent({ target });
  });
};

const events = {
  ".code-block-header__copy": { click: [copyEvent] },
  ".code-block-header__echart": { click: [echartControl] },
  ".code-block-header__unfold": { click: [unfoldEvent] },
};

let removeEvents = [];
const bindEvent = () => {
  if (textRef.value) {
    removeEvents.forEach((f) => f.remove());
    Object.entries(events).forEach(([selector, value]) => {
      Object.entries(value).forEach(([eventType, eventHandlers]) => {
        const event = (e) => {
          if (e.target.classList.value.includes(selector.split(".")[1])) {
            eventHandlers.forEach((fun) => fun(e));
          }
        };
        textRef.value.addEventListener(eventType, event);
        removeEvents.push({
          selector,
          eventType,
          remove: () => {
            textRef.value &&
              textRef.value.removeEventListener(eventType, event);
          },
        });
      });
    });
  }
};

let lightbox;
let lightboxTime;
const initphotoswipe = () => {
  if (!lightbox && isImage) {
    clearTimeout(lightboxTime);
    lightboxTime = setTimeout(async () => {
      const options = {
        gallery: "#TextComp" + props.data.id,
        childSelector: ".gallery__item",
        pswpModule: () => import("photoswipe"),
        paddingFn: (viewportSize) => {
          // 滑动区域填充
          return { top: 30, bottom: 30, left: 70, right: 70 };
        },
      };
      const items = document.querySelectorAll(".gallery__item");
      Array.from(items).map(async (dom) => {
        const url = dom.getAttribute("url");
        const a = dom.querySelector("a");
        const { width, height } = await getImgWH(url);
        a.setAttribute("data-pswp-width", width);
        a.setAttribute("data-pswp-height", height);
      });
      lightbox = new PhotoSwipeLightbox(options);
      lightbox.init();
    }, 100);
  }
};

const resize = () => {
  echartOptions.forEach((e) => {
    if (e.show && e.chart) {
      e.chart.resize();
    }
  });
};

onMounted(() => {
  initphotoswipe();
  window.addEventListener("resize", resize);
  bindEvent();
});

onUnmounted(() => {
  clearTimeout(showTimer)
  clearTimeout(lightboxTime);
  if (lightbox) {
    lightbox.destroy();
    lightbox = null;
  }
  window.removeEventListener("resize", resize);
  removeEvents.forEach((f) => f.remove());
});
</script>

<style lang="scss">
@import url(./TextStyle.scss);

.gallery__item {
  display: inline-block;
  margin-right: 10px;
  .md_img {
    img {
      max-width: 300px;
      transition: all 0.2s;
    }
    &:hover img {
      transform: scale(1.05);
    }
  }
}

.quize {
  .markdown-body {
    color: var(--tp_message_quize_color);
  }
}
.markdown-body code.hljs {
  /* padding-left: 0;
  padding-right: 0; */
  margin-bottom: -50px;
}
.markdown-body .highlight pre,
.markdown-body pre {
  padding: 10px;
  border-radius: 0px;
}

.markdown-body .code-block-wrapper {
  padding-top: 26px;
  padding-left: 0;
  padding-right: 0;
}

.markdown-body .code_echarts_wrapper {
  display: none;
  width: 100%;
  height: 400px;
  overflow: auto;
  margin: -48px 0 -35px;
}

.markdown-body .code-block-header__unfold:hover,
.markdown-body .code-block-header__inset:hover,
.markdown-body .code-block-header__contrast:hover,
.markdown-body .code-block-header__copy:hover {
  color: var(--tp_pre_icon_hover_color);
}

.answer {
  &.status_500 {
    .markdown-body {
      color: var(--tp_500_color);
    }
  }

  .markdown-body {
    color: var(--tp_message_answer_color);

    span.text_cursor {
      display: inline-block;
      width: 0;
      vertical-align: -4px;
      height: 18px;
      margin-left: 5px;
      border-left: 1px var(--tp_textcolor) solid;
      animation: cursor 1s steps(1) infinite;

      @keyframes cursor {
        0%,
        100% {
          opacity: 1;
        }

        50% {
          opacity: 0;
        }
      }
    }
  }
}

pre.code-block-wrapper {
  .block {
    margin: 0 3px;
    background-size: 100% 100%;
    width: 16px;
    height: 16px;

    &.iconfont {
      margin-top: -6px;
    }
  }

  .code-block-body {
    transition: 0.2s;

    &.hide {
      height: 0;
      padding-top: 0;
      padding-bottom: 0;
    }
  }

  .code-block-header {
    color: var(--tp_pre_icon_color);
    top: 0;
    padding-top: 5px;
    padding-bottom: 4px;
    border-bottom: 1px solid var(--tp_bordecolor);
    background-color: var(--tp_pre_header_bgcolor);

    &.quize {
      justify-content: space-between;
    }

    .code-block-header__unfold {
      cursor: pointer;
      user-select: none;
    }

    .code-block-header__lang {
      margin-right: 5px;
    }

    .code-block-header__copy,
    .code-block-header__inset,
    .code-block-header__contrast,
    .code-block-header__echart {
      cursor: pointer;
    }
  }
}
</style>
<style lang="scss" scoped>
.TextComp {
  padding: 2px;

  .markdown-body {
    min-height: 21px;
  }

  .text_loading {
    .loading_svg {
      width: 20px;
    }
  }
}
</style>
