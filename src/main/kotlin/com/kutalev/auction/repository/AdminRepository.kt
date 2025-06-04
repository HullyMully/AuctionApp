package com.kutalev.auction.repository

import com.kutalev.auction.model.Admin
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AdminRepository : JpaRepository<Admin, String> {
    fun findByEmail(email: String): Admin?
} 