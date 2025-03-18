<template>
    <div class="voice_clone">
        <n-modal v-model:show="showModal" preset="dialog" :style="{ width: '600px' }" transform-origin="center"
            title="复刻声音"
            :closable="false"
            :show-icon="false">
            <n-form ref="formRef" :model="formData" :rules="rules" label-placement="left" :label-width="80"
                require-mark-placement="right-hanging">
                <n-form-item label="声音名称" path="voiceName">
                    <n-input v-model:value="formData.voiceName" placeholder="请输入声音名称" />
                </n-form-item>

                <n-form-item label="音频文件" path="voiceSrcUrl">
                    <n-upload accept=".mp3,.wav,.m4a" :max="1" :show-file-list="false" @change="handleUpload">
                        <n-button type="info">上传音频文件</n-button>
                    </n-upload>
                </n-form-item>
                <n-form-item label=" " style="color: #b7b7b7; font-size: 12px; margin-top: -20px;">
                    声道数：单/双声道<br />
                    采样率：大于等于 16000 Hz<br />
                    格式：WAV（16bit）、MP3、M4A<br />
                    文件大小：10MB 以内<br />
                    音频时长：10～20秒，不建议超过60秒。在朗读时请保持连贯，至少包含一段超过5秒的连续语音。<br />
                </n-form-item>

                <div v-if="formData.voiceSrcUrl" class="audio-preview" style="margin-top: 10px;">
                    <n-form-item label="原音试听">
                        <audio :src="formData.voiceSrcUrl" controls style="height: 30px"></audio>
                    </n-form-item>
                </div>
            </n-form>

            <template #action>
                <n-space>
                    <n-button quaternary @click="showModal = false">取消</n-button>
                    <n-button type="info" :disabled="!formData.voiceName || !formData.voiceSrcUrl"
                        :loading="submitting" @click="handleClone">
                        复刻
                    </n-button>
                </n-space>
            </template>
        </n-modal>

        <div class="progress_section" v-if="cloning">
            <n-progress type="line" :percentage="progress" :processing="progress < 100" :indicator-placement="'inside'">
                <span>声音复刻中...</span>
            </n-progress>
        </div>
    </div>

    <div class="clone_list">
        <div class="bind-btn-wrapper">
            <n-button type="info" @click="showModal = true">复刻声音</n-button>
        </div>
        <n-table :bordered="false" :single-line="false" :style="tableStyle">
            <thead>
                <tr>
                    <th>声音名称</th>
                    <th>试听</th>
                    <th>创建时间</th>
                    <th>操作</th>
                </tr>
            </thead>
            <tbody>
                <tr v-if="cloneList.length === 0">
                    <td colspan="4" style="text-align: center; padding: 20px;">暂无复刻声音</td>
                </tr>
                <tr v-else v-for="item in cloneList" :key="item.id">
                    <td>{{ item.name }}</td>
                    <td><audio :src="item.voiceSrcUrl" controls></audio></td>
                    <td>{{ item.createTime }}</td>
                    <td>
                        <n-button text @click="handleDelete(item)" class="action-btn">删除</n-button>
                    </td>
                </tr>
            </tbody>
        </n-table>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { NUpload, NButton, NProgress, NTable, NModal, NForm, NFormItem, NInput, NSpace } from 'naive-ui';
import { useMessage  } from 'naive-ui';
import { uploadFile } from '@/api';
import server from '@/api/require';
import { useToast } from '@/hooks/useToast';

const message = useMessage();
const { showToast, closeWatch, clearWatch } = useToast();
let selectItem = null;
const showModal = ref(false);
const submitting = ref(false);
const cloneList = ref([]);
const formRef = ref(null);

const formData = ref({
    voiceName: '',
    voiceSrcUrl: ''
});

const rules = {
    voiceName: {
        required: true,
        message: '',
        trigger: ['blur', 'change']
    },
    voiceSrcUrl: {
        required: true,
        message: '请上传音频文件',
        trigger: ['blur', 'change']
    }
};

const getCloneList = async () => {
    try {
        const res = await server({
            url: '/user/voice/list',
            method: 'GET'
        });
        if (res && res.length > 0) {
            cloneList.value = res.map(item => ({
                id: item.id,
                name: item.voiceName,
                createTime: new Date(item.createTime).toLocaleString(),
                voiceSrcUrl: item.voiceSrcUrl
            }));
        } else {
            cloneList.value = [];
        }
    } catch (err) {
        message.error('获取声音列表失败');
    }
};

const handleUpload = async ({ file }) => {
    if (file.file.size > 10 * 1024 * 1024) {
        message.error('文件大小不能超过10MB');
        return;
    }

    const fileFormData = new FormData();
    fileFormData.append('file', file.file);

    uploadFile(fileFormData).then((res) => {
        console.log(res);
        formData.value.voiceSrcUrl = res;
        message.success('文件上传成功');
    }).catch((err) => {
        console.log(err);
        message.error('文件上传失败');
    });
};

const handleClone = async () => {
    try {
        submitting.value = true;
        const res = await server({
            url: '/user/voice/add',
            method: 'POST',
            data: {
                voiceName: formData.value.voiceName,
                voiceSrcUrl: formData.value.voiceSrcUrl
            }
        });

        if (res) {
            message.success('声音复刻成功');
            showModal.value = false;
            formData.value = {
                voiceName: '',
                voiceSrcUrl: ''
            };
            getCloneList(); // 刷新列表
        }
    } catch (err) {
        message.error(err.response.data || '声音复刻失败');
    } finally {
        submitting.value = false;
    }
};

const handleDelete = (item) => {
    selectItem = item;
    showToast('del', '确定删除该复刻的声音吗？')
};

const doHandleDelete = async (confirm) => {
    if (!confirm) {
        selectItem = null;
        return;
    }
    if (!selectItem) return;

    server({
        url: '/user/voice/delete',
        method: 'POST',
        data: {
            id: selectItem.id
        }
    }).then((res) => {
        message.success('删除成功');
        getCloneList();
    }).catch((err) => {
        message.error(err.response.data || '删除失败');
    });
};

onMounted(() => {
    getCloneList();
});

closeWatch('del', doHandleDelete)

const tableStyle = {
    '--n-border-radius': '0px',
    '--n-td-text-color': 'var(--tp_textcolor)',
    '--n-th-text-color': 'var(--tp_textcolor)',
    '--n-td-color': 'var(--tp_footer_bgcolor)',
    '--n-th-color': 'var(--tp_footer_bgcolor)',
    '--n-merged-border-color': 'var(--tp_aside_border_color)'
};
</script>

<style lang="scss" scoped>
.clone_list {

    .bind-btn-wrapper {
        margin-bottom: 16px;
    }

    audio {
        height: 30px;
    }
}
</style>