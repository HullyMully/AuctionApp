package com.kutalev.auction.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "bids")
class Bid {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "bid_id")
    var bidId: String = ""

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bidder_id", nullable = false)
    var bidder: User? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auction_id", nullable = false)
    var auction: Auction? = null

    @Column(nullable = false)
    var amount: Double = 0.0

    @Column(name = "bid_time", nullable = false)
    var bidTime: LocalDateTime = LocalDateTime.now()
} 