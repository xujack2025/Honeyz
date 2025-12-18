package com.example.honeyz.ui.admin.manageNews

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.honeyz.ui.customer.NewsItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageNewsScreen(navController: NavHostController, newsViewModel: NewsViewModel) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Manage News") },
                actions = {
                    IconButton(onClick = { navController.navigate("addNews") }) {
                        Icon(Icons.Default.Add, contentDescription = "Add News")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Display the list of news articles
            for (news in newsViewModel.newsList) {
                NewsItem(news = news)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}