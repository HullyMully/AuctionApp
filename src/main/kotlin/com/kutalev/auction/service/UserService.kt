package com.kutalev.auction.service

import com.kutalev.auction.dto.CreateUserRequest
import com.kutalev.auction.dto.UserDto
import com.kutalev.auction.model.User
import com.kutalev.auction.model.Role
import com.kutalev.auction.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val keycloakService: KeycloakService
) {
    fun getAllUsers(): List<UserDto> = 
        userRepository.findAll().map { it.toDto() }

    fun getUserById(userId: String): UserDto =
        userRepository.findById(userId)
            .orElseThrow { RuntimeException("User not found") }
            .toDto()

    @Transactional
    fun createUser(request: CreateUserRequest): UserDto {
        if (userRepository.findByEmail(request.email).isPresent) {
            throw RuntimeException("User with email ${request.email} already exists")
        }

        val keycloakId = keycloakService.createUser(
            email = request.email,
            username = request.name,
            password = request.password,
            role = request.role.name
        )

        val user = User().apply {
            name = request.name
            email = request.email
            password = ""
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
    fun updateUser(currentUserId: String, targetUserId: String, request: CreateUserRequest): UserDto {
        val currentUser = userRepository.findById(currentUserId)
            .orElseThrow { RuntimeException("Current user not found") }

        val targetUser = userRepository.findById(targetUserId)
            .orElseThrow { RuntimeException("Target user not found") }

        if (currentUserId != targetUserId && currentUser.role != Role.ADMIN) {
            throw RuntimeException("You can only update your own profile")
        }

        if (request.email != targetUser.email && userRepository.findByEmail(request.email).isPresent) {
            throw RuntimeException("User with email ${request.email} already exists")
        }

        targetUser.name = request.name
        targetUser.email = request.email
        if (request.password.isNotBlank()) {
            targetUser.password = passwordEncoder.encode(request.password)
        }

        return userRepository.save(targetUser).toDto()
    }

    @Transactional
    fun deleteUser(currentUserId: String, targetUserId: String) {
        val currentUser = userRepository.findById(currentUserId)
            .orElseThrow { RuntimeException("Current user not found") }

        val targetUser = userRepository.findById(targetUserId)
            .orElseThrow { RuntimeException("Target user not found") }

        if (currentUserId != targetUserId && currentUser.role != Role.ADMIN) {
            throw RuntimeException("You can only delete your own profile or you must be an Admin")
        }
        
        targetUser.keycloakId?.let { keycloakService.deleteUser(it) }

        userRepository.delete(targetUser)
    }

    private fun User.toDto() = UserDto(
        userId = userId,
        name = name,
        email = email,
        role = role
    )
} 