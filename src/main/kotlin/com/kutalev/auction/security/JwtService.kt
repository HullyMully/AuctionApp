package com.kutalev.auction.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Service
import java.util.*

@Service
class JwtService(private val jwtConfig: JwtConfig) {
    private val key = Keys.hmacShaKeyFor(jwtConfig.secret.toByteArray())

    fun generateToken(userId: String, role: String): String {
        val claims = HashMap<String, Any>()
        claims["role"] = role

        return Jwts.builder()
            .setClaims(claims)
            .setSubject(userId)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + jwtConfig.expiration))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }

    fun validateToken(token: String): Boolean {
        return try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun getUserIdFromToken(token: String): String {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body
            .subject
    }

    fun getRoleFromToken(token: String): String {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body
            .get("role", String::class.java)
    }
} 