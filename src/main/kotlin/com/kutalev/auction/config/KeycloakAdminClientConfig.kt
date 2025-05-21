package com.kutalev.auction.config

import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.KeycloakBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class KeycloakAdminClientConfig(
    @Value("\${keycloak.auth-server-url}")
    private val authServerUrl: String,
    @Value("\${keycloak.realm}")
    private val realm: String,
    @Value("\${keycloak.admin-cli-client-id:admin-cli}")
    private val adminCliClientId: String,
    @Value("\${keycloak.admin-username}")
    private val adminUsername: String,
    @Value("\${keycloak.admin-password}")
    private val adminPassword: String
) {

    @Bean
    fun keycloakAdminClient(): Keycloak {
        return KeycloakBuilder.builder()
            .serverUrl(authServerUrl)
            .realm("master") // Мастер- realm для администрирования
            .clientId(adminCliClientId)
            .username(adminUsername)
            .password(adminPassword)
            .grantType("password")
            .build()
    }
} 