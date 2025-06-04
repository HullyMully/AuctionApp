package com.kutalev.auction.controller

import com.fasterxml.jackson.databind.JsonNode
import com.kutalev.auction.service.UserSyncService
import org.keycloak.admin.client.Keycloak
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

@RestController
@RequestMapping("/api/keycloak")
class KeycloakEventController(
    private val userSyncService: UserSyncService,
    private val keycloakAdminClient: Keycloak
) {

    @PostMapping("/events")
    fun handleKeycloakEvent(@RequestBody event: JsonNode): ResponseEntity<Void> {
        logger.info("Получено событие от Keycloak: ${event.toString()}")

        val eventType = event.get("type")?.asText()
        val userId = event.get("details")?.get("user_id")?.asText()

        if (eventType == null || userId == null) {
            logger.warn("Не удалось получить тип события или user_id из события Keycloak")
            return ResponseEntity.badRequest().build()
        }

        logger.info("Обработка события Keycloak: Тип = ${eventType}, User ID = ${userId}")

        when (eventType) {
            "CREATE_USER", "REGISTER" -> {
                try {
                    val userRepresentation = keycloakAdminClient.realm("auction").users().get(userId).toRepresentation()
                    userSyncService.getOrCreateUserFromKeycloakUser(userRepresentation)
                    logger.info("Успешно синхронизирован новый пользователь: ${userId}")
                } catch (e: Exception) {
                    logger.error("Ошибка при синхронизации нового пользователя ${userId}", e)
                }
            }
            "UPDATE_PROFILE", "UPDATE_EMAIL" -> {
                 try {
                    val userRepresentation = keycloakAdminClient.realm("auction").users().get(userId).toRepresentation()
                    userSyncService.getOrCreateUserFromKeycloakUser(userRepresentation)
                     logger.info("Успешно обновлен пользователь: ${userId}")
                } catch (e: Exception) {
                    logger.error("Ошибка при обновлении пользователя ${userId}", e)
                }
            }
            "DELETE_USER" -> {
                try {
                    userSyncService.deleteUserByKeycloakId(userId)
                    logger.info("Успешно удален пользователь: ${userId}")
                } catch (e: Exception) {
                    logger.error("Ошибка при удалении пользователя ${userId}", e)
                }
            }
            // TODO: Обработать другие типы событий, если необходимо
            else -> {
                logger.info("Пропущено необрабатываемое событие Keycloak типа: ${eventType}")
            }
        }

        return ResponseEntity.ok().build()
    }
}
 