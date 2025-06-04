package com.kutalev.auction.dto

import com.kutalev.auction.model.Role

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val role: Role = Role.USER
) 