package com.example.honeyz.ui.customer

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

data class CartItem(
    val cartItemId: String = "",
    val productId: String = "",
    val quantity: Int = 0
)

class CartViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    var cartItems: MutableList<CartItem> = mutableListOf()

    fun fetchCartItemsForUser(onResult: (Boolean) -> Unit) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            db.collection("users").document(userId).collection("cart")
                .get()
                .addOnSuccessListener { result ->
                    val items = result.documents.map { document ->
                        document.toObject(CartItem::class.java)!!
                    }
                    cartItems = items.toMutableList()
                    onResult(true)
                }
                .addOnFailureListener {
                    onResult(false)
                }
        } else {
            onResult(false)
        }
    }


    // Function to add a product to the cart
    fun addToCart(productId: String, quantity: Int) {
        Log.d("CartViewModel", "addToCart called with productId: $productId and quantity: $quantity")
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val cartDocument = db.collection("users").document(userId).collection("cart").document()

        val cartItem = mapOf(
            "cartItemId" to cartDocument.id,
            "productId" to productId,
            "quantity" to quantity
        )

        cartDocument.set(cartItem)
            .addOnSuccessListener {
                // Add the cart item locally if Firestore is successful
                Log.d("CartViewModel", "Item added to cart successfully")
                cartItems.add(CartItem(cartItemId = cartDocument.id, productId = productId, quantity = quantity))
            }
            .addOnFailureListener { e ->
                Log.e("Cart", "Error adding item to cart: ${e.message}")
            }
    }
}
