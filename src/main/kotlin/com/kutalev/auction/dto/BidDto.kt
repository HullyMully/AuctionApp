package com.kutalev.auction.dto

import java.time.LocalDateTime

data class BidDto(
    val bidId: String,
    val bidderId: String,
    val bidderName: String,
    val auctionId: String,
    val auctionTitle: String,
    val amount: Double,
    val bidTime: LocalDateTime
) 