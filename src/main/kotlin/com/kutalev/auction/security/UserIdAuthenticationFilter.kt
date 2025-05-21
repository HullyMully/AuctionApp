package com.kutalev.auction.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import com.kutalev.auction.repository.UserRepository
import com.kutalev.auction.model.Role

class UserIdAuthenticationFilter(
    private val userRepository: UserRepository
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val userId = request.getHeader("User-Id")
        
        if (!userId.isNullOrEmpty()) {
            val user = userRepository.findById(userId).orElse(null)
            
            if (user != null) {
                val authorities = mutableListOf(SimpleGrantedAuthority("ROLE_USER"))
                if (user.role == Role.ADMIN) {
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