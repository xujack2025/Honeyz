package com.example.honeyz.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.honeyz.R
import com.example.honeyz.ui.viewmodel.LoginViewModel
import com.example.honeyz.ui.viewmodel.UserRole

@Composable
fun LoginScreen(navController: NavHostController, loginViewModel: LoginViewModel
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Back Button with Icon and Text
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Yellow)
            .height(70.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        Button(
            onClick = { navController.navigate("home") },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent // Light background color
            ),
            modifier = Modifier.fillMaxHeight(),
            contentPadding = PaddingValues(8.dp), // Control padding inside the button

        ) {
            // Back Icon
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color(0xFFFFA726), // Orange tint for the back arrow
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Back Text
            Text(
                text = "Back",
                color = Color.Black,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }

    Spacer(modifier = Modifier.height(24.dp))


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 100.dp)
            .padding(start = 34.dp, end = 34.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo at the top
        Column (modifier = Modifier
            .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ){
            Image(
                painter = painterResource(id = R.drawable.logo), // Your logo resource
                contentDescription = "Logo",
                modifier = Modifier
                    .size(120.dp)
                    .padding(bottom = 16.dp),
                contentScale = ContentScale.Crop
            )
            // Sign In Title
            Text(
                text = "Sign In",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = "Use the account below to sign in.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }

        Column (
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ){
            Spacer(modifier = Modifier.height(24.dp))

            // Email
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("email") },
                modifier = Modifier
                    .fillMaxWidth(),
                singleLine = true,

            )

            Spacer(modifier = Modifier.height(16.dp))

            //Password
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(), // To mask the password
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Error Message (if any)
            errorMessage?.let { error ->
                Text(text = error, color = Color.Red)
            }

            // Login Button
            Button(
                onClick = {
                    loginViewModel.login(email, password) { success, error ->
                        if (success) {
                            when (loginViewModel.userRole.value) {
                                UserRole.ADMIN -> navController.navigate("admin"){
                                    popUpTo(navController.graph.startDestinationId) {
                                        inclusive = true
                                    }
                                }
                                UserRole.CUSTOMER -> navController.navigate("customer"){
                                    popUpTo(navController.graph.startDestinationId) {
                                        inclusive = true
                                    }
                                }
                                else -> errorMessage = "Unrecognized role"
                            }
                        } else {
                            // Show error message
                            errorMessage = error ?: "Login failed"
                        }
                    }
                }
            ) {
                Text("Login")
            }
        }


    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    val dummyNavController = rememberNavController()
    val loginViewModel = LoginViewModel()

    LoginScreen(
        navController = dummyNavController,
        loginViewModel = loginViewModel
    )
}
