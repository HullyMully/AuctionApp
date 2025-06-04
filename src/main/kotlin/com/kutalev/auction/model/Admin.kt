package com.kutalev.auction.model

import jakarta.persistence.*

@Entity
@Table(name = "admins")
data class Admin(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val adminId: String = "",
    
    @Column(nullable = false)
    var username: String,
    
    @Column(nullable = false, unique = true)
    var email: String,
    
    @Column(nullable = false)
    var role: String = "ADMIN"
) 