package com.ganecamp.domain.usecase.ganecampuser

import com.ganecamp.domain.model.GanecampUser
import com.ganecamp.domain.repository.GanecampUserRepository
import com.ganecamp.domain.result.OperationResult
import javax.inject.Inject

class UpdateUserUseCase @Inject constructor(
    private val repository: GanecampUserRepository
) {
    suspend operator fun invoke(user: GanecampUser): OperationResult<Unit> {
        return repository.updateUser(user)
    }
}