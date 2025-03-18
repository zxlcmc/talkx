<script setup>
import { computed, onMounted, ref } from "vue";
import { IosInformationCircle } from '@vicons/ionicons4'
import { Icon } from '@vicons/utils'
import { NInput, NButton, useMessage } from 'naive-ui'
import { useRoute, useRouter } from 'vue-router'
import { inputStyle } from '@/common/style'
import LoaidngMask from '@/components/mask/loading.vue'
import { delay } from '@/common/utils'
import { useLoginStore, useChatStore } from '@/store'
import { sign } from '@/api/index'

const route = useRoute()
const router = useRouter()
const message = useMessage()
const useChat = useChatStore()
const loginStore = useLoginStore()
const codeText = ref('')
const verityText = ref('请输入验证码')
const isSend = ref(false)
const inputRef = ref(null)

const phone = computed(() => route.query.phone)

const sendCode = async () => {
    isSend.value = true
    const { err, errMsg } = await sign(phone.value)
    if (err) {
        verityText.value = errMsg
        isSend.value = false
        return
    }
    message.success('验证码发送成功')
    isSend.value = false
}

onMounted(() => { inputRef.value && inputRef.value.focus() })

const verityFun = (value = codeText.value) => {
    if (!value) {
        verityText.value = "请输入验证码"
        return
    }
    verityText.value = '';
    return true
}

const allowInput = (value) => {
    verityFun(value)
    return true
};

let send = false
const next = async () => {  
    if (send || !verityFun()) return
    send = true
    const { err, errMsg } = await loginStore.login({ phoneNum: phone.value, code: codeText.value, inviteCode: route.query.inviteCode })
    if (err) {
        verityText.value = errMsg
    } else {
        useChat.uploadLocaSession() 
        router.push({ name: 'dialogue', query: { type: 'add', reload: 1 } });
    }
    send = false
}
const back = () => {
    router.back()
}
</script>

<template>
    <div class="verify tpage">
        <img class="logo" src="@/assets/talkx.svg" alt="" />
        <div class="title">请检查您的短信以获取验证码。</div>
        <div class="row">
            <div class="row_title">我们已经向 {{ phone }} 发送了一个六位数的验证码。该码即将过期。</div>
            <NInput ref="inputRef" v-model:value="codeText" :style="{ ...inputStyle, textAlign: 'center' }" type="phone"
                placeholder="" :allow-input="allowInput" />
            <div class="info" v-if="verityText">
                <Icon size="20" style="vertical-align: sub;">
                    <IosInformationCircle />
                </Icon>
                <span class="text">{{ verityText }}</span>
            </div>
        </div>
        <NButton block type="info" class="btn" @click="next">提交</NButton>
        <div class="tips">没有收到验证码？<span class="text" @click="sendCode">重新发送</span> 或者 <span class="text"
                @click="back">返回上一页</span></div>
    </div>
    <!-- <LoaidngMask v-show="isSend" /> -->
</template>

<style lang="scss" scoped>
.verify {
    padding: 40px;

    .logo {
        width: 48px;
    }

    .title {
        font-size: 24px;
        font-weight: bold;
        margin: 20px 0;
    }

    .desc {
        margin: 20px 0 30px;
    }

    .row {
        .row_title {
            margin-bottom: 10px;
        }

        .info {
            margin-top: 10px;

            .text {
                margin-left: 5px;
                vertical-align: 1px;
            }
        }
    }

    .btn {
        height: 36px;
        margin-top: 30px;
    }

    .tips {
        margin-top: 10px;

        .text {
            cursor: pointer;
            font-weight: 650;
        }
    }
}
</style>
