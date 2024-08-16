package com.ganecamp.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.ganecamp.data.database.entities.AnimalEventEntity
import com.ganecamp.data.database.entities.EventEntity
import com.ganecamp.data.database.entities.LotEventEntity
import com.ganecamp.data.model.DescriptionData

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

    @Query(
        """
        SELECT 
            ae.id as id,
            e.title as title,
            ae.event_date as date,
            e.description as description
        FROM animal_event_table ae
        INNER JOIN event_table e ON ae.event_id = e.id
        WHERE ae.animal_id = :animalId
    """
    )
    suspend fun animalEvents(animalId: Int): List<DescriptionData>

    @Query(
        """
        SELECT 
            al.id as id,
            e.title as title,
            al.event_date as date,
            e.description as description
        FROM lot_event_table al
        INNER JOIN event_table e ON al.event_id = e.id
        WHERE al.lot_id = :lotId
    """
    )
    suspend fun lotEvents(lotId: Int): List<DescriptionData>

    @Insert
    suspend fun addEventToAnimal(animalEventEntity: AnimalEventEntity)

    @Insert
    suspend fun addEventToLot(lotEventEntity: LotEventEntity)

}