package com.ganecamp.domain.services

import com.ganecamp.data.firibase.dao.GanecampUserDao
import com.ganecamp.model.objects.GanecampUser
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GanecampUserService @Inject constructor(private val userDao: GanecampUserDao) {

    suspend fun getUserByEmail(email: String) = userDao.getUserByEmail(email)

    suspend fun createUser(user: GanecampUser) = userDao.createUser(user)

    suspend fun updateUser(user: GanecampUser) = userDao.updateUser(user)

}