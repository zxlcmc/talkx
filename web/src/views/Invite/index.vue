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
import { checkInviteCode } from '@/api/user'

const route = useRoute()
const router = useRouter()
const message = useMessage()
const useChat = useChatStore()
const loginStore = useLoginStore()
const codeText = ref('')
const verityText = ref('')
const isSend = ref(false)
const inputRef = ref(null)

const phone = computed(() => route.query.phone)

const sendCode = async () => {
    isSend.value = true
    const { err, errMsg } = await sign(phone.value)
    if (err) {
        return message.error(errMsg)
    }
    message.success('验证码发送成功')
    isSend.value = false
}

onMounted(() => {
    inputRef.value && inputRef.value.focus();
    if (route.query.inviteCode) { codeText.value = route.query.inviteCode }
})

const verityFun = (value = codeText.value) => {
    return true
}

const allowInput = (value) => {
    verityFun(value)
    return true
};

let send = false
const next = async () => {
    if (send) return
    send = true
    if (codeText.value) {
        const { exist, errMsg } = await checkInviteCode({ inviteCode: codeText.value })
        if (errMsg) {
            send = false
            return message.error(errMsg)
        }
        if (!exist) {
            send = false
            return message.error('邀请码无效')
        }
    }
    await sendCode()
    router.push({ name: 'verify', query: { phone: phone.value, inviteCode: codeText.value } })
    send = false
}
const back = () => { router.back() }
</script>

<template>
    <div class="invite tpage">
        <img class="logo" src="@/assets/talkx.svg" alt="" />
        <div class="title">邀请注册免费获得蒜粒</div>
        <div class="row">
            <div class="row_title">为新用户 {{ phone }} 填写邀请码，可以获得一定数量蒜粒。蒜粒可用于高级模型如 GPT-4。</div>
            <NInput ref="inputRef" v-model:value="codeText" :style="{ ...inputStyle, textAlign: 'center' }" type="phone"
                placeholder="" :allow-input="allowInput" />
            <div class="info" v-if="verityText">
                <Icon size="20" style="vertical-align: sub;">
                    <IosInformationCircle />
                </Icon>
                <span class="text">{{ verityText }}</span>
            </div>
        </div>
        <NButton block type="info" class="btn" @click="next">{{ codeText ? '继续' : '不填写邀请码，直接注册' }}</NButton>
        <div class="tips">你还可以 <span class="text" @click="back">返回上一页</span></div>
    </div>
    <!-- <LoaidngMask v-show="isSend" /> -->
</template>

<style lang="scss" scoped>
.invite {
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
