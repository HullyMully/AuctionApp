package com.kutalev.auction.service

import com.kutalev.auction.model.Role
import com.kutalev.auction.model.User
import com.kutalev.auction.repository.UserRepository
import org.keycloak.admin.client.Keycloak
import org.keycloak.representations.idm.UserRepresentation
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserSyncService(
    private val userRepository: UserRepository,
    private val keycloakAdminClient: Keycloak
) {
    @Transactional
    fun getOrCreateUserFromJwt(jwt: Jwt): User {
        val keycloakId = jwt.subject
        var email = jwt.getClaimAsString("email")
        val name = jwt.getClaimAsString("preferred_username") ?: jwt.getClaimAsString("name") ?: email ?: ""

        println("UserSyncService: getOrCreateUserFromJwt - keycloakId=$keycloakId, email=$email, name=$name")

        val realmRoles = try {
            val roles = keycloakAdminClient.realm("auction").users().get(keycloakId).roles().realmLevel().listEffective().map { it.name }
            println("UserSyncService: getOrCreateUserFromJwt - Роли из Keycloak для $keycloakId: $roles")
            roles
        } catch (e: Exception) {
            println("UserSyncService: Ошибка при получении ролей для пользователя $keycloakId из Keycloak в getOrCreateUserFromJwt: ${e.message}")
            emptyList()
        }

        val localRole = if (realmRoles.contains("ADMIN")) {
            Role.ADMIN
        } else {
            Role.USER
        }
         println("UserSyncService: getOrCreateUserFromJwt - Определенная локальная роль для $keycloakId: $localRole")

        if (email.isNullOrBlank()) {
            email = "$keycloakId@noemail.local"
            println("UserSyncService: getOrCreateUserFromJwt - email is blank, using fallback: $email")
        }

        var user = userRepository.findByKeycloakId(keycloakId)
        if (user == null && email != null) {
            user = userRepository.findByEmail(email).orElse(null)
            println("UserSyncService: getOrCreateUserFromJwt - user by email found? ${user != null}")
        } else {
            println("UserSyncService: getOrCreateUserFromJwt - user by keycloakId found? ${user != null}")
        }
        if (user == null) {
            try {
                user = User().apply {
                    this.keycloakId = keycloakId
                    this.email = email ?: ""
                    this.name = name
                    this.authProvider = "KEYCLOAK"
                    this.role = localRole
                    this.password = ""
                }
                val savedUser = userRepository.save(user)
                println("UserSyncService: getOrCreateUserFromJwt - User created in DB: $email, keycloakId: $keycloakId, userId: ${savedUser.userId}, role: ${savedUser.role}")
                val freshUser = userRepository.findById(savedUser.userId).orElse(null)
                return freshUser ?: savedUser
            } catch (ex: Exception) {
                println("UserSyncService: getOrCreateUserFromJwt - Failed to create user in DB: $email, keycloakId: $keycloakId, error: ${ex.message}")
                throw ex
            }
        } else {
            user.name = name
            user.email = email ?: user.email
            user.role = localRole
            println("UserSyncService: getOrCreateUserFromJwt - Обновление пользователя ${user.userId}: устанавливаю роль ${user.role}")
            val updatedUser = userRepository.save(user)
            println("UserSyncService: getOrCreateUserFromJwt - Пользователь ${updatedUser.userId} обновлен в БД, роль: ${updatedUser.role}")
            val freshUser = userRepository.findById(updatedUser.userId).orElse(null)
            return freshUser ?: updatedUser
        }
    }

    @Transactional
    fun getOrCreateUserFromKeycloakUser(userRepresentation: UserRepresentation): User {
        val keycloakId = userRepresentation.id
        var email = userRepresentation.email
        val name = userRepresentation.username

        println("UserSyncService: getOrCreateUserFromKeycloakUser - keycloakId=$keycloakId, email=$email, name=$name")

        val realmRoles = try {
            val roles = keycloakAdminClient.realm("auction").users().get(keycloakId).roles().realmLevel().listEffective().map { it.name }
            println("UserSyncService: getOrCreateUserFromKeycloakUser - Роли из Keycloak для $keycloakId: $roles")
            roles
        } catch (e: Exception) {
            println("UserSyncService: Ошибка при получении ролей для пользователя $keycloakId из Keycloak в getOrCreateUserFromKeycloakUser: ${e.message}")
            emptyList()
        }

        val localRole = if (realmRoles.contains("ADMIN")) {
            Role.ADMIN
        } else {
            Role.USER
        }
         println("UserSyncService: getOrCreateUserFromKeycloakUser - Определенная локальная роль для $keycloakId: $localRole")

        if (email.isNullOrBlank()) {
            email = "$keycloakId@noemail.local"
             println("UserSyncService: getOrCreateUserFromKeycloakUser - email is blank, using fallback: $email")
        }

        var user = userRepository.findByKeycloakId(keycloakId)
         if (user == null && email != null) {
            user = userRepository.findByEmail(email).orElse(null)
            println("UserSyncService: getOrCreateUserFromKeycloakUser - user by email found? ${user != null}")
        } else {
             println("UserSyncService: getOrCreateUserFromKeycloakUser - user by keycloakId found? ${user != null}")
        }

        if (user == null) {
             try {
                user = User().apply {
                    this.keycloakId = keycloakId
                    this.email = email ?: ""
                    this.name = name
                    this.authProvider = "KEYCLOAK"
                    this.role = localRole
                    this.password = ""
                }
                val savedUser = userRepository.save(user)
                 println("UserSyncService: getOrCreateUserFromKeycloakUser - User created in DB: $email, keycloakId: $keycloakId, userId: ${savedUser.userId}, role: ${savedUser.role}")
                val freshUser = userRepository.findById(savedUser.userId).orElse(null)
                return freshUser ?: savedUser
             } catch (ex: Exception) {
                 println("UserSyncService: getOrCreateUserFromKeycloakUser - Failed to create user in DB: $email, keycloakId: $keycloakId, error: ${ex.message}")
                throw ex
            }
        } else {
            user.name = name
            user.email = email ?: user.email
            user.role = localRole
            println("UserSyncService: getOrCreateUserFromKeycloakUser - Обновление пользователя ${user.userId}: устанавливаю роль ${user.role}")
            val updatedUser = userRepository.save(user)
            println("UserSyncService: getOrCreateUserFromKeycloakUser - Пользователь ${updatedUser.userId} обновлен в БД, роль: ${updatedUser.role}")
            val freshUser = userRepository.findById(updatedUser.userId).orElse(null)
            return freshUser ?: updatedUser
        }
    }

    @Transactional
    fun deleteUserByKeycloakId(keycloakId: String) {
        val user = userRepository.findByKeycloakId(keycloakId)
        if (user != null) {
            userRepository.delete(user)
             println("UserSyncService: Удален пользователь с keycloakId: $keycloakId")
        } else {
             println("UserSyncService: Пользователь с keycloakId: $keycloakId не найден в БД, удаление не требуется.")
        }
    }
} 