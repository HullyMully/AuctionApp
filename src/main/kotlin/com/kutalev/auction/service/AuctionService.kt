package com.kutalev.auction.service

import com.kutalev.auction.dto.AuctionDto
import com.kutalev.auction.dto.CreateAuctionRequest
import com.kutalev.auction.dto.UpdateAuctionRequest
import com.kutalev.auction.model.Auction
import com.kutalev.auction.model.AuctionStatus
import com.kutalev.auction.repository.AuctionRepository
import com.kutalev.auction.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class AuctionService(
    private val auctionRepository: AuctionRepository,
    private val userRepository: UserRepository
) {
    private val logger = LoggerFactory.getLogger(AuctionService::class.java)

    fun getAllAuctions(): List<AuctionDto> =
        auctionRepository.findAll().map { it.toDto() }

    fun getActiveAuctions(): List<AuctionDto> =
        auctionRepository.findByStatus(AuctionStatus.ACTIVE).map { it.toDto() }

    fun getAuctionById(auctionId: String): AuctionDto =
        auctionRepository.findById(auctionId)
            .orElseThrow { RuntimeException("Auction not found") }
            .toDto()

    @Transactional
    fun createAuction(userId: String, request: CreateAuctionRequest): AuctionDto {
        logger.info("Creating auction for user: $userId")
        logger.debug("Auction request: $request")

        try {
            val seller = userRepository.findById(userId)
                .orElseThrow { RuntimeException("User not found") }
            logger.debug("Found seller: ${seller.name}")

            val now = LocalDateTime.now()
            if (request.startTime.isBefore(now)) {
                logger.error("Start time is in the past: ${request.startTime}")
                throw RuntimeException("Start time cannot be in the past")
            }

            if (request.endTime.isBefore(request.startTime)) {
                logger.error("End time is before start time: ${request.endTime} < ${request.startTime}")
                throw RuntimeException("End time must be after start time")
            }

            val auction = Auction()
            auction.title = request.title
            auction.description = request.description
            auction.startPrice = request.startPrice
            auction.currentPrice = request.startPrice
            auction.startTime = request.startTime
            auction.endTime = request.endTime
            auction.seller = seller
            auction.status = AuctionStatus.PENDING
            
            logger.debug("Created auction object: $auction")

            val savedAuction = auctionRepository.save(auction)
            logger.info("Successfully saved auction with id: ${savedAuction.auctionId}")
            
            return savedAuction.toDto()
        } catch (e: Exception) {
            logger.error("Error creating auction", e)
            throw e
        }
    }

    @Transactional
    fun updateAuction(userId: String, auctionId: String, request: UpdateAuctionRequest): AuctionDto {
        logger.info("Updating auction: $auctionId for user: $userId")
        logger.debug("Update request: $request")

        val auction = auctionRepository.findById(auctionId)
            .orElseThrow { RuntimeException("Auction not found") }

        if (auction.seller?.userId != userId) {
            logger.error("User $userId is not the seller of auction $auctionId")
            throw RuntimeException("Only the seller can update the auction")
        }

        if (auction.status != AuctionStatus.PENDING) {
            logger.error("Cannot update non-pending auction: ${auction.status}")
            throw RuntimeException("Can only update pending auctions")
        }

        if (request.startTime != null && request.startTime.isBefore(LocalDateTime.now())) {
            logger.error("Start time cannot be in the past: ${request.startTime}")
            throw RuntimeException("Start time cannot be in the past")
        }

        if (request.endTime != null && request.startTime != null && request.endTime.isBefore(request.startTime)) {
            logger.error("End time must be after start time: ${request.endTime} < ${request.startTime}")
            throw RuntimeException("End time must be after start time")
        }

        request.title?.let { auction.title = it }
        request.description?.let { auction.description = it }
        request.startPrice?.let {
            if (auction.bids.isNotEmpty()) {
                logger.error("Cannot update start price after bids have been placed")
                throw RuntimeException("Cannot update start price after bids have been placed")
            }
            auction.startPrice = it
            auction.currentPrice = it
        }
        request.startTime?.let { auction.startTime = it }
        request.endTime?.let { auction.endTime = it }

        val savedAuction = auctionRepository.save(auction)
        logger.info("Successfully updated auction: ${savedAuction.auctionId}")
        return savedAuction.toDto()
    }

    @Transactional
    fun deleteAuction(userId: String, auctionId: String) {
        logger.info("Deleting auction: $auctionId for user: $userId")

        val auction = auctionRepository.findById(auctionId)
            .orElseThrow { RuntimeException("Auction not found") }

        if (auction.seller?.userId != userId) {
            logger.error("User $userId is not the seller of auction $auctionId")
            throw RuntimeException("Only the seller can delete the auction")
        }

        if (auction.bids.isNotEmpty()) {
            logger.error("Cannot delete auction with bids")
            throw RuntimeException("Cannot delete auction with bids")
        }

        auctionRepository.delete(auction)
        logger.info("Successfully deleted auction: $auctionId")
    }

    @Transactional
    fun deleteAuctionByAdmin(auctionId: String) {
        val auction = auctionRepository.findById(auctionId)
            .orElseThrow { RuntimeException("Auction not found") }

        auctionRepository.delete(auction)
        logger.info("Successfully deleted auction by admin: $auctionId")
    }

    @Transactional
    fun updateAuctionStatusByAdmin(auctionId: String, status: AuctionStatus): AuctionDto {
        logger.info("Updating auction status by admin for auction: $auctionId to $status")

        val auction = auctionRepository.findById(auctionId)
            .orElseThrow { RuntimeException("Auction not found") }

        auction.status = status

        val savedAuction = auctionRepository.save(auction)
        logger.info("Successfully updated auction status by admin: ${savedAuction.auctionId} to ${savedAuction.status}")
        return savedAuction.toDto()
    }

    private fun Auction.toDto(): AuctionDto {
        return AuctionDto(
            auctionId = auctionId,
            title = title,
            description = description,
            startPrice = startPrice,
            currentPrice = currentPrice,
            startTime = startTime,
            endTime = endTime,
            sellerId = seller?.userId ?: "",
            sellerName = seller?.name ?: "",
            status = status
        )
    }
} 