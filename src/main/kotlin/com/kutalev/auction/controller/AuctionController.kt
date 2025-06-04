package com.kutalev.auction.controller

import com.kutalev.auction.dto.AuctionDto
import com.kutalev.auction.dto.CreateAuctionRequest
import com.kutalev.auction.dto.UpdateAuctionRequest
import com.kutalev.auction.service.AuctionService
import com.kutalev.auction.service.UserSyncService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auctions")
class AuctionController(
    private val auctionService: AuctionService,
    private val userSyncService: UserSyncService
) {
    @GetMapping
    fun getAllAuctions(): ResponseEntity<List<AuctionDto>> = 
        ResponseEntity.ok(auctionService.getAllAuctions())

    @GetMapping("/active")
    fun getActiveAuctions(): ResponseEntity<List<AuctionDto>> = 
        ResponseEntity.ok(auctionService.getActiveAuctions())

    @GetMapping("/{auctionId}")
    fun getAuctionById(@PathVariable auctionId: String): ResponseEntity<AuctionDto> =
        ResponseEntity.ok(auctionService.getAuctionById(auctionId))

    @PostMapping
    fun createAuction(
        @AuthenticationPrincipal jwt: Jwt,
        @RequestBody request: CreateAuctionRequest
    ): ResponseEntity<AuctionDto> {
        val user = userSyncService.getOrCreateUserFromJwt(jwt)
        val auction = auctionService.createAuction(user.userId, request)
        return ResponseEntity.ok(auction)
    }

    @PutMapping("/{auctionId}")
    fun updateAuction(
        @AuthenticationPrincipal jwt: Jwt,
        @PathVariable auctionId: String,
        @RequestBody request: UpdateAuctionRequest
    ): ResponseEntity<AuctionDto> {
        val user = userSyncService.getOrCreateUserFromJwt(jwt)
        val auction = auctionService.updateAuction(user.userId, auctionId, request)
        return ResponseEntity.ok(auction)
    }

    @DeleteMapping("/{auctionId}")
    fun deleteAuction(
        @AuthenticationPrincipal jwt: Jwt,
        @PathVariable auctionId: String
    ): ResponseEntity<Unit> {
        val user = userSyncService.getOrCreateUserFromJwt(jwt)
        auctionService.deleteAuction(user.userId, auctionId)
        return ResponseEntity.ok().build()
    }
} 