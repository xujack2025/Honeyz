package com.example.honeyz.ui.admin.manageProduct

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.honeyz.model.Product
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(navController: NavHostController, stockViewModel: StockViewModel) {
    var productName by remember { mutableStateOf("") }
    var productDescription by remember { mutableStateOf("") }
    var productPrice by remember { mutableStateOf("0.0") }
    var productStock by remember { mutableStateOf("0") }
    var imageUrl by remember { mutableStateOf("") } // New field for image URL
    var isFormValid by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val db = FirebaseFirestore.getInstance()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Product") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Form for adding a new product
            OutlinedTextField(
                value = productName,
                onValueChange = {
                    productName = it
                    isFormValid = validateForm(productName, productDescription, productPrice, productStock, imageUrl)
                },
                label = { Text("Product Name") },
                modifier = Modifier.fillMaxWidth(),
                isError = productName.isEmpty()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = productDescription,
                onValueChange = {
                    productDescription = it
                    isFormValid = validateForm(productName, productDescription, productPrice, productStock, imageUrl)
                },
                label = { Text("Product Description") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = productPrice,
                onValueChange = {
                    productPrice = it
                    isFormValid = validateForm(productName, productDescription, productPrice, productStock, imageUrl)
                },
                label = { Text("Product Price") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = productStock,
                onValueChange = {
                    productStock = it
                    isFormValid = validateForm(productName, productDescription, productPrice, productStock, imageUrl)
                },
                label = { Text("Product Stock") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // New field for image URL
            OutlinedTextField(
                value = imageUrl,
                onValueChange = {
                    imageUrl = it
                    isFormValid = validateForm(productName, productDescription, productPrice, productStock, imageUrl)
                },
                label = { Text("Image URL") },
                modifier = Modifier.fillMaxWidth(),
                isError = imageUrl.isEmpty()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    scope.launch {
                        val stock = productStock.toIntOrNull() ?: 0
//                        val price = productPrice.toDoubleOrNull() ?: 0.0

                        val newProduct = Product(
                            name = productName,
                            description = productDescription,
                            price = productPrice,
                            stock = stock,
                            photoUrl = imageUrl // Use the provided image URL
                        )

                        stockViewModel.addProduct(newProduct)
                        addProductToFirestore(db, newProduct)

                        // Reset form fields after submission
                        productName = ""
                        productDescription = ""
                        productPrice = "0.0"
                        productStock = "0"
                        imageUrl = ""
                        isFormValid = false

                        navController.popBackStack() // Go back to the previous screen
                    }
                },
                enabled = isFormValid,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add Product")
            }
        }
    }
}

fun validateForm(name: String, description: String, price: String, stock: String, imageUrl: String): Boolean {
    return name.isNotEmpty() && description.isNotEmpty() && price.isNotEmpty() && stock.isNotEmpty() && imageUrl.isNotEmpty()
}

suspend fun addProductToFirestore(db: FirebaseFirestore, product: Product) {
    try {
        db.collection("Products")
            .add(product)
            .addOnSuccessListener { documentReference ->
                Log.d("Firestore", "Document added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error adding document", e)
            }
    } catch (e: Exception) {
        Log.e("Firestore", "Failed to add product: ${e.message}")
    }
}
