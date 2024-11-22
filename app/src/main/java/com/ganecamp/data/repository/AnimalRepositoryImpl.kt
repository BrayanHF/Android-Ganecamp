package com.ganecamp.data.repository

import com.ganecamp.data.firestore.AnimalDataSource
import com.ganecamp.data.mapper.ExceptionToErrorTypeMapper
import com.ganecamp.data.model.toAnimal
import com.ganecamp.data.model.toFirestoreAnimal
import com.ganecamp.data.result.DataResult
import com.ganecamp.domain.model.Animal
import com.ganecamp.domain.repository.AnimalRepository
import com.ganecamp.domain.result.OperationResult
import javax.inject.Inject

class AnimalRepositoryImpl @Inject constructor(
    private val dataSource: AnimalDataSource
) : AnimalRepository {

    override suspend fun getAllAnimals(): OperationResult<List<Animal>> {
        return when (val result = dataSource.getAllAnimals()) {
            is DataResult.Success -> OperationResult.Success(result.data.map { it.toAnimal() })
            is DataResult.Error -> OperationResult.Error(ExceptionToErrorTypeMapper.map(result.exception))
        }
    }

    override suspend fun getAnimalById(id: String): OperationResult<Animal> {
        return when (val result = dataSource.getAnimalById(id)) {
            is DataResult.Success -> OperationResult.Success(result.data.toAnimal())
            is DataResult.Error -> OperationResult.Error(ExceptionToErrorTypeMapper.map(result.exception))
        }
    }

    override suspend fun getAnimalByTag(tag: String): OperationResult<Animal> {
        return when (val result = dataSource.getAnimalByTag(tag)) {
            is DataResult.Success -> OperationResult.Success(result.data.toAnimal())
            is DataResult.Error -> OperationResult.Error(ExceptionToErrorTypeMapper.map(result.exception))
        }
    }

    override suspend fun addAnimal(animal: Animal): OperationResult<String> {
        return when (val result = dataSource.addAnimal(animal.toFirestoreAnimal())) {
            is DataResult.Success -> OperationResult.Success(result.data)
            is DataResult.Error -> OperationResult.Error(ExceptionToErrorTypeMapper.map(result.exception))
        }
    }

    override suspend fun updateAnimal(animal: Animal): OperationResult<Unit> {
        return when (val result = dataSource.updateAnimal(animal.toFirestoreAnimal())) {
            is DataResult.Success -> OperationResult.Success(Unit)
            is DataResult.Error -> OperationResult.Error(ExceptionToErrorTypeMapper.map(result.exception))
        }
    }

    override suspend fun deleteAnimalById(id: String): OperationResult<Unit> {
        return when (val result = dataSource.deleteAnimalById(id)) {
            is DataResult.Success -> OperationResult.Success(Unit)
            is DataResult.Error -> OperationResult.Error(ExceptionToErrorTypeMapper.map(result.exception))
        }
    }

}