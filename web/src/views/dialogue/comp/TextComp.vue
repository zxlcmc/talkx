<script setup>
import hljs from "highlight.js";
import * as echarts from "echarts";
import { v4 as uuidv4 } from "uuid";
import MarkdownIt from "markdown-it";
// import minline from 'markdown-it-for-inline'
import mila from "markdown-it-link-attributes";
import mdKatex from "@traptitech/markdown-it-katex";
import thinkPlugin, {thinkHeaderClick} from '@/common/markdownPlugins/thinkPlugin'
import { useMessage } from "naive-ui";
import { useChatStore, useLoginStore } from "@/store";
import { copyToClip } from "@/common/copy";
import { getImgWH, optimizeKatex } from "@/common/utils";
import {
  onMounted,
  ref,
  computed,
  onUnmounted,
  watch,
  nextTick
} from "vue";
import { useCommunication, separate_symbol } from "@/hooks/useCommunication";
import { useToast } from "@/hooks/useToast";
// import EchartComp from "./EchartComp.vue";

import "photoswipe/style.css";
import PhotoSwipeLightbox from "photoswipe/lightbox";
import "photoswipe-dynamic-caption-plugin/photoswipe-dynamic-caption-plugin.css";
import PhotoSwipeDynamicCaption from "photoswipe-dynamic-caption-plugin";

let unTextWatch = ()=>{}
const textRef = ref(null);
const echartOptions = [];
const message = useMessage();
const useChat = useChatStore();
const useLogin = useLoginStore();
const { showToast } = useToast();
const { send } = useCommunication();
const themeObj = { dark: "dark", light: "" };

// const props = defineProps(["text", "type", "chat", "cursor", "mtype"]);
const props = defineProps({
  text: String,
  type: String,
  mtype: String,
  chat: {
    type: Array,
    default: [],
  },
  cursor: Boolean,
});
watch([() => props.chat[1], () => useChat.sending], () => {
  if (
    props.type == "answer" &&
    props.chat[1]?.showEchart &&
    !useChat.sending &&
    props.chat[1].content.includes("```echarts")
  ) {
    nextTick(() => {
      const echartIcons = document.querySelectorAll(
        `.text_wrapper[msg-id="${props.chat[1].id}"] .code-block-header__echart`
      );
      // echartOptions.forEach((e, i) => {
      //   e.show = false;  
      //   echartControl({ target: echartIcons[i] });
      // });
    });
  }
});


const getEchartDefaultConfig = (item)=>{
  let config = Object.assign({ 
      grid: {
        left: '20',
        right: '20',
        bottom: '20',
        containLabel: true
      }
  }, eval("(" + item.options + ")")  )
  return config
}

// 监听风格变化
watch(
  () => useLogin.theme,
  () => { 
    echartOptions.forEach(item=>{ 
      if (item.show && item.chart) {
        item.chart?.dispose()
        item.chart = echarts.init(item.chartDom, themeObj[useLogin.theme]);
        item.chart.setOption(getEchartDefaultConfig(item), true);
      }
    })
  }
);

const share = computed(() => props.mtype === "share");
const cLoading = computed(() => props.type == "answer" && props.loading);
const resize = () => {
  echartOptions.forEach((e) => {
    if (e.show && e.chart) {
      e.chart.resize();
    }
  });
};

const copyEvent = function (e) {
  const btn = e.target;
  const code = btn.parentElement?.nextElementSibling?.textContent;
  if (code) {
    copyToClip(code.trim()).then(() => message.success("复制成功"));
  }
};

const insetEvent = function (e) {
  const btn = e.target;
  const code = btn.parentElement?.nextElementSibling?.textContent;
  if (code) {
    send(
      "insert-code",
      code.trim(),
      () => {},
      (code, message) => {
        showToast("feature", message);
      }
    );
  }
};

const contrastEvent = function (e) {
  const btn = e.target;
  const code = btn.parentElement?.nextElementSibling?.textContent;
  if (code && props.chat[1]) {
    // 获取编辑器提问的信息
    const editer = props.chat[1].editer;
    console.log("对比", { editer, code });
    send("open-ai-response-in-diff-view", { editer, code: code.trim() });
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

const showEchart = (item, dom, hideChart) => {
  try {
    if (!item.chart) { 
      item.chart = echarts.init(dom, themeObj[useLogin.theme]);
      item.chartDom = dom;
    }

    //     item.options = `{
    //     title: {
    //         text: '学生成绩折线图'
    //     },
    //     tooltip: {
    //         trigger: 'axis'
    //     },
    //     xAxis: {
    //         type: 'category',
    //         data: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10'],
    //         name: '学生编号'
    //     },
    //     yAxis: {
    //         type: 'value',
    //         name: '成绩'
    //     },
    //     series: [{
    //         data: [85, 90, 78, 88, 92, 76, 81, 95, 89, 84],
    //         type: 'line',
    //         smooth: true
    //     }]
    // }`;
    // item.chart.setOption(JSON.parse(item.options)); 
    item.chart.setOption(getEchartDefaultConfig(item), true);
  } catch (err) {
    console.log("err", err);
    item.chart = null;
    console.log("options:", item.options);
    hideChart();
  }
};

const chartConfigArr = [
  { icon: "icon-chart-pie", title: "显示图表" },
  { icon: "icon-code", title: "显示源码" },
];
const updateIcon = (span, obj) => {
  const { icon, title } = chartConfigArr[+obj.show];
  const { icon: removeIcon } = chartConfigArr[+!obj.show];
  span.classList.remove(removeIcon);
  span.classList.add(icon);
  span.setAttribute("title", title);
};

const echartControl = (e) => {
  const span = e.target;
  const eid = span.getAttribute("eid");
  const code = span.parentElement?.nextElementSibling;
  const wrapper = span.parentElement?.nextElementSibling?.nextElementSibling;
  const obj = echartOptions.find((o) => o.id == eid);
  if (obj) {
    const hideChart = () => { 
      obj.show = false;
      updateIcon(span, obj);
      code.style.display = "block";
      wrapper.style.display = "none";
      obj.chart?.dispose();
      obj.chart = null;
      wrapper.innerHTML = "";
    };
    const showChart = () => {
      obj.show = true;
      updateIcon(span, obj);
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

const renderEchartIcon = (style, obj) => {
  const { id, show } = obj;
  const { icon, title } = chartConfigArr[+show];
  return `<span ${style} class="block code-block-header__echart iconfont ${icon}" eid="${id}" title="${title}"></span> `;
};

function highlightBlock(str, lang, extra, { code }) {
  const quize = props.type == "quize";
  const isEchartIcon = lang === "echarts";
  let quizeParams = {};
  if (extra) {
    extra.split(separate_symbol).reduce((prev, curr) => {
      const [key, value] = curr.split(":");
      prev[key] = value;
      return prev;
    }, quizeParams);
  }
  const editer = props.chat ? props.chat[1]?.editer : null; // && props.chat[1].editer.isDiffView;
  const style = `style="display:${useChat.sending ? "none" : "inline"};"`;
  // const style = `style="opacity:${useChat.sending ? "0.5" : "1"};"`;
  if (quize) {
    hideCodeArea();
  }
  let echartId = uuidv4();
  let echartIconHtml = "";
  if (isEchartIcon) {
    const option = { id: echartId, show: false, options: code };
    echartIconHtml = renderEchartIcon(style, option);
    echartOptions.push(option);
  }
  const spans = [];
  if (!quizeParams.fileName) {
    spans.push(`<span class="code-block-header__lang">${lang || ""}</span>`);
  }
  if (!quize) {
    spans.push(`<span ${style} class="block code-block-header__copy iconfont icon-file-copy" title="复制"></span>`);
    if (echartIconHtml) {
      spans.push(echartIconHtml);
    }
    if (!share.value) {
      if (!(useChat.isWeb || isEchartIcon)) {
        spans.push(`<span ${style} class="block code-block-header__inset iconfont icon-code" title="插入代码"></span>`);
        if (editer) {
          spans.push(`<span ${style} class="block code-block-header__contrast iconfont icon-columns" title="对比代码"></span>`);
        }
      }
    }
  }else{
    spans.push(`
      <span class="code-block-header__fileName">${quizeParams.fileName || ""} ${ quizeParams.line || ""}</span>
      <span class="code-block-header__unfold">隐藏</span>
		`)
  } 
  return `<pre class="code-block-wrapper">
      <div class="code-block-header ${props.type}">${spans.join('')}</div>
      <code class="hljs code-block-body ${lang}">${str}</code>
      ${ isEchartIcon ? `<div class="code_echarts_wrapper" eid="${echartId}" ></div>` : ""}
	</pre>`
  
  // return `<pre class="code-block-wrapper">
	// 	<div class="code-block-header ${props.type}">
	// 		${
  //       quizeParams.fileName
  //         ? ""
  //         : `<span class="code-block-header__lang">${lang || ""}</span>`
  //     }
	// 		${
  //       !quize ? `<span ${style} class="block code-block-header__copy iconfont icon-file-copy" title="复制"></span>
	// 			${
  //         // isEchartIcon
  //         //   ? `<span ${style} class="block code-block-header__echart iconfont icon-chart-pie" eid="${echartId}" title="显示图表"></span> `
  //         //   : ""
  //         echartIconHtml
  //       }
	// 			${
  //         share.value
  //           ? ``
  //           : `${
  //               useChat.isWeb || isEchartIcon
  //                 ? ""
  //                 : `<span ${style} class="block code-block-header__inset iconfont icon-code" title="插入代码"></span>
  //                 ${
  //                   editer
  //                     ? `<span ${style} class="block code-block-header__contrast iconfont icon-columns" title="对比代码"></span> `
  //                     : ""
  //                 }`
  //             }`
  //       }`
  //         : `
	// 			<span class="code-block-header__fileName">${quizeParams.fileName || ""} ${
  //             quizeParams.line || ""
  //           }</span>
	// 			<span class="code-block-header__unfold">隐藏</span>
	// 		`
  //     }
	// 	</div>
	// 	<code class="hljs code-block-body ${lang}">${str}</code>
  //   ${
  //     isEchartIcon
  //       ? `<div class="code_echarts_wrapper" eid="${echartId}" ></div>`
  //       : ""
  //   }
	// </pre>`;
}



watch([() => useChat.sending], () => {
  if(!props.cursor) return 
  const doms = document.querySelectorAll(
    ".code-block-header__echart,.code-block-header__copy,.code-block-header__inset,.code-block-header__contrast"
  );
  const cursor = document.querySelectorAll(".text_cursor");
  if (!doms) return;
  Array.from(doms).forEach((dom) => {
    dom.style.display = useChat.sending ? "none" : "inline";
  });
  if (!useChat.sending) {
    Array.from(cursor).forEach((dom) => dom.remove());
    props.cursor && initphotoswipe();
  } 
   
});

//  在不开启html的情况下 渲染span ？
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

// 解析规则 think
mdi.use(thinkPlugin)

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
let showTimer; 
const text = ref('')


const readerText = () => { 
  echartOptions.length = 0;
  let value = props.text ?? "";
  value = optimizeKatex(value)
  const tag = "P_P_P_P";
  if (props.cursor && useChat.sending) {
    value += tag;
  }
  if (!props.asRawText) {
    value = mdi.render(value);
  }
  value = value.replace(tag, `<span class="text_cursor"></span>`);
  value = preUnify(value);

  // 
  clearTimeout(showTimer)
  showTimer = setTimeout(() => {
    // 最后一个在回答在中的时候，不渲染echart图表
    if (textRef.value && props.type == "answer" && (!(useChat.sending && props.cursor) || !useChat.sending)) {
      const echartIcon = textRef.value.querySelectorAll(".code-block-header__echart");
      Array.from(echartIcon).forEach(d=>d && d.click())
    }
  });

  return value;
}
  
// 回答中就一直更新， 回答完成就停止更新
text.value = readerText()
if(props.cursor){
  if(useChat.sending){ 
    unTextWatch = watch([()=>props.text, () => useChat.sending], ()=>{        
        text.value = readerText()  
        if(!useChat.sending){   
          unTextWatch() 
          text.value = readerText()
          console.log('回答完成');
        } 
    })  
  }
} 

// const text = props.cursor ? computed(readerText) : ref(readerText());
 
const events = {
  ".code-block-header__copy": { click: [copyEvent] },
  ".code-block-header__echart": { click: [echartControl] },
  ".code-block-header__inset": { click: [insetEvent] },
  ".code-block-header__contrast": { click: [contrastEvent] },
  ".code-block-header__unfold": { click: [unfoldEvent] },
  ".think-header": { click: [thinkHeaderClick] },
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
  if (!lightbox && isImage && !useChat.sending) {
    clearTimeout(lightboxTime);
    lightboxTime = setTimeout(async () => {
      const options = {
        gallery: "#TextComp" + props.chat[1].id,
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
      // console.log("加载图片预览");
    }, 100);
  }
};
 

onMounted(() => { 
  initphotoswipe();
  window.addEventListener("resize", resize);
  bindEvent(); 
});

onUnmounted(() => {
  clearTimeout(showTimer);
  clearTimeout(lightboxTime);
  if (lightbox) {
    lightbox.destroy();
    lightbox = null;
  }
  window.removeEventListener("resize", resize);
  removeEvents.forEach((f) => f.remove());
});
</script>

<template>
  <div class="TextComp" ref="textRef" :id="'TextComp' + props.chat[1]?.id">
    <div class="markdown-body" v-html="text"></div>
    <!-- <EchartComp v-if="echartOptions.show" :options="echartOptions.options" /> -->
  </div>
</template>
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

    .code-block-header__copy {
      cursor: pointer;
      /* background-image: url(@/assets/icons/copy.png); */
    }

    .code-block-header__inset {
      cursor: pointer;
      /* background-image: url(@/assets/icons/inset.png); */
    }

    .code-block-header__contrast {
      cursor: pointer;
      /* background-image: url(@/assets/icons/contrast.png); */
    }
    .code-block-header__echart {
      cursor: pointer;
    }
  }
}
</style>
<style lang="scss" scoped>
.TextComp {
  padding: 2px;
  /* color: #fff; */

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
