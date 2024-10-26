package com.example.bangkitapi.data.database.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.bangkitapi.data.database.entity.EventEntity

@Dao
interface EventDao {
    @Insert
    suspend fun insertFavorite(event: EventEntity)

    @Query("SELECT * FROM favorite_events WHERE eventName = :eventName LIMIT 1")
    suspend fun getFavoriteByName(eventName: String): EventEntity?

    @Query("DELETE FROM favorite_events WHERE eventName = :eventName")
    suspend fun deleteFavorite(eventName: String)

    @Query("SELECT * FROM favorite_events")  // Adjust table name if different
    fun getAllFavorites(): List<EventEntity>
}