package com.example.honeyz.ui.admin

import androidx.compose.runtime.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.honeyz.model.OrderStatus
import com.example.honeyz.ui.navigation.AdminBottomAppBar
import com.example.honeyz.ui.viewmodel.LoginViewModel
import com.example.honeyz.ui.admin.manageProduct.StockViewModel
import com.example.honeyz.ui.viewmodel.UserRole
import com.example.honeyz.viewmodel.OrderViewModel
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    navController: NavHostController,
    stockViewModel: StockViewModel,
    loginViewModel: LoginViewModel,
    orderViewModel: OrderViewModel
) {
    // Fetch the current user from FirebaseAuth
    val currentUser = FirebaseAuth.getInstance().currentUser
    // Get the email part before @
    val username = currentUser?.email?.substringBefore("@") ?: "Admin"

    // Collect real-time data from view models
    val products = stockViewModel.products
    val orders by orderViewModel.orders.collectAsState()
    val userRole by loginViewModel.userRole

    // Calculations
    val totalProducts = products.size
    val totalUsers = if (userRole == UserRole.ADMIN) 1 else 0 // Mock for now
    val totalPendingOrders = orders.count { it.status == OrderStatus.PENDING }
    val totalDeliveredOrders = orders.count { it.status == OrderStatus.DELIVERED }
    val totalCanceledOrders = orders.count { it.status == OrderStatus.CANCELLED }
    val lowStockProducts = products.count { it.stock < 10 }

    // Assuming LoginViewModel has a way to count users based on their role
//    val totalAdmins = loginViewModel.getAdminUsersCount()
//    val totalCustomers = loginViewModel.getCustomerUsersCount()

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.padding(16.dp),
                title = { Text("Admin Dashboard", style = MaterialTheme.typography.headlineMedium) }
            )
        },
        bottomBar = {
            AdminBottomAppBar(navController)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Section
            Text(text = "Welcome, $username", style = MaterialTheme.typography.headlineLarge)

            Spacer(modifier = Modifier.height(16.dp))

            // Summary Cards
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SummaryCard(title = "Total Users", value = totalUsers.toString())
                SummaryCard(title = "Total Products", value = totalProducts.toString())
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SummaryCard(title = "Orders Pending", value = totalPendingOrders.toString())
                SummaryCard(title = "Orders Delivered", value = totalDeliveredOrders.toString())
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SummaryCard(title = "Orders Canceled", value = totalCanceledOrders.toString())
                SummaryCard(title = "Low Stock", value = lowStockProducts.toString())
            }
        }
    }
}

@Composable
fun SummaryCard(title: String, value: String) {
    Card(
        modifier = Modifier
            .size(170.dp)
            .padding(10.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = MaterialTheme.shapes.extraLarge
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp).fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
            )
        }
    }
}
