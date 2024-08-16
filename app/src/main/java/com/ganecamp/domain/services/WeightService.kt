package com.ganecamp.domain.services

import com.ganecamp.data.database.dao.WeightDao
import com.ganecamp.data.database.entities.toEntity
import com.ganecamp.domain.model.Weight
import com.ganecamp.domain.model.toDomain
import javax.inject.Inject

class WeightService @Inject constructor(private val weightDao: WeightDao) {

    suspend fun animalWeights(animalId: Int): List<Weight> {
        return weightDao.animalWeights(animalId).map { weightEntity ->
            weightEntity.toDomain()
        }
    }

    suspend fun insertWeight(weight: Weight) = weightDao.insertWeight(weight.toEntity())

}