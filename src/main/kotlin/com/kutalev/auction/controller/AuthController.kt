package com.kutalev.auction.controller

import com.kutalev.auction.dto.LoginRequest
import com.kutalev.auction.dto.RegisterRequest
import com.kutalev.auction.dto.UserDto
import com.kutalev.auction.service.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService
) {
    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest): ResponseEntity<UserDto> {
        val user = authService.registerUser(request)
        return ResponseEntity.ok(user)
    }

    // Метод login больше не нужен, так как аутентификация теперь происходит через Keycloak
    // Пользователи будут перенаправляться на страницу входа Keycloak
} 