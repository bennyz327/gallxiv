import type { User } from './User.ts'

/**
 * @description OK
 */
export type Me200 = User

export type MeQueryResponse = Me200

export type MeQuery = {
  Response: Me200
  Errors: any
}