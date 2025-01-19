import type { UserPostMetadataRequest } from './UserPostMetadataRequest.ts'

/**
 * @description OK
 */
export type UserPost200 = string

export type UserPostMutationRequest = {
  /**
   * @type object
   */
  metadata: UserPostMetadataRequest
  /**
   * @type array
   */
  images: Blob[]
}

export type UserPostMutationResponse = UserPost200

export type UserPostMutation = {
  Response: UserPost200
  Request: UserPostMutationRequest
  Errors: any
}