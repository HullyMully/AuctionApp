package com.example.auctionapp

data class AuctionProduct(
    var productId: String? = null,
    var price: Int? = null,
    var imageUrl: String? = null,
    var name: String? = null,
    var description: String? = null,
    var userId: String? = null
) {
    // Конструктор без аргументов
    constructor() : this(null, null, null, null, null, null)
}