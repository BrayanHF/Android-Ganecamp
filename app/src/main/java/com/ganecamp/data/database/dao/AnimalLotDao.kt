package com.ganecamp.data.database.dao

import androidx.room.Dao
import androidx.room.Query

@Dao
interface AnimalLotDao {

    @Query("INSERT INTO animal_lot_table (animal_id, lot_id) VALUES (:animalId, :lotId)")
    suspend fun addLotToAnimal(animalId: Int, lotId: Int)

    @Query("UPDATE animal_lot_table SET lot_id = :lotId WHERE animal_id = :animalId")
    suspend fun changeLotToAnimal(animalId: Int, lotId: Int)

    @Query("DELETE FROM animal_lot_table WHERE animal_id = :animalId")
    suspend fun removeFromLot(animalId: Int)

}