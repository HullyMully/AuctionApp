package com.kutalev.auction.controller

import com.kutalev.auction.dto.UserDto
import com.kutalev.auction.dto.CreateUserRequest
import com.kutalev.auction.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService
) {
    @GetMapping
    fun getAllUsers(): ResponseEntity<List<UserDto>> {
        val users = userService.getAllUsers()
        println("UserController: getAllUsers() -> ${users.size} users")
        return ResponseEntity.ok(users)
    }

    @GetMapping("/{userId}")
    fun getUserById(@PathVariable userId: String): ResponseEntity<UserDto> =
        ResponseEntity.ok(userService.getUserById(userId))

    @PostMapping
    fun createUser(@RequestBody request: CreateUserRequest): ResponseEntity<UserDto> {
        val user = userService.createUser(request)
        return ResponseEntity.ok(user)
    }

    @PutMapping("/{userId}")
    fun updateUser(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable userId: String,
        @RequestBody request: CreateUserRequest
    ): ResponseEntity<UserDto> {
        val user = userService.updateUser(userDetails.username, userId, request)
        return ResponseEntity.ok(user)
    }

    @DeleteMapping("/{userId}")
    fun deleteUser(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable userId: String
    ): ResponseEntity<Unit> {
        userService.deleteUser(userDetails.username, userId)
        return ResponseEntity.ok().build()
    }
} 