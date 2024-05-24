package com.ganecamp.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.ganecamp.data.database.entities.EventEntity

@Dao
interface EventDao {

    @Query("SELECT * FROM event_table")
    suspend fun getAllEvents(): List<EventEntity>

    @Query("SELECT * FROM event_table WHERE id = :id")
    suspend fun getEventById(id: Int): EventEntity

    @Insert
    suspend fun insertEvent(event: EventEntity)

    @Delete
    suspend fun updateEvent(event: EventEntity)

    @Query("DELETE FROM event_table WHERE id = :id")
    suspend fun deleteEventById(id: Int)

    @Query("DELETE FROM event_table")
    suspend fun deleteAllEvents()

}