package com.ganecamp.domain.services

import com.ganecamp.data.firibase.dao.VaccineDao
import com.ganecamp.data.firibase.model.Vaccine
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VaccineService @Inject constructor(private val vaccineDao: VaccineDao) {

    suspend fun getAllVaccines() = vaccineDao.getAllVaccines()

    suspend fun getVaccineById(id: String) = vaccineDao.getVaccineById(id)

    suspend fun createVaccine(vaccine: Vaccine) = vaccineDao.createVaccine(vaccine)

    suspend fun updateVaccine(vaccine: Vaccine) = vaccineDao.updateVaccine(vaccine)

    suspend fun deleteVaccineById(id: String) = vaccineDao.deleteVaccineById(id)

}