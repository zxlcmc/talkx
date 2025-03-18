<template>
  <div class="edit_schema tpage">
    <div class="topbar flex_b flex_sx_center">
      <div class="flex_sx_center back hover_bg" tabindex="0" @click="back">
        <span class="text">取消</span>
      </div>
      <div class="right">
        <NButton block type="info" class="btn" @click="over">保存</NButton>
      </div>
    </div>
    <div class="form">
      <div class="row" v-for="(item, index) in list" :key="index">
        <div class="label flex_b">
          <div class="lable_text">{{ item.label }}</div>
          <div class="lable_right" v-if="item.maxLength">
            {{ form[item.key].length }} / {{ item.maxLength }}
          </div>
        </div>
        <div class="value">
          <NInput
            v-if="item.type === 'input'"
            :maxlength="item.maxLength"
            v-model:value="form[item.key]"
            :style="style"
            :placeholder="item.placeholder"
          />
          <NInput
            v-else-if="item.type === 'textarea'"
            type="textarea"
            :autosize="item.autosize"
            :maxlength="item.maxLength"
            v-model:value="form[item.key]"
            :style="style"
            :placeholder="item.placeholder"
          />
          <SelectComp
            v-else-if="item.type === 'select'"
            :options="options"
            :style="style"
            :maxlength="item.maxLength"
            :placeholder="item.placeholder"
            v-model:value="form[item.key]"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, reactive } from "vue";
import { useRoute, useRouter } from "vue-router";
import { inputStyle } from "@/common/style";
import { routerBack } from "@/common/utils";
import { NInput, NButton, NSelect, useMessage } from "naive-ui";
import { saveSchema } from "@/api/schema";
import { useSchemaStore } from "@/store";
import { verify } from "@/common/verify";
import SelectComp from "./comp/SelectComp.vue";

let isClick = false;
const route = useRoute();
const router = useRouter();
const message = useMessage();
const useSchema = useSchemaStore();
const id = computed(() => route.query.id);
const type = computed(() => route.query.type);
const form = reactive({ name: "", sqlType: "", schema: "" });

const style = Object.assign(inputStyle, {
  fontSize: "var(--tp_size)",
  lineHeight: "50px",
  "--n-border": "none",
  "--n-border-hover": "none",
  "--n-border-focus": "none",
  backgroundColor: "var(--tp_footer_bgcolor)",
  "--n-border-radius": "0px",
  "--n-border-disabled": "none",
});
const ltype = ["MySQL", "Oracle", "SQL Server", "PostgreSQL", "MariaDB"];
const options = computed(() => ltype.map((v) => ({ label: v, value: v })));
const list = [
  {
    type: "input",
    label: "表结构名称",
    maxLength: "32",
    key: "name",
    placeholder: "请填写32字已便于自己识别的表结构名称",
  },
  {
    type: "select",
    maxLength: "32",
    label: "语言类型",
    placeholder: "请选择语言类型",
    key: "sqlType",
  },
  {
    type: "textarea",
    label: "表结构信息",
    key: "schema",
    maxLength: "8192",
    autosize: { minRows: 10, maxRows: 10 },
    placeholder: `请填写表结构信息，字段越详细越好。MySQL里可以直接使用 "SHOW CREATE TABLE table_name" 来获取表的信息`,
  },
];

const initData = () => {
  if (!id.value) return;
  const { name, sqlType, schema } = useSchema.getSchema(id.value) || {};
  Object.assign(form, { name, sqlType, schema });
};
const rule = {
  name: [{ type: "empty", message: "请填写表结构名称" }],
  sqlType: [{ type: "empty", message: "请选择语言类型" }],
  schema: [{ type: "empty", message: "请填写表结构信息" }],
};

const verifyForm = async () => {
  const arr = Object.entries(rule).map(([key, value]) => ({
    key,
    rules: value,
  }));
  for (let i = 0; i < arr.length; i++) {
    const { key, rules } = arr[i];
    const errMsg = await verify(form[key], rules);
    if (errMsg) {
      return { err: true, errMsg };
    }
  }
  return { err: false, data: form };
};

const back = () => {
  if (isClick) return;
  isClick = true;
  routerBack(router, { name: "sqlchat" });
  isClick = false;
};

const over = async () => {
  if (isClick) return;
  isClick = true;
  const { err, errMsg, data } = await verifyForm();
  if (errMsg) {
    isClick = false;
    message.error(errMsg);
    return;
  }
  const { errMsg: errMsg2 } = await saveSchema({ ...form, id: id.value });
  if (errMsg2) {
    isClick = false;
    message.error(errMsg2);
    return;
  }
  message.success(!id.value ? "添加成功" : "保存成功");
  isClick = false;
  back();
};

onMounted(() => {
  initData();
});
</script>

<style lang="scss" scoped>
.edit_schema {
  padding-bottom: 30px;
  box-sizing: border-box;
  background: var(--tp_dl_bg_color);
  .form {
    overflow-y: auto;
    height: calc(100vh - 52px);
    .row {
      line-height: 50px;
      .label {
        height: 50px;
        padding: 0 10px;
      }
      .value {
        background-color: var(--tp_footer_bgcolor);
        &::v-deep(.n-select) {
          .n-base-selection {
            min-height: 50px;
            line-height: 50px;
            .n-base-selection-label {
              min-height: 50px;
            }
            .n-base-selection__state-border,
            .n-base-selection__border {
              border: 1px solid transparent;
            }
          }
        }
      }
    }
  }
}
</style>
