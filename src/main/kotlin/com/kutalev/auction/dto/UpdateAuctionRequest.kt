package com.kutalev.auction.dto

import java.time.LocalDateTime

data class UpdateAuctionRequest(
    val title: String?,
    val description: String?,
    val startPrice: Double?,
    val startTime: LocalDateTime?,
    val endTime: LocalDateTime?
) 