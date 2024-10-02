package com.ganecamp.domain.services

import com.ganecamp.data.firibase.dao.FarmDao
import com.ganecamp.model.objects.Farm
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FarmService @Inject constructor(private val farmDao: FarmDao) {

    suspend fun getFarmByToken(token: String) = farmDao.getFarmByToken(token)

    suspend fun updateFarm(farm: Farm) = farmDao.updateFarm(farm)

}