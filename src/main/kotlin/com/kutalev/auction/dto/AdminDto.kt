package com.kutalev.auction.dto

data class AdminDto(
    val adminId: String?,
    val username: String,
    val email: String,
    val role: String
)

data class CreateAdminRequest(
    val username: String,
    val email: String
)

data class UpdateAdminRequest(
    val username: String?,
    val email: String?
) 