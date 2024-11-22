package com.ganecamp.domain.usecase.animal

import com.ganecamp.domain.model.Animal
import com.ganecamp.domain.repository.AnimalRepository
import com.ganecamp.domain.result.OperationResult
import javax.inject.Inject

class GetAllAnimalsUseCase @Inject constructor(
    private val animalRepository: AnimalRepository
) {
    suspend operator fun invoke(): OperationResult<List<Animal>> {
        return animalRepository.getAllAnimals()
    }
}