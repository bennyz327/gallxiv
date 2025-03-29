import client from '@/api/app-axios'
import type { GetImageQueryResponse, GetImagePathParams } from '../../types/GetImage.ts'
import type { RequestConfig, ResponseErrorConfig } from '@/api/app-axios'
import type { QueryKey, QueryObserverOptions, UseQueryReturnType } from '@tanstack/vue-query'
import type { MaybeRef } from 'vue'
import { queryOptions, useQuery } from '@tanstack/vue-query'
import { unref } from 'vue'

export const getImageQueryKey = (postId: MaybeRef<GetImagePathParams['postId']>, id: MaybeRef<GetImagePathParams['id']>) =>
  [{ url: '/api/image/:postId/:id', params: { postId: postId, id: id } }] as const

export type GetImageQueryKey = ReturnType<typeof getImageQueryKey>

/**
 * {@link /api/image/:postId/:id}
 */
async function getImage(postId: GetImagePathParams['postId'], id: GetImagePathParams['id'], config: Partial<RequestConfig> = {}) {
  const res = await client<GetImageQueryResponse, ResponseErrorConfig<Error>, unknown>({ method: 'GET', url: `/api/image/${postId}/${id}`, ...config })
  return res.data
}

export function getImageQueryOptions(
  postId: MaybeRef<GetImagePathParams['postId']>,
  id: MaybeRef<GetImagePathParams['id']>,
  config: Partial<RequestConfig> = {},
) {
  const queryKey = getImageQueryKey(postId, id)
  return queryOptions<GetImageQueryResponse, ResponseErrorConfig<Error>, GetImageQueryResponse, typeof queryKey>({
    enabled: !!(postId && id),
    queryKey,
    queryFn: async ({ signal }) => {
      config.signal = signal
      return getImage(unref(postId), unref(id), unref(config))
    },
  })
}

/**
 * {@link /api/image/:postId/:id}
 */
export function useGetImage<TData = GetImageQueryResponse, TQueryData = GetImageQueryResponse, TQueryKey extends QueryKey = GetImageQueryKey>(
  postId: MaybeRef<GetImagePathParams['postId']>,
  id: MaybeRef<GetImagePathParams['id']>,
  options: {
    query?: Partial<QueryObserverOptions<GetImageQueryResponse, ResponseErrorConfig<Error>, TData, TQueryData, TQueryKey>>
    client?: Partial<RequestConfig>
  } = {},
) {
  const { query: queryOptions, client: config = {} } = options ?? {}
  const queryKey = queryOptions?.queryKey ?? getImageQueryKey(postId, id)

  const query = useQuery({
    ...(getImageQueryOptions(postId, id, config) as unknown as QueryObserverOptions),
    queryKey: queryKey as QueryKey,
    ...(queryOptions as unknown as Omit<QueryObserverOptions, 'queryKey'>),
  }) as UseQueryReturnType<TData, ResponseErrorConfig<Error>> & { queryKey: TQueryKey }

  query.queryKey = queryKey as TQueryKey

  return query
}