package com.kutalev.auction.dto

import com.kutalev.auction.model.Role

data class UserDto(
    val userId: String,
    val name: String,
    val email: String,
    val role: Role
)

data class UpdateUserRequest(
    val name: String?,
    val email: String?
)

data class UpdateUserRoleRequest(
    val role: Role
) 