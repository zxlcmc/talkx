<template>
  <div class="SelectComp"> 
    <n-popselect
      trigger="click"
      arrow-wrapper-class="SelectComp"
      :value="props.value"
      @change="(e) => emits('update:value', e)"
      :options="options"
      size="medium"
      scrollable
    >
      <NInput
        :style="props.style"
        :value="props.value" 
        :maxlength="props.maxlength"
        @input="(e) => emits('update:value', e)"
        :placeholder="props.placeholder"
      />
    </n-popselect>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { NInput, NPopselect } from "naive-ui"; 
const props = defineProps({
  options: {
    type: Object,
    default: () => [],
  },
  style: {
    type: Object,
    default: () => {},
  },
  placeholder: {
    type: String,
    default: "",
  },
  maxlength: {
    type: String,
    default: "",
  },
  value: {
    type: String,
    default: "",
  },
});
const emits = defineEmits(["update:value"]); 
</script>

<style lang="scss">
.n-base-select-menu[arrow-wrapper-class="SelectComp"] {
  width: 100vw;
}
</style>
