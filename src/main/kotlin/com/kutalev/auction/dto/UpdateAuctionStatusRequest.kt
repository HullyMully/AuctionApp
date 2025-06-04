package com.kutalev.auction.dto

import com.kutalev.auction.model.AuctionStatus

data class UpdateAuctionStatusRequest(
    val status: AuctionStatus
) 