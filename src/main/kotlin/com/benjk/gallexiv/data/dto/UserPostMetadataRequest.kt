package com.benjk.gallexiv.data.dto

data class UserPostMetadataRequest(
    val title: String,
    val description: String? = "",
    val tags: String? = "",
    val isPublic: Boolean? = true,
)
