package com.kutalev.auction.controller

import com.kutalev.auction.dto.*
import com.kutalev.auction.service.AuctionService
import com.kutalev.auction.service.BidService
import com.kutalev.auction.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*
import org.springframework.security.oauth2.jwt.Jwt
import com.kutalev.auction.service.UserSyncService
import com.kutalev.auction.model.AuctionStatus

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
class AdminController(
    private val userService: UserService,
    private val auctionService: AuctionService,
    private val bidService: BidService,
    private val userSyncService: UserSyncService
) {
    // Управление пользователями
    @GetMapping("/users")
    fun getAllUsers(): ResponseEntity<List<UserDto>> =
        ResponseEntity.ok(userService.getAllUsers())

    @DeleteMapping("/users/{userId}")
    fun deleteUserByAdmin(
        @AuthenticationPrincipal jwt: Jwt,
        @PathVariable userId: String
    ): ResponseEntity<Unit> {
        val currentUser = userSyncService.getOrCreateUserFromJwt(jwt)
        userService.deleteUser(currentUser.userId, userId)
        return ResponseEntity.ok().build()
    }

    // Управление аукционами
    @GetMapping("/auctions")
    fun getAllAuctions(): ResponseEntity<List<AuctionDto>> =
        ResponseEntity.ok(auctionService.getAllAuctions())

    @DeleteMapping("/auctions/{auctionId}")
    fun deleteAuctionByAdmin(
        @AuthenticationPrincipal jwt: Jwt,
        @PathVariable auctionId: String
    ): ResponseEntity<Unit> {
        val currentUser = userSyncService.getOrCreateUserFromJwt(jwt)
        auctionService.deleteAuctionByAdmin(auctionId)
        return ResponseEntity.ok().build()
    }

    @PutMapping("/auctions/{auctionId}/status")
    fun updateAuctionStatusByAdmin(
        @AuthenticationPrincipal jwt: Jwt,
        @PathVariable auctionId: String,
        @RequestBody request: UpdateAuctionStatusRequest
    ): ResponseEntity<AuctionDto> {
        val currentUser = userSyncService.getOrCreateUserFromJwt(jwt)
        val updatedAuction = auctionService.updateAuctionStatusByAdmin(auctionId, request.status)
        return ResponseEntity.ok(updatedAuction)
    }

    // Управление ставками
    @GetMapping("/bids")
    fun getAllBids(): ResponseEntity<List<BidDto>> =
        ResponseEntity.ok(bidService.getAllBids())

    @DeleteMapping("/bids/{bidId}")
    fun deleteBidByAdmin(
        @AuthenticationPrincipal jwt: Jwt,
        @PathVariable bidId: String
    ): ResponseEntity<Unit> {
        val currentUser = userSyncService.getOrCreateUserFromJwt(jwt)
        bidService.deleteBidByAdmin(bidId)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/sync-users")
    @PreAuthorize("hasRole('ADMIN')")
    fun syncUsers(): ResponseEntity<String> {
        userSyncService.syncUsersFromKeycloak()
        return ResponseEntity.ok("User synchronization initiated.")
    }
} 