package com.example.honeyz.ui.home

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.honeyz.R
import com.example.honeyz.ui.User.cart.CartListPage
import com.example.honeyz.ui.User.cart.CartViewModel
import com.example.honeyz.ui.User.checkout.CheckoutScreen
import com.example.honeyz.ui.User.home.HomeScreen
import com.example.honeyz.ui.User.payment.PaymentConfirmationScreen
import com.example.honeyz.ui.User.product.ProductDetailScreen
import com.example.honeyz.ui.User.product.ProductViewModel
import com.example.honeyz.ui.theme.light_secondary_background
import kotlinx.coroutines.launch
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.dimensionResource
import com.example.honeyz.model.Promotion
import com.example.honeyz.ui.component.PromotionCard
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


enum class MainScreen(@StringRes val title: Int){
    Start(title = R.string.app_name),
    Product(title = R.string.view_product),
    ProductDetail(title = R.string.product_detail),
    Cart(title = R.string.cart),
    Checkout(title = R.string.checkout),
    Payment(title = R.string.payment),
    Home(title = R.string.app_name)
}

@Composable
fun MainScreen(
    navController: NavHostController,
    viewModel: ProductViewModel = viewModel(),
){
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val cartItemCount by viewModel.cartItemCount.collectAsState(initial = 0)
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = MainScreen.values().find { it.name == backStackEntry?.destination?.route } ?: MainScreen.Start


    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            HoneyZAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() },
                onCartClick = {
                    if (viewModel.isUserLoggedIn()) {
                        navController.navigate(MainScreen.Cart.name)
                    }
                    else {
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = "Please log in to open the cart.",
                                duration = SnackbarDuration.Short
                            )
                        }
                    }
                },
                cartItemCount = cartItemCount,
                modifier = Modifier.shadow(4.dp)
            )
        },
        bottomBar = { BottomAppBar(navController) },
        content = { paddingValues ->
            // Your content here
            Box(modifier = Modifier.padding(paddingValues)) {
                HomeScreen(
                    modifier = Modifier.padding(paddingValues),
                    onPromotionClicked = { promotionId ->
                        // Handle the promotion click here
                        navController.navigate("promotionDetail/$promotionId")
                    }
                )
            }
        }
    )

}

@Composable
fun BottomAppBar(navController: NavHostController) {
    BottomAppBar(
        modifier = Modifier.fillMaxWidth() // Ensures the BottomAppBar fills the width
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround // Distribute icons evenly
        ) {
            IconButton(onClick = {
                navController.navigate(MainScreen.Start.name) }) {
                Icon(Icons.Filled.Home, contentDescription = "Home")

            }
            IconButton(onClick = {
                navController.navigate(MainScreen.Product.name) {
                    launchSingleTop = true // Prevent multiple instances of the LoginScreen
                }
            }) {
                Icon(Icons.Filled.Person, contentDescription = "Profile")
            }

            IconButton(onClick = { navController.navigate("settings") }) {
                Icon(Icons.Filled.Settings, contentDescription = "Settings")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HoneyZAppBar(
    currentScreen: MainScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    cartItemCount: Int, // Pass the cart item count here
    onCartClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(stringResource(currentScreen.title)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = light_secondary_background
        ),
        modifier = modifier,
//        navigationIcon = {
//            if (canNavigateBack) {
//                IconButton(onClick = navigateUp) {
//                    Icon(
//                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
//                        contentDescription = stringResource(R.string.back_button)
//                    )
//                }
//            }
//        },
        actions = {
            IconButton(onClick = onCartClick) {
                BadgedBox(
                    badge = {
                        if (cartItemCount > 0) {
                            Badge { Text(cartItemCount.toString()) }
                        }
                    },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_cart),
                        contentDescription = stringResource(R.string.cart)
                    )
                }
            }
        }
    )
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onPromotionClicked: (String) -> Unit
) {
    val db = Firebase.firestore
    var promotionList by remember { mutableStateOf<List<Promotion>>(emptyList()) }

    LaunchedEffect(Unit) {
        db.collection("Promotions")
            .get()
            .addOnSuccessListener { result ->
                val promotions = result.documents.mapNotNull { document ->
                    document.toObject(Promotion::class.java)?.copy(id = document.id)
                }
                promotionList = promotions
            }
    }
    PromotionList(
        modifier = modifier,
        promotionList = promotionList,
        onPromotionClicked = onPromotionClicked
    )
}
@Composable
fun PromotionList(
    modifier: Modifier = Modifier,
    promotionList: List<Promotion>,
    onPromotionClicked: (String) -> Unit
){
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium))
    ) {
        items(promotionList) { promotion ->
            PromotionCard(
                promotion,
                modifier = Modifier
                    .clickable { onPromotionClicked(promotion.id) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    val dummynavController = rememberNavController()
    MainScreen(navController = dummynavController)
}
