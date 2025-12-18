package com.example.honeyz.ui.admin.manageProduct

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.honeyz.model.Product
import com.example.honeyz.ui.navigation.AdminBottomAppBar
import com.example.honeyz.ui.viewmodel.LoginViewModel
import androidx.compose.foundation.layout.Box
import coil.compose.rememberAsyncImagePainter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageProduct(
    navController: NavHostController,
    stockViewModel: StockViewModel,
    loginViewModel: LoginViewModel
) {

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.padding(4.dp, 4.dp, 4.dp, 0.dp),
                title = {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Manage Products", style = MaterialTheme.typography.headlineMedium)
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate("addProduct") }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Product")
                    }
                }
            )
        },
        bottomBar = { AdminBottomAppBar(navController) }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp, 0.dp, 16.dp, 0.dp)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Scrollable product list (Admin can see all products)
            for (product in stockViewModel.products) {
                ProductItemCard(product = product, stockViewModel = stockViewModel)
                Spacer(modifier = Modifier.height(8.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                loginViewModel.logout()
                navController.navigate("login") { popUpTo("login") { inclusive = true } }
            }) {
                Text("Logout")
            }
        }
    }
}

@Composable
fun ProductItemCard(product: Product, stockViewModel: StockViewModel, isSoldOut: Boolean = product.stock == 0) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 0.dp, 0.dp, 8.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = if (isSoldOut) Color.LightGray else MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(
                modifier = Modifier.width(150.dp)
            ) {
                // Image from Firestore using async loader
                Image(
                    painter = rememberAsyncImagePainter(model = product.photoUrl), // Use URL or image reference from Firestore
                    contentDescription = product.name,
                    modifier = Modifier.fillMaxWidth().height(100.dp)
                )
                Text(text = product.name, style = MaterialTheme.typography.bodyLarge)
                if (isSoldOut) {
                    Text(text = "Sold Out", style = MaterialTheme.typography.bodySmall, color = Color.Red)
                } else {
                    Text(text = "Stock: ${product.stock}", style = MaterialTheme.typography.bodySmall)
                }
            }
            Column {
                val buttonModifier = Modifier
                    .width(150.dp)
                    .padding(8.dp)

                val buttonShape = RoundedCornerShape(8.dp)

                Button(
                    onClick = { stockViewModel.increaseStock(product) },
                    modifier = buttonModifier,
                    shape = buttonShape
                ) {
                    Text("Increase")
                }

                Button(
                    onClick = { stockViewModel.decreaseStock(product) },
                    modifier = buttonModifier,
                    shape = buttonShape,
                    enabled = !isSoldOut
                ) {
                    Text("Decrease")
                }

                // Toggle disabled status
                Button(
                    onClick = { stockViewModel.toggleProductEnabled(product) },
                    modifier = buttonModifier,
                    shape = buttonShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (product.isDisabled) Color.Gray else Color.Red
                    )
                ) {
                    Text(if (product.isDisabled) "Enable" else "Disable")
                }
            }
        }
    }
}