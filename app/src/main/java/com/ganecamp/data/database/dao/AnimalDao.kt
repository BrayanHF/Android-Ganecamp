package com.ganecamp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ganecamp.data.database.entities.AnimalEntity
import com.ganecamp.data.model.SimpleAnimalData

@Dao
interface AnimalDao {

    @Query(
        """
        SELECT 
            a.tag AS tag,
            a.id AS id,
            a.gender AS gender,
            a.state AS state,
            IFNULL(al.lot_id, 0) AS lotId
        FROM animal_table AS a
        LEFT JOIN animal_lot_table AS al ON a.id = al.animal_id
        """
    )
    suspend fun getAllAnimals(): List<SimpleAnimalData>

    @Query("SELECT * FROM animal_table WHERE tag = :tag")
    suspend fun getAnimalByTag(tag: String): AnimalEntity

    @Insert
    suspend fun insertAnimal(animal: AnimalEntity)

    @Update
    suspend fun updateAnimal(animal: AnimalEntity)

    @Query("DELETE FROM animal_table WHERE tag = :tag")
    suspend fun deleteAnimalByTag(tag: String)

    @Query("DELETE FROM animal_table")
    suspend fun deleteAllAnimals()

}