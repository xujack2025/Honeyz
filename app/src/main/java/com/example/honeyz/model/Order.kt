package com.example.honeyz.model

data class Order(
    var orderId: String = "",            // Firestore or system-generated ID
    val customerName: String = "",       // Name of the customer
    val productName: String = "",        // Name of the product
    val quantity: Int = 1,               // Default quantity is 1
    var status: OrderStatus = OrderStatus.PENDING,  // Default status is PENDING
    val paymentMethod: String = "",      // Payment method (e.g., Credit Card, PayPal, etc.)
    val totalPrice: Double = 0.0         // Total price for the order
)

enum class OrderStatus {
    PENDING, SHIPPED, DELIVERED, CANCELLED
}
