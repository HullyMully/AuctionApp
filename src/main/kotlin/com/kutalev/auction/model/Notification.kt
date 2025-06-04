package com.kutalev.auction.model

import jakarta.persistence.*
import java.time.LocalDateTime
import com.fasterxml.jackson.annotation.JsonBackReference

@Entity
@Table(name = "notifications")
class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "notification_id")
    var notificationId: String = ""

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    var user: User? = null

    @Column(nullable = false)
    var message: String = ""

    @Column(name = "created_at", nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now()
} 