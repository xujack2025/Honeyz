package com.example.honeyz.ui.admin.manageNews

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.honeyz.model.News
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class NewsViewModel : ViewModel() {
    // A list that holds all the news items
    var newsList = mutableStateListOf<News>()
        private set

    private val db = FirebaseFirestore.getInstance()

    init {
        // Automatically load news articles when ViewModel is created
        loadNewsFromFirestore()
    }

    // Function to fetch news articles from Firestore
    private fun loadNewsFromFirestore() {
        viewModelScope.launch {
            try {
                val result = db.collection("News").get().await()
                val fetchedNews = mutableListOf<News>()

                for (document in result) {
                    val news = document.toObject(News::class.java)
                    news.id = document.id // Assign Firestore document ID
                    fetchedNews.add(news)
                }

                // Clear the existing list and add all the fetched news
                newsList.clear()
                newsList.addAll(fetchedNews)

            } catch (e: FirebaseFirestoreException) {
                Log.e("Firestore", "Error loading news: ${e.message}")
            }
        }
    }

    // Function to add a new news article to Firestore
    fun addNews(news: News) {
        viewModelScope.launch {
            try {
                val newDocRef = db.collection("News").add(news).await()
                news.id = newDocRef.id // Set the Firestore document ID to the news object
                newsList.add(news) // Add to local list
            } catch (e: FirebaseFirestoreException) {
                Log.e("Firestore", "Error adding news: ${e.message}")
            }
        }
    }

    // Function to update an existing news article in Firestore
    fun updateNews(news: News) {
        viewModelScope.launch {
            try {
                db.collection("News").document(news.id).set(news).await()
                val index = newsList.indexOfFirst { it.id == news.id }
                if (index >= 0) {
                    newsList[index] = news // Update the local list with the new data
                }
            } catch (e: FirebaseFirestoreException) {
                Log.e("Firestore", "Error updating news: ${e.message}")
            }
        }
    }

    // Function to delete a news article from Firestore
    fun deleteNews(news: News) {
        viewModelScope.launch {
            try {
                db.collection("News").document(news.id).delete().await()
                newsList.remove(news) // Remove from local list
            } catch (e: FirebaseFirestoreException) {
                Log.e("Firestore", "Error deleting news: ${e.message}")
            }
        }
    }
}
