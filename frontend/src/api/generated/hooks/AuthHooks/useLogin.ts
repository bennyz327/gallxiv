import client from '@/api/app-axios'
import type { LoginMutationRequest, LoginMutationResponse } from '../../types/Login.ts'
import type { RequestConfig, ResponseErrorConfig } from '@/api/app-axios'
import type { MutationObserverOptions } from '@tanstack/vue-query'
import type { MaybeRef } from 'vue'
import { useMutation } from '@tanstack/vue-query'

export const loginMutationKey = () => [{ url: '/api/auth/login' }] as const

export type LoginMutationKey = ReturnType<typeof loginMutationKey>

/**
 * {@link /api/auth/login}
 */
async function login(data: LoginMutationRequest, config: Partial<RequestConfig<LoginMutationRequest>> = {}) {
  const res = await client<LoginMutationResponse, ResponseErrorConfig<Error>, LoginMutationRequest>({ method: 'POST', url: `/api/auth/login`, data, ...config })
  return res.data
}

/**
 * {@link /api/auth/login}
 */
export function useLogin(
  options: {
    mutation?: MutationObserverOptions<LoginMutationResponse, ResponseErrorConfig<Error>, { data: MaybeRef<LoginMutationRequest> }>
    client?: Partial<RequestConfig<LoginMutationRequest>>
  } = {},
) {
  const { mutation: mutationOptions, client: config = {} } = options ?? {}
  const mutationKey = mutationOptions?.mutationKey ?? loginMutationKey()

  return useMutation<LoginMutationResponse, ResponseErrorConfig<Error>, { data: LoginMutationRequest }>({
    mutationFn: async ({ data }) => {
      return login(data, config)
    },
    mutationKey,
    ...mutationOptions,
  })
}