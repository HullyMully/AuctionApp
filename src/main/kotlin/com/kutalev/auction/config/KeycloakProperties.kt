package com.kutalev.auction.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "keycloak")
data class KeycloakProperties(
    val authServerUrl: String,
    val realm: String,
    val resource: String,
    val publicClient: Boolean,
    val sslRequired: String,
    val principalAttribute: String,
    val useResourceRoleMappings: Boolean,
    val adminUsername: String,
    val adminPassword: String
) 