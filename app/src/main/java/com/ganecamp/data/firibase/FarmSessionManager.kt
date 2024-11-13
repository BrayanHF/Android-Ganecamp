package com.ganecamp.data.firibase

import com.ganecamp.data.firibase.model.Farm
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