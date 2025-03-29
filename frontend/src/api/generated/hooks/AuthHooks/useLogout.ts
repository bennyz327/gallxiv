import client from '@/api/app-axios'
import type { LogoutMutationResponse } from '../../types/Logout.ts'
import type { RequestConfig, ResponseErrorConfig } from '@/api/app-axios'
import type { MutationObserverOptions } from '@tanstack/vue-query'
import { useMutation } from '@tanstack/vue-query'

export const logoutMutationKey = () => [{ url: '/api/auth/logout' }] as const

export type LogoutMutationKey = ReturnType<typeof logoutMutationKey>

/**
 * {@link /api/auth/logout}
 */
async function logout(config: Partial<RequestConfig> = {}) {
  const res = await client<LogoutMutationResponse, ResponseErrorConfig<Error>, unknown>({ method: 'POST', url: `/api/auth/logout`, ...config })
  return res.data
}

/**
 * {@link /api/auth/logout}
 */
export function useLogout(
  options: {
    mutation?: MutationObserverOptions<LogoutMutationResponse, ResponseErrorConfig<Error>>
    client?: Partial<RequestConfig>
  } = {},
) {
  const { mutation: mutationOptions, client: config = {} } = options ?? {}
  const mutationKey = mutationOptions?.mutationKey ?? logoutMutationKey()

  return useMutation<LogoutMutationResponse, ResponseErrorConfig<Error>>({
    mutationFn: async () => {
      return logout(config)
    },
    mutationKey,
    ...mutationOptions,
  })
}