package com.example.honeyz.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.honeyz.R
import com.example.honeyz.model.CartItem
import com.example.honeyz.ui.User.cart.CartViewModel

@Composable
fun CartItemRow(viewModel: CartViewModel, item: CartItem, onRemove: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicText(text = item.productName, modifier = Modifier.weight(1f))
        BasicText(text = "RM ${"%.2f".format(item.price)}")
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 16.dp)
        ) {
            IconButton(onClick = { viewModel.decreaseQuantity(item)}) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_remove),
                    contentDescription = "Icon Remove"
                )
            }
            Text(
                text = "${item.quantity}",
                fontSize = 24.sp
            )
            IconButton(onClick = { viewModel.increaseQuantity(item) }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add),
                    contentDescription = "Icon Add"
                )
            }
        }

    }
    Spacer(modifier = Modifier.width(16.dp))
    Button(onClick = onRemove) {
        Text("Remove")
    }
}