package com.kutalev.auction.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class JwtAuthenticationFilter(
    private val jwtService: JwtService
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")
        
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            val token = authHeader.substring(7)
            
            if (jwtService.validateToken(token)) {
                val userId = jwtService.getUserIdFromToken(token)
                val role = jwtService.getRoleFromToken(token)
                
                val authorities = mutableListOf(SimpleGrantedAuthority("ROLE_USER"))
                if (role == "ADMIN") {
                    authorities.add(SimpleGrantedAuthority("ROLE_ADMIN"))
                }
                
                val authentication = UsernamePasswordAuthenticationToken(
                    userId,
                    null,
                    authorities
                )
                
                SecurityContextHolder.getContext().authentication = authentication
            }
        }
        
        filterChain.doFilter(request, response)
    }
} 