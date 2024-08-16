package com.ganecamp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ganecamp.data.database.entities.WeightEntity

@Dao
interface WeightDao {

    @Query("SELECT * FROM weight_table WHERE animal_id = :animalId")
    suspend fun animalWeights(animalId: Int): List<WeightEntity>

    @Insert
    suspend fun insertWeight(weight: WeightEntity)

}