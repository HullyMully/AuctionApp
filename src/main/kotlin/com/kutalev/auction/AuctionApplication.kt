package com.kutalev.auction

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.boot.actuate.autoconfigure.metrics.SystemMetricsAutoConfiguration
import org.springframework.boot.actuate.autoconfigure.metrics.web.tomcat.TomcatMetricsAutoConfiguration

@SpringBootApplication(exclude = [SystemMetricsAutoConfiguration::class, TomcatMetricsAutoConfiguration::class])
@EnableScheduling
class AuctionApplication

fun main(args: Array<String>) {
    runApplication<AuctionApplication>(*args)
} 