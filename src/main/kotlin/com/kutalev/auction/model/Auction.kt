package com.kutalev.auction.model

import jakarta.persistence.*
import java.time.LocalDateTime
import com.fasterxml.jackson.annotation.JsonManagedReference
import com.fasterxml.jackson.annotation.JsonBackReference

@Entity
@Table(name = "auctions")
class Auction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "auction_id")
    var auctionId: String = ""

    @Column(nullable = false)
    var title: String = ""

    @Column(nullable = false)
    var description: String = ""

    @Column(name = "start_price", nullable = false)
    var startPrice: Double = 0.0

    @Column(name = "current_price", nullable = false)
    var currentPrice: Double = 0.0

    @Column(name = "start_time", nullable = false)
    var startTime: LocalDateTime = LocalDateTime.now()

    @Column(name = "end_time", nullable = false)
    var endTime: LocalDateTime = LocalDateTime.now().plusDays(7)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    var seller: User? = null

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: AuctionStatus = AuctionStatus.PENDING

    @OneToMany(mappedBy = "auction", cascade = [CascadeType.ALL], orphanRemoval = true)
    @JsonManagedReference
    var bids: MutableList<Bid> = mutableListOf()

    override fun toString(): String {
        return "Auction(auctionId='$auctionId', title='$title', currentPrice=$currentPrice, status=$status)"
    }
} 