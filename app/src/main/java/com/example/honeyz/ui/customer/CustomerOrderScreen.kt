package com.example.honeyz.ui.customer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.honeyz.model.Order
import com.example.honeyz.viewmodel.OrderViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerOrderScreen(navController: NavHostController, orderViewModel: OrderViewModel) {
    var customerName by remember { mutableStateOf("") }
    var productName by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("1") }
    var paymentMethod by remember { mutableStateOf("") }
    var totalPrice by remember { mutableStateOf("") }
    var isFormValid by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Place an Order") }
            )
        },
        bottomBar = { CustomerBottomAppBar(navController) },

        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = customerName,
                    onValueChange = {
                        customerName = it
                        isFormValid = validateOrderForm(customerName, productName, quantity, paymentMethod, totalPrice)
                    },
                    label = { Text("Customer Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = productName,
                    onValueChange = {
                        productName = it
                        isFormValid = validateOrderForm(customerName, productName, quantity, paymentMethod, totalPrice)
                    },
                    label = { Text("Product Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = quantity,
                    onValueChange = {
                        quantity = it
                        isFormValid = validateOrderForm(customerName, productName, quantity, paymentMethod, totalPrice)
                    },
                    label = { Text("Quantity") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = totalPrice,
                    onValueChange = {
                        totalPrice = it
                        isFormValid = validateOrderForm(customerName, productName, quantity, paymentMethod, totalPrice)
                    },
                    label = { Text("Total Price") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = paymentMethod,
                    onValueChange = {
                        paymentMethod = it
                        isFormValid = validateOrderForm(customerName, productName, quantity, paymentMethod, totalPrice)
                    },
                    label = { Text("Payment Method (e.g. Credit Card, PayPal)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val order = Order(
                            orderId = "",  // Auto-assigned by firestore
                            customerName = customerName,
                            productName = productName,
                            quantity = quantity.toInt(),
                            paymentMethod = paymentMethod,
                            totalPrice = totalPrice.toDouble()
                        )
                        orderViewModel.addOrder(order)

                        // Show confirmation and go back to the previous screen
                        navController.popBackStack()
                    },
                    enabled = isFormValid,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Submit Order")
                }
            }
        }
    )
}

// Helper function to validate form
fun validateOrderForm(
    customerName: String,
    productName: String,
    quantity: String,
    paymentMethod: String,
    totalPrice: String
): Boolean {
    return customerName.isNotEmpty() && productName.isNotEmpty() && quantity.isNotEmpty() && paymentMethod.isNotEmpty() && totalPrice.isNotEmpty()
}
