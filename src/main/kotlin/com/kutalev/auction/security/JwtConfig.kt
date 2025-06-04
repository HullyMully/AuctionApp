package com.kutalev.auction.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class JwtConfig {
    @Value("\${jwt.secret}")
    lateinit var secret: String

    @Value("\${jwt.expiration}")
    var expiration: Long = 86400000 // 24 часа в миллисекундах
} 