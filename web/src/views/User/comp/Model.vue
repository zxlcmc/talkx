<template>
  <div class="model">
    <div class="desc">
      <!-- 注意，使用带 <img class="vip_img" src="@/assets/VIP.png" alt="">
            标识的模型，需要在个人中心配置已开通该模型权限的ApiKey。如果你没有，可将情况反馈我们，我们会综合评估后给出解决方案。 -->
      <div class="section">
        不同的模型，提问和回答都需要消耗一定数量的蒜粒。蒜粒可以通过邀请好友注册或购买获得。
      </div>
      <div class="section">
        <!-- 注意，模型的蒜粒消耗数量会随着官方、市场及成本等多因素的变化随时调整，不另做通知。 -->
        使用自备的 ApiKey 不会消耗蒜粒。现在
        <span class="set_text" @click="setApiKey">去设置</span>
      </div>
      <div class="section">
        不知道怎么选择？<span class="model_look" @click="modelLook"
          >「点击这里」</span
        >
        去查看模型的简介
      </div>
    </div>
    <div class="list">
      <!-- <div
        class="mitem flex_b"
        v-for="item in list"
        :key="item.model"
        @click="change(item.model)"
      >
        <div class="left">
          <NCheckbox :checked="item.model == props.selected" />
          <span class="text">{{ item.model }}</span>
        </div>
        <div class="right"> 
          {{ item.costCoin > 0 ? `${item.costCoin} / 次` : "免费" }}
        </div>
      </div> -->

      <n-table class="listtable" :style="tableStyle">
        <thead>
          <tr>
            <th>模型</th>
            <th>提问</th>
            <th>回答</th>
          </tr>
        </thead>
        <tbody>
          <tr
            v-for="item in list"
            :key="item.model"
            @click="change(item)"
            :class="{
              disabled: !item.canSelection,
            }"
          >
            <td class="model flex_b">
              <div class="model_text"> 
                <NCheckbox
                  :checked="item.model == props.selected"
                  :disabled="!item.canSelection"
                />
                <span class="text">{{ item.model }}</span>
              </div>
              <div class="cTags flex">
                <div
                  class="tagItem"
                  v-for="item in item.commentTags"
                  :key="item.name"
                  :style="item.style"
                >
                  {{ item.name }}
                </div>
              </div>
            </td>
            <td>{{ item.inputCoins > 0 ? item.inputCoins : "免费" }}</td>
            <td>{{ item.outputCoins > 0 ? item.outputCoins : "免费" }}</td>
          </tr>
        </tbody>
      </n-table>

      <div class="desc">
        计算单位为：每 1k tokens<br />
        示例：0.03 表示 1k tokens 所需要的蒜粒值为：0.03
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { NCheckbox, NTable } from "naive-ui";
import { useRouter } from "vue-router";
import { modelList } from "@/api/user";
import { model_intro_url } from "@/common/config";
const router = useRouter();
const props = defineProps(["selected"]);
const emit = defineEmits(["update:selected"]);
const openUrl = window.__talkx__.openUrl;
const change = (item) => {
  if (!item.canSelection) return;
  emit("update:selected", item.model);
};
const list = ref([]);
modelList().then(
  (res) =>
    (list.value = res.map((item) => {
      const { commentTags = [] } = item;
      return Object.assign({}, item, {
        commentTags: commentTags.map(({ color, bgColor, name }) => {
          return { name, style: { color, backgroundColor: bgColor } };
        }),
      });
    }))
);
const setApiKey = () => {
  router.push({ name: "userEdit", query: { type: "api_key" } });
};
const modelLook = () => {
  openUrl(model_intro_url);
};

const tableStyle = {
  "--n-border-radius": "0px",
  "--n-td-text-color": " var(--tp_textcolor)",
  "--n-th-text-color": " var(--tp_textcolor)",
  "--n-td-color": "var(--tp_footer_bgcolor)",
  "--n-th-color": "var(--tp_footer_bgcolor)",
  "--n-merged-border-color": "var(--tp_aside_border_color)",
};
</script>

<style lang="scss">
.model {
  overflow-y: auto;
  height: calc(100% - 53px);
  .vip_img {
    width: 18px;
    display: inline-block;
  }

  .desc {
    padding: 20px;

    .vip_img {
      vertical-align: sub;
    }

    .section {
      &:not(:last-child) {
        margin-bottom: 20px;
      }
      .model_look {
        cursor: pointer;
        color: var(--tp_model_look_color);
      }
    }

    .set_text {
      cursor: pointer;
      text-decoration: underline;
    }
  }

  .list {
    .mitem {
      cursor: pointer;
      padding: 0 10px;
      line-height: 50px;
      margin-bottom: 1px;
      background-color: var(--tp_footer_bgcolor);

      .left {
        .text {
          margin-left: 10px;
          @include oneOverFlow();
        }
      }

      .right {
        .vip {
          height: 100%;
        }
      }
    }

    .listtable {
      border: none;
      th {
        padding: 5px 12px;
      }
      td {
        cursor: pointer;
      }
      .model {
        .model_text{
          /* width: 160px;
          @include oneOverFlow(); */
        }
        .text {
          margin-left: 10px;
        }

        .cTags {
          padding-left: 10px;
          align-items: center;
          .tagItem { 
            font-size: 10px;
            padding: 0px 5px;
            border-radius: 3px;
            white-space: nowrap;
          }
        }
      }
      tr.disabled {
        opacity: 0.4;
        td {
          cursor: not-allowed;
        }
      }
    }
  }
}
</style>
