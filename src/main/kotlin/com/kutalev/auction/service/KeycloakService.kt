package com.kutalev.auction.service

import com.kutalev.auction.config.KeycloakProperties
import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.KeycloakBuilder
import org.keycloak.representations.idm.CredentialRepresentation
import org.keycloak.representations.idm.UserRepresentation
import org.springframework.stereotype.Service
import java.util.*
import javax.ws.rs.core.Response

@Service
class KeycloakService(
    private val keycloakProperties: KeycloakProperties
) {
    private fun getKeycloakInstance(): Keycloak {
        return KeycloakBuilder.builder()
            .serverUrl(keycloakProperties.authServerUrl)
            .realm("master") // Мастер-Realm для администрирования
            .clientId("admin-cli")
            .username(keycloakProperties.adminUsername)
            .password(keycloakProperties.adminPassword)
            .build()
    }

    fun createUser(email: String, username: String, password: String, role: String): String {
        val keycloak = getKeycloakInstance()
        val realmResource = keycloak.realm(keycloakProperties.realm)
        val usersResource = realmResource.users()

        // Создаем пользователя
        val user = UserRepresentation().apply {
            this.email = email
            this.username = username
            this.isEnabled = true
            this.isEmailVerified = true
        }

        val response = usersResource.create(user)
        if (response.status != 201) {
            throw RuntimeException("Failed to create user in Keycloak: ${response.statusInfo}")
        }

        // Получаем ID созданного пользователя
        val userId = response.location.path.substringAfterLast("/")

        // Устанавливаем пароль
        val credential = CredentialRepresentation().apply {
            this.type = CredentialRepresentation.PASSWORD
            this.value = password
            this.isTemporary = false
        }

        usersResource.get(userId).resetPassword(credential)

        // Добавляем роль
        val roleRepresentation = realmResource.roles().get(role).toRepresentation()
        usersResource.get(userId).roles().realmLevel().add(listOf(roleRepresentation))

        return userId
    }

    fun deleteUser(keycloakId: String) {
        val keycloak = getKeycloakInstance()
        val realmResource = keycloak.realm(keycloakProperties.realm)
        val response = realmResource.users().delete(keycloakId)

        if (response.status != 204) {
            throw RuntimeException("Failed to delete user from Keycloak: ${response.statusInfo}")
        }
    }

    fun updateUser(keycloakId: String, email: String? = null, username: String? = null) {
        val keycloak = getKeycloakInstance()
        val realmResource = keycloak.realm(keycloakProperties.realm)
        val userResource = realmResource.users().get(keycloakId)
        val user = userResource.toRepresentation()

        email?.let { user.email = it }
        username?.let { user.username = it }

        userResource.update(user)
    }

    fun listUsers(): List<UserRepresentation> {
        val keycloak = getKeycloakInstance()
        val realmResource = keycloak.realm(keycloakProperties.realm)
        return realmResource.users().list()
    }
} 