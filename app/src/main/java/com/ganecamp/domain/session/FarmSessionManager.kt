package com.ganecamp.domain.session

import com.ganecamp.domain.model.Farm
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FarmSessionManager @Inject constructor() {

    private var farm: Farm? = null

    fun getFarm(): Farm? = farm

    fun setFarm(farm: Farm) {
        this.farm = farm
    }

    fun clearFarm() {
        farm = null
    }

}