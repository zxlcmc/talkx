<template>
    <n-form class="formComp" ref="formRef" :model="formData" label-placement="left" :label-width="112"
        require-mark-placement="right-hanging" size="medium">
        <n-form-item label="是否开启记忆">
            <n-space>
                <n-switch v-model:value="formData.isSupportMemory" :checked-value="1" :unchecked-value="0"
                    @update:value="handleSubmit" />
            </n-space>
        </n-form-item>
        <n-form-item v-if="formData.isSupportMemory == 0" label=" " style="color: #b7b7b7; font-size: 12px; margin-top: -20px;">
            开启后，你与AI的对话过程中会自动产生记忆，这能够提高对话质量，但是响应速度会受到一定的影响。
        </n-form-item>
        <n-form-item v-if="formData.isSupportMemory == 1" label=" " style="color: #b7b7b7; font-size: 12px; margin-top: -20px;">
            关闭后，现有记忆不会被删除，但也不会使用。这能够提高响应速度，但是对话质量会受到一定的影响。
        </n-form-item>
        <n-table v-if="formData.isSupportMemory == 1" :bordered="false" :single-line="false" :style="tableStyle">
            <thead>
                <tr>
                    <th>关于你的信息，AI所产生记忆</th>
                    <th width="70px">操作</th>
                </tr>
            </thead>
            <tbody>
                <tr v-if="tableData.length === 0">
                    <td colspan="2" style="text-align: center; padding: 20px;">暂时还没有记忆</td>
                </tr>
                <tr v-else v-for="item in tableData" :key="item.id">
                    <td>{{ item.memory }}</td>
                    <td>
                        <span class="update-btn" @click="handleUpdate(item)">修改</span>
                        <span class="delete-btn" @click="handleDelete(item)">删除</span>
                    </td>
                </tr>
            </tbody>
        </n-table>
    </n-form>

    <n-modal v-model:show="showUpdateModal" preset="dialog" title="修改记忆内容" 
        :closable="false"
        :show-icon="false"
        transform-origin="center">
        <n-form ref="formRef" :model="updateMemoryData" :rules="rules" label-placement="left" :label-width="80"
            require-mark-placement="right-hanging">
            <n-input type="textarea" v-model:value="updateMemoryData.memory" placeholder="输入记忆内容" maxlength="255" />
        </n-form>
        
        <template #action>
            <n-space>
                <n-button quaternary @click="handleUpdateCancel">取消</n-button>
                <n-button type="info" :loading="loading" @click="handleUpdateConfirm">确认</n-button>
            </n-space>
        </template>
    </n-modal>
</template>

<script setup>
import { useRoute, useRouter } from "vue-router";
import { computed, ref, h } from "vue";
import { useToast } from '@/hooks/useToast';
import { NForm, NFormItem, NSpace, NSwitch, NTable, NButton, NModal, NInput, useMessage } from 'naive-ui';
import { queryFriendByUserFriendId, updateMemoryConfig } from "@/api/user";
import server from '@/api/require';
let selectItem = null;
const { showToast, closeWatch } = useToast();
const route = useRoute();
const message = useMessage();
const formRef = ref(null);
const id = computed(() => route.query.userFriendId);
const formData = ref({
    isSupportMemory: 0
});
const showUpdateModal = ref(false);
const updateMemoryData = ref({
    id: '',
    memory: ''
});

const tableStyle = {
    '--n-border-radius': '0px',
    '--n-td-text-color': 'var(--tp_textcolor)',
    '--n-th-text-color': 'var(--tp_textcolor)',
    '--n-td-color': 'var(--tp_footer_bgcolor)',
    '--n-th-color': 'var(--tp_footer_bgcolor)',
    '--n-merged-border-color': 'var(--tp_aside_border_color)'
};

const tableData = ref([]);

const handleUpdate = (row) => {
    showUpdateModal.value = true;
    updateMemoryData.value = {
        id: row.id,
        memory: row.memory
    };
};

const handleUpdateCancel = () => {
    showUpdateModal.value = false;
    updateMemoryData.value = { };
};

const handleUpdateConfirm = async () => {
    if (!updateMemoryData) {
        return
    }
    server({
        url: '/friend/memory/update',
        method: 'POST',
        data: {
            memoryId: updateMemoryData.value.id,
            updatedMemory: updateMemoryData.value.memory
        }
    }).then(() => {
        message.success('修改成功');
        showUpdateModal.value = false;
        getMemoryList();
    }).catch(err => {
        message.error(err.response?.data || '修改失败');
    });
};

const handleDelete = async (row) => {
    selectItem = row;
    window.__talkx_event_stop = true;
    showToast('del', '确定要删除该记忆吗？');
};

const handleDeleteMemory = async (confirm) => {
    if (!confirm) {
        selectItem = null;
        return;
    }
    if (!selectItem) return;
    try {
        await server({
            url: '/friend/memory/delete',
            method: 'POST',
            data: {
                memoryId: selectItem.id
            }
        });
        message.success('删除成功');
        await getMemoryList();
    } catch (err) {
        message.error(err.response?.data || '删除失败');
    } finally {
        selectItem = null;
    }
};


const getData = async () => {
    try {
        const friendData = await queryFriendByUserFriendId({
            userFriendId: id.value
        });
        const { isSupportMemory } = friendData;
        formData.value.isSupportMemory = isSupportMemory;
    } catch (err) {
        message.error('获取配置失败');
    }
};

const getMemoryList = async () => {
    try {
        const res = await server({
            url: '/friend/memory/list',
            method: 'get',
            params: {
                userFriendId: id.value
            }
        });
        tableData.value = res.results || [];
    } catch (err) {
        message.error(err.response?.data || '获取失败');
    }
};

const handleSubmit = async () => {
    try {
        await updateMemoryConfig({
            id: id.value,
            isSupportMemory: formData.value.isSupportMemory
        });
        message.success('保存成功');
    } catch (err) {
        message.error(err.response?.data || '保存失败');
        // 保存失败时恢复原值
        getData();
    }
};

getData();
getMemoryList();
closeWatch('del', handleDeleteMemory);
</script>

<style lang="scss" scoped>
.formComp {
    color: var(--lebel_color);

    .diver {
        font-size: 15px;
        line-height: 30px;
        padding: 15px 0px;
    }

    :deep(.n-form-item) {
        .n-form-item-label {
            color: var(--lebel_color);
        }
    }
}
.update-btn {
    margin-right: 5px;
}
.delete-btn, .update-btn {
    color: var(--tp_primary_color);
    cursor: pointer;

    &:hover {
        opacity: 0.8;
    }
}
</style>