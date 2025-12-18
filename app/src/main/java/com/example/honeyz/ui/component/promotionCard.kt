package com.example.honeyz.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.honeyz.model.Promotion
import com.example.honeyz.ui.theme.light_primary_background

@Composable
fun PromotionCard(promotion: Promotion, modifier: Modifier = Modifier) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = promotion.imageUrl),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = light_primary_background,
                        shape = RoundedCornerShape(16.dp)

                    )
                    .height(150.dp),
                contentScale = ContentScale.Crop
            )
            Text(
                text = promotion.description,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }
}

@Preview
@Composable
private fun PreviewPromotionCard() {
    PromotionCard(
        promotion = Promotion(
            id = "1",
            description = "Description...",
            imageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQlpsLnisnMXCKDIcPz7yq3t7_E5pcShTshsA&s"
        )
    )
}
