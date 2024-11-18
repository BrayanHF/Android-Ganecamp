package com.ganecamp.ui.navigation.screens

import kotlinx.serialization.Serializable

@Serializable
data class AnimalDetailNav(val animalId: String)

@Serializable
data class LotDetailNav(val lotId: String)