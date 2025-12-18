package com.example.honeyz.ui.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import com.example.honeyz.ui.customer.CustomerBottomAppBar
import com.example.honeyz.ui.navigation.AdminBottomAppBar
import com.example.honeyz.ui.viewmodel.LoginViewModel
import com.example.honeyz.ui.viewmodel.UserRole

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainLayout(
    navController: NavHostController,
    loginViewModel: LoginViewModel,
    content: @Composable (paddingValues: PaddingValues) -> Unit
) {
    val userRole by loginViewModel.userRole

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Profile", style = MaterialTheme.typography.headlineMedium) },
            )
        },

        bottomBar = {
            when (userRole) {
                UserRole.ADMIN -> AdminBottomAppBar(navController)
                UserRole.CUSTOMER -> CustomerBottomAppBar(navController)
                else -> BottomAppBar(navController)
            }
        },
        content = content
    )
}