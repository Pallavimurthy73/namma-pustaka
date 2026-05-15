package com.nammapustaka.app.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ReviewDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(review: ReviewEntity)

    @Insert
    suspend fun insertAll(reviews: List<ReviewEntity>)

    @Query("SELECT * FROM reviews ORDER BY id DESC")
    fun observeAllReviews(): Flow<List<ReviewEntity>>
}
