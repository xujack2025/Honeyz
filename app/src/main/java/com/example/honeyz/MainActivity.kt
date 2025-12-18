package com.example.honeyz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import com.example.honeyz.ui.viewmodel.LoginViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.MaterialTheme
import com.example.honeyz.ui.navigation.NavGraph


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp() // Starting point of the app, contains navigation and main composables
        }
    }
}

@Composable
fun MyApp() {
    val navController = rememberNavController()
    val loginViewModel: LoginViewModel = viewModel()

    // Applying Material Theme
    MaterialTheme {
        // Launching the navigation graph
        NavGraph(navController = navController, loginViewModel = loginViewModel)
    }
}
