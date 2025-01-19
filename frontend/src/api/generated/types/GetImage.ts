export type GetImagePathParams = {
  /**
   * @type string
   */
  id: string
}

/**
 * @description OK
 */
export type GetImage200 = Blob

export type GetImageQueryResponse = GetImage200

export type GetImageQuery = {
  Response: GetImage200
  PathParams: GetImagePathParams
  Errors: any
}