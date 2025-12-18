package com.example.honeyz.data

import com.example.honeyz.model.CartItem

data class CartUiState(
    val cartItemList: List<CartItem> = emptyList(),
    val userUID: String = "",
    val errorMessage: String? = null,
    val errorCount: Int = 0
)
