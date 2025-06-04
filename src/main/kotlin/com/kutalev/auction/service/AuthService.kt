package com.kutalev.auction.service

import com.kutalev.auction.dto.RegisterRequest
import com.kutalev.auction.dto.UserDto
import com.kutalev.auction.model.Role
import com.kutalev.auction.model.User
import com.kutalev.auction.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val keycloakService: KeycloakService
) {
    @Transactional
    fun registerUser(request: RegisterRequest): UserDto {
        if (userRepository.findByEmail(request.email).isPresent) {
            throw RuntimeException("User with this email already exists")
        }

        // Создаем пользователя в Keycloak
        val keycloakId = keycloakService.createUser(
            email = request.email,
            username = request.name,
            password = request.password,
            role = request.role.name
        )

        // Создаем пользователя в нашей базе данных
        val user = User().apply {
            name = request.name
            email = request.email
            password = "" // Пароль теперь хранится только в Keycloak
            role = request.role
            authProvider = "KEYCLOAK"
            this.keycloakId = keycloakId
        }
        
        val savedUser = userRepository.save(user)
        return UserDto(
            userId = savedUser.userId,
            name = savedUser.name,
            email = savedUser.email,
            role = savedUser.role
        )
    }

    @Transactional
    fun deleteUser(userId: String) {
        val user = userRepository.findById(userId)
            .orElseThrow { RuntimeException("User not found") }
        
        // Удаляем пользователя из Keycloak
        user.keycloakId?.let { keycloakService.deleteUser(it) }
        
        // Удаляем пользователя из нашей базы данных
        userRepository.delete(user)
    }

    @Transactional
    fun updateUser(userId: String, email: String? = null, name: String? = null): UserDto {
        val user = userRepository.findById(userId)
            .orElseThrow { RuntimeException("User not found") }

        // Обновляем пользователя в Keycloak
        user.keycloakId?.let { keycloakId ->
            keycloakService.updateUser(keycloakId, email, name)
        }

        // Обновляем пользователя в нашей базе данных
        email?.let { user.email = it }
        name?.let { user.name = it }

        val updatedUser = userRepository.save(user)
        return UserDto(
            userId = updatedUser.userId,
            name = updatedUser.name,
            email = updatedUser.email,
            role = updatedUser.role
        )
    }
} 