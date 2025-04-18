import type { Post } from './Post.ts'

export type Image = {
  /**
   * @type string | undefined
   */
  id?: string
  /**
   * @type string | undefined
   */
  originalFilename?: string
  /**
   * @type string | undefined
   */
  originalExtension?: string
  /**
   * @type array | undefined
   */
  refPosts?: Post[]
}