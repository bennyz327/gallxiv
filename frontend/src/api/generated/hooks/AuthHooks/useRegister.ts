import client from '@/api/app-axios'
import type { RegisterMutationRequest, RegisterMutationResponse } from '../../types/Register.ts'
import type { RequestConfig, ResponseErrorConfig } from '@/api/app-axios'
import type { MutationObserverOptions } from '@tanstack/vue-query'
import type { MaybeRef } from 'vue'
import { useMutation } from '@tanstack/vue-query'

export const registerMutationKey = () => [{ url: '/api/auth/register' }] as const

export type RegisterMutationKey = ReturnType<typeof registerMutationKey>

/**
 * {@link /api/auth/register}
 */
async function register(data: RegisterMutationRequest, config: Partial<RequestConfig<RegisterMutationRequest>> = {}) {
  const res = await client<RegisterMutationResponse, ResponseErrorConfig<Error>, RegisterMutationRequest>({
    method: 'POST',
    url: `/api/auth/register`,
    data,
    ...config,
  })
  return res.data
}

/**
 * {@link /api/auth/register}
 */
export function useRegister(
  options: {
    mutation?: MutationObserverOptions<RegisterMutationResponse, ResponseErrorConfig<Error>, { data: MaybeRef<RegisterMutationRequest> }>
    client?: Partial<RequestConfig<RegisterMutationRequest>>
  } = {},
) {
  const { mutation: mutationOptions, client: config = {} } = options ?? {}
  const mutationKey = mutationOptions?.mutationKey ?? registerMutationKey()

  return useMutation<RegisterMutationResponse, ResponseErrorConfig<Error>, { data: RegisterMutationRequest }>({
    mutationFn: async ({ data }) => {
      return register(data, config)
    },
    mutationKey,
    ...mutationOptions,
  })
}