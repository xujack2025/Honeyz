package com.example.honeyz.ui.User.cart

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.honeyz.data.CartUiState
import com.example.honeyz.model.CartItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class CartViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val _cartUiState = MutableStateFlow(CartUiState())
    val cartUiState: StateFlow<CartUiState> = _cartUiState.asStateFlow()


    fun getCurrentUserUID(): String? {
         return auth.currentUser?.uid
    }

    fun isUserLoggedIn(): Boolean {
        return FirebaseAuth.getInstance().currentUser != null
    }

    fun updateCartInFirestore(cartItemList: List<CartItem>) {
        val userUID = getCurrentUserUID()
        if(userUID != null){
            viewModelScope.launch {
                try {
                    // Create a map of cart items to update
                    val cartData = hashMapOf<String, Any>()
                    cartItemList.forEach { item ->
                        cartData[item.productId] = item // Assuming `item.id` uniquely identifies each cart item
                    }

                    // Update the Firestore document (assumes a collection named "carts" and a document with userUID)
                    db.collection("carts")
                        .document(userUID)
                        .set(cartData)
                        .await() // Wait for the operation to complete
                } catch (e: Exception) {
                    // Handle error (e.g., logging, showing a message to the user)
                    Log.e("CartViewModel", "Error updating cart: ${e.message}")
                }
            }
        }
    }

    // Function to fetch cart items from Firestore
    fun fetchCartItems() {
        val firestore = FirebaseFirestore.getInstance()
        val userUID = getCurrentUserUID()
        if(userUID != null){
            firestore.collection("carts").document(userUID).collection("items")
                .get()
                .addOnSuccessListener { documents ->
                    val items = documents.mapNotNull { document ->
                        document.toObject(CartItem::class.java)
                    }
                    _cartUiState.update { currentState ->
                        currentState.copy( cartItemList = items)
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("Fetch Item", "Error fetching cart items.", exception)
                    // Handle the error
                }
        }
    }

    // Function to update quantity of an item
//    fun updateItemQuantity(item: CartItem, newQuantity: Int) {
//        _cartUiState.update { currentState ->
//            // Update the quantity of the specific item in the cartItemList
//            val updatedCartItems = currentState.cartItemList.map { cartItem ->
//                if (cartItem == item) {
//                    cartItem.copy(quantity = newQuantity) // Update quantity
//                } else {
//                    cartItem
//                }
//            }
//            currentState.copy(cartItemList = updatedCartItems)
//        }
//    }

    // Function to increase quantity
    fun increaseQuantity(item: CartItem) {
        _cartUiState.update { currentState ->
            val availableStock = item.stock
            var newErrorMessage: String? = null
            var newErrorCount = currentState.errorCount

            val updatedCartItems = currentState.cartItemList.map { cartItem ->
                if (cartItem == item) {
                    // Check if the order exceeds available stock
                    return@map if (cartItem.quantity < availableStock) {
                        cartItem.copy(quantity = cartItem.quantity + 1) // Increment quantity
                    } else {
                        newErrorMessage = "You already ordered all the available stock Boss!!"
                        newErrorCount += 1
                        cartItem // Return the unchanged item
                    }
                } else {
                    cartItem // Return the unchanged item
                }
            }

            // Check for max error count
            if (newErrorCount >= 3) {
                newErrorMessage = "We have nothing left...."
            }

            currentState.copy(
                cartItemList = updatedCartItems,
                errorMessage = newErrorMessage,
                errorCount = newErrorCount
            )
        }
    }

    // Function to decrease quantity
    fun decreaseQuantity(item: CartItem) {
        _cartUiState.update { currentState ->
            var newErrorMessage: String? = null
            var newErrorCount = currentState.errorCount

            val updatedCartItems = currentState.cartItemList.map { cartItem ->
                if (cartItem == item) {
                    // Check if the order exceeds available stock
                    return@map if (cartItem.quantity > 1) {
                        cartItem.copy(quantity = cartItem.quantity - 1) // Increment quantity
                    } else {
                        newErrorMessage = "You had reached the bottom void."
                        newErrorCount += 1
                        cartItem // Return the unchanged item
                    }
                } else {
                    cartItem // Return the unchanged item
                }
            }

            // Check for max error count
            if (newErrorCount >= 3) {
                newErrorMessage = "Just leave me alone"
            }

            currentState.copy(
                cartItemList = updatedCartItems,
                errorMessage = newErrorMessage,
                errorCount = newErrorCount
            )
        }
    }

//    // Function to clear error message
//    fun clearErrorMessage() {
//        _cartUiState.update { currentState ->
//            currentState.copy(errorMessage = null)
//        }
//    }

    // Function to remove an item
    fun removeItem(item: CartItem) {
        _cartUiState.update { currentState ->
            val updatedCartItems = currentState.cartItemList.filter { it != item }
            currentState.copy(cartItemList = updatedCartItems)
        }
    }
}
