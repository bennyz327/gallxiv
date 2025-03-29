package com.benjk.gallexiv.data.dto

data class RegisterRequest(
    val account: String,
    val email: String,
    val username: String,
    val password: String
)