package com.example.honeyz.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.honeyz.model.Order
import com.example.honeyz.model.OrderStatus
import com.example.honeyz.model.Product
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class OrderViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    // Use StateFlow to hold the list of orders
    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders

    init {
        // Load orders from Firestore when the ViewModel is initialized
        viewModelScope.launch {
            fetchOrdersFromFirestore()
        }
    }

    // Fetch all orders from Firestore
    fun fetchOrdersFromFirestore() {
        viewModelScope.launch {
            try {
                val result = db.collection("Orders").get().await()
                val fetchedOrders = mutableListOf<Order>()
                for (document in result) {
                    val order = document.toObject(Order::class.java)
                    fetchedOrders.add(order.copy(orderId = document.id)) // Assign Firestore ID
                }
                _orders.value = fetchedOrders // Update StateFlow with fetched orders
            } catch (e: FirebaseFirestoreException) {
                Log.e("Firestore", "Error fetching orders: ${e.message}")
            }
        }
    }

    // Add a new order to Firestore and update local state
    fun addOrder(order: Order) {
        viewModelScope.launch {
            try {
                val newOrderRef = db.collection("Orders").add(order).await()
                val orderWithId = order.copy(orderId = newOrderRef.id) // Assign the Firestore ID
                _orders.value = _orders.value + orderWithId // Update the local list
                Log.d("Firestore", "Order added with ID: ${newOrderRef.id}")
            } catch (e: Exception) {
                Log.e("Firestore", "Error adding order: ${e.message}")
            }
        }
    }

    // Function to submit an order to Firestore
    fun submitOrder(order: Order) {
        viewModelScope.launch {
            try {
                // Create a new document reference with auto-generated ID
                val newOrderRef = db.collection("Orders").document()
                val newOrder = order.copy(orderId = newOrderRef.id) // Assign Firestore-generated ID

                // Set the document data
                newOrderRef.set(newOrder).await()

                // Update the local state
                _orders.value = _orders.value + newOrder

                Log.d("Firestore", "Order submitted with ID: ${newOrder.orderId}")
            } catch (e: Exception) {
                Log.w("Firestore", "Error submitting order: ${e.message}")
            }
        }
    }


    // Update order status in Firestore and update local state
    fun updateOrderStatus(orderId: String, newStatus: OrderStatus) {
        viewModelScope.launch {
            try {
                db.collection("Orders")
                    .document(orderId)
                    .update("status", newStatus)
                    .await()

                // Update the local order list
                val updatedOrders = _orders.value.map {
                    if (it.orderId == orderId) it.copy(status = newStatus) else it
                }
                _orders.value = updatedOrders

                Log.d("Firestore", "Order status updated: $orderId")
            } catch (e: Exception) {
                Log.e("Firestore", "Error updating order status: ${e.message}")
            }
        }
    }

    // Utility function for dashboard metrics
    fun getTotalOrders(): Int {
        return _orders.value.size
    }

    fun getTotalPendingOrders(): Int {
        return _orders.value.count { it.status == OrderStatus.PENDING }
    }

    fun getTotalShippedOrders(): Int {
        return _orders.value.count { it.status == OrderStatus.SHIPPED }
    }

    fun getTotalDeliveredOrders(): Int {
        return _orders.value.count { it.status == OrderStatus.DELIVERED }
    }

    fun getTotalCanceledOrders(): Int {
        return _orders.value.count { it.status == OrderStatus.CANCELLED }
    }

    fun getLowStockProducts(): Int {
        return _orders.value.count { it.quantity < 10 }  // Example condition
    }

    // Get orders by status
    fun getOrdersByStatus(status: OrderStatus): Int {
        return orders.value.count { it.status == status }
    }
}
