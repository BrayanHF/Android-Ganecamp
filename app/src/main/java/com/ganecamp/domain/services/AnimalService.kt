package com.ganecamp.domain.services

import com.ganecamp.data.database.dao.AnimalDao
import com.ganecamp.data.database.entities.toEntity
import com.ganecamp.domain.model.Animal
import com.ganecamp.domain.model.AnimalDetail
import com.ganecamp.domain.model.toDomain
import javax.inject.Inject

class AnimalService @Inject constructor(private val animalDao: AnimalDao) {

    suspend fun getAllAnimals(): List<Animal> {
        return animalDao.getAllAnimals().map { simpleAnimalData ->
            simpleAnimalData.toDomain()
        }
    }

    suspend fun getAnimalByTag(tag: String): AnimalDetail = animalDao.getAnimalByTag(tag).toDomain()

    suspend fun insertAnimal(animal: AnimalDetail) = animalDao.insertAnimal(animal.toEntity())

    suspend fun updateAnimal(animal: AnimalDetail) = animalDao.updateAnimal(animal.toEntity())

    suspend fun deleteAnimal(tag: String) = animalDao.deleteAnimalByTag(tag)

    suspend fun deleteAllAnimals() = animalDao.deleteAllAnimals()

}