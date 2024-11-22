package com.ganecamp.domain.repository

import com.ganecamp.domain.model.Animal
import com.ganecamp.domain.model.Lot
import com.ganecamp.domain.result.OperationResult

interface LotRepository {
    suspend fun getAllLots(): OperationResult<List<Lot>>
    suspend fun getLotById(id: String): OperationResult<Lot>
    suspend fun addLot(lot: Lot): OperationResult<String>
    suspend fun updateLot(lot: Lot): OperationResult<Unit>
    suspend fun deleteLotById(id: String): OperationResult<Unit>
    suspend fun getAnimalsByLotId(lotId: String): OperationResult<List<Animal>>
}