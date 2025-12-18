package com.example.honeyz.model

data class News(
    var id: String = "",               // Firestore ID for the news article
    val title: String = "",             // Title of the news article
    val description: String = "",       // Brief description or summary
    val imageUrl: String? = null,       // Image URL for the news article
    val link: String = "",              // Link to full article or external website
    val datePosted: Long = System.currentTimeMillis(), // Timestamp for when the article was posted
    var isPublished: Boolean = true     // Whether the article is published or not
)
