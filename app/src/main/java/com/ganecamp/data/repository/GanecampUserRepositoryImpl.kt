package com.ganecamp.data.repository

import com.ganecamp.data.firestore.GanecampUserDataSource
import com.ganecamp.data.mapper.ExceptionToErrorTypeMapper
import com.ganecamp.data.model.toFirestoreGanecampUser
import com.ganecamp.data.model.toGanecampUser
import com.ganecamp.data.result.DataResult
import com.ganecamp.domain.model.GanecampUser
import com.ganecamp.domain.repository.GanecampUserRepository
import com.ganecamp.domain.result.OperationResult
import javax.inject.Inject

class GanecampUserRepositoryImpl @Inject constructor(
    private val dataSource: GanecampUserDataSource
) : GanecampUserRepository {

    override suspend fun getUserByEmail(email: String): OperationResult<GanecampUser> {
        return when (val result = dataSource.getUserByEmail(email)) {
            is DataResult.Success -> OperationResult.Success(result.data.toGanecampUser())
            is DataResult.Error -> OperationResult.Error(ExceptionToErrorTypeMapper.map(result.exception))
        }
    }

    override suspend fun addUser(user: GanecampUser): OperationResult<Unit> {
        return when (val result = dataSource.addUser(user.toFirestoreGanecampUser())) {
            is DataResult.Success -> OperationResult.Success(Unit)
            is DataResult.Error -> OperationResult.Error(ExceptionToErrorTypeMapper.map(result.exception))
        }
    }

    override suspend fun updateUser(user: GanecampUser): OperationResult<Unit> {
        return when (val result = dataSource.updateUser(user.toFirestoreGanecampUser())) {
            is DataResult.Success -> OperationResult.Success(Unit)
            is DataResult.Error -> OperationResult.Error(ExceptionToErrorTypeMapper.map(result.exception))
        }
    }

}