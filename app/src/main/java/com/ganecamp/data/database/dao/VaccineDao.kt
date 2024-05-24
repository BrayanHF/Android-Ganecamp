package com.ganecamp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ganecamp.data.database.entities.VaccineEntity

@Dao
interface VaccineDao {

    @Query("SELECT * FROM vaccine_table")
    suspend fun getAllVaccines(): List<VaccineEntity>

    @Query("SELECT * FROM vaccine_table WHERE id = :id")
    suspend fun getVaccineById(id: Int): VaccineEntity

    @Insert
    suspend fun insertVaccine(vaccine: VaccineEntity)

    @Update
    suspend fun updateVaccine(vaccine: VaccineEntity)

    @Query("DELETE FROM vaccine_table WHERE id = :id")
    suspend fun deleteVaccineById(id: Int)

    @Query("DELETE FROM vaccine_table")
    suspend fun deleteAllVaccines()

}