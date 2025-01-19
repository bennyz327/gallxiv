import type { Post } from './Post.ts'

export type GetPostPathParams = {
  /**
   * @type string
   */
  account: string
  /**
   * @type string
   */
  postId: string
}

/**
 * @description OK
 */
export type GetPost200 = Post

export type GetPostQueryResponse = GetPost200

export type GetPostQuery = {
  Response: GetPost200
  PathParams: GetPostPathParams
  Errors: any
}