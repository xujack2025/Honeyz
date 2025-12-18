package com.example.honeyz.ui.User.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.honeyz.R
import com.example.honeyz.ui.component.PromotionCard
import com.example.honeyz.model.Promotion
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

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

//@Composable
//fun SearchBar() {
//    var searchQuery = remember { mutableStateOf(TextFieldValue()) }
//
//    OutlinedTextField(
//        value = searchQuery.value,
//        onValueChange = { searchQuery.value = it },
//        placeholder = { Text("Search listings...") },
//        modifier = Modifier.fillMaxWidth()
//    )
//}

@Preview(showBackground = true)
@Composable
fun PreviewHomePage() {
    HomeScreen(
        onPromotionClicked = {}
    )
}