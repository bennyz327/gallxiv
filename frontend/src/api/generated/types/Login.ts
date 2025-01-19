import type { LoginRequest } from './LoginRequest.ts'

/**
 * @description OK
 */
export type Login200 = any

export type LoginMutationRequest = LoginRequest

export type LoginMutationResponse = Login200

export type LoginMutation = {
  Response: Login200
  Request: LoginMutationRequest
  Errors: any
}