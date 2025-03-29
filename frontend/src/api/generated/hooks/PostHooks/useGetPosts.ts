import client from '@/api/app-axios'
import type { GetPostsQueryResponse } from '../../types/GetPosts.ts'
import type { RequestConfig, ResponseErrorConfig } from '@/api/app-axios'
import type { QueryKey, QueryObserverOptions, UseQueryReturnType } from '@tanstack/vue-query'
import { queryOptions, useQuery } from '@tanstack/vue-query'
import { unref } from 'vue'

export const getPostsQueryKey = () => [{ url: '/api/posts' }] as const

export type GetPostsQueryKey = ReturnType<typeof getPostsQueryKey>

/**
 * {@link /api/posts}
 */
async function getPosts(config: Partial<RequestConfig> = {}) {
  const res = await client<GetPostsQueryResponse, ResponseErrorConfig<Error>, unknown>({ method: 'GET', url: `/api/posts`, ...config })
  return res.data
}

export function getPostsQueryOptions(config: Partial<RequestConfig> = {}) {
  const queryKey = getPostsQueryKey()
  return queryOptions<GetPostsQueryResponse, ResponseErrorConfig<Error>, GetPostsQueryResponse, typeof queryKey>({
    queryKey,
    queryFn: async ({ signal }) => {
      config.signal = signal
      return getPosts(unref(config))
    },
  })
}

/**
 * {@link /api/posts}
 */
export function useGetPosts<TData = GetPostsQueryResponse, TQueryData = GetPostsQueryResponse, TQueryKey extends QueryKey = GetPostsQueryKey>(
  options: {
    query?: Partial<QueryObserverOptions<GetPostsQueryResponse, ResponseErrorConfig<Error>, TData, TQueryData, TQueryKey>>
    client?: Partial<RequestConfig>
  } = {},
) {
  const { query: queryOptions, client: config = {} } = options ?? {}
  const queryKey = queryOptions?.queryKey ?? getPostsQueryKey()

  const query = useQuery({
    ...(getPostsQueryOptions(config) as unknown as QueryObserverOptions),
    queryKey: queryKey as QueryKey,
    ...(queryOptions as unknown as Omit<QueryObserverOptions, 'queryKey'>),
  }) as UseQueryReturnType<TData, ResponseErrorConfig<Error>> & { queryKey: TQueryKey }

  query.queryKey = queryKey as TQueryKey

  return query
}
