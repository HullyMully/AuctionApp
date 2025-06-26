package com.kutalev.auction.controller

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class LogTestController {
    private val log = LoggerFactory.getLogger(LogTestController::class.java)

    @GetMapping("/api/log-test")
    fun logTest(): String {
        log.info("Тестовый лог из /api/log-test!")
        return "Лог записан!"
    }
} 