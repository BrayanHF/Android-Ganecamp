package com.ganecamp.domain.services

import com.ganecamp.data.database.dao.LotDao
import com.ganecamp.data.database.entities.toEntity
import com.ganecamp.domain.model.Lot
import com.ganecamp.domain.model.LotDetail
import com.ganecamp.domain.model.toDomain
import javax.inject.Inject

class LotService @Inject constructor(private val lotDao: LotDao) {

    suspend fun getAllLots(): List<Lot> {
        return lotDao.getAllLots().map { simpleLotData ->
            simpleLotData.toDomain()
        }
    }

    suspend fun getLotById(id: String): LotDetail = lotDao.getLotById(id).toDomain()

    suspend fun insertLot(lot: LotDetail) = lotDao.insertLot(lot.toEntity())

    suspend fun updateLot(lot: LotDetail) = lotDao.updateLot(lot.toEntity())

    suspend fun deleteLot(id: String) = lotDao.deleteLot(id)

    suspend fun deleteAllLots() = lotDao.deleteAllLots()

}