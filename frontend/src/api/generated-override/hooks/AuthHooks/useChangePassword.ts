import client from '@/api/app-axios'
import type { RequestConfig, ResponseErrorConfig } from '@/api/app-axios'
import type { MutationObserverOptions } from '@tanstack/vue-query'
import type { MaybeRef } from 'vue'
import { useMutation } from '@tanstack/vue-query'
import { unref } from 'vue'

export interface ChangePasswordRequest {
  currentPassword: string
  newPassword: string
}

export const changePasswordMutationKey = () => [{ url: '/api/auth/change-password' }] as const

export type ChangePasswordMutationKey = ReturnType<typeof changePasswordMutationKey>

/**
 * {@link /api/auth/change-password}
 */
async function changePassword(data: ChangePasswordRequest, config: Partial<RequestConfig<ChangePasswordRequest>> = {}) {
  await client({
    method: 'POST',
    url: `/api/auth/change-password`,
    data: unref(data),
    ...config,
  })
}

/**
 * {@link /api/auth/change-password}
 */
export function useChangePassword(
  options: {
    mutation?: MutationObserverOptions<void, ResponseErrorConfig<any>, { data: MaybeRef<ChangePasswordRequest> }>
    client?: Partial<RequestConfig<ChangePasswordRequest>>
  } = {},
) {
  const { mutation: mutationOptions, client: config = {} } = options ?? {}
  const mutationKey = mutationOptions?.mutationKey ?? changePasswordMutationKey()

  return useMutation<void, ResponseErrorConfig<any>, { data: ChangePasswordRequest }>({
    mutationFn: async ({ data }) => {
      return changePassword(data, config)
    },
    mutationKey,
    ...mutationOptions,
  })
}