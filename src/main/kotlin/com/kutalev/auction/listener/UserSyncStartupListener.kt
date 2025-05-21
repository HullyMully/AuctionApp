package com.kutalev.auction.listener

import com.kutalev.auction.service.UserSyncService
import org.keycloak.admin.client.Keycloak
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

@Component
class UserSyncStartupListener(
    private val keycloakAdminClient: Keycloak,
    private val userSyncService: UserSyncService,
    @Value("\${keycloak.realm}")
    private val realm: String
) {

    @EventListener
    fun onApplicationEvent(event: ContextRefreshedEvent) {
        logger.info("Начало синхронизации пользователей из Keycloak при запуске")
        try {
            val users = keycloakAdminClient.realm(realm).users().list()
            logger.info("Получено ${users.size} пользователей из Keycloak")

            users.forEach { userRepresentation ->
                try {
                    // Здесь мы используем getOrCreateUserFromJwt, но нам нужен метод для синхронизации
                    // без JWT. Создадим новый метод в UserSyncService.
                    val user = userSyncService.getOrCreateUserFromKeycloakUser(userRepresentation)
                    logger.debug("Синхронизирован пользователь: ${user.userId} - ${user.name}")
                } catch (e: Exception) {
                    logger.error("Ошибка при синхронизации пользователя ${userRepresentation.username}", e)
                }
            }
            logger.info("Завершение синхронизации пользователей из Keycloak при запуске")
        } catch (e: Exception) {
            logger.error("Ошибка при получении пользователей из Keycloak", e)
        }
    }
} 