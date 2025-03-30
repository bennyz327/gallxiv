import {createRouter, createWebHistory} from 'vue-router'
import HomeView from '../views/HomeView.vue'
import {useUserStore} from '@/stores/user'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
      meta: { requiresAuth: false }
    },
    {
      path: '/post/:account/:id',
      name: 'Post Page',
      component: () => import('../views/PostPage.vue'),
      meta: { requiresAuth: false }
    },
    {
      path: '/create-post',
      name: 'Create Post',
      component: () => import('../views/CreatePostView.vue'),
      meta: { requiresAuth: true }
    },
    {
      path: '/change-password',
      name: 'Change Password',
      component: () => import('../views/ChangePasswordView.vue'),
      meta: { requiresAuth: true }
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('../views/LoginView.vue'),
      meta: { guestOnly: true }
    },
    {
      path: '/register',
      name: 'register',
      component: () => import('../views/RegisterView.vue'),
      meta: { guestOnly: true }
    },
  ],
})

// 全局導航守衛
router.beforeEach(async (to, from, next) => {
  const userStore = useUserStore()

  // 如果用戶信息未初始化，嘗試獲取
  if (!userStore.user) {
    try {
      await userStore.init()
    } catch (error) {
      // 獲取用戶信息失敗，但不中斷導航流程
      console.error('Failed to initialize user store:', error)
    }
  }

  // 檢查是否需要登入權限
  if (to.meta.requiresAuth && !userStore.isLoggedIn) {
    // 未登入但需要權限，重定向到登入頁
    next({ name: 'login', query: { redirect: to.fullPath } })
  }
  // 檢查是否為僅限訪客頁面（如登入頁）
  else if (to.meta.guestOnly && userStore.isLoggedIn) {
    // 已登入但訪問僅限訪客頁面，重定向到首頁
    next({ name: 'home' })
  }
  else {
    // 其他情況正常導航
    next()
  }
})

export default router
