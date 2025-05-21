package com.kutalev.auction.model

enum class AuctionStatus {
    PENDING,    // Аукцион еще не начался
    ACTIVE,     // Аукцион активен
    ENDED,      // Аукцион завершен
    CANCELLED   // Аукцион отменен
} 