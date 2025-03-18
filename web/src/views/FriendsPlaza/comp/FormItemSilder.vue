<template>
  <n-form-item label="上下文数量" path="messageContextSize">
    <template v-slot:label>
      <span class="">上下文数量</span>
      <Popover text="tttttt" a="1" />
    </template>
    <div class="flex">
      <n-slider
        :min="2"
        :max="64"
        v-model:value="formData.messageContextSize"
        :step="2"
      />
      <n-input
        :min="2"
        :max="64"
        :step="2"
        v-model:value="formData.messageContextSize"
        placeholder=""
      ></n-input>
    </div>
  </n-form-item>
</template>

<script setup>
import Popover from "./Popover.vue";
import { NFormItem, NInput, NSlider } from "naive-ui";
import { computed } from "vue";

const props = defineProps(["formData"]);
const formData = computed(() => props.formData);
</script>

<style lang="scss"></style>
