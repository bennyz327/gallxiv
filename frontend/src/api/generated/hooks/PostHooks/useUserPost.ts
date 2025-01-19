import client from '@/api/app-axios'
import type { RequestConfig, ResponseErrorConfig } from '@/api/app-axios'
import type { MutationObserverOptions } from '@tanstack/vue-query'
import type { UserPostMutationRequest, UserPostMutationResponse } from '../../types/UserPost.ts'
import type { MaybeRef } from 'vue'
import { useMutation } from '@tanstack/vue-query'

export const userPostMutationKey = () => [{ url: '/api/user/post' }] as const

export type UserPostMutationKey = ReturnType<typeof userPostMutationKey>

/**
 * {@link /api/user/post}
 */
async function userPost(data: UserPostMutationRequest, config: Partial<RequestConfig<UserPostMutationRequest>> = {}) {
  const formData = new FormData()
  if (data) {
    Object.keys(data).forEach((key) => {
      const value = data[key as keyof typeof data]
      if (typeof key === 'string' && (typeof value === 'string' || value instanceof Blob)) {
        formData.append(key, value)
      }
    })
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