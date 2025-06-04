package com.kutalev.auction.model

import jakarta.persistence.*
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonManagedReference
import com.fasterxml.jackson.annotation.JsonBackReference

@Entity
@Table(name = "users")
class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id")
    var userId: String = ""
    
    @Column(nullable = false)
    var name: String = ""
    
    @Column(nullable = false, unique = true)
    var email: String = ""
    
    @Column(nullable = false)
    @JsonIgnore
    var password: String = ""
    
    @Column(name = "auth_provider", nullable = false)
    var authProvider: String = "KEYCLOAK"

    @Column(name = "keycloak_id")
    var keycloakId: String? = null

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var role: Role = Role.USER

    @OneToMany(mappedBy = "seller", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JsonManagedReference
    var auctions: MutableList<Auction> = mutableListOf()

    @OneToMany(mappedBy = "bidder", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JsonManagedReference
    var bids: MutableList<Bid> = mutableListOf()

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JsonManagedReference
    var notifications: MutableList<Notification> = mutableListOf()

    override fun toString(): String {
        return "User(userId='$userId', name='$name', email='$email', role=$role, keycloakId=$keycloakId)"
    }
} 