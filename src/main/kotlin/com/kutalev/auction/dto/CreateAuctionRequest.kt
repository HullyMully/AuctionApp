package com.kutalev.auction.dto

import java.time.LocalDateTime

data class CreateAuctionRequest(
    val title: String,
    val description: String,
    val startPrice: Double,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime
) 