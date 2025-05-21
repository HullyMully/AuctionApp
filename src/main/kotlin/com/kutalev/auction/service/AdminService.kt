package com.kutalev.auction.service

import com.kutalev.auction.dto.AdminDto
import com.kutalev.auction.dto.CreateAdminRequest
import com.kutalev.auction.dto.UpdateAdminRequest
import com.kutalev.auction.model.Admin
import com.kutalev.auction.repository.AdminRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AdminService(
    private val adminRepository: AdminRepository
) {
    fun getAllAdmins(): List<AdminDto> {
        return adminRepository.findAll().map { it.toDto() }
    }

    fun getAdminById(adminId: String): AdminDto {
        return adminRepository.findById(adminId)
            .orElseThrow { RuntimeException("Admin not found") }
            .toDto()
    }

    @Transactional
    fun createAdmin(request: CreateAdminRequest): AdminDto {
        if (adminRepository.findByEmail(request.email) != null) {
            throw RuntimeException("Admin with this email already exists")
        }

        val admin = Admin(
            username = request.username,
            email = request.email
        )
        return adminRepository.save(admin).toDto()
    }

    @Transactional
    fun updateAdmin(adminId: String, request: UpdateAdminRequest): AdminDto {
        val admin = adminRepository.findById(adminId)
            .orElseThrow { RuntimeException("Admin not found") }

        request.username?.let { admin.username = it }
        request.email?.let { 
            if (adminRepository.findByEmail(it) != null) {
                throw RuntimeException("Admin with this email already exists")
            }
            admin.email = it 
        }

        return adminRepository.save(admin).toDto()
    }

    @Transactional
    fun deleteAdmin(adminId: String) {
        if (!adminRepository.existsById(adminId)) {
            throw RuntimeException("Admin not found")
        }
        adminRepository.deleteById(adminId)
    }

    private fun Admin.toDto(): AdminDto {
        return AdminDto(
            adminId = adminId,
            username = username,
            email = email,
            role = role
        )
    }
} 