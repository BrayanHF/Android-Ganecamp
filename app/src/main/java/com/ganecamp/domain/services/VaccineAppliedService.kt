package com.ganecamp.domain.services

import com.ganecamp.data.firibase.dao.VaccineAppliedDao
import com.ganecamp.data.firibase.model.AnimalVaccine
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VaccineAppliedService @Inject constructor(private val animalVaccineDao: VaccineAppliedDao) {

    suspend fun getVaccinesApplied(animalId: String) = animalVaccineDao.getVaccinesApplied(animalId)

    suspend fun applyVaccineAnimal(animalId: String, animalVaccine: AnimalVaccine) =
        animalVaccineDao.applyVaccineAnimal(animalId, animalVaccine)

    suspend fun updateVaccineApplied(animalId: String, animalVaccine: AnimalVaccine) =
        animalVaccineDao.updateVaccineApplied(animalId, animalVaccine)

    suspend fun deleteVaccineApplied(id: String) = animalVaccineDao.deleteVaccineApplied(id)

}