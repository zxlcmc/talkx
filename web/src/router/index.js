
import { createRouter, createWebHashHistory } from 'vue-router'

const Global_name = '__AsideRouter_click'

const routes = [
    {
        path: '/',
        redirect: '/chat',
        children: [
            {
                path: '/chat',
                name: 'chat',
                redirect: '/dialogue',
                component: () => import('@/views/chat/index.vue'),
                children: [
                    {
                        path: '/dialogue',
                        name: 'dialogue',
                        component: () => import('@/views/dialogue/index.vue'),
                        children: [
                            {
                                path: '/duser',
                                name: 'd_user',
                                meta: { asideRouter: true, variableName: Global_name },
                                component: () => import('@/views/User/index.vue'),
                            },
                            {
                                path: '/duserEdit',
                                name: 'd_userEdit',
                                meta: { asideRouter: true },
                                component: () => import('@/views/User/Edit.vue'),
                            },
                            {
                                path: '/dpay',
                                name: 'd_pay',
                                meta: { asideRouter: true },
                                component: () => import('@/views/Pay/index.vue'),
                            },
                            {
                                path: '/dagreement',
                                name: 'd_agreement',
                                meta: { asideRouter: true },
                                component: () => import('@/views/Agreement/index.vue'),
                            },
                            {
                                path: '/dhistory',
                                name: 'd_history',
                                meta: {
                                    asideRouter: true, variableName: Global_name, fixed: 'right'
                                },
                                component: () => import('@/views/History/index.vue'),
                            },
                            {
                                path: '/dfavorites',
                                name: 'd_favorites',
                                meta: {
                                    asideRouter: true, variableName: Global_name 
                                },
                                component: () => import('@/views/Favorites/index.vue'),
                            },
                            {
                                path: '/friendsPlaza',
                                name: 'd_friendsPlaza',
                                meta: { contentRouter: true },
                                component: () => import('@/views/FriendsPlaza/index.vue'),
                            },
                            {
                                path: '/friendsPlazaEdit',
                                name: 'd_friendsPlazaEdit',
                                meta: { contentRouter: true },
                                component: () => import('@/views/FriendsPlaza/Edit.vue'),
                            },
                            {
                                path: '/device',
                                name: 'd_device',
                                meta: { contentRouter: true },
                                component: () => import('@/views/Device/index.vue'),
                            },
                        ]
                    },
                    {
                        path: '/login',
                        name: 'login',
                        component: () => import('@/views/Login/index.vue'),
                    },
                    {
                        path: '/sqlchat',
                        name: 'sqlchat',
                        component: () => import('@/views/SqlChat/index.vue'),
                    },
                    {
                        path: '/editSchema',
                        name: 'editSchema',
                        component: () => import('@/views/EditSchema/index.vue'),
                    },
                    {
                        path: '/invite',
                        name: 'invite',
                        component: () => import('@/views/Invite/index.vue'),
                    },
                    {
                        path: '/favorites',
                        name: 'favorites',
                        component: () => import('@/views/Favorites/index.vue'),
                    },
                    {
                        path: '/verify',
                        name: 'verify',
                        component: () => import('@/views/Verify/index.vue'),
                    },
                    {
                        path: '/history',
                        name: 'history',
                        component: () => import('@/views/History/index.vue'),
                    },
                    {
                        path: '/notify',
                        name: 'notify',
                        component: () => import('@/views/Notify/index.vue'),
                    },
                    {
                        path: '/user',
                        name: 'user',
                        component: () => import('@/views/User/index.vue'),
                    },
                    {
                        path: '/userEdit',
                        name: 'userEdit',
                        component: () => import('@/views/User/Edit.vue'),
                    },
                    {
                        path: '/agreement',
                        name: 'agreement',
                        component: () => import('@/views/Agreement/index.vue'),
                    },
                    {
                        path: '/pay',
                        name: 'pay',
                        component: () => import('@/views/Pay/index.vue'),
                    }
                ]
            },

        ]
    },
    {
        path: '/share/:uid/:inviteCode',
        name: 'share',
        component: () => import('@/views/Share/index.vue'),
    },
    {
        path: '/404',
        name: '404',
        component: () => import('@/views/NotFount.vue'),
    },
    {
        path: '/:pathMatch(.*)*',
        name: 'notFound',
        redirect: '/404',
    }
]

const router = createRouter({
    history: createWebHashHistory(),
    routes,
})


router.beforeEach((to, form, next) => {
    console.log(`${form.name}==>${to.name}`);
    if (form.meta.asideRouter && form.meta.variableName && to.name == "dialogue") {
        window[form.meta.variableName] = true
        const AsideRouter = document.querySelector('.AsideRouter')
        console.log('AsideRouter', AsideRouter);
        AsideRouter && AsideRouter.click(); // 触发 AsideRouter 组件动画
        setTimeout(() => {
            next()
            window[form.meta.variableName] = false
        }, 300)
    } else {
        next()
    }
})

export default router