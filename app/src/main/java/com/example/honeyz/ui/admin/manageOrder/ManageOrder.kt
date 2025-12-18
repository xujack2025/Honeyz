package com.example.honeyz.ui.admin.manageOrder

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.honeyz.model.Order
import com.example.honeyz.model.OrderStatus
import com.example.honeyz.ui.navigation.AdminBottomAppBar
import com.example.honeyz.viewmodel.OrderViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageOrderScreen(navController: NavHostController, orderViewModel: OrderViewModel) {
    // Fetch the list of orders from Firestore when the screen is first launched
    LaunchedEffect(Unit) {
        orderViewModel.fetchOrdersFromFirestore()
    }

    // Get the orders list as a state
    val orders by orderViewModel.orders.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Order Management", style = MaterialTheme.typography.headlineMedium) }
            )
        },
        bottomBar = {
            AdminBottomAppBar(navController)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            if (orders.isEmpty()) {
                // Show a message if no orders are available
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "No orders available", style = MaterialTheme.typography.bodyLarge)
                }
            } else {
                // Display orders
                for (order in orders) {
                    OrderItem(order = order, onUpdateStatus = { newStatus ->
                        orderViewModel.updateOrderStatus(order.orderId, newStatus)
                    })
                }
            }
        }
    }
}

@Composable
fun OrderItem(order: Order, onUpdateStatus: (OrderStatus) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Customer: ${order.customerName}")
            Text(text = "Product: ${order.productName}")
            Text(text = "Quantity: ${order.quantity}")
            Text(text = "Total Price: $${order.totalPrice}")
            Text(text = "Payment Method: ${order.paymentMethod}")
            Text(text = "Status: ${order.status}")

            Spacer(modifier = Modifier.height(8.dp))

            OrderButton(text = "Mark as Shipped", onClick = { onUpdateStatus(OrderStatus.SHIPPED) })
            Spacer(modifier = Modifier.height(8.dp))

            OrderButton(text = "Mark as Delivered", onClick = { onUpdateStatus(OrderStatus.DELIVERED) })
            Spacer(modifier = Modifier.height(8.dp))

            OrderButton(text = "Cancel Order", onClick = { onUpdateStatus(OrderStatus.CANCELLED) })
        }
    }
}

@Composable
fun OrderButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium
    ) {
        Text(text = text)
    }
}
