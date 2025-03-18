<template>
  <div class="formAvater">
    <n-popover
      trigger="click"
      placement="bottom"
      :show="showPopover"
      @update:show="popoverClose"
    >
      <template #trigger>
        <div
          class="avater"
          :style="{ width: size.width + 'px', height: size.height + 'px' }"
        >
          <div class="preview">
            <template v-if="tabValue === 'img'">
              <img v-show="imgs" class="img" :src="imgs" />
            </template>
            <template v-else-if="tabValue === 'icon'">
              <!-- 展示icon -->
              <!-- <div
                class="preview_icon"
                :style="{ 'background-color': avatarIconObj.color }"
              >
                <span
                  class="aicon"
                  :class="avatarIconObj.icon"
                  :style="{
                    'font-size': size.width * 0.8 + 'px',
                    'line-height': size.width + 'px',
                  }"
                ></span>
              </div> -->
              <!-- 使用canvas绘制icon -->
              <div class="preview_icon">
                <CanvasAvater
                  ref="iconCanvasRef"
                  :text="avatarIconPreview.text"
                  :bgColor="avatarIconObj.color"
                  :size="size"
                  :fontSize="size.width * 0.8"
                  type="icon"
                  :fontFamily="avatarIconPreview.family"
                />
              </div>
            </template>
            <template v-else-if="tabValue === 'text'">
              <div class="" style="text-align: center">
                <CanvasAvater
                  ref="textCanvasRef"
                  :text="textObj.text"
                  :bgColor="textObj.color"
                  :size="size"
                  :offset="{ x: 0, y: 1 }"
                />
              </div>
            </template>
          </div>
          <div class="icon">
            <span class="iconfont icon-edit"></span>
          </div>
        </div>
      </template>

      <div class="content" :class="{ small: resize.smallRef.value }">
        <n-tabs type="line" animated v-model:value="tabValue">
          <n-tab-pane name="img" tab="图片头像">
            <div class="imgAvater">
              <Upload v-model:imgs="imgs" :max="1" :size="1024 * 1024 * 5" />
              <div class="tips">请先删除后再上传新头像</div>
              <NButton class="nextButtom" @click="confirm" type="info">
                确认
              </NButton>
            </div>
          </n-tab-pane>
          <n-tab-pane name="icon" tab="图标头像">
            <div class="iconAvater">
              <div class="icon_wrapper">
                <AvatarIcons
                  @font="fontChange"
                  v-model:value="avatarIconObj.icon"
                  :bgColor="avatarIconObj.color"
                />
              </div>
              <div class="color_wrapper">
                <ColorBar v-model:value="avatarIconObj.color" />
              </div>
              <NButton class="nextButtom" @click="confirm" type="info">
                确认
              </NButton>
            </div>
          </n-tab-pane>
          <n-tab-pane name="text" tab="文字头像">
            <div class="textAvater">
              <n-input
                class="_input"
                v-model:value="textObj.text"
                maxlength="4"
                show-count
                placeholder=""
              />
              <div class="color_wrapper">
                <ColorBar v-model:value="textObj.color" />
              </div>
              <NButton class="nextButtom" @click="confirm" type="info">
                确认
              </NButton>
            </div>
          </n-tab-pane>
        </n-tabs>
      </div>
    </n-popover>
  </div>
</template>

<script setup>
import Upload from "@/components/Upload/index.vue";
import CanvasAvater from "@/components/CanvasAvater/index.vue";
import AvatarIcons from "@/components/AvatarIcons/index.vue";
import ColorBar from "@/components/ColorBar/index.vue";
import { computed, inject, onMounted, ref, watch } from "vue";
import { NPopover, NTabs, NTabPane, NButton, NInput } from "naive-ui";
import { base64ToFile } from "@/common/utils"; 
import { uploadImg } from "@/api/index";
import { formData } from "../compositions/form";

const resize = inject("resize");
const showPopover = ref(false);
const iconCanvasRef = ref(null);
const textCanvasRef = ref(null);
const size = ref({ width: 100, height: 100 });
const tabValue = ref("img"); // img icon text
const imgs = ref("");
const avatarIconObj = ref({ color: "", icon: "" });
const textObj = ref({ color: "", text: "文字头像" });

const avatarIconPreview = ref({ text: "", family: "aicon" });
const fontChange = (obj) => {
  avatarIconPreview.value.text = obj.text;
};
const base64Handler = async (base64) => {
  const file = base64ToFile(base64);
  const formData = new FormData();
  formData.append("file", file);
  const url = await uploadImg(formData);
  return url;
};

const popoverClose = (v) => {
  showPopover.value = v;
  // 点击空白处理
};

let isClick = false;
const confirm = async () => {
  if (isClick) return;
  isClick = true;
  let avatar = imgs.value;
  const cssAvatar = {
    tab: tabValue.value,
    text: textObj.value,
    icon: avatarIconObj.value,
  };
  switch (tabValue.value) {
    case "img":
      break;
    case "icon":
      avatar = await base64Handler(iconCanvasRef.value.toUrl());
      break;
    case "text":
      avatar = await base64Handler(textCanvasRef.value.toUrl());
      break;
  }
  formData.value.avatar = avatar;
  formData.value.cssAvatar = JSON.stringify(cssAvatar);
  isClick = false;
  showPopover.value = false;
};

const update = () => {
  imgs.value = formData.value.avatar; 
  const cssAvatar = formData.value.cssAvatar;
  if (cssAvatar) {
    const { text, icon, tab } = JSON.parse(cssAvatar);
    tabValue.value = 'img';
    textObj.value = text;
    avatarIconObj.value = icon;
  }
};
defineExpose({ update });

onMounted(() => {
  update();
});
</script>

<style lang="scss" scoped>
.content {
  width: 350px;

  .nextButtom {
    width: 100%;
    margin-top: 10px;
  }

  .imgAvater {
    .tips {
      padding-top: 5px;
      text-align: center;
    }
  }

  .iconAvater {
    .icon_wrapper {
      height: 250px;
      overflow-y: auto;
    }
    .color_wrapper {
      padding: 10px 0 0;
    }
  }

  .textAvater {
    .color_wrapper {
      padding: 10px 0 0;
    }
  }

  &.small {
    width: 320px;
    .iconAvater {
      .icon_wrapper {
        height: 130px;
      }
    }
  }
}

.formAvater {
  .avater {
    margin: auto;
    cursor: pointer;
    position: relative;
    border-radius: 20px;
    .preview {
      width: 100%;
      height: 100%;
      border-radius: 20px;
      overflow: hidden;
      .preview_icon {
        width: 100%;
        height: 100%;
        color: #fff;
        text-align: center;
      }
    }

    .img {
      width: 100px;
      height: 100px;
      border-radius: 20px;
      cursor: pointer;
    }
    .icon {
      width: 25px;
      height: 25px;
      cursor: pointer;
      text-align: center;
      position: absolute;
      right: -5px;
      bottom: -5px;
      background-color: #fff;
      box-shadow: 0 0 2px 1px #000;
      border-radius: 50%;
    }
  }
}
</style>
