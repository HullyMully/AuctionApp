package com.kutalev.auction.listener

import com.kutalev.auction.service.KeycloakService
import com.kutalev.auction.service.UserSyncService
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.stereotype.Component
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

@Component
class UserSyncStartupListener(
    private val keycloakService: KeycloakService,
    private val userSyncService: UserSyncService
) : ApplicationListener<ContextRefreshedEvent> {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        logger.info("Начало синхронизации пользователей из Keycloak при запуске")
        runBlocking {
            try {
                // Ждем 30 секунд, пока Keycloak полностью запустится
                delay(30000)
                userSyncService.syncUsersFromKeycloak()
            } catch (e: Exception) {
                logger.error("Ошибка при получении пользователей из Keycloak", e)
            }
        }
    }
} 