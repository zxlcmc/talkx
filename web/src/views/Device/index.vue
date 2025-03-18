<template>
  <div class="Device">
    <div class="content">
      <n-card title="智体" :bordered="false"
      style="margin-bottom: 16px; background-color: transparent; color: var(--tp_avatar_color);">
        <n-tabs type="line" animated pane-wrapper-class="talkx-pane-wrapper" 
        >
          <n-tab-pane name="device" tab="管理设备">
            <DeviceList />
          </n-tab-pane>
          <n-tab-pane name="voice" tab="我的声音">
            <VoiceClone />
          </n-tab-pane>
          <n-tab-pane name="audio" tab="智体设置">
            <AudioConfig />
          </n-tab-pane>
          <n-tab-pane name="memory" tab="记忆">
            <MemoryManagement />
          </n-tab-pane>
        </n-tabs>
      </n-card>
    </div>
  </div>
</template>

<script setup>
import { NTabs, NCard, NTabPane } from 'naive-ui';
import DeviceList from './comp/DeviceList.vue';
import AudioConfig from './comp/AudioConfig.vue';
import VoiceClone from './comp/VoiceClone.vue';
import MemoryManagement from './comp/MemoryManagement.vue';
</script>

<style lang="scss" scoped>
.talkx-pane-wrapper .n-tab-pane {
  overflow: auto;
  height: calc(100% - 180px);
}
.Device {
  color: var(--tp_avatar_color);
  background-color: var(--tp_dl_bg_color);
  .content {
    margin: 0 auto;
    max-width: var(--msg_content-width);
    display: flex;
    flex-direction: column;
  }
}
</style>