package com.ganecamp.ui.navigation.screens

import com.ganecamp.domain.enums.EntityType
import kotlinx.serialization.Serializable

@Serializable
data class AnimalFormNav(val animalId: String?, val tag: String)

@Serializable
data class LotFormNav(val lotId: String?)

@Serializable
data class VaccineAddFormNav(val animalId: String)

@Serializable
data class EventAddFormNav(val entityId: String, val entityType: EntityType)

@Serializable
data class WeightFormNav(val animalId: String)