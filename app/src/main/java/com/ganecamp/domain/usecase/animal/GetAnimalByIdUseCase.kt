package com.ganecamp.domain.usecase.animal

import com.ganecamp.domain.model.Animal
import com.ganecamp.domain.repository.AnimalRepository
import com.ganecamp.domain.result.OperationResult
import javax.inject.Inject

class GetAnimalByIdUseCase @Inject constructor(
    private val animalRepository: AnimalRepository
) {
    suspend operator fun invoke(id: String): OperationResult<Animal> {
        return animalRepository.getAnimalById(id)
    }
}