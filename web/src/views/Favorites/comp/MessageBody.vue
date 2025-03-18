<template>
  <div class="messag-body">
    <div class="header flex_b">
      <div class="time">
        {{ format(new Date(data.createTime), "yyyy-MM-dd hh:mm:ss") }}
      </div>
      <div class="right">
        <span
          class="hover_light iconfont icon-delete"
          @click="control('delete')"
          title="删除"
        ></span>
        <span
          class="hover_light iconfont icon-file-copy"
          @click="control('copy')"
          title="复制"
        ></span>
      </div>
    </div>
    <div class="text_wrapper" :msg-id="data.id">
      <!-- 文件列表暂时不需要支持 -->
      <!-- <FileListComp v-if="others.length" :list="others" /> -->
      <!-- <SimpleGallery
        v-if="imgs.length"
        :galleryID="'gallery' + data.id"
        :images="imgs"
      /> -->
      <TextComp :text="data.content" :data="data" />
    </div>
  </div>
</template>

<script setup>
import { useMessage } from "naive-ui";
import { format } from "@/common/utils";
import { copyToClip } from "@/common/copy";
import { ref, inject, onUnmounted, onMounted } from "vue";
import FileListComp from "@/views/dialogue/comp/FileListComp.vue";
import SimpleGallery from "@/components/SimpleGallery/index.vue";
import TextComp from "@/components/Chat/TextComp.vue";

const imgs = ref([]);
const others = ref([]);
const message = useMessage();
const resize = inject("resize");
const props = defineProps({
  data: {
    type: Object,
    default: () => {},
  },
});
const { data } = props;
const emits = defineEmits(["control"]);
let isStop = false;
const computedImgs = () => {
  imgs.value = [];
  others.value = [];
  // TODO 文件处理
  const attachments = data.attachments || [];
  attachments.forEach(async (obj) => {
    const { mimeType, url: src } = obj;
    const img = IMG_TYPES.includes(mimeType);
    if (img) {
      const { width, height } = await getImgWH(src);
      const params = {
        width,
        height,
        src,
        thumbnailURL: src + "?image_process=resize,w_150",
      };
      if (isStop) return;
      imgs.value.push(params);
    } else {
      others.value.push(obj);
    }
  });
};

const control = async (type) => {
  const { content } = props.data;
  switch (type) {
    case "delete":
      emits("control", { type: "delete", data: props.data });
      break;
    case "copy":
      await copyToClip(content);
      message.success("复制成功！");
      break;

    default:
      break;
  }
};

onMounted(() => {
  computedImgs();
});

onUnmounted(() => {
  isStop = true;
});
</script>

<style lang="scss" scoped>
.messag-body {
  .iconfont,
  .xicon {
    cursor: pointer;
    color: var(--tp_textcolor);
    margin-left: 5px;
  }

  padding: 10px;
  box-sizing: border-box;

  /* --tp_answer_bgcolor: transparent; */
  --tp_message_answer_color: var(--pc_message_answer_color);
  .msg_item {
    .msg_body {
      padding-left: 34px;
      .text_wrapper {
        padding: 5px 0;
        .TextComp {
          padding: 2px 0;
        }
      }
    }
  }

  &:hover {
    .header {
      .icon {
        opacity: 1;
      }
    }
  }

  .header {
    align-items: center;
    .time{
      color: #7A8084;
    }
    .iconfont {
      opacity: 1;
      transition: opacity 0.2s;
    }
  }

  .text_wrapper {
    padding: 5px;
    margin: 5px 0 0;
    line-height: 30px;
    background: var(--tp_answer_bgcolor);
  }
}
</style>
