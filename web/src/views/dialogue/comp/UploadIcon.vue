<template>
  <div class="upload_comp">
    <Upload
      ref="uploadCompRef"
      :max="Infinity"
      :accept="CHAR_UPLOAD_TYPES"
      :showFileList="false" 
      @change="change"
      triggerClass="upload_icon"
      upLoadApi="file"
      @error="error"
      v-model:imgs="files"
    >
    </Upload>
    <span class="iconfont icon-fujian" @click="click"></span>
  </div>
</template>

<script setup>
import { useMessage } from "naive-ui";
import { computed, onMounted, ref } from "vue";
import { CHAR_UPLOAD_TYPES } from "@/common/config";
import {
  files,
  infoList,
  fileList,
  addWatch,
} from "../compositions/buttom/fileList";
import Upload from "@/components/Upload/index.vue";

const message = useMessage();
const uploadCompRef = ref(null);

const click = () => {
  if (!uploadCompRef.value) return;
  const uploadRef = uploadCompRef.value.root();
  uploadRef.value.openOpenFileDialog(); // 打开文件选择
};

const change = (list) => {
  infoList.value = list; 
};

const error = (e) => { 
  message.error(e);
};

onMounted(() => {
  addWatch(() => {
    uploadCompRef.value?.clear();
  });
});
</script>

<style lang="scss">
.upload_comp {
  position: absolute;
  bottom: 15px;
  left: 5px;
  height: 20px;
  line-height: 20px;
  cursor: pointer;
  .iconfont {
    font-size: 20px;
  }
}
</style>
