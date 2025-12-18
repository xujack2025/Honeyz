package com.example.honeyz.model

data class CartItem(
    val cartId: String = "",
    val productId: String = "",
    val productName: String = "",
    val price: String = "",
    var quantity: Int = 0,
    val stock: Int = 1,
    var subtotal: Double = 0.0
)
