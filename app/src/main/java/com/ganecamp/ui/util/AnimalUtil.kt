package com.ganecamp.ui.util

import androidx.compose.ui.graphics.Color
import com.ganecamp.R
import com.ganecamp.domain.enums.AnimalBreed
import com.ganecamp.domain.enums.AnimalBreed.BON
import com.ganecamp.domain.enums.AnimalBreed.BRAHMAN
import com.ganecamp.domain.enums.AnimalBreed.GYR
import com.ganecamp.domain.enums.AnimalBreed.HOLSTEIN
import com.ganecamp.domain.enums.AnimalBreed.JERSEY
import com.ganecamp.domain.enums.AnimalBreed.ROMO
import com.ganecamp.domain.enums.AnimalBreed.ZEBU
import com.ganecamp.domain.enums.AnimalGender
import com.ganecamp.domain.enums.AnimalState
import com.ganecamp.ui.theme.DarkGreen
import com.ganecamp.ui.theme.LightGray
import com.ganecamp.ui.theme.Orange
import com.ganecamp.ui.theme.Red
import com.ganecamp.ui.theme.Yellow

fun geAnimalGenderRes(animalGender: AnimalGender): Pair<Int, Int> = when (animalGender) {
    AnimalGender.Male -> Pair(R.drawable.ic_bull, R.string.male)
    else -> Pair(R.drawable.ic_cow, R.string.female)
}

fun getAnimalStateRes(animalState: AnimalState): Pair<Int, Color> {
    return when (animalState) {
        AnimalState.Healthy -> Pair(R.string.healthy, DarkGreen)
        AnimalState.Sick -> Pair(R.string.sick, Yellow)
        AnimalState.Injured -> Pair(R.string.injured, Orange)
        AnimalState.Dead -> Pair(R.string.dead, Red)
        AnimalState.Sold -> Pair(R.string.sold, LightGray)
    }
}

fun getAnimalBreedRes(animalBreed: AnimalBreed): Int = when (animalBreed) {
    ZEBU -> R.string.zebu
    BRAHMAN -> R.string.brahman
    ROMO -> R.string.romo
    GYR -> R.string.gyr
    JERSEY -> R.string.jersey
    HOLSTEIN -> R.string.holstein
    BON -> R.string.bon
}