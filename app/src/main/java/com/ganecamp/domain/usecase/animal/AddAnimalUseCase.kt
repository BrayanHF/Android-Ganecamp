package com.ganecamp.domain.usecase.animal

import com.ganecamp.domain.model.Animal
import com.ganecamp.domain.model.Weight
import com.ganecamp.domain.repository.AnimalRepository
import com.ganecamp.domain.result.OperationResult
import com.ganecamp.domain.usecase.weight.AddWeightUseCase
import javax.inject.Inject

class AddAnimalUseCase @Inject constructor(
    private val animalRepository: AnimalRepository, private val addWeightUseCase: AddWeightUseCase
) {
    suspend operator fun invoke(animal: Animal, registerWeight: Weight): OperationResult<String> {
        return when (val result = animalRepository.addAnimal(animal)) {
            is OperationResult.Success -> {
                when (val weightResult = registerWeight(result.data, registerWeight)) {
                    is OperationResult.Success -> OperationResult.Success(result.data)
                    is OperationResult.Error -> weightResult
                }
            }

            is OperationResult.Error -> result
        }
    }

    private suspend fun registerWeight(animalId: String, weight: Weight): OperationResult<Unit> {
        return addWeightUseCase(animalId, weight)
    }
}