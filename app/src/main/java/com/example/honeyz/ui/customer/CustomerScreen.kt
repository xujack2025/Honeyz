package com.example.honeyz.ui.customer

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.honeyz.ui.admin.manageProduct.StockViewModel
import com.example.honeyz.ui.home.MainScreen
import com.example.honeyz.ui.viewmodel.LoginViewModel
import com.example.honeyz.R.drawable as AppIcon
import com.example.honeyz.R.string as AppText

@Composable
fun CustomerScreen(navController: NavHostController, stockViewModel: StockViewModel, loginViewModel: LoginViewModel) {
    Scaffold (
        bottomBar = { CustomerBottomAppBar(navController)}
    ){ paddingValues -> 16.dp

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Display the product name and stock
            Text(text = "Available Products", style = MaterialTheme.typography.headlineMedium)

            Spacer(modifier = Modifier.height(16.dp))

            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f), // Takes available vertical space
                columns = GridCells.Fixed(2), // Define 2 columns for the grid
                contentPadding = PaddingValues(8.dp), // Padding around the content
            ) {
                // Display each product, filter to show only enabled products and sort by stock status
                val availableProducts = stockViewModel.products.filter { !it.isDisabled }
                val sortedProducts = availableProducts.sortedBy { it.stock == 0 }

                items(sortedProducts) { product ->
                    Card(
                        modifier = Modifier
                            .padding(8.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth() // Make sure the card fills the available width
                        ) {
                            // Product image from URL
                            if (product.photoUrl != null) {
                                Image(
                                    painter = rememberAsyncImagePainter(product.photoUrl),
                                    contentDescription = product.name,
                                    modifier = Modifier
                                        .fillMaxWidth() // Fill width instead of entire size
                                        .height(150.dp),
                                    contentScale = ContentScale.Crop
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Product information
                            Text(text = product.name, style = MaterialTheme.typography.bodyLarge)
                            Text(text = product.description)
                            Text(text = "RM ${product.price}")

                            // Show "Sold Out" if stock is 0, otherwise show stock count
                            if (product.stock == 0) {
                                Text(text = "Sold Out", color = MaterialTheme.colorScheme.error)
                            } else {
                                Text(text = "Stock: ${product.stock}")
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Buy Button: Disabled if the product is sold out
                            Button(
                                onClick = {
                                    stockViewModel.decreaseStock(product) // Buy product (decrease stock)
                                },
                                modifier = Modifier.fillMaxWidth(), // Ensure button takes full width
                                enabled = product.stock > 0 // Disable button if stock is 0
                            ) {
                                Text("Buy")
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Logout Button
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
fun CustomerBottomAppBar(navController: NavHostController) {
    val backStackEntry = navController.currentBackStackEntryAsState()

    BottomAppBar(
        modifier = Modifier.fillMaxWidth() // Ensures the BottomAppBar fills the width
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround // Distribute icons evenly
        ) {
            IconButton(onClick = {
                navController.navigate(MainScreen.Start.name) {
                    launchSingleTop = true // Prevent multiple instances of the LoginScreen
                }
            }) {
                Icon(Icons.Filled.Home, contentDescription = "Home")

            }
            IconButton(onClick = {
                navController.navigate(MainScreen.Product.name) {
                    launchSingleTop = true // Prevent multiple instances of the LoginScreen
                }
            }) {
                Icon(Icons.Filled.Person, contentDescription = "Profile")
            }

            IconButton(onClick = { navController.navigate("settings") {
                launchSingleTop = true
            }
            }) {
                Icon(Icons.Filled.Settings, contentDescription = "Settings")
            }
        }
    }

//    BottomAppBar {
//        IconButton(
//            onClick = {
//                navController.navigate("home") {
//                    launchSingleTop = true
//                    popUpTo("home") { inclusive = true }
//                }
//            },
//            enabled = backStackEntry.value?.destination?.route != "home"
//        ) {
//            Icon(Icons.Filled.Home, contentDescription = "Home")
//        }
//
//        IconButton(
//            onClick = {
//                navController.navigate("customer") {
//                    launchSingleTop = true
//                    popUpTo("customer") { inclusive = true }
//                }
//            },
//            enabled = backStackEntry.value?.destination?.route != "customer"
//        ) {
//            Icon(Icons.Filled.Place, contentDescription = "Products")
//        }
//
//        IconButton(
//            onClick = {
//                navController.navigate("customerOrder") {
//                    launchSingleTop = true
//                    popUpTo("customerOrder") { inclusive = true }
//                }
//            },
//            enabled = backStackEntry.value?.destination?.route != "customerOrder"
//        ) {
//            Icon(painter = painterResource(id = AppIcon.ic_order), contentDescription = "Orders")
//        }
//
//        IconButton(
//            onClick = {
//                navController.navigate("customerCart") {
//                    launchSingleTop = true
//                    popUpTo("customerCart") { inclusive = true }
//                }
//            },
//            enabled = backStackEntry.value?.destination?.route != "customerCart"
//        ) {
//            Icon(Icons.Filled.ShoppingCart, contentDescription = "customerCart")
//        }
//
//        IconButton(
//            onClick = {
//                navController.navigate("settings") {
//                    launchSingleTop = true
//                    popUpTo("settings") { inclusive = true }
//                }
//            },
//            enabled = backStackEntry.value?.destination?.route != "settings"
//        ) {
//            Icon(Icons.Filled.Settings, contentDescription = "Settings")
//        }
//    }
}

@Preview
@Composable
fun PreviewCustomerScreen() {
    // Provide dummy ViewModels and a NavController
    val dummyNavController = rememberNavController()
    val stockViewModel = StockViewModel() // You can initialize with dummy data
    val loginViewModel = LoginViewModel()

    // Call the actual CustomerScreen with dummy data
    CustomerScreen(
        navController = dummyNavController,
        stockViewModel = stockViewModel,
        loginViewModel = loginViewModel
    )
}
