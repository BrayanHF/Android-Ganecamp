package com.ganecamp.ui.general

import androidx.compose.ui.graphics.Color

data class AnimalStateInfo(val textRes: Int, val color: Color)

data class AnimalGenderInfo(val iconRes: Int, val textRes: Int)

data class ErrorMessages(val moreThanOnePointError: String, val invalidCharactersError: String)