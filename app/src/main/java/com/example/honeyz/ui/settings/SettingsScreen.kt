package com.example.honeyz.ui.settings

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.honeyz.R
import com.example.honeyz.ui.home.BottomAppBar
import com.example.honeyz.ui.home.MainLayout
import com.example.honeyz.ui.navigation.AdminBottomAppBar
import com.example.honeyz.ui.viewmodel.LoginViewModel
import com.example.honeyz.ui.viewmodel.UserRole
import com.example.honeyz.R.drawable as AppIcon
import com.example.honeyz.R.string as AppText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavHostController,
    loginViewModel: LoginViewModel,
    restartApp: (String) -> Unit,
    openScreen: (String) -> Unit
) {
    // Get the email from the LoginViewModel
    val email = loginViewModel.email.value ?: "Anonymous"
    val username = email.substringBefore("@")

    MainLayout(navController, loginViewModel) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile section
            ProfileHeaderSection(username = username, email = email)

            // Check the user role and display corresponding settings content
            when (loginViewModel.userRole.value) {
                UserRole.ADMIN -> {
                    AdminSettingsScreenContent(
                        onSignOutClick = { loginViewModel.logout(); restartApp("login") },
                        onDeleteMyAccountClick = { loginViewModel.deleteAccount { success, message ->
                            if (success) restartApp("login") else Log.e("DeleteAccount", message ?: "Unknown error")
                        } }
                    )
                }
                UserRole.CUSTOMER -> {
                    CustomerSettingsScreenContent(
                        onSignOutClick = { loginViewModel.logout(); restartApp("login") },
                        onDeleteMyAccountClick = { loginViewModel.deleteAccount { success, message ->
                            if (success) restartApp("login") else Log.e("DeleteAccount", message ?: "Unknown error")
                        } }
                    )
                }
                else -> {
                    SettingsScreenContent(
                        isAnonymousAccount = true,
                        onLoginClick = { openScreen("login") },
                        onSignUpClick = { openScreen("signup") },
                        onSignOutClick = { loginViewModel.logout(); restartApp("login") },
                        onDeleteMyAccountClick = { loginViewModel.deleteAccount { success, message ->
                            if (success) restartApp("login") else Log.e("DeleteAccount", message ?: "Unknown error")
                        } }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreenContent(
    isAnonymousAccount: Boolean,
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit,
    onSignOutClick: () -> Unit,
    onDeleteMyAccountClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        // If the user is anonymous, show Sign In and Sign Up options
        if (isAnonymousAccount) {
            RegularCardEditor(
                title = AppText.sign_in,
                icon = AppIcon.ic_login,
                content = "",
                iconSize = 24.dp, // Set icon size
                modifier = Modifier.padding(16.dp,4.dp),
                onEditClick = { onLoginClick() }
            )

            RegularCardEditor(
                title = AppText.create_account,
                icon = R.drawable.ic_create_account,
                content = "",
                iconSize = 24.dp, // Set icon size
                modifier = Modifier.padding(16.dp,4.dp),
                onEditClick = { onSignUpClick() }
            )

        } else {
            // If user is authenticated, show Sign Out and Delete Account options
            SignOutCard(onSignOutClick = onSignOutClick)
            DeleteMyAccountCard(onDeleteMyAccountClick = onDeleteMyAccountClick)
        }
    }
}

@Composable
fun AdminSettingsScreenContent(
    onSignOutClick: () -> Unit,
    onDeleteMyAccountClick: () -> Unit
) {
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        Text(text = "Admin Settings", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(16.dp))

        // Admin-specific options
        RegularCardEditor(
            title = AppText.edit_profile,
            icon = AppIcon.ic_edit_profile,
            content = "",
            onEditClick = { /* Navigate to profile editing */ },
            iconSize = 24.dp
        )

        // Common options (e.g., Sign out, Delete account)
        SignOutCard(onSignOutClick = onSignOutClick)
        DeleteMyAccountCard(onDeleteMyAccountClick = onDeleteMyAccountClick)
    }
}

@Composable
fun CustomerSettingsScreenContent(
    onSignOutClick: () -> Unit,
    onDeleteMyAccountClick: () -> Unit
) {
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        Text(text = "Customer Settings", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(16.dp))

//        // Customer-specific options
//        RegularCardEditor(
//            title = AppText.order_history,
//            icon = R.drawable.ic_order_history,
//            content = "",
//            onEditClick = { /* Navigate to order history */ },
//            iconSize = 24.dp
//        )
//
//        RegularCardEditor(
//            title = AppText.notification_settings,
//            icon = R.drawable.ic_notifications,
//            content = "",
//            onEditClick = { /* Navigate to notification settings */ },
//            iconSize = 24.dp
//        )

        // Common options (e.g., Sign out, Delete account)
        SignOutCard(onSignOutClick = onSignOutClick)
        DeleteMyAccountCard(onDeleteMyAccountClick = onDeleteMyAccountClick)
    }
}

@Composable
fun SignOutCard(onSignOutClick: () -> Unit) {
    var showDialog by remember { mutableStateOf(false) }

    RegularCardEditor(
        title = AppText.sign_out,
        icon = AppIcon.ic_logout,
        content = "",
        iconSize = 24.dp, // Set icon size
        onEditClick = { showDialog = true }
    )

    if (showDialog) {
        AlertDialog(
            title = { Text(stringResource(AppText.sign_out_title)) },
            text = { Text(stringResource(AppText.sign_out_description)) },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text(stringResource(AppText.cancel))
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    onSignOutClick()
                    showDialog = false
                }) {
                    Text(stringResource(AppText.sign_out))
                }
            },
            onDismissRequest = { showDialog = false }
        )
    }
}

@Composable
fun DeleteMyAccountCard(onDeleteMyAccountClick: () -> Unit) {
    var showDialog by remember { mutableStateOf(false) }

    RegularCardEditor(
        title = AppText.delete_my_account,
        icon = R.drawable.ic_delete_account,
        content = "",
        iconSize = 24.dp, // Set icon size
        onEditClick = { showDialog = true }
    )

    if (showDialog) {
        AlertDialog(
            title = { Text(stringResource(AppText.delete_account_title)) },
            text = { Text(stringResource(AppText.delete_account_description)) },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    onDeleteMyAccountClick()
                    showDialog = false
                }) {
                    Text("Delete")
                }
            },
            onDismissRequest = { showDialog = false }
        )
    }
}

@Composable
fun RegularCardEditor(
    @StringRes title: Int,
    @DrawableRes icon: Int,
    content: String,
    modifier: Modifier = Modifier.padding(16.dp,4.dp),
    onEditClick: () -> Unit,
    iconSize: androidx.compose.ui.unit.Dp // Added parameter for icon size
) {
    Card(
        modifier = modifier
            .background(Color.Transparent),
        onClick = onEditClick,
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(stringResource(title), color = MaterialTheme.colorScheme.onSurface)
            }

            if (content.isNotBlank()) {
                Text(text = content, modifier = Modifier.padding(16.dp, 0.dp))
            }

            // Icon with customizable size
            Icon(
                painter = painterResource(id = icon),
                contentDescription = stringResource(id = title),
                modifier = Modifier.size(iconSize), // Set icon size here
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun ProfileHeaderSection(username: String, email: String) {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primary)
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Placeholder profile image
            Image(
                painter = painterResource(id = R.drawable.ic_login),
                contentDescription = "Profile Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .background(Color.White, CircleShape)
            )
            Text(text = email, style = MaterialTheme.typography.bodyLarge, color = Color.White)
            Text(text = username, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSettingsScreen() {
    SettingsScreenContent(
        isAnonymousAccount = false,
        onLoginClick = {},
        onSignUpClick = {},
        onSignOutClick = {},
        onDeleteMyAccountClick = {}
    )
}
