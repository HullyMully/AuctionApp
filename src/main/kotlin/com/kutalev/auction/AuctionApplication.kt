package com.kutalev.auction

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class AuctionApplication

fun main(args: Array<String>) {
    runApplication<AuctionApplication>(*args)
} 