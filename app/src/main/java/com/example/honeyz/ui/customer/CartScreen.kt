package com.example.honeyz.ui.customer

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(navController: NavHostController, cartViewModel: CartViewModel) {
    var isLoading by remember { mutableStateOf(true) }
    var errorOccurred by remember { mutableStateOf(false) }

    // Fetch cart items when the screen is first launched
    LaunchedEffect(Unit) {
        cartViewModel.fetchCartItemsForUser { success ->
            isLoading = false
            errorOccurred = !success
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "My Cart") },
            )
        },
        bottomBar = {
            CustomerBottomAppBar(navController)
        },
        content = { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues)) {
                if (isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else if (errorOccurred) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Error loading cart items")
                            Button(
                                onClick = {
                                    Log.d("CartScreen", "Add to Cart button clicked")
                                    cartViewModel.addToCart(productId = "12345", quantity = 1)
                                },
                                modifier = Modifier.fillMaxWidth().padding(16.dp)
                            ) {
                                Text("Add to Cart")
                            }

                        }
                    }
                } else {
                    // Display the cart items
                    LazyColumn {
                        items(cartViewModel.cartItems) { cartItem ->
                            CartItemCard(cartItem = cartItem)
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }

                // Always display the "Add to Cart" button, even if there are cart items or errors
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { cartViewModel.addToCart(productId = "12345", quantity = 1) },
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                ) {
                    Text("Add to Cart")
                }
            }
        }
    )
}


@Composable
fun CartItemCard(cartItem: CartItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Product ID: ${cartItem.productId}")
            Text(text = "Quantity: ${cartItem.quantity}")
            Text(text = "Cart Item ID: ${cartItem.cartItemId}")
        }
    }
}
