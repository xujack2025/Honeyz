package com.example.honeyz.ui.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.honeyz.R.drawable as AppIcon

@Composable
fun AdminBottomAppBar(navController: NavHostController) {
    BottomAppBar(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround // Distribute icons evenly
        ){
            IconButton(onClick = {
                navController.navigate("admin") {
                    popUpTo("admin") { inclusive = true } // Go back to Admin page and clear the backstack
                }
            }) {
                Icon(imageVector = Icons.Filled.Home, contentDescription = "Home")
            }

            IconButton(onClick = {
                navController.navigate("manageProduct") {
                    launchSingleTop = true // Prevent multiple instances of the same screen
                }
            }) {
                Icon(imageVector = Icons.Filled.ShoppingCart, contentDescription = "Manage Products")
            }

            IconButton(onClick = {
                navController.navigate("manageOrder") {
                    launchSingleTop = true // Prevent multiple instances of the same screen
                }
            }) {
                Icon(painter = painterResource(id = AppIcon.ic_order), contentDescription = "Manage Orders", modifier = Modifier.size(24.dp))
            }

            IconButton(onClick = {
                navController.navigate("settings") {
                    launchSingleTop = true // Ensure only one instance
                }
            }) {
                Icon(imageVector = Icons.Filled.Person, contentDescription = "Settings")
            }
        }

    }
}