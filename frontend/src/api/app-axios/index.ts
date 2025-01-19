import axios, {type AxiosError, type AxiosRequestConfig, type AxiosResponse} from "axios";

export const client = async <TData, TError = unknown, TVariables = unknown>(config: RequestConfig<TVariables>): Promise<ResponseConfig<TData>> => {
  return axios.request(config)
}

export type RequestConfig<T = unknown> = Omit<AxiosRequestConfig<T>, 'data'> & { data?: T | FormData }

export type ResponseConfig<T = unknown> = AxiosResponse<T>

export type ResponseErrorConfig<E = unknown> = AxiosError<E>

export default client
