package com.ganecamp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ganecamp.data.database.entities.LotEntity
import com.ganecamp.data.model.SimpleLotData

@Dao
interface LotDao {

    @Query(
        """
        SELECT 
            alt.lot_id AS id, 
            COUNT(alt.animal_id) AS animalCount
        FROM animal_lot_table AS alt
        LEFT JOIN lot_table AS l ON alt.lot_id = l.id
        GROUP BY lot_id
        """
    )
    suspend fun getAllLots(): List<SimpleLotData>

    @Query("SELECT * FROM lot_table WHERE id = :id")
    suspend fun getLotById(id: String): LotEntity

    @Insert
    suspend fun insertLot(lot: LotEntity)

    @Update
    suspend fun updateLot(lot: LotEntity)

    @Query("DELETE FROM lot_table WHERE id = :id")
    suspend fun deleteLot(id: String)

    @Query("DELETE FROM lot_table")
    suspend fun deleteAllLots()

}