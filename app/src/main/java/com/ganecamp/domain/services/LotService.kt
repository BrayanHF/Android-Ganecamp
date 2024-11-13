package com.ganecamp.domain.services

import com.ganecamp.data.firibase.dao.LotDao
import com.ganecamp.data.firibase.model.Lot
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LotService @Inject constructor(private val lotDao: LotDao) {

    suspend fun getAllLots() = lotDao.getAllLots()

    suspend fun getLotById(id: String) = lotDao.getLotById(id)

    suspend fun createLot(lot: Lot) = lotDao.createLot(lot)

    suspend fun updateLot(lot: Lot) = lotDao.updateLot(lot)

    suspend fun deleteLotById(id: String) = lotDao.deleteLotById(id)

    suspend fun getAnimalsByLotId(id: String) = lotDao.getAnimalsByLotId(id)

}