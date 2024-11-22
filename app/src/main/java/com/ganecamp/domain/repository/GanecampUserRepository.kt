package com.ganecamp.domain.repository

import com.ganecamp.domain.model.GanecampUser
import com.ganecamp.domain.result.OperationResult

interface GanecampUserRepository {
    suspend fun getUserByEmail(email: String): OperationResult<GanecampUser>
    suspend fun addUser(user: GanecampUser): OperationResult<Unit>
    suspend fun updateUser(user: GanecampUser): OperationResult<Unit>
}