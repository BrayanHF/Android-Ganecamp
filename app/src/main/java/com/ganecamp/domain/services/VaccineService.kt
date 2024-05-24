package com.ganecamp.domain.services

import com.ganecamp.data.database.dao.VaccineDao
import com.ganecamp.data.database.entities.toEntity
import com.ganecamp.domain.model.Vaccine
import com.ganecamp.domain.model.toDomain
import javax.inject.Inject

class VaccineService @Inject constructor(private val vaccineDao: VaccineDao) {

    suspend fun getAllVaccines(): List<Vaccine> {
        return vaccineDao.getAllVaccines().map { vaccineEntity ->
            vaccineEntity.toDomain()
        }
    }

    suspend fun getVaccineById(id: Int): Vaccine = vaccineDao.getVaccineById(id).toDomain()

    suspend fun insertVaccine(vaccine: Vaccine) = vaccineDao.insertVaccine(vaccine.toEntity())

    suspend fun updateVaccine(vaccine: Vaccine) = vaccineDao.updateVaccine(vaccine.toEntity())

    suspend fun deleteVaccineById(id: Int) = vaccineDao.deleteVaccineById(id)

    suspend fun deleteAllVaccines() = vaccineDao.deleteAllVaccines()

}