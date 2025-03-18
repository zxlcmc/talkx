<template>
  <n-form
    class="formComp"
    ref="formRef"
    :label-placement="resize.smallRef.value ? 'top' : 'left'"
    :label-width="110"
    :model="formData"
    :rules="rules"
  >
    <!-- <n-form-item label="基本信息" /> -->
    <div class="diver">基本信息</div>
    <n-form-item label="昵称" path="name">
      <n-input
        class="_input"
        v-model:value="formData.name"
        maxlength="12"
        show-count
        placeholder=""
      />
    </n-form-item>
    <n-form-item label="标签" path="tag">
      <n-select
        class="tp_select"
        v-model:value="formData.tag"
        :options="tags"
      />
    </n-form-item>
    <n-form-item label="简介" path="intro">
      <div class="item_contet">
        <n-input
          class="_input"
          v-model:value="formData.intro"
          maxlength="30"
          show-count
          placeholder=""
        />
        <div class="desc">简单介绍它的作用</div>
      </div>
    </n-form-item>
    <n-form-item label="招呼语" path="welcome">
      <div class="item_contet">
        <n-input
          class="_input"
          v-model:value="formData.welcome"
          maxlength="100"
          show-count
          placeholder=""
        />
        <div class="desc">建立新话题页面所展示的招呼</div>
      </div>
    </n-form-item>
    <n-form-item label="指令" path="systemPrompt">
      <div class="item_contet">
        <n-input
          class="_input"
          type="textarea"
          show-count
          :autosize="{ minRows: 5, maxRows: 5 }"
          v-model:value="formData.systemPrompt"
          maxlength="4000"
          placeholder=""
        />
        <div class="desc">
          设置这个AI的系统指令（角色描述），通过
          <span class="model_look" @click="modelLook">「这里」</span>
          可以获取一些帮助
        </div>
      </div>
    </n-form-item>
    <n-form-item
      label="快速开始"
      path="conversationStart"
      v-if="formData.friendSource == SourceType.created"
    >
      <div class="item_contet">
        <div class="rowInput" v-for="(row, i) in stars" :key="row">
          <n-input
            :key="i"
            v-model:value="row.text"
            @input="cStartChange($event, i)"
            class="_input"
            maxlength="100"
            show-count
            placeholder=""
          />
          <div class="close">
            <n-button class="_close_btn" @click="closeCstart(i)">
              <span class="iconfont icon-close"> </span>
            </n-button>
          </div>
        </div>
        <div class="desc">提供用户快速开始对话的示例</div>
      </div>
    </n-form-item>

    <!-- <n-form-item label="模型设置" /> -->
    <div class="diver">模型设置</div>

    <n-form-item class="modelSet" path="messageContextSize">
      <template v-slot:label>
        <span>{{ config.messageContextSize.label }}</span>
        <Popover :text="config.messageContextSize.popover" />
      </template>
      <div class="flex">
        <n-slider
          class="slider"
          v-model:value="formData.messageContextSize"
          v-bind="config.messageContextSize.attrs"
        />
        <n-input
          class="input _input"
          :value="formData.messageContextSize"
          @input="inputChange($event, 'messageContextSize')"
          placeholder=""
        ></n-input>
      </div>
    </n-form-item>
    <n-form-item class="modelSet" path="openaiRequestBody.maxTokens">
      <template v-slot:label>
        <span>{{ config.openaiRequestBody.maxTokens.label }}</span>
        <Popover :text="config.openaiRequestBody.maxTokens.popover" />
      </template>
      <div class="flex">
        <n-slider
          class="slider"
          v-bind="config.openaiRequestBody.maxTokens.attrs"
          v-model:value="formData.openaiRequestBody.maxTokens"
        />
        <n-input
          class="input _input"
          :value="formData.openaiRequestBody.maxTokens"
          @input="inputChange($event, 'openaiRequestBody.maxTokens')"
          placeholder=""
        ></n-input>
      </div>
    </n-form-item>
    <n-form-item class="modelSet" path="openaiRequestBody.temperature">
      <template v-slot:label>
        <span>{{ config.openaiRequestBody.temperature.label }}</span>
        <Popover :text="config.openaiRequestBody.temperature.popover" />
      </template>
      <div class="flex">
        <n-slider
          class="slider"
          @update:value="temperatureUpdate"
          v-bind="config.openaiRequestBody.temperature.attrs"
          v-model:value="formData.openaiRequestBody.temperature"
        />
        <n-input
          class="input _input"
          :value="formData.openaiRequestBody.temperature"
          @input="inputChange($event, 'openaiRequestBody.temperature')"
          placeholder=""
        ></n-input>
      </div>
    </n-form-item>
    <n-form-item class="modelSet" path="openaiRequestBody.topP">
      <template v-slot:label>
        <span>{{ config.openaiRequestBody.topP.label }}</span>
        <Popover :text="config.openaiRequestBody.topP.popover" />
      </template>
      <div class="flex">
        <n-slider
          class="slider"
          @update:value="topPUpdate"
          v-bind="config.openaiRequestBody.topP.attrs"
          v-model:value="formData.openaiRequestBody.topP"
        />
        <n-input
          class="input _input"
          :value="formData.openaiRequestBody.topP"
          @input="inputChange($event, 'openaiRequestBody.topP')"
          placeholder=""
        ></n-input>
      </div>
    </n-form-item>
    <n-form-item class="modelSet" path="openaiRequestBody.presencePenalty">
      <template v-slot:label>
        <span>{{ config.openaiRequestBody.presencePenalty.label }}</span>
        <Popover :text="config.openaiRequestBody.presencePenalty.popover" />
      </template>
      <div class="flex">
        <n-slider
          class="slider"
          v-bind="config.openaiRequestBody.presencePenalty.attrs"
          v-model:value="formData.openaiRequestBody.presencePenalty"
        />
        <n-input
          class="input _input"
          :value="formData.openaiRequestBody.presencePenalty"
          @input="inputChange($event, 'openaiRequestBody.presencePenalty')"
          placeholder=""
        ></n-input>
      </div>
    </n-form-item>
    <n-form-item class="modelSet" path="openaiRequestBody.frequencyPenalty">
      <template v-slot:label>
        <span>{{ config.openaiRequestBody.frequencyPenalty.label }}</span>
        <Popover :text="config.openaiRequestBody.frequencyPenalty.popover" />
      </template>
      <div class="flex">
        <n-slider
          class="slider"
          v-bind="config.openaiRequestBody.frequencyPenalty.attrs"
          v-model:value="formData.openaiRequestBody.frequencyPenalty"
        />
        <n-input
          class="input _input"
          :value="formData.openaiRequestBody.frequencyPenalty"
          @input="inputChange($event, 'openaiRequestBody.frequencyPenalty')"
          placeholder=""
        ></n-input>
      </div>
    </n-form-item>
    <div class="diver">其他设置</div>
    <n-form-item label="Prompt Template" path="contentPrompt">
      <div class="item_contet">
        <n-input
          class="_input"
          type="textarea"
          show-count
          :autosize="{ minRows: 5, maxRows: 5 }"
          v-model:value="formData.contentPrompt"
          maxlength="4000"
          placeholder=""
        />
        <div class="desc">
          修改用户的提问时的模板脚本，默认情况不建议设置。
        </div>
      </div>
    </n-form-item>
    <n-form-item>
      <div class="footer flex">
        <n-button class="_close_btn" @click="close">取消</n-button>
        <n-button class="_confirm_btn" @click="confirm" type="info">
          确定
        </n-button>
      </div>
    </n-form-item>
  </n-form>
</template>

<script setup>
import { computed, inject, onMounted, ref, watch } from "vue";
import {
  NForm,
  NFormItem,
  NInput,
  NSlider,
  NButton,
  NRadio,
  NRadioGroup,
  NSelect,
  useMessage,
} from "naive-ui";
import Popover from "./Popover.vue";
import { debance } from "@/common/utils";
import { systemPrompt_url } from "@/common/config";
import {
  SourceType,
  formData,
  formRef,
  rules,
  config,
} from "../compositions/form";

const message = useMessage();
const resize = inject("resize");
const props = defineProps(["tags"]);
const emit = defineEmits(["confirm", "close"]);
const openUrl = window.__talkx__.openUrl;

const stars = ref([{ text: "" }]);

const cStartChange = (e, i) => {
  const len = stars.value.length;
  if (e && i == len - 1 && len < 4) {
    stars.value.push({ text: "" });
  }
};
const closeCstart = (i) => {
  const len = stars.value.length;
  if (len <= 1) {
    message.warning("至少需要一个快速开始");
    return;
  }
  stars.value.splice(i, 1);
};

watch(
  stars,
  (val, old) => {
    formData.value.conversationStart = val
      .filter(({ text }) => text.trim())
      .map(({ text }) => text);
  },
  { deep: true }
);

