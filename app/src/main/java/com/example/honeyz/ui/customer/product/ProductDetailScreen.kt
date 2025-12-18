package com.example.honeyz.ui.User.product

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.honeyz.R
import com.example.honeyz.model.Product
import com.example.honeyz.ui.theme.light_primary
import com.example.honeyz.ui.theme.light_secondary_background

@Composable
fun ProductDetailScreen(
    productViewModel: ProductViewModel,
    onAddToCartClicked: (Product, Int) -> Unit
) {
    val productUiState by productViewModel.productUiState.collectAsState()
    val product = productUiState.product
    val quantity = productUiState.quantity
    val errorMessage = productUiState.errorMessage

    product?.let {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            // Product Image
            Image(
                painter = rememberAsyncImagePainter(model = product.photoUrl),  // Use the image URL from the product object
                contentDescription = "${product.name} Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(300.dp)
                    .padding(bottom = 16.dp)
            )
            // Product Name
            Text(
                text = product.name,
                color = light_primary,
                fontWeight = FontWeight.W500,
                fontSize = 24.sp,
                fontFamily = FontFamily(Font(R.font.roboto_medium)),
                textAlign = TextAlign.Left,
                modifier = Modifier
                    .padding(bottom = 8.dp)

            )
            // Product Description
            Text(
                text = product.description,
                fontWeight = FontWeight.Normal,
                fontFamily = FontFamily(Font(R.font.readex_pro)),
                fontSize = 20.sp
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ){
                // Product Price
                Text(
                    text = "RM ${product.price}",
                    fontSize = 24.sp,
                    color = light_primary,
                    fontFamily = FontFamily(Font(R.font.roboto_medium)),
                    modifier = Modifier.padding(vertical = 16.dp)
                )
                // Quantity selection
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .border(
                            width = 2.dp,
                            color = light_secondary_background,
                            shape = RoundedCornerShape(10.dp)
                        )
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_remove),
                        contentDescription = "Decrease",
                        tint = light_primary,
                        modifier = Modifier
                            .clickable { productViewModel.decreaseQuantity() }
                            .align(Alignment.CenterVertically)
                    )
                    Text(
                        text = quantity.toString(),
                        fontSize = 24.sp,
                        fontFamily = FontFamily(Font(R.font.outfit)),
                        fontWeight = FontWeight.W500,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .align(Alignment.CenterVertically)
                    )
                    Icon(
                        painter = painterResource(R.drawable.ic_add),
                        contentDescription = "Addition",
                        tint = light_primary,
                        modifier = Modifier
                            .clickable { productViewModel.increaseQuantity() }
                            .align(Alignment.CenterVertically)
                    )
                }
            }
            errorMessage?.let { message ->
                Text(
                    text = message,
                    color = Color.Red,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
//            Button(
//                onClick = { cartViewModel.addToCart(productId = "12345", quantity = 1) },
//                modifier = Modifier.fillMaxWidth().padding(16.dp)
//            ) {
//                Text("Add to Cart")
//            }

            Button(onClick = {onAddToCartClicked(it, quantity)}) {
                Text(text = "Add to Cart")
            }
        }
    } ?: run {
        // If the product is null, you can show a loading or error state
        Text(text = "Loading product details...")
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewProductDetailScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        // Product Image
        AsyncImage(
            model = null,
            contentDescription = "Product Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(300.dp)
                .padding(bottom = 16.dp)
        )
        // Product Name
        Text(
            text = "Test Product",
            color = light_primary,
            fontWeight = FontWeight.W500,
            fontSize = 24.sp,
            fontFamily = FontFamily(Font(R.font.roboto_medium)),
            textAlign = TextAlign.Left,
            modifier = Modifier
                .padding(bottom = 8.dp)

        )
        // Product Description
        Text(
            text = "Description... ",
            fontWeight = FontWeight.Normal,
            fontFamily = FontFamily(Font(R.font.readex_pro)),
            fontSize = 20.sp
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text(
                text = "RM ${"%.2f".format(99.99)}",
                fontSize = 24.sp,
                color = light_primary,
                fontFamily = FontFamily(Font(R.font.roboto_medium)),
                modifier = Modifier
                    .padding(vertical = 16.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = light_secondary_background,
                        shape = RoundedCornerShape(10.dp)
                    )
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_remove),
                    contentDescription = "Decrease",
                    tint = light_primary,
                    modifier = Modifier
                        .clickable { }
                        .align(Alignment.CenterVertically)
                )
                Text(
                    text = "1",
                    fontSize = 24.sp,
                    fontFamily = FontFamily(Font(R.font.roboto_medium)),
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .align(Alignment.CenterVertically)
                )
                Icon(
                    painter = painterResource(R.drawable.ic_add),
                    contentDescription = "Addition",
                    tint = light_primary,
                    modifier = Modifier
                        .clickable { }
                        .align(Alignment.CenterVertically)
                )
            }
        }
    }
}
