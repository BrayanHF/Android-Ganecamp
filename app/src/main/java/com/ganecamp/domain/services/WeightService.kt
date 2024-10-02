package com.ganecamp.domain.services

import com.ganecamp.data.firibase.dao.WeightDao
import com.ganecamp.model.objects.Weight
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeightService @Inject constructor(private val weightDao: WeightDao) {

    suspend fun getAnimalWeights(idAnimal: String) = weightDao.getAnimalWeights(idAnimal)

    suspend fun createWeight(animalId: String, weight: Weight) =
        weightDao.createWeight(animalId, weight)

    suspend fun updateWeight(animalId: String, weight: Weight) =
        weightDao.updateWeight(animalId, weight)

    suspend fun deleteWeightById(id: String) = weightDao.deleteWeightById(id)

    suspend fun loadWeightValue() = weightDao.loadWeightValue()

}