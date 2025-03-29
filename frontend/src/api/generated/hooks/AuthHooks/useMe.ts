import client from '@/api/app-axios'
import type { MeQueryResponse } from '../../types/Me.ts'
import type { RequestConfig, ResponseErrorConfig } from '@/api/app-axios'
import type { QueryKey, QueryObserverOptions, UseQueryReturnType } from '@tanstack/vue-query'
import { queryOptions, useQuery } from '@tanstack/vue-query'
import { unref } from 'vue'

export const meQueryKey = () => [{ url: '/api/me' }] as const

export type MeQueryKey = ReturnType<typeof meQueryKey>

/**
 * {@link /api/me}
 */
async function me(config: Partial<RequestConfig> = {}) {
  const res = await client<MeQueryResponse, ResponseErrorConfig<Error>, unknown>({ method: 'GET', url: `/api/me`, ...config })
  return res.data
}

export function meQueryOptions(config: Partial<RequestConfig> = {}) {
  const queryKey = meQueryKey()
  return queryOptions<MeQueryResponse, ResponseErrorConfig<Error>, MeQueryResponse, typeof queryKey>({
    queryKey,
    queryFn: async ({ signal }) => {
      config.signal = signal
      return me(unref(config))
    },
  })
}

/**
 * {@link /api/me}
 */
export function useMe<TData = MeQueryResponse, TQueryData = MeQueryResponse, TQueryKey extends QueryKey = MeQueryKey>(
  options: {
    query?: Partial<QueryObserverOptions<MeQueryResponse, ResponseErrorConfig<Error>, TData, TQueryData, TQueryKey>>
    client?: Partial<RequestConfig>
  } = {},
) {
  const { query: queryOptions, client: config = {} } = options ?? {}
  const queryKey = queryOptions?.queryKey ?? meQueryKey()

  const query = useQuery({
    ...(meQueryOptions(config) as unknown as QueryObserverOptions),
    queryKey: queryKey as QueryKey,
    ...(queryOptions as unknown as Omit<QueryObserverOptions, 'queryKey'>),
  }) as UseQueryReturnType<TData, ResponseErrorConfig<Error>> & { queryKey: TQueryKey }

  query.queryKey = queryKey as TQueryKey

  return query
}