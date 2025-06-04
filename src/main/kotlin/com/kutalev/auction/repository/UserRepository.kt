package com.kutalev.auction.repository

import com.kutalev.auction.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface UserRepository : JpaRepository<User, String> {
    fun existsByEmail(email: String): Boolean
    fun findByEmail(email: String): Optional<User>
    fun findByKeycloakId(keycloakId: String): User?
} 