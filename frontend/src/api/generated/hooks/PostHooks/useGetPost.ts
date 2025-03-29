import client from '@/api/app-axios'
import type { GetPostQueryResponse, GetPostPathParams } from '../../types/GetPost.ts'
import type { RequestConfig, ResponseErrorConfig } from '@/api/app-axios'
import type { QueryKey, QueryObserverOptions, UseQueryReturnType } from '@tanstack/vue-query'
import type { MaybeRef } from 'vue'
import { queryOptions, useQuery } from '@tanstack/vue-query'
import { unref } from 'vue'

export const getPostQueryKey = (account: MaybeRef<GetPostPathParams['account']>, postId: MaybeRef<GetPostPathParams['postId']>) =>
  [{ url: '/api/user/:account/post/:postId', params: { account: account, postId: postId } }] as const

export type GetPostQueryKey = ReturnType<typeof getPostQueryKey>

/**
 * {@link /api/user/:account/post/:postId}
 */
async function getPost(account: GetPostPathParams['account'], postId: GetPostPathParams['postId'], config: Partial<RequestConfig> = {}) {
  const res = await client<GetPostQueryResponse, ResponseErrorConfig<Error>, unknown>({ method: 'GET', url: `/api/user/${account}/post/${postId}`, ...config })
  return res.data
}

export function getPostQueryOptions(
  account: MaybeRef<GetPostPathParams['account']>,
  postId: MaybeRef<GetPostPathParams['postId']>,
  config: Partial<RequestConfig> = {},
) {
  const queryKey = getPostQueryKey(account, postId)
  return queryOptions<GetPostQueryResponse, ResponseErrorConfig<Error>, GetPostQueryResponse, typeof queryKey>({
    enabled: !!(account && postId),
    queryKey,
    queryFn: async ({ signal }) => {
      config.signal = signal
      return getPost(unref(account), unref(postId), unref(config))
    },
  })
}

/**
 * {@link /api/user/:account/post/:postId}
 */
export function useGetPost<TData = GetPostQueryResponse, TQueryData = GetPostQueryResponse, TQueryKey extends QueryKey = GetPostQueryKey>(
  account: MaybeRef<GetPostPathParams['account']>,
  postId: MaybeRef<GetPostPathParams['postId']>,
  options: {
    query?: Partial<QueryObserverOptions<GetPostQueryResponse, ResponseErrorConfig<Error>, TData, TQueryData, TQueryKey>>
    client?: Partial<RequestConfig>
  } = {},
) {
  const { query: queryOptions, client: config = {} } = options ?? {}
  const queryKey = queryOptions?.queryKey ?? getPostQueryKey(account, postId)

  const query = useQuery({
    ...(getPostQueryOptions(account, postId, config) as unknown as QueryObserverOptions),
    queryKey: queryKey as QueryKey,
    ...(queryOptions as unknown as Omit<QueryObserverOptions, 'queryKey'>),
  }) as UseQueryReturnType<TData, ResponseErrorConfig<Error>> & { queryKey: TQueryKey }

  query.queryKey = queryKey as TQueryKey

  return query
}