package com.ganecamp.ui.navigation

sealed class ScreenInternal(val route: String) {
    data object AnimalDetail : ScreenInternal("animalDetail/{animalId}/{lotId}")
    data object LotDetail : ScreenInternal("lotDetail/{lotId}")
    data object AnimalForm : ScreenInternal("formAnimal/{animalId}")
    data object LotForm : ScreenInternal("formLot/{lotId}")
}