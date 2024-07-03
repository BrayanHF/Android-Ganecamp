package com.ganecamp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ganecamp.data.database.entities.VaccineEntity
import com.ganecamp.data.model.DescriptionData

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

    @Query(
        """
        SELECT 
            av.id as id,
            v.name as title,
            av.application_date as date,
            v.description as description
        FROM animal_vaccine_table av
        INNER JOIN vaccine_table v ON av.vaccine_id = v.id
        WHERE av.animal_id = :animalId
    """
    )
    suspend fun animalVaccines(animalId: Int): List<DescriptionData>

}