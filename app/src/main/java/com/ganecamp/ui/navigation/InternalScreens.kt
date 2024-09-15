package com.ganecamp.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
data class AnimalDetailNav(val animalId: Int)

@Serializable
data class LotDetailNav(val lotId: Int)

@Serializable
data class AnimalFormNav(val animalId: Int, val tag: String)

@Serializable
data class LotFormNav(val lotId: Int)