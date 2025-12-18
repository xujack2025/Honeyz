package com.example.honeyz.ui.User.payment


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.honeyz.R
import com.example.honeyz.ui.theme.light_primary
import com.example.honeyz.ui.theme.light_secondary
import com.example.honeyz.ui.theme.light_secondary_background

@Composable
fun PaymentConfirmationScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(24.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
//            Button(
//                onClick = { TODO() },
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = light_primary_background,
//                    contentColor = light_primary),
//                border = BorderStroke(2.dp, light_primary),
//                modifier = Modifier
//                    .size(60.dp)
//                    .wrapContentSize()
//                    .align(AbsoluteAlignment.Right)
//
//            ) {
//                Icon(
//                    painter = painterResource(R.drawable.ic_close),
//                    contentDescription = "Close",
//                    tint = light_primary,
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .clickable { TODO() }
//                )
//            }

            // Confirmation Circle
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color(0xFFFFA500), CircleShape)
                    .border(
                        width = 3.dp,
                        color = light_secondary,
                        shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_check),
                    contentDescription = "Payment Confirmed",
                    tint = Color.White,
                    modifier = Modifier.size(50.dp)
                )
            }

            Text(
                text = "Payment Confirmed!",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(top = 16.dp)
            )

            Text(
                text = "RM 211.97", // TODO: Change the Price Accordingly
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Text(
                text = "Your payment has been confirmed, it may take 1–2 hours for your payment to go through and show up in your transaction list.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Card displaying selected payment details
            Card(
                colors = CardDefaults.cardColors(light_secondary_background),
                modifier = Modifier
                    .fillMaxWidth(),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(16.dp)
                ) {
//                   Image(
//                        painter = painterResource(id = R.drawable.ic_mastercard), // Replace with your card icon
//                        contentDescription = "Mastercard",
//                        modifier = Modifier.size(24.dp)
//                   )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(text = "Mastercard Ending in 1234")
                        Text(text = "RM 211.97")
                    }
                }
            }

            Spacer(modifier = Modifier.height(250.dp))

            Button(
                colors = ButtonDefaults.buttonColors(light_primary),
                onClick = { TODO() },
                modifier = Modifier
                    .width(225.dp)
            ) {
                Text(text = "Go Home")
            }
        }
    }
}

@Preview
@Composable
private fun PreviewPaymentConfirmationScreen(){
    PaymentConfirmationScreen()
}