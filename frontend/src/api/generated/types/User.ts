export const userRoleEnum = {
  ADMIN: 'ADMIN',
  USER: 'USER',
} as const

export type UserRoleEnum = (typeof userRoleEnum)[keyof typeof userRoleEnum]

export type User = {
  /**
   * @type string | undefined, uuid
   */
  id?: string
  /**
   * @type string | undefined
   */
  account?: string
  /**
   * @type string | undefined
   */
  email?: string
  /**
   * @type string | undefined
   */
  username?: string
  /**
   * @type string | undefined
   */
  password?: string
  /**
   * @type string
   */
  role: UserRoleEnum
}