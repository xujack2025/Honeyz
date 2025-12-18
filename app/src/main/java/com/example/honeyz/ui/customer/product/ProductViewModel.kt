package com.example.honeyz.ui.User.product

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.honeyz.data.ProductUiState
import com.example.honeyz.model.CartItem
import com.example.honeyz.model.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ProductViewModel: ViewModel() {
    // MutableStateFlow for quantity
    private val _productUiState = MutableStateFlow(ProductUiState())
    val productUiState: StateFlow<ProductUiState> = _productUiState.asStateFlow()

    // MutableStateFlow for Cart
    private val _cartItemCount = MutableStateFlow(0)
    val cartItemCount: StateFlow<Int> = _cartItemCount.asStateFlow()

    fun isUserLoggedIn(): Boolean {
        return FirebaseAuth.getInstance().currentUser != null
    }

    fun addToCart(product: Product, quantity: Int) {
        val user = FirebaseAuth.getInstance().currentUser

        if (user != null) {
            // Convert the product price from String to Double, and handle potential null/invalid value
            val priceAsDouble = product.price.toDoubleOrNull() ?: 0.0

            // Perform the subtotal calculation using the converted double price
            val subtotal = priceAsDouble * quantity

            // Convert the price back to a string if necessary for the cartItem (optional)
            val priceAsString = priceAsDouble.toString()

            // Create the CartItem object with the calculated subtotal
            val cartItem = CartItem(
                productId = product.id,
                productName = product.name,
                price = priceAsString, // Store price as string if required in Firestore
                quantity = quantity,
                subtotal = subtotal // subtotal is calculated as a Double
            )

            // Add the cartItem to Firestore
            Firebase.firestore.collection("users")
                .document(user.uid)
                .collection("Cart")
                .add(cartItem)
                .addOnSuccessListener { documentReference ->
                    Log.d("Firestore", "Cart item added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "Error adding cart item", e)
                }
        } else {
            _productUiState.update { currentState ->
                currentState.copy(errorMessage = "Please log in to add items to the cart.")
            }
        }
    }


    // Function to set the selected product
    fun selectProduct(product: Product) {
        _productUiState.update { currentState ->
            currentState.copy(product = product)
        }
    }

    // Function to increase quantity
    fun increaseQuantity() {
        _productUiState.update { currentState ->
            val availableStock = currentState.product?.stock ?: 0

            // Check if the order exceed available stock
            if (currentState.quantity < availableStock) {
                currentState.copy(
                    quantity = currentState.quantity + 1,
                    errorMessage = null,
                    errorCount = 0
                )
            } else if(currentState.errorCount < 3) {
                currentState.copy(
                    errorMessage = "You already ordered all the available stock Boss!!",
                    errorCount = currentState.errorCount + 1
                )
            } else {
               currentState.copy(
                   errorMessage = "We have nothing left...."
               )
            }
        }
    }

    // Function to decrease quantity
    fun decreaseQuantity() {
        _productUiState.update { currentState ->
            if (currentState.quantity > 1) {
               currentState.copy(
                   quantity = currentState.quantity - 1,
                   errorMessage = null
               )
            }
            else if (currentState.errorCount < 3){
                currentState.copy(
                    errorMessage = "You have reach the bottom of the void....",
                    errorCount = currentState.errorCount + 1
                )
            } else{
                currentState.copy(
                    errorMessage = "Just leave me alone."
                )
            }
        }
    }
}