package com.ganecamp.domain.usecase.animal

import com.ganecamp.domain.repository.AnimalRepository
import com.ganecamp.domain.result.OperationResult
import javax.inject.Inject

class DeleteAnimalByIdUseCase @Inject constructor(
    private val animalRepository: AnimalRepository
) {
    suspend operator fun invoke(id: String): OperationResult<Unit> {
        return animalRepository.deleteAnimalById(id)
    }
}