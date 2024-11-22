package com.ganecamp.domain.repository

import com.ganecamp.domain.model.Animal
import com.ganecamp.domain.result.OperationResult

interface AnimalRepository {
    suspend fun getAllAnimals(): OperationResult<List<Animal>>
    suspend fun getAnimalById(id: String): OperationResult<Animal>
    suspend fun getAnimalByTag(tag: String): OperationResult<Animal>
    suspend fun addAnimal(animal: Animal): OperationResult<String>
    suspend fun updateAnimal(animal: Animal): OperationResult<Unit>
    suspend fun deleteAnimalById(id: String): OperationResult<Unit>
}