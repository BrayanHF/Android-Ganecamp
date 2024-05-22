package com.ganecamp.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.ganecamp.data.database.entities.LotEntity
import com.ganecamp.data.model.SimpleLotData

@Dao
interface LotDao {

    // TODO: Queries

    suspend fun getAllLots(): List<SimpleLotData>

    suspend fun getLotById(id: String): LotEntity

    suspend fun insertLot(lot: LotEntity)

    suspend fun updateLot(lot: LotEntity)

    suspend fun deleteLot(id: String)

    suspend fun deleteAllLots()


}