package com.kutalev.auction

import com.kutalev.auction.config.KeycloakProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.boot.actuate.autoconfigure.metrics.SystemMetricsAutoConfiguration
import org.springframework.boot.actuate.autoconfigure.metrics.web.tomcat.TomcatMetricsAutoConfiguration

@SpringBootApplication(exclude = [SystemMetricsAutoConfiguration::class, TomcatMetricsAutoConfiguration::class])
@EnableScheduling
@EnableConfigurationProperties(KeycloakProperties::class)
class AuctionApplication

fun main(args: Array<String>) {
    runApplication<AuctionApplication>(*args)
} 