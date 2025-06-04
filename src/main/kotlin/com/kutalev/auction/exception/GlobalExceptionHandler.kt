package com.kutalev.auction.exception

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {
    private val logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(Exception::class)
    fun handleAllExceptions(ex: Exception): ResponseEntity<ErrorResponse> {
        logger.error("Unexpected error occurred", ex)
        val errorResponse = ErrorResponse(
            status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            error = "Internal Server Error",
            message = ex.message ?: "An unexpected error occurred",
            path = "/api/auctions" // В реальном приложении нужно получать путь из запроса
        )
        return ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(RuntimeException::class)
    fun handleRuntimeException(ex: RuntimeException): ResponseEntity<ErrorResponse> {
        logger.error("Runtime error occurred", ex)
        val errorResponse = ErrorResponse(
            status = HttpStatus.BAD_REQUEST.value(),
            error = "Bad Request",
            message = ex.message ?: "An error occurred",
            path = "/api/auctions" // В реальном приложении нужно получать путь из запроса
        )
        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }
}

data class ErrorResponse(
    val timestamp: String = java.time.LocalDateTime.now().toString(),
    val status: Int,
    val error: String,
    val message: String,
    val path: String
) 