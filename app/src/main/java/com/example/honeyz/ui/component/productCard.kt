package com.example.honeyz.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.honeyz.R
import com.example.honeyz.model.Product
import com.example.honeyz.ui.theme.light_primary
import com.example.honeyz.ui.theme.light_primary_background


@Composable
fun ProductCard(product: Product, modifier: Modifier = Modifier){
    Card(
        modifier = modifier
            .background(
                color = light_primary_background,
                shape = RoundedCornerShape(20.dp)
            )
            .width(dimensionResource(R.dimen.product_list_card_width)),
        elevation = CardDefaults.cardElevation(4.dp)
    ){
        Column(modifier = Modifier.background(color = light_primary_background)){
            Image(
                painter = rememberAsyncImagePainter(model = product.photoUrl),
                contentDescription = "${product.name} Image",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(165.dp)
                    .background(
                        color = light_primary_background,
                        shape = RoundedCornerShape(20.dp)
                    )
            )
            Text(
                // Product Name
                text = product.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.W300,
                //fontFamily = FontFamily(Font(R.font.outfit)),
                modifier = Modifier
                    .padding(
                        start = dimensionResource(R.dimen.padding_small),
                        bottom = dimensionResource(R.dimen.padding_medium)
                    )
                    .fillMaxWidth(),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                // Product Price
                text = "RM ${product.price}",
                fontFamily = FontFamily(Font(R.font.roboto_medium)),
                fontWeight = FontWeight.W400,
                fontSize = 20.sp,
                color = light_primary,
                modifier = Modifier
                    .padding(
                        start = dimensionResource(R.dimen.padding_small),
                        bottom = dimensionResource(R.dimen.padding_small)
                    )
                    .fillMaxWidth(),
            )
        }
    }
}

@Preview
@Composable
private fun ProductCardPreview(){
    ProductCard(product = Product(
        photoUrl = "",
        id = "",
        name = "Test Product",
        description = "Description... ",
        price = "",
        stock = 99,
        isDisabled = false
    ))
}