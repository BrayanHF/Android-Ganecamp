package com.ganecamp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ganecamp.data.database.entities.LotEntity
import com.ganecamp.data.model.SimpleAnimalData
import com.ganecamp.data.model.SimpleLotData

@Dao
interface LotDao {

    @Query(
        """
        SELECT 
            l.id AS id, 
            COUNT(alt.animal_id) AS animalCount
        FROM lot_table AS l
        LEFT JOIN animal_lot_table AS alt ON alt.lot_id = l.id
        GROUP BY l.id
        """
    )
    suspend fun getAllLots(): List<SimpleLotData>

    @Query("SELECT id FROM lot_table ORDER BY id ASC")
    suspend fun getAllLotsIDs(): List<Int>

    @Query("SELECT * FROM lot_table WHERE id = :id")
    suspend fun getLotById(id: Int): LotEntity

    @Insert
    suspend fun insertLot(lot: LotEntity)

    @Update
    suspend fun updateLot(lot: LotEntity)

    @Query("DELETE FROM lot_table WHERE id = :id")
    suspend fun deleteLot(id: Int)

    @Query("DELETE FROM lot_table")
    suspend fun deleteAllLots()

    @Query(
        """
        SELECT a.id, a.tag, a.gender, a.state, al.lot_id as lotId 
        FROM animal_table AS a
        INNER JOIN animal_lot_table AS al ON a.id = al.animal_id
        WHERE al.lot_id = :lotId
    """
    )
    suspend fun getAnimalsByLotId(lotId: Int): List<SimpleAnimalData>

}