package com.ganecamp.domain.usecase.animal

import com.ganecamp.domain.model.Animal
import com.ganecamp.domain.repository.AnimalRepository
import com.ganecamp.domain.result.OperationResult
import javax.inject.Inject

class GetAnimalByTagUseCase @Inject constructor(
    private val animalRepository: AnimalRepository
) {
    suspend operator fun invoke(tag: String): OperationResult<Animal> {
        return animalRepository.getAnimalByTag(tag)
    }
}