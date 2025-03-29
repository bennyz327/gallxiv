export type UserPostMetadataRequest = {
  /**
   * @type string
   */
  title: string
  /**
   * @type string | undefined
   */
  description?: string
  /**
   * @type string | undefined
   */
  tags?: string
  /**
   * @type boolean | undefined
   */
  isPublic?: boolean
}