import { driver } from "driver.js";
import "driver.js/dist/driver.css";
import { delay } from '@/common/utils'
import { useLoginStore, useGlobalStore } from '@/store'

// https://driverjs.com/docs/configuration

let DriverObj = {
    index: 0,
    destroy: () => { },
    next: () => { },
    guide: () => { },
    clear: () => { },
}

export const getDriver = () => DriverObj


let resolveName = '__talkx_temp_promise_'

let resolveKey = ''
export const runGuideOver = (key) => {
    const name = key ? resolveName + key : resolveKey;
    window[name] && window[name]()
}

/**
 * 初始化引导
 * 游客/未引导完成
 * 只在首页引导 dialogue
 */
export const initGuide = async (resize, route, router) => {
    const isDialogue = location.hash.startsWith("#/dialogue") 

    const useLogin = useLoginStore()
    const useGlobal = useGlobalStore() 
    
    if (!isDialogue || useLogin.isLogin || resize.IDERef.value) return
 

    // let webSteps = [
    //     {
    //         desc: '引导点击第第一个通话AI',
    //         key: "AI_Plaza",
    //         precondition: async () => {
    //             try {
    //                 resolveKey = resolveName + webSteps[0].key
    //                 const p = new Promise(c => window[resolveKey] = c)
    //                 if (!['d_friendsPlaza'].includes(route.name)) {
    //                     router.push({ name: 'd_friendsPlaza' })
    //                 }
    //                 await p
    //             } catch (e) {
    //                 console.log('errr', e)
    //                 await new Promise()
    //             }
    //         },
    //         element: '.FriendsPlaza .lists .FriendList .item:first-child',
    //         popover: {
    //             title: '提示',
    //             description: '点击<span class="iconfont icon-heart"></span>添加需要对话的AI',
    //         }
    //     },
    //     {
    //         desc: '引导点击第第一个通话AI',
    //         key: "FriendList",
    //         element: '.list_wrapper .fitem:first-child',
    //         precondition: async () => {
    //             const resolveKey0 = resolveName + webSteps[0].key
    //             resolveKey = resolveName + webSteps[1].key;
    //             let p = new Promise(c => window[resolveKey] = c)
    //             // 如果 已经引导完 webSteps[0] 
    //             if (['d_friendsPlaza'].includes(route.name) && window[resolveKey0]) {
    //                 p = 1
    //             } else if (!['d_friendsPlaza', 'd_friendsPlazaEdit', 'dialogue'].includes(route.name)) {
    //                 // 如果不在对应的路由页面
    //                 router.push({ name: 'dialogue' })
    //             }
    //             await p
    //         },
    //         popover: {
    //             title: '提示',
    //             description: '点击需要对话的AI',
    //         }
    //     },
    //     {
    //         desc: '引导输入内容提问',
    //         key: "bottom_area",
    //         precondition: async () => {
    //             resolveKey = resolveName + webSteps[2].key;
    //             const p = new Promise(c => window[resolveKey] = c)
    //             if (!['dialogue'].includes(route.name)) {
    //                 router.push({ name: 'dialogue' })
    //             }
    //             await p
    //         },
    //         element: '.input_area>.n-input',
    //         popover: {
    //             title: '提示',
    //             side: "top", align: 'end',
    //             description: '输入消息后，点击<span class="iconfont icon-fasong"></span>或回车发送给AI，等待回复。',
    //         }
    //     },
    // ]

    const webSteps = [
        {
            desc: '引导输入内容提问',
            key: "bottom_area",
            precondition: async () => {
                resolveKey = resolveName + webSteps[0].key;
                const p = new Promise(c => window[resolveKey] = c)
                if (!['dialogue'].includes(route.name)) {
                    router.push({ name: 'dialogue' })
                }
                await p
            },
            element: '.input_area>.n-input',
            popover: {
                title: '提示',
                side: "top", align: 'end',
                description: '首先，在此处输入消息后，点击“<span class="iconfont icon-fasong"></span>或回车发送给AI，等待回复。',
            }
        },
        {
            desc: '引导点击第第一个通话AI',
            key: "FriendList",
            element: '.list_wrapper .fitem:first-child',
            precondition: async () => {
                const resolveKey0 = resolveName + webSteps[0].key
                resolveKey = resolveName + webSteps[1].key;
                let p = new Promise(c => window[resolveKey] = c)
                // 如果 已经引导完 webSteps[0] 
                if (['d_friendsPlaza', 'dialogue'].includes(route.name) && window[resolveKey0]) {
                    p = 1
                } else if (!['d_friendsPlaza', 'd_friendsPlazaEdit', 'dialogue'].includes(route.name)) {
                    // 如果不在对应的路由页面
                    router.push({ name: 'dialogue' })
                }
                await p
            },
            popover: {
                title: '提示',
                description: '选择特定领域的AI获得更专业的帮助。',
            }
        },
        {
            desc: '引导点击AI广场',
            key: "FriendList",
            element: '.friendList_wrapper .icon-fenleiorguangchangorqitatianchong',
            precondition: async () => {
                resolveKey = resolveName + webSteps[1].key
                let p
                if (!window[resolveKey]) {
                    p = new Promise(c => window[resolveKey] = c)
                }
                if (!['dialogue'].includes(route.name)) {
                    // 如果不在对应的路由页面
                    router.push({ name: 'dialogue' })
                }
                await p
            },
            popover: {
                title: '提示',
                description: '进入AI广场可以添加海量AI。',
            }
        },
        {
            desc: '引导点击AI广场弟一个好友',
            key: "AI_Plaza",
            precondition: async () => {
                try {
                    resolveKey = resolveName + webSteps[3].key
                    const p = new Promise(c => window[resolveKey] = c)
                    if (!['d_friendsPlaza'].includes(route.name)) {
                        router.push({ name: 'd_friendsPlaza' })
                    }
                    await p
                } catch (e) {
                    console.log('errr', e)
                    await new Promise()
                }
            },
            after: () => {
                router.push({ name: 'dialogue' })
            },
            element: '.FriendsPlaza .lists .FriendList .item:first-child',
            popover: {
                title: '提示',
                description: '点击<span class="iconfont icon-heart"></span>添加到我的AI。',
            }
        },
    ]

    const mobileSteps = [
        {
            desc: '引导输入内容提问',
            key: "bottom_area",
            precondition: async () => {
                resolveKey = resolveName + mobileSteps[0].key
                window[resolveKey] = null; // 防止web端影响
                const p = new Promise(c => window[resolveKey] = c)
                if (!['dialogue'].includes(route.name)) {
                    router.push({ name: 'dialogue' })
                }
                await p
            },
            element: '.input_area>.n-input',
            popover: {
                title: '提示',
                description: '首先，在此处输入消息后，点击<span class="iconfont icon-fasong"></span>发送给AI，等待回复。',
            }
        },
        {
            desc: '移动端引导 切换好友',
            key: "bottom_area",
            precondition: async () => {
                resolveKey = resolveName + mobileSteps[1].key
                let p
                if (!window[resolveKey]) {
                    p = new Promise(c => window[resolveKey] = c)
                }
                if (!['dialogue'].includes(route.name)) {
                    let aside = route.meta.asideRouter
                    router.push({ name: 'dialogue' })
                    if (aside) {
                        await delay(300)
                    }
                }
                await p
            },
            element: '.userList .currFriend',
            popover: {
                title: '提示',
                side: "top", align: 'start',
                description: '点击「我的AI」可以选择特定领域的AI获得更专业的帮助。',
            }
        },

        {
            key: "bottom_area",
            desc: '移动端引导 AI广场',
            precondition: async () => {
                resolveKey = resolveName + mobileSteps[2].key
                let p
                if (!window[resolveKey]) {
                    p = new Promise(c => window[resolveKey] = c)
                }
                if (!['dialogue'].includes(route.name)) {
                    let aside = route.meta.asideRouter
                    router.push({ name: 'dialogue' })
                    if (aside) {
                        await delay(300)
                    }
                }
                await p
            },
            element: '.aiItem.square',
            popover: {
                title: '提示',
                side: "top", align: 'start',
                description: '进入AI广场可以添加海量AI。',
            }
        },
        {
            desc: '引导点击第第一个通话AI',
            key: "AI_Plaza",
            precondition: async () => {
                resolveKey = resolveName + mobileSteps[3].key

                console.log('mobileSteps-3-precondition-', resolveKey);
                const p = new Promise(c => window[resolveKey] = c)
                if (!['d_friendsPlaza'].includes(route.name)) {
                    router.push({ name: 'd_friendsPlaza' })
                }
                await p
                console.log('precondition-over');
            },
            after: () => {
                router.push({ name: 'dialogue' })
            },
            element: '.FriendsPlaza .lists .FriendList .item:first-child',
            popover: {
                title: '提示',
                description: '点击<span class="iconfont icon-heart"></span>添加到我的AI。',
            }
        },

    ]


    let isSmall
    const init = async () => {
        // 清除上一个状态的引导
        if (DriverObj) { DriverObj.clear() }
        isSmall = resize.smallRef.value
        const type = isSmall ? 'mobile' : 'web';
        useGlobal.setGuideType(type)
        if (useGlobal.guideOver) return
        const steps = type === 'mobile' ? mobileSteps : webSteps;

        resolveName = `__talkx_temp_promise__${type}_`; // 区分端

        const runPrecondition = async () => {
            const precondition = steps[DriverObj.index]?.precondition
            if (typeof precondition === 'function') {
                await precondition()
            }
        }

        const runAfer = async () => {
            const after = steps[DriverObj.index]?.after
            if (typeof after === 'function') {
                await after()
            }
        }

        let isClick = false
        let activeClose = false

        const onNextClick = async () => {
            await runAfer()
            DriverObj.index++;
            useGlobal.setGuideProgress(DriverObj.index)
            if (DriverObj.index >= steps.length) { // 全部引导完成 
                useGlobal.setGuideOver()
                DriverObj.clear()
                return
            }
            await runPrecondition()
            driverObj.moveNext()
        }
        const driverObj = driver({
            showProgress: true,
            nextBtnText: "下一步",
            prevBtnText: "上一步",
            doneBtnText: "结束引导",
            showButtons: ["next", "close"],
            stagePadding: 5,
            steps,
            onNextClick,
            onPrevClick: async () => {
                await runAfer()
                DriverObj.index--;
                useGlobal.setGuideProgress(DriverObj.index)
                await runPrecondition()
                driverObj.movePrevious()
            },
            onDestroyStarted: async (...args) => {
                if (isClick) return
                isClick = true
                console.log('onDestroyStarted');
                if (!activeClose) {
                    onNextClick()
                } else {
                    DriverObj.clear()
                }
                isClick = false

            },
            onDestroyed: async () => {
                useGlobal.setGuideOver()
                isClick = false
            },
            onCloseClick: () => {
                activeClose = true
                DriverObj.clear()
            }
        })

        DriverObj = {
            index: useGlobal.guide[type].progress,
            driver: driverObj,
            destroy: () => {
                DriverObj.driver.destroy()
                DriverObj.next()
            },
            clear: () => {
                DriverObj.driver.destroy()
            },
            next: async () => {
                await runAfer()
                DriverObj.index++;
                useGlobal.setGuideProgress(DriverObj.index)
                if (DriverObj.index >= steps.length) { // 全部引导完成 
                    useGlobal.setGuideOver()
                }
            },
            guide: () => {
                useGlobal.setGuideProgress(DriverObj.index)
                if (DriverObj.index < steps.length) {
                    DriverObj.driver.drive(DriverObj.index)
                }
            }
        }

        await runPrecondition()
        DriverObj.guide()
    }

    init()
    resize.change(() => {
        if (isSmall !== resize.smallRef.value) {
            DriverObj.clear()
            // setTimeout(init, 100)
        }
    })
}