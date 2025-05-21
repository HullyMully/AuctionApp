package com.kutalev.auction.controller

import com.kutalev.auction.dto.BidDto
import com.kutalev.auction.dto.CreateBidRequest
import com.kutalev.auction.service.BidService
import com.kutalev.auction.service.UserSyncService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/bids")
class BidController(
    private val bidService: BidService,
    private val userSyncService: UserSyncService
) {
    @GetMapping
    fun getAllBids(): ResponseEntity<List<BidDto>> = 
        ResponseEntity.ok(bidService.getAllBids())

    @GetMapping("/{bidId}")
    fun getBidById(@PathVariable bidId: String): ResponseEntity<BidDto> =
        ResponseEntity.ok(bidService.getBidById(bidId))

    @PostMapping("/auctions/{auctionId}")
    fun placeBid(
        @AuthenticationPrincipal jwt: Jwt,
        @PathVariable auctionId: String,
        @RequestBody request: CreateBidRequest
    ): ResponseEntity<BidDto> {
        val user = userSyncService.getOrCreateUserFromJwt(jwt)
        val bid = bidService.createBid(user.userId, auctionId, request.amount)
        return ResponseEntity.ok(bid)
    }

    @GetMapping("/auctions/{auctionId}")
    fun getAuctionBids(
        @PathVariable auctionId: String
    ): ResponseEntity<List<BidDto>> = 
        ResponseEntity.ok(bidService.getAuctionBids(auctionId))

    @GetMapping("/users/{userId}")
    fun getUserBids(
        @PathVariable userId: String
    ): ResponseEntity<List<BidDto>> = 
        ResponseEntity.ok(bidService.getUserBids(userId))

    @DeleteMapping("/{bidId}")
    fun deleteBid(
        @AuthenticationPrincipal jwt: Jwt,
        @PathVariable bidId: String
    ): ResponseEntity<Unit> {
        val user = userSyncService.getOrCreateUserFromJwt(jwt)
        bidService.deleteBid(user.userId, bidId)
        return ResponseEntity.ok().build()
    }
} 