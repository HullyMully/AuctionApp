package com.kutalev.auction.service

import com.kutalev.auction.dto.BidDto
import com.kutalev.auction.dto.CreateBidRequest
import com.kutalev.auction.model.AuctionStatus
import com.kutalev.auction.model.Bid
import com.kutalev.auction.repository.AuctionRepository
import com.kutalev.auction.repository.BidRepository
import com.kutalev.auction.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class BidService(
    private val bidRepository: BidRepository,
    private val auctionRepository: AuctionRepository,
    private val userRepository: UserRepository
) {
    fun getAllBids(): List<BidDto> = 
        bidRepository.findAll().map { it.toDto() }

    fun getBidById(bidId: String): BidDto =
        bidRepository.findById(bidId)
            .orElseThrow { RuntimeException("Bid not found") }
            .toDto()

    @Transactional
    fun createBid(userId: String, auctionId: String, amount: Double): BidDto {
        val bidder = userRepository.findById(userId)
            .orElseThrow { RuntimeException("User not found") }

        val auction = auctionRepository.findById(auctionId)
            .orElseThrow { RuntimeException("Auction not found") }

        if (auction.status != AuctionStatus.ACTIVE) {
            throw RuntimeException("Can only bid on active auctions")
        }

        if (auction.endTime.isBefore(LocalDateTime.now())) {
            auction.status = AuctionStatus.ENDED
            auctionRepository.save(auction)
            throw RuntimeException("Auction has ended")
        }

        if (auction.seller?.userId == userId) {
            throw RuntimeException("Cannot bid on your own auction")
        }

        if (amount <= auction.currentPrice) {
            throw RuntimeException("Bid amount must be higher than current price")
        }

        val bid = Bid().apply {
            this.bidder = bidder
            this.auction = auction
            this.amount = amount
            this.bidTime = LocalDateTime.now()
        }

        auction.currentPrice = amount
        auctionRepository.save(auction)

        return bidRepository.save(bid).toDto()
    }

    fun getAuctionBids(auctionId: String): List<BidDto> =
        bidRepository.findByAuctionAuctionId(auctionId).map { it.toDto() }

    fun getUserBids(userId: String): List<BidDto> =
        bidRepository.findByBidderUserId(userId).map { it.toDto() }

    @Transactional
    fun deleteBid(userId: String, bidId: String) {
        val bid = bidRepository.findById(bidId)
            .orElseThrow { RuntimeException("Bid not found") }

        if (bid.bidder?.userId != userId) {
            throw RuntimeException("You can only delete your own bids")
        }

        if (bid.auction?.status != AuctionStatus.PENDING) {
            throw RuntimeException("Can only delete bids on pending auctions")
        }

        bidRepository.delete(bid)
    }

    @Transactional
    fun deleteBidByAdmin(bidId: String) {
        val bid = bidRepository.findById(bidId)
            .orElseThrow { RuntimeException("Bid not found") }

        bidRepository.delete(bid)
    }

    private fun Bid.toDto() = BidDto(
        bidId = bidId,
        bidderId = bidder?.userId ?: "",
        bidderName = bidder?.name ?: "",
        auctionId = auction?.auctionId ?: "",
        auctionTitle = auction?.title ?: "",
        amount = amount,
        bidTime = bidTime
    )
} 