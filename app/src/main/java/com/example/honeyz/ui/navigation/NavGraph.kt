package com.example.honeyz.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.honeyz.ui.User.cart.CartListPage
import com.example.honeyz.ui.User.checkout.CheckoutScreen
import com.example.honeyz.ui.User.home.HomeScreen
import com.example.honeyz.ui.User.payment.PaymentConfirmationScreen
import com.example.honeyz.ui.User.product.ProductDetailScreen
import com.example.honeyz.ui.User.product.ProductViewModel
import com.example.honeyz.ui.User.product.ViewProduct
import com.example.honeyz.ui.admin.AdminDashboardScreen
import com.example.honeyz.ui.admin.manageProduct.ManageProduct
import com.example.honeyz.ui.admin.manageOrder.ManageOrderScreen
import com.example.honeyz.ui.admin.manageProduct.AddProductScreen
import com.example.honeyz.ui.settings.SettingsScreen
import com.example.honeyz.ui.customer.CustomerScreen
import com.example.honeyz.ui.home.MainScreen
import com.example.honeyz.ui.login.LoginScreen
import com.example.honeyz.ui.viewmodel.LoginViewModel
import com.example.honeyz.ui.admin.manageProduct.StockViewModel
import com.example.honeyz.ui.customer.CartScreen
import com.example.honeyz.ui.customer.CartViewModel
import com.example.honeyz.ui.customer.CustomerOrderScreen
import com.example.honeyz.ui.promotion.PromotionDetailScreen
import com.example.honeyz.ui.signup.SignupScreen
import com.example.honeyz.viewmodel.OrderViewModel
import kotlinx.coroutines.launch

@Composable
fun NavGraph(
    navController: NavHostController,
    loginViewModel: LoginViewModel
) {
    // Shared ViewModel for managing stock
    val stockViewModel: StockViewModel = viewModel()

    // Shared ViewModel for managing order
    val orderViewModel: OrderViewModel = viewModel()

    // Shared ViewModel for managing cart
    val cartViewModel: CartViewModel = viewModel()

    val productViewModel: ProductViewModel = viewModel() // Fetch the viewModel here

    // Function to restart the app by clearing the back stack and navigating to the login screen
    val restartApp: (String) -> Unit = { route ->
        navController.navigate(route) {
            popUpTo(navController.graph.startDestinationId) { inclusive = true }
        }
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Function to open a screen without clearing the back stack
    val openScreen: (String) -> Unit = { route ->
        navController.navigate(route)
    }



    NavHost(navController = navController, startDestination = "home") {
        // Login screen composable
        composable("login") {
            LoginScreen(navController, loginViewModel)
        }

        // Admin screen composable
        composable("admin") {
            AdminDashboardScreen(navController, stockViewModel, loginViewModel, orderViewModel)
        }

        // Customer screen composable
//        composable("customer") {
//            CustomerScreen(navController, stockViewModel, loginViewModel)
//        }

        //Main Screen Composable
        composable("home") {
            MainScreen(navController, productViewModel)
        }

        // Manage Products screen (only for Admin)
        composable("manageProduct") {
            ManageProduct(navController, stockViewModel, loginViewModel)
        }

        // Manage Products screen (only for Admin)
        composable("manageOrder") {
            ManageOrderScreen(navController, orderViewModel)
        }
        composable("addProduct") {
            AddProductScreen(navController, stockViewModel)
        }

        // Settings screen composable
        composable("settings") {
            SettingsScreen(navController,loginViewModel, restartApp, openScreen)
        }

        // CustomerOrderScreen composable
        composable("customerOrder") {
            CustomerOrderScreen(navController, orderViewModel)
        }

        // SignupScreen composable
        composable(route = "signup") {
            SignupScreen(navController)
        }

        // SignupScreen composable
        composable(route = "customerCart") {
            CartScreen(navController, cartViewModel = viewModel())
        }


        composable("customer"){
            MainScreen(navController, productViewModel)
        }

        composable(route = MainScreen.Start.name) {
            MainScreen(navController, productViewModel)
        }

        composable(route = MainScreen.Product.name) {
            ViewProduct(
                navController = navController,
                onProductClicked = { product ->
                    productViewModel.selectProduct(product)
                    navController.navigate(MainScreen.ProductDetail.name)
                },
            )
        }

        composable(route = MainScreen.ProductDetail.name) {
            ProductDetailScreen(
                productViewModel = productViewModel, // Pass the correct viewModel
                onAddToCartClicked = { product, quantity ->
                    if (loginViewModel.isUserLoggedIn()) {
                        product.let {
                            productViewModel.addToCart(it, quantity)
                        }
                    } else {
                        scope.launch {
                            snackbarHostState.showSnackbar("Please log in to add items to the cart.")
                        }
                    }
                }
            )
        }

        composable(route = MainScreen.Cart.name) {
            val cartListViewModel: com.example.honeyz.ui.User.cart.CartViewModel = viewModel()
            CartListPage(
                viewModel = cartListViewModel,
                onCheckout = { }
            )
        }

        composable(route = MainScreen.Checkout.name) {
            CheckoutScreen()
        }

        composable(route = MainScreen.Payment.name) {
            PaymentConfirmationScreen()
        }

        composable(
            route = "promotionDetail/{promotionId}",
            arguments = listOf(navArgument("promotionId") { type = NavType.StringType })
        ) { backStackEntry ->
            val promotionId = backStackEntry.arguments?.getString("promotionId")
            if (promotionId != null) {
                PromotionDetailScreen(promotionId = promotionId)
            }
        }
    }
}
