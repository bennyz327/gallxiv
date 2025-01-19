import type { Post } from './Post.ts'

/**
 * @description OK
 */
export type GetPosts200 = Post[]

export type GetPostsQueryResponse = GetPosts200

export type GetPostsQuery = {
  Response: GetPosts200
  Errors: any
}