package com.ganecamp.domain.usecase.animal

import com.ganecamp.domain.model.Animal
import com.ganecamp.domain.repository.AnimalRepository
import com.ganecamp.domain.result.OperationResult
import javax.inject.Inject

class UpdateAnimalUseCase @Inject constructor(
    private val animalRepository: AnimalRepository
) {
    suspend operator fun invoke(animal: Animal): OperationResult<Unit> {
        return animalRepository.updateAnimal(animal)
    }
}
