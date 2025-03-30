package com.benjk.gallexiv.data.dto

data class ChangePasswordRequest(
    val currentPassword: String,
    val newPassword: String
)