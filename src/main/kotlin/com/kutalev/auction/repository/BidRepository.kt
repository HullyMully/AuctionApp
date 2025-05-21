package com.kutalev.auction.repository

import com.kutalev.auction.model.Bid
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BidRepository : JpaRepository<Bid, String> {
    fun findByAuctionAuctionId(auctionId: String): List<Bid>
    fun findByBidderUserId(userId: String): List<Bid>
    fun findFirstByAuctionAuctionIdOrderByAmountDesc(auctionId: String): Bid?
} 