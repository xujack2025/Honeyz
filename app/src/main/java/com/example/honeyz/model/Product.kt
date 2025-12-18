package com.example.honeyz.model

data class Product(
    var id: String = "",               // Firestore ID for the product
    val photoUrl: String? = null,       // Firestore image URL (if using Firestore to store images)
    val name: String = "",
    val description: String = "",
    val price: String = "",
    var stock: Int = 1,
    var isDisabled: Boolean = false     // Whether the product is disabled or not
)
