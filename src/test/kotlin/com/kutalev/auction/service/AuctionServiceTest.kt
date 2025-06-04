package com.kutalev.auction.service

import com.kutalev.auction.model.Auction
import com.kutalev.auction.model.User
import com.kutalev.auction.model.AuctionStatus
import com.kutalev.auction.repository.AuctionRepository
import com.kutalev.auction.repository.UserRepository
import com.kutalev.auction.dto.CreateAuctionRequest
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import java.time.LocalDateTime
import java.util.*

class AuctionServiceTest {

    private val auctionRepository = mockk<AuctionRepository>()
    private val userRepository = mockk<UserRepository>()
    private val auctionService = AuctionService(auctionRepository, userRepository)

    @Test
    fun `should create new auction`() {
        // given
        val userId = "test-user-id"
        val user = User().apply {
            this.userId = userId
            this.name = "Test User"
            this.email = "test@example.com"
        }
        val request = CreateAuctionRequest(
            title = "Test Auction",
            description = "Test Description",
            startPrice = 100.0,
            startTime = LocalDateTime.now().plusHours(1),
            endTime = LocalDateTime.now().plusDays(1)
        )
        val auction = Auction().apply {
            this.title = request.title
            this.description = request.description
            this.startPrice = request.startPrice
            this.currentPrice = request.startPrice
            this.startTime = request.startTime
            this.endTime = request.endTime
            this.seller = user
            this.status = AuctionStatus.PENDING
        }
        every { userRepository.findById(userId) } returns Optional.of(user)
        every { auctionRepository.save(any()) } returns auction

        // when
        val result = auctionService.createAuction(userId, request)

        // then
        assertNotNull(result)
        assertEquals(request.title, result.title)
        assertEquals(request.description, result.description)
        assertEquals(request.startPrice, result.startPrice)
        verify { userRepository.findById(userId) }
        verify { auctionRepository.save(any()) }
    }
} 