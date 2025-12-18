package com.example.honeyz.ui.User.checkout

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import com.example.honeyz.R
import com.example.honeyz.ui.theme.light_primary

@Composable
fun CheckoutScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        // Checkout Title
        Text(
            text = "Checkout",
            style = MaterialTheme.typography.titleLarge,
            fontFamily = FontFamily(Font(R.font.roboto_medium)),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Product Item
        ProductItem()

        Spacer(modifier = Modifier.height(16.dp))

        // Order Summary
        OrderSummary()

        Spacer(modifier = Modifier.height(16.dp))

        // Checkout Button
        Button(
            colors = ButtonDefaults.buttonColors(light_primary),
            onClick = { /* TODO */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Proceed to Payment")
        }
    }
}

@Composable
fun ProductItem() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Kinohimitsu Royal Honey 500g",
                style = MaterialTheme.typography.bodyLarge,
                fontFamily = FontFamily( Font(R.font.roboto)),
            )
            Text(
                text = "RM 99.99",
                style = MaterialTheme.typography.bodyMedium,
                fontFamily = FontFamily( Font(R.font.roboto)),
            )
            Text(text = "Honey Type: Wild Royal Honey", style = MaterialTheme.typography.bodySmall)
            Text(text = "Bee Type: Apis dorsata", style = MaterialTheme.typography.bodySmall)
            Text(text = "Colour: Milky Yellowish", style = MaterialTheme.typography.bodySmall)

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text(text = "QTY: 1", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.weight(1f))
                Text(text = "Remove", color = Color.Red)
            }
        }
    }
}

@Composable
fun OrderSummary() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Order Summary",
                style = MaterialTheme.typography.titleMedium,
                fontFamily = FontFamily( Font(R.font.roboto)),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Below is a list of your items.",
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Price Breakdown", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Base Price: RM 199.98", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Taxes: RM 11.99", style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Total: RM 211.97",
                style = MaterialTheme.typography.titleLarge,
                fontFamily = FontFamily( Font(R.font.roboto)),
                color = Color(0xFFFFA500) // Use an appropriate color for total
            )
        }
    }
}

@Preview
@Composable
private fun PreviewCheckoutScreen(){
    CheckoutScreen()
}
