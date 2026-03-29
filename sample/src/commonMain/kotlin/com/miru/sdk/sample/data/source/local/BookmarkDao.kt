package com.miru.sdk.sample.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.miru.sdk.sample.data.model.BookmarkEntity
import kotlinx.coroutines.flow.Flow

/**
 * Room DAO for bookmark CRUD operations.
 */
@Dao
interface BookmarkDao {

    @Query("SELECT * FROM bookmarks ORDER BY bookmarkedAt DESC")
    fun observeAll(): Flow<List<BookmarkEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM bookmarks WHERE id = :articleId)")
    suspend fun isBookmarked(articleId: Long): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: BookmarkEntity)

    @Query("DELETE FROM bookmarks WHERE id = :articleId")
    suspend fun deleteById(articleId: Long)

    @Query("SELECT * FROM bookmarks WHERE id = :articleId LIMIT 1")
    suspend fun getById(articleId: Long): BookmarkEntity?
}
