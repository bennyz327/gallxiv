import type { Image } from './Image.ts'
import type { User } from './User.ts'

export type Post = {
  /**
   * @type string | undefined, uuid
   */
  id?: string
  /**
   * @type string | undefined
   */
  title?: string
  /**
   * @type string | undefined
   */
  description?: string
  /**
   * @type string | undefined
   */
  tags?: string
  /**
   * @type object | undefined
   */
  user?: User
  /**
   * @type array | undefined
   */
  images?: Image[]
  /**
   * @type boolean
   */
  isPublic: boolean
  /**
   * @type boolean
   */
  isSensitive: boolean
}