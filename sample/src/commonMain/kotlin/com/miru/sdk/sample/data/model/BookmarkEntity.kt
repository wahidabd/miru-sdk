package com.miru.sdk.sample.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity representing a bookmarked article.
 * Stored in the local SQLite database.
 */
@Entity(tableName = "bookmarks")
data class BookmarkEntity(
    @PrimaryKey
    val id: Long,
    val title: String,
    val description: String,
    val content: String,
    val author: String,
    val source: String,
    val imageUrl: String?,
    val articleUrl: String,
    val publishedAt: String,
    val bookmarkedAt: Long = 0L
)
