package com.ganecamp.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
data class AnimalDetailNav(val animalId: String)

@Serializable
data class LotDetailNav(val lotId: String)

@Serializable
data class AnimalFormNav(val animalId: String?, val tag: String)

@Serializable
data class LotFormNav(val lotId: String?)

@Serializable
data class VaccineAddFormNav(val animalId: String)