package com.ganecamp.domain.repository

import com.ganecamp.domain.model.Vaccine
import com.ganecamp.domain.result.OperationResult

interface VaccineRepository {
    suspend fun getAllVaccines(): OperationResult<List<Vaccine>>
    suspend fun getVaccineById(id: String): OperationResult<Vaccine>
    suspend fun addVaccine(vaccine: Vaccine): OperationResult<String>
    suspend fun updateVaccine(vaccine: Vaccine): OperationResult<Unit>
    suspend fun deleteVaccineById(id: String): OperationResult<Unit>
}