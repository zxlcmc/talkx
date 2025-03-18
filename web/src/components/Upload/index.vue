<template>
  <n-upload
    ref="uploadRef"
    class="uploadComp"
    :headers="headers"
    :style="style"
    :max="max"
    :action="upload_img_url"
    :default-file-list="imglist"
    :list-type="listType"
    :show-preview-button="true"
    :on-before-upload="beforeUplaod"
    :on-remove="remove"
    :on-finish="finish"
    :show-file-list="showFileList"
    :on-error="error"
    :accept="accept"
    :trigger-class="triggerClass"
    :custom-request="customRequest"
  >
    <!-- :custom-request="customRequest" -->
    <slot></slot>
  </n-upload>
</template>

<script setup>
import { NUpload, useMessage } from "naive-ui";
import { IMG_ACCEPT } from "@/common/config";
import { computed, onMounted, ref, watch } from "vue";
import { upload_img_url, uploadImg, uploadFile } from "@/api/index";
import { useLoginStore } from "@/store/modules/login";

const uploadRef = ref(null);
const message = useMessage();
const useLogin = useLoginStore();
const props = defineProps({
  max: { type: Number, default: 4 },
  accept: { type: String, default: IMG_ACCEPT },
  size: { type: Number, default: Infinity },
  showFileList: { type: Boolean, default: true },
  listType: { type: String, default: "image-card" }, // text image image-card ,
  imgs: String,
  triggerClass: String,
  upLoadApi: String,
});
const { accept, size, listType, showFileList } = props;
const replaceText = "^base64url^";
const base64Sign = "data:image/png;base64,";
const imglist = computed(() => {
  if (!props.imgs) {
    return [];
  }
  // 分隔处理base64中的 ，
  const str = props.imgs.replaceAll(base64Sign, replaceText);
  return str.split(",").map((url, id) => {
    url = url.replaceAll(replaceText, base64Sign);
    return { url, status: "finished", id };
  });
});
// watch(
//   () => props.imgs,
//   () => {
//     console.log("图片变化");
//   }
// );
const emit = defineEmits([
  "update:imgs",
  "success",
  "error",
  "remove",
  "change",
]);
const style = {
  "--n-dragger-color": "var(--tp_dl_bg_color)",
};
const headers = { "talkx-token": useLogin.token };

const list = ref(imglist.value.map(({ url }, id) => ({ url, id })) || []);
let imgUrl = "";
let tempErr;
const customRequest = async ({ file, onFinish, onError }) => {
  try {
    const formData = new FormData();
    formData.append("file", file.file);
    let upLoadApi = uploadImg;
    if (props.upLoadApi === "file") {
      upLoadApi = uploadFile;
    }
    imgUrl = await upLoadApi(formData);
    list.value.push({
      url: imgUrl,
      id: file.id,
      name: file.name,
      size: file.file.size,
    });
    onFinish();
  } catch (err) {
    console.log("customRequest-err", err);
    tempErr = err;
    onError(); // 参数传递不去
  }
};

// 维护一个
const remove = (args) => {
  const { file } = args;
  const index = list.value.findIndex(({ id }) => id === file.id);
  list.value.splice(index, 1);
  const url = imglist.value[index].url;
  const arr = imglist.value.filter((o, i) => i !== index).map(({ url }) => url);
  const imgs = arr.join(",");
  emit("update:imgs", imgs);
  emit("change", list.value);
  emit("remove", { imgs, url });
};

const finish = (args) => {
  const arr = imglist.value.map(({ url }) => url);
  arr.push(imgUrl);
  const imgs = arr.join(",");
  emit("update:imgs", imgs);
  emit("success", { imgs, url: imgUrl });
  emit("change", list.value);
};

const error = ({ event, file }) => { 
  const msg = event|| tempErr.response.data
  msg && emit("error", msg);
};

const beforeUplaod = ({ file, fileList }) => {
  if (file.file.size > size) {
    message.error(`文件太大了`);
    return false;
  }
  return true;
};

defineExpose({ root: () => uploadRef, clear: () => (list.value = []) });
</script>

<style lang="scss" scoped>
.uploadComp {
  &::v-deep(.n-upload-dragger) {
    .n-base-icon {
      color: var(--tp_toast_color);
    }
    &:hover {
      border: 1px dashed var(--blue);
    }
  }
  &::v-deep(.n-upload-file-list) {
    .n-upload-file {
      .n-upload-file-info {
      }
    }
  }
}
</style>
