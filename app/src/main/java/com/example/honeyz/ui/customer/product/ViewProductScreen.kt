package com.example.honeyz.ui.User.product


import android.util.Log

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.honeyz.R
import com.example.honeyz.ui.component.ProductCard
import com.example.honeyz.model.Product
import com.example.honeyz.ui.customer.CustomerBottomAppBar
import com.example.honeyz.ui.home.BottomAppBar
import com.example.honeyz.ui.theme.light_primary_background
import com.example.honeyz.ui.theme.light_secondary_background
import com.example.honeyz.ui.theme.secondary_text
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewProduct(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    onProductClicked: (Product) -> Unit
){
    var productList by remember { mutableStateOf<List<Product>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var searchStr by remember { mutableStateOf("") }

    // Fetch data from Firestore
    LaunchedEffect(Unit) {
        Firebase.firestore.collection("Products")
            .get()
            .addOnSuccessListener { result ->
                val products = result.documents.mapNotNull { document ->
                    document.toObject(Product::class.java)?.copy(id = document.id)
                }
                productList = products
                isLoading = false
                Log.d("Firebase", "Fetched products: $products")
            }
            .addOnFailureListener { exception ->
                isLoading = false
                Log.e("Firebase", "Error getting promotions", exception)
            }
    }
    if (isLoading) {
        CircularProgressIndicator()
    }
    else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "Products") },
                )
            },
            bottomBar = {
                BottomAppBar(navController)
            }
        ){
            paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
            ){
                Column(modifier = modifier) {
                    SearchBar(
                        searchStr = searchStr,
                        onSearchChanged = { searchStr = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .background(
                                color = light_primary_background,
                                shape = RoundedCornerShape(20.dp)
                            )
                            .shadow(
                                elevation = 10.dp,
                                shape = RoundedCornerShape(20.dp)
                            )
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))
                    val filteredProducts = productList.filter { product ->
                        product.name.contains(searchStr, ignoreCase = true)
                    }
                    if (filteredProducts.isEmpty()){
                        Text(
                            text = stringResource(R.string.no_result)
                        )
                    }
                    else{
                        ProductGrid(
                            productList = filteredProducts,
                            modifier = modifier,
                            onProductClicked = onProductClicked
                        )
                    }
                }
            }

        }


    }
}

@Composable
fun SearchBar(
    searchStr: String,
    onSearchChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        TextField(
            value = searchStr,
            onValueChange = {onSearchChanged(it)},
            placeholder = {Text(text = stringResource(R.string.search))},
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search Icon"
                )
            },
            textStyle = TextStyle(
                fontSize = 25.sp,
                lineHeight = 30.sp
            ),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = light_secondary_background,
                unfocusedContainerColor = light_primary_background,
                focusedPlaceholderColor = secondary_text,
                unfocusedPlaceholderColor = secondary_text,
                focusedIndicatorColor = light_primary_background,
                unfocusedIndicatorColor = light_primary_background
            ),
            modifier = modifier
        )
    }
}

@Composable
fun ProductGrid(
    productList: List<Product>,
    modifier: Modifier = Modifier,
    onProductClicked: (Product) -> Unit
){
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier
    ){
        items(productList) { product ->
            ProductCard(
                product,
                modifier = Modifier
                    .clickable{onProductClicked(product)}
            )
        }
    }
}
