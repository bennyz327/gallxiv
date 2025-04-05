package com.benjk.gallexiv.data.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UpdateUserRequest(
    @field:NotBlank(message = "使用者名稱不得為空")
    @field:Size(min = 3, max = 50, message = "使用者名稱長度必須介於 3 到 50 個字元之間")
    val username: String
)
