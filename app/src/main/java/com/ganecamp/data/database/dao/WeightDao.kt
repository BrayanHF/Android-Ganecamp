package com.ganecamp.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.ganecamp.data.database.entities.WeightEntity
import com.ganecamp.data.model.DescriptionData

@Dao
interface WeightDao {

    @Query("""
        SELECT *
        FROM weight_table w 
        WHERE w.animal_id = :animalId
    """)
    suspend fun animalWeights(animalId: Int): List<WeightEntity>

}