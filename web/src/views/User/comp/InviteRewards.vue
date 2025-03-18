<template>
    <div class="invite_rewards">
        <div class="invite_desc">
            通过下面这个邀请码注册的用户，你和邀请者都将获得 {{ number }} 蒜粒。
        </div>
        <div class="invite_code" @click="copy(useLogin.inviteCode)">{{ useLogin.inviteCode }}</div>
        <div class="invite_area">
            <div class="flex_b">
                <div class="">邀请文案</div>
                <div class="exchange hover_bg flex_sx_center" @click="setText"><span
                        class="iconfont icon-refresh"></span>换一个</div>
            </div>
            <div class="text">{{ text }} <span>{{ url }}</span></div>
            <NButton block type="info" class="btn" @click="copy(text + ' ' + url)">复制邀请文案</NButton>
        </div>
    </div>
</template>

<script setup>
import { computed, ref } from 'vue';
import { useLoginStore } from '@/store'
import { copyToClip } from '@/common/copy'
import { random } from '@/common/utils'
import { NButton, useMessage } from 'naive-ui'
import { getinviteReward } from '@/api/hashrate'
const message = useMessage()
const useLogin = useLoginStore()

const texts = [
    '可免费使用的AI编程助手，支持GPT-4。赶快注册免费体验！',
    '超级好用的GPT编程助手，完全免费，还支持GPT-4。点击链接注册还有奖励',
    '免费畅享AI编程助手，开启编程新境界，更有GPT-4等你来玩！',
    'AI编程助手助你免费掌握技能，开发效率起飞，邀请好友一起来体验！',
    '探索AI编程助手的无限可能，尽在TalkX！赶快注册免费使用吧！',
    '想突破编程瓶颈？想更快学习技术？还不快加入TalkX，借助GPT-4的力量，迈向未来！',
    '革命性的AI编程助手已诞生！立即免费加入，还能解锁GPT-4的惊喜功能！',
]

const text = ref('')
const number = ref('')
const url = computed(() => useLogin.user?.inviteUrl)

let rindex = 1
const indexArr = texts.map((t, i) => i); 
const setText = () => {
    const arr = indexArr.filter(t => t !== rindex) 
    const newRindex = random(0, arr.length - 1)
    text.value = texts[arr[newRindex]]
    rindex = arr[newRindex]
};
setText()
const copy = (str) => copyToClip(str).then(() => message.success('复制成功'));

const initData = async () => {
    number.value = await getinviteReward()
}
initData()
</script>

<style lang="scss" scoped> .invite_rewards {
     .invite_desc {
         padding: 25px;
     }

     .invite_code {
         cursor: pointer;
         font-size: 32px;
         text-align: center;
         padding: 20px 0 40px;
         font-weight: 700;
         color: var(--tp_invite_color);
         border-bottom: 1px solid var(--tp_bordecolor);
         margin: 0 20px 60px;
     }

     .invite_area {
         padding: 10px;

         .flex_b {
             padding: 5px 0;

             .exchange {
                 padding: 0 3px;
                 cursor: pointer;
                 user-select: none;

                 .iconfont {
                     margin-right: 4px;
                 }
             }
         }

         .text {
             padding: 30px;
             color: var(--tp_invite_text_color);
             text-align: center;
             background-color: var(--tp_invite_text_bgcolor);
         }

         .btn {
             width: 230px;
             cursor: pointer;
             margin: 20px auto 0;
         }
     }
 }
</style>