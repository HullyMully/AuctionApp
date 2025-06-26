package com.kutalev.auction.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.core.convert.converter.Converter
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt

@Configuration
@EnableMethodSecurity
class SecurityConfig {
        @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeHttpRequests { authz ->
                authz
                    .requestMatchers("/actuator/prometheus").permitAll()
                    .requestMatchers("/api/images/**").permitAll() 
                    .requestMatchers("/api/log-test").permitAll()
                    .anyRequest().authenticated()
            }
            .csrf { it.disable() }
        return http.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers(
                        "/api/auth/**",
                        "/api/users/register",
                        "/api/users",
                        "/api/users/**",
                        "/api/auctions/**",
                        "/api/auctions",
                        "/actuator/prometheus"
                    ).permitAll()
                    .requestMatchers("/api/admin/**").hasRole("ADMIN")
                    .anyRequest().authenticated()
            }
            .oauth2ResourceServer { oauth2ResourceServer ->
                oauth2ResourceServer.jwt { jwt ->
                    jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())
                }
            }
        return http.build()
    }

    @Bean
    fun jwtAuthenticationConverter(): JwtAuthenticationConverter {
        val grantedAuthoritiesConverter: Converter<Jwt, Collection<GrantedAuthority>> = Converter {
            jwt ->
            val realmAccess = jwt.getClaim<Map<String, Any>>("realm_access")

            val roles = if (realmAccess != null && realmAccess.containsKey("roles")) {
                 @Suppress("UNCHECKED_CAST")
                 realmAccess["roles"] as? List<String> ?: emptyList()
            } else {
                emptyList()
            }

            roles.map { SimpleGrantedAuthority("ROLE_" + it) }
        }

        val jwtAuthenticationConverter = JwtAuthenticationConverter()
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter)
        return jwtAuthenticationConverter
    }
} 