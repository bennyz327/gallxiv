import client from '@/api/app-axios'
import type { UserPostMutationRequest, UserPostMutationResponse } from '@/api/generated/types/UserPost'
import type { RequestConfig, ResponseErrorConfig } from '@/api/app-axios'
import type { MutationObserverOptions } from '@tanstack/vue-query'
import type { MaybeRef } from 'vue'
import { useMutation } from '@tanstack/vue-query'

export const userPostMutationKey = () => [{ url: '/api/user/post' }] as const

export type UserPostMutationKey = ReturnType<typeof userPostMutationKey>

/**
 * {@link /api/user/post}
 * 修正版本: 正確處理 metadata 和圖片上傳
 */
async function userPost(data: UserPostMutationRequest, config: Partial<RequestConfig<UserPostMutationRequest>> = {}) {
  const formData = new FormData()
  if (data) {
    // 處理 metadata - 轉換為 JSON 並作為 Blob 添加
    if (data.metadata) {
      formData.append('metadata', new Blob([JSON.stringify(data.metadata)], {
        type: 'application/json'
      }));
    }
    
    // 處理圖片數組 - 逐一添加每張圖片
    if (data.images && Array.isArray(data.images)) {
      data.images.forEach(image => {
        formData.append('images', image);
      });
    }
  }

  const res = await client<UserPostMutationResponse, ResponseErrorConfig<Error>, UserPostMutationRequest>({
    method: 'POST',
    url: `/api/user/post`,
    data: formData,
    headers: { 'Content-Type': 'multipart/form-data', ...config.headers },
    ...config,
  })
  return res.data
}

/**
 * {@link /api/user/post}
 * 修正版本的 useUserPost Hook，可以正確處理表單數據
 */
export function useUserPost(
  options: {
    mutation?: MutationObserverOptions<UserPostMutationResponse, ResponseErrorConfig<Error>, { data: MaybeRef<UserPostMutationRequest> }>
    client?: Partial<RequestConfig<UserPostMutationRequest>>
  } = {},
) {
  const { mutation: mutationOptions, client: config = {} } = options ?? {}
  const mutationKey = mutationOptions?.mutationKey ?? userPostMutationKey()

  return useMutation<UserPostMutationResponse, ResponseErrorConfig<Error>, { data: UserPostMutationRequest }>({
    mutationFn: async ({ data }) => {
      return userPost(data, config)
    },
    mutationKey,
    ...mutationOptions,
  })
}