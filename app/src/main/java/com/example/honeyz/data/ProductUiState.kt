package com.example.honeyz.data

import com.example.honeyz.model.CartItem
import com.example.honeyz.model.Product

data class ProductUiState(
    val quantity: Int = 1,
    val price: String = "",
    val product: Product? = null,
    val errorMessage: String? = null,
    val errorCount: Int = 0,
    val cartItemCount: Int = 0
)
