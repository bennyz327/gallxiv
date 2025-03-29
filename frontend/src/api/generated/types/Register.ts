import type { RegisterRequest } from './RegisterRequest.ts'

/**
 * @description OK
 */
export type Register200 = {
  [key: string]: string
}

export type RegisterMutationRequest = RegisterRequest

export type RegisterMutationResponse = Register200

export type RegisterMutation = {
  Response: Register200
  Request: RegisterMutationRequest
  Errors: any
}