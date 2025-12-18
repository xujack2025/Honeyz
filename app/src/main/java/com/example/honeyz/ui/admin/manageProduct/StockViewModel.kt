package com.example.honeyz.ui.admin.manageProduct

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.honeyz.model.Product
import com.example.honeyz.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

// Function to retrieve a list of products from Firestore
suspend fun getProductFromFireStore(): List<Product> {
    val db = FirebaseFirestore.getInstance()
    val productList = mutableListOf<Product>()

    try {
        Log.d("Firestore", "Fetching products from Firestore...")

        // Fetch all products from the "Products" collection
        val result = db.collection("Products").get().await()

        Log.d("Firestore", "Successfully fetched ${result.size()} products from Firestore.")

        for (document in result) {
            // Convert Firestore document to Product object and add to the list
            val product = document.toObject(Product::class.java)
            product.id = document.id // Assign the document ID to the product
            productList.add(product)

            // Log each product fetched
            Log.d("Firestore", "Fetched product: ${product.name} (ID: ${product.id}, Stock: ${product.stock}, Price: ${product.price})")
        }
    } catch (e: FirebaseFirestoreException) {
        Log.e("FirestoreError", "Error fetching products: ${e.message}")
    } catch (e: Exception) {
        // Catch any other non-Firestore-related exceptions
        Log.e("Error", "Unexpected error: ${e.message}")
    }

    return productList
}


class StockViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    // State to hold products
    private val _products = MutableStateFlow<List<Product>>(emptyList())

    var products = mutableStateListOf<Product>()
        private set

    init {
        // Fetch products from Firestore and add to the product list
        viewModelScope.launch {
            Log.d("StockViewModel", "Fetching products from Firestore...")
            val retrievedProducts = getProductFromFireStore()
            products.addAll(retrievedProducts)
            Log.d("StockViewModel", "Products fetched: ${retrievedProducts.size}")
        }
    }

    private suspend fun fetchProducts() {
        try {
            val result = db.collection("Products").get().await()
            val productList = result.map { document ->
                document.toObject(Product::class.java).copy(id = document.id)
            }
            _products.value = productList
        } catch (e: FirebaseFirestoreException) {
            Log.e("Firestore", "Error fetching products: ${e.message}")
        }
    }

    // Get total products
    fun getTotalProducts(): Int {
        return products.size
    }

    // Get low stock products (stock less than 10)
    fun getLowStockProducts(): Int {
        return products.count { it.stock < 10 }
    }

    // Add a new product (locally and in Firestore)
    fun addProduct(product: Product) {
        viewModelScope.launch {
            try {
                // Add product to Firestore
                val documentRef = db.collection("Products").add(product).await()
                product.id = documentRef.id // Save the Firestore document ID
                products.add(product) // Add the product locally after success
            } catch (e: FirebaseFirestoreException) {
                Log.e("Firestore", "Error adding product: ${e.message}")
            }
        }
    }

    // Increase stock of a product (locally and in Firestore)
    fun increaseStock(product: Product) {
        val index = products.indexOf(product)
        if (index >= 0) {
            val updatedProduct = product.copy(stock = product.stock + 1)
            products[index] = updatedProduct

            // Update Firestore
            viewModelScope.launch {
                updateProductInFirestore(updatedProduct)
            }
        }
    }

    // Decrease stock of a product (locally and in Firestore)
    fun decreaseStock(product: Product) {
        val index = products.indexOf(product)
        if (index >= 0 && products[index].stock > 0) {
            val updatedProduct = product.copy(stock = product.stock - 1)
            products[index] = updatedProduct

            // Update Firestore
            viewModelScope.launch {
                updateProductInFirestore(updatedProduct)
            }
        }
    }

    // Enable or disable a product (locally and in Firestore)
    fun toggleProductEnabled(product: Product) {
        val index = products.indexOf(product)
        if (index >= 0) {
            val updatedProduct = product.copy(isDisabled = !product.isDisabled)
            products[index] = updatedProduct

            // Update Firestore
            viewModelScope.launch {
                updateProductInFirestore(updatedProduct)
            }
        }
    }

    // Helper function to update the product in Firestore
    private suspend fun updateProductInFirestore(product: Product) {
        try {
            db.collection("Products").document(product.id).set(product).await()
        } catch (e: FirebaseFirestoreException) {
            Log.e("Firestore", "Error updating product: ${e.message}")
        }
    }
}
