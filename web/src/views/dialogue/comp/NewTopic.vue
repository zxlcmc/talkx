<template>
  <div class="NewTopic"> 
    <MessageBody
      v-for="(msg, i) in newType1Data"
      :key="i"
      :item="msg"
      v-model:sending="sending"
      @change="messageChange"
      @delmsg="delmsg"
      :end="i == newType1Data.length - 1"
    />
  </div>
</template>

<script setup>
import { ref } from "vue";
import MessageBody from "./MessageBody.vue";
import chatLogic from "../chat";
const { newType1Data, useChat } = chatLogic;
const sending = ref(useChat.sending); 
const delmsg = () => {};
const messageChange = () => {};
</script>

<style lang="scss">
.NewTopic {
  height: 100%;
}
</style>