const update = () => {
  stars.value = formData.value.conversationStart
    .filter((t) => t)
    .map((text) => ({ text })); 
  if (stars.value.length < 4) {
    stars.value.push({ text: "" });
  }
};

const modelLook = () => openUrl(systemPrompt_url);
const tags = computed(
  () => props.tags.map(({ name: label }) => ({ label, value: label })) || []
);
const verifyHandler = debance((vFun) => vFun(), 300);
const inputChange = (val, key) => {
  const [key1, key2] = key.split(".");
  let value = val;
  if (key1 && key2) {
    const stepVerify = config[key1][key2].stepVerify;
    if (stepVerify) {
      verifyHandler(() => {
        formData.value[key1][key2] = stepVerify(value);
      });
    }
    formData.value[key1][key2] = value;
  } else {
    const stepVerify = config[key1].stepVerify;
    if (stepVerify) {
      verifyHandler(() => {
        formData.value[key1] = stepVerify(value);
      });
    }
    formData.value[key1] = value;
  }
};
// 属性互斥
const topPUpdate = () => {
  formData.value.openaiRequestBody.temperature = 0;
};
const temperatureUpdate = () => {
  formData.value.openaiRequestBody.topP = 0;
};

let isClick = false;
const over = () => (isClick = false);
const close = () => emit("close");
const confirm = async () => {
  if (!formRef.value || isClick) return;
  isClick = true;

  const errors = await new Promise((c) => formRef.value.validate(c));
  if (errors) {
    return over();
  }
  emit("confirm", over);
};

onMounted(() => update());
defineExpose({ update });
</script>

<style lang="scss" scoped>
.formComp {
  color: var(--lebel_color);
  .diver {
    font-size: 15px;
    line-height: 30px;
  }
  .flex {
    width: 100%;
    align-content: center;
  }
  .slider {
    flex: 1;
    margin-top: 8px;
  }
  .input {
    width: 80px;
    text-align: center;
    margin-left: 10px;
  }

  .item_contet {
    width: 100%;
    .desc {
      line-height: 30px;
      margin-bottom: -20px;
      color: var(--border_color);
      .model_look {
        cursor: pointer;
      }
    }
    .rowInput {
      display: flex;
      margin-bottom: 5px;
      &:nth-last-child(2) {
        margin-bottom: 0px;
      }
      ._close_btn {
        width: 40px;
      }
    }
  }

  .footer {
    width: 100%;
    text-align: center;
    justify-content: right;
    margin-top: 20px;
    button {
      width: 100px;
      margin-left: 15px;
    }
  }
  .modelSet {
    &::v-deep(.n-form-item-feedback-wrapper) {
      min-height: 15px;
    }
  }
  &::v-deep(.n-form-item) {
    .n-form-item-label {
      color: var(--lebel_color);
    }
    .n-input {
      .n-input__border {
      }
    }
    .n-form-item-blank {
      .n-radio-group {
        .n-radio {
          .n-radio__label {
            color: var(--tp_aside_color);
          }
        }
      }
    }
  }
}
</style>
