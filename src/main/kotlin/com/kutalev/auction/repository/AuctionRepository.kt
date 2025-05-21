package com.kutalev.auction.repository

import com.kutalev.auction.model.Auction
import com.kutalev.auction.model.AuctionStatus
import com.kutalev.auction.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AuctionRepository : JpaRepository<Auction, String> {
    fun findBySeller(seller: User): List<Auction>
    fun findByStatus(status: AuctionStatus): List<Auction>
    fun findBySellerAndStatus(seller: User, status: AuctionStatus): List<Auction>
} 