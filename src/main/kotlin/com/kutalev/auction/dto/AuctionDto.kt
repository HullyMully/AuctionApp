package com.kutalev.auction.dto

import com.kutalev.auction.model.AuctionStatus
import java.time.LocalDateTime

data class AuctionDto(
    val auctionId: String,
    val title: String,
    val description: String,
    val startPrice: Double,
    val currentPrice: Double,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val sellerId: String,
    val sellerName: String,
    val status: AuctionStatus
) 