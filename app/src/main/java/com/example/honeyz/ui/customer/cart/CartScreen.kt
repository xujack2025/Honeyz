package com.example.honeyz.ui.User.cart

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.honeyz.ui.component.CartItemRow


@Composable
fun CartListPage(viewModel: CartViewModel, onCheckout: () -> Unit) {
    val cartUiState by viewModel.cartUiState.collectAsState()
    val cartItems = cartUiState.cartItemList
    val userUID = cartUiState.userUID

    // Calculate total price
    val totalPrice = cartItems.sumOf {
        val priceAsDouble = it.price.toDoubleOrNull() ?: 0.0 // Convert price to Double, default to 0.0 if invalid
        priceAsDouble * it.quantity
    }


    // Fetch cart items
    LaunchedEffect(Unit) {
        viewModel.fetchCartItems()
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Text(text = "Your Cart", fontSize = 24.sp)

        Spacer(modifier = Modifier.height(16.dp))

        // Iterate over cart items directly
        cartItems.forEach { item ->
            CartItemRow(
                viewModel = viewModel,
                item = item,
                onRemove = {
                    viewModel.removeItem(item) // Remove item from ViewModel
                }
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(text = "Total: RM ${"%.2f".format(totalPrice)}", fontSize = 20.sp)

        Button(
            onClick = onCheckout,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(text = "Checkout")
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun PreviewCartItem(){

}