package com.ganecamp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ganecamp.data.database.entities.AnimalEntity

@Dao
interface AnimalDao {

    @Query("SELECT * FROM animal_table ORDER BY id")
    suspend fun getAllAnimals(): List<AnimalEntity>

}