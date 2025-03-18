<template>
    <div class="device_list">
        <div class="bind-btn-wrapper">
            <n-button type="info" @click="handleBind">绑定新设备</n-button>
        </div>
        <n-table :bordered="false" :single-line="false" :style="tableStyle">
            <thead>
                <tr>
                    <th>设备型号</th>
                    <th>芯片型号</th>
                    <th>固件版本</th>
                    <th>MAC地址</th>
                    <th>绑定时间</th>
                    <th>自动更新</th>
                    <th>操作</th>
                </tr>
            </thead>
            <tbody>
                <tr v-if="list.length === 0">
                    <td colspan="7" style="text-align: center; padding: 20px;">没有任何设备，请先绑定</td>
                </tr>
                <tr v-else v-for="item in list" :key="item.id">
                    <td>{{ item.deviceModel }}</td>
                    <td>{{ item.chipModelName }}</td>
                    <td>{{ item.firmwareVersion }}</td>
                    <td>{{ item.macAddress }}</td>
                    <td>{{ item.bindTime }}</td>
                    <td>
                        <n-switch
                            :value="item.otaUpdate === 1"
                            @update:value="(value) => handleOtaUpdate(item, value)"
                        />
                    </td>
                    <td>
                        <span class="unbind-btn" @click="handleUnbind(item)">解绑</span>
                    </td>
                </tr>
            </tbody>
        </n-table>
    </div>
    <n-modal v-model:show="showBindModal" preset="dialog" title="绑定设备" 
        :closable="false"
        :show-icon="false"
        transform-origin="center">
        <n-form ref="formRef" :model="formData" :rules="rules" label-placement="left" :label-width="80"
            require-mark-placement="right-hanging">
            <n-form-item label="验证码" path="code">
                <n-input v-model:value="formData.code" placeholder="6位数字验证码" maxlength="6" />
            </n-form-item>
            <n-form-item label=" " style="color: #b7b7b7; font-size: 12px; margin-top: -20px;">
                输入设备播报的6位数字验证码
            </n-form-item>
        </n-form>
        
        <template #action>
            <n-space>
                <n-button quaternary @click="handleBindCancel">取消</n-button>
                <n-button type="info" :loading="loading" @click="handleBindConfirm">确认</n-button>
            </n-space>
        </template>
    </n-modal>
</template>

<script setup>
import server from '@/api/require';
import { useToast } from '@/hooks/useToast';
import { NButton, NInput, NModal, NTable, NSwitch, NForm, NFormItem, NSpace, useMessage } from 'naive-ui';
import { onMounted, ref } from 'vue';
import { useRoute } from 'vue-router';

const route = useRoute();
const message = useMessage();
const list = ref([]);
const { showToast, closeWatch, clearWatch } = useToast();
let selectItem = null;
const showBindModal = ref(false);
const bindCode = ref('');
const loading = ref(false);

const formRef = ref(null);
const formData = ref({
    code: ''
});

const rules = {
    code: {
        required: true,
        message: '',
        trigger: ['blur', 'change'],
        validator: (rule, value) => {
            if (!/^\d{6}$/.test(value)) {
                return new Error('请输入6位数字验证码');
            }
            return true;
        }
    }
};

const handleBind = () => {
    showBindModal.value = true;
    formData.value.code = '';
};

const handleBindConfirm = async () => {
    try {
        await formRef.value?.validate();
        loading.value = true;
        const { userFriendId } = route.query;
        await server({
            url: '/device/bind',
            method: 'POST',
            data: {
                code: formData.value.code,
                userFriendId
            }
        });
        message.success('绑定成功');
        showBindModal.value = false;
        getDeviceList();
    } catch (err) {
        if (err?.response?.data) {
            message.error(err.response.data);
        }
    } finally {
        loading.value = false;
    }
};

const handleBindCancel = () => {
    showBindModal.value = false;
    formData.value.code = '';
};

const tableStyle = {
    '--n-border-radius': '0px',
    '--n-td-text-color': 'var(--tp_textcolor)',
    '--n-th-text-color': 'var(--tp_textcolor)',
    '--n-td-color': 'var(--tp_footer_bgcolor)',
    '--n-th-color': 'var(--tp_footer_bgcolor)',
    '--n-merged-border-color': 'var(--tp_aside_border_color)'
};

const getDeviceList = async () => {
    const { userFriendId } = route.query;
    if (userFriendId) {
        server({
            url: '/device/list',
            method: "GET",
            params: {
                userFriendId
            }
        }).then((res) => {
            list.value = res;
        }).catch((err) => {
            
        });
    }
};

const handleUnbind = (item) => {
    selectItem = item;
    window.__talkx_event_stop = true;
    showToast('del', '确定要解绑该设备吗？');
};

const handleUnbindDevice = async (confirm) => {
    if (!confirm) {
        selectItem = null;
        return;
    }
    if (!selectItem) return;
    server({
        url: '/device/unbind',
        method: "POST",
        data: {
            deviceId: selectItem.id
        }
    }).then((res) => {
        getDeviceList();
    }).catch(res => {
        message.error(res.response.data || '解绑失败');
    }).finally(() => {
        selectItem = null;
    });
};

const handleOtaUpdate = async (item, value) => {
    const beforeValue = item.otaUpdate;
    item.otaUpdate = value ? 1 : 0;
    server({
        url: '/device/update',
        method: 'POST',
        data: {
            deviceId: item.id,
            autoOtaUpgrade: value ? 1 : 0
        }
    }).then((data) => {
        getDeviceList();
    }).catch((err) => {
        item.otaUpdate = beforeValue;
        message.error(err.response.data || '设置失败');
    });
};

onMounted(() => {
    getDeviceList();
});

closeWatch('del', handleUnbindDevice);
</script>

<style lang="scss" scoped>
.unbind-btn {
    color: var(--tp_primary_color);
    cursor: pointer;

    &:hover {
        opacity: 0.8;
    }
}

.device_list {
    .bind-btn-wrapper {
        margin-bottom: 16px;
    }
}
</style>