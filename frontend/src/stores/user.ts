import {computed, ref} from 'vue'
import {defineStore} from 'pinia'
import {meQueryKey, meQueryOptions, type User as ApiUser, userRoleEnum} from '@/api/generated'
import {useQueryClient} from '@tanstack/vue-query'

export type User = ApiUser

export const useUserStore = defineStore('user', () => {
  const user = ref<User | null>(null)
  const isLoggedIn = computed(() => !!user.value)
  const isAdmin = computed(() => user.value?.role === userRoleEnum.ADMIN)
  const queryClient = useQueryClient()

  // 初始化用戶數據，從API獲取當前登入用戶信息
  const init = async () => {
    try {
      // 使用 queryClient.fetchQuery 與 Kubb 生成的 queryOptions 來獲取用戶資料
      const userData = await queryClient.fetchQuery(meQueryOptions())
      user.value = userData
      return userData
    } catch (error) {
      user.value = null
      return null
    }
  }

  // 登出後的清理工作
  const logoutCallback = async () => {
    try {
      // 清除用戶數據
      user.value = null

      // 使用生成的 queryKey 使查詢緩存失效
      await queryClient.invalidateQueries({
        queryKey: meQueryKey(),
      })

      // 移除特定查詢的緩存
      queryClient.removeQueries({
        queryKey: meQueryKey(),
      })
    } catch (error) {
      console.error('Logout error:', error)
    }
  }

  return {
    user,
    isLoggedIn,
    isAdmin,
    init,
    logoutCallback,
  }
})
