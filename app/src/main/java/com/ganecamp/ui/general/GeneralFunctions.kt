package com.ganecamp.ui.general

import com.ganecamp.R
import com.ganecamp.ui.theme.DarkGreen
import com.ganecamp.ui.theme.LightGray
import com.ganecamp.ui.theme.Orange
import com.ganecamp.ui.theme.Red
import com.ganecamp.ui.theme.Yellow
import com.ganecamp.utilities.enums.Breed
import com.ganecamp.utilities.enums.Breed.BON
import com.ganecamp.utilities.enums.Breed.BRAHMAN
import com.ganecamp.utilities.enums.Breed.GYR
import com.ganecamp.utilities.enums.Breed.HOLSTEIN
import com.ganecamp.utilities.enums.Breed.JERSEY
import com.ganecamp.utilities.enums.Breed.ROMO
import com.ganecamp.utilities.enums.Breed.ZEBU
import com.ganecamp.utilities.enums.Gender
import com.ganecamp.utilities.enums.State
import java.math.BigDecimal
import java.math.RoundingMode

fun validateNumber(input: String, errorMessages: ErrorMessages): String? {
    val moreThanOnePointOrComma = input.count { it == '.' || it == ',' } > 1
    val invalidCharacters = input.any { !it.isDigit() && it != '.' && it != ',' }

    return when {
        moreThanOnePointOrComma -> errorMessages.moreThanOnePointError
        invalidCharacters -> errorMessages.invalidCharactersError
        else -> null
    }
}

fun sanitizeNumberDuringTyping(input: String): String {
    val sanitizedInput = input.filter { it.isDigit() || it == '.' || it == ',' }
    return sanitizedInput.replace(",", ".")
}

fun formatNumber(input: String): String {
    return try {
        val number = BigDecimal(input).setScale(2, RoundingMode.HALF_UP)
        if (number == BigDecimal.ZERO) {
            ""
        } else {
            number.stripTrailingZeros().toPlainString()
        }
    } catch (e: Exception) {
        ""
    }
}

fun geAnimalGenderInfo(gender: Gender) = when (gender) {
    Gender.Male -> AnimalGenderInfo(R.drawable.ic_bull, R.string.male)
    else -> AnimalGenderInfo(R.drawable.ic_cow, R.string.female)
}

fun getAnimalStateInfo(state: State): AnimalStateInfo {
    return when (state) {
        State.Healthy -> AnimalStateInfo(R.string.healthy, DarkGreen)
        State.Sick -> AnimalStateInfo(R.string.sick, Yellow)
        State.Injured -> AnimalStateInfo(R.string.injured, Orange)
        State.Dead -> AnimalStateInfo(R.string.dead, Red)
        State.Sold -> AnimalStateInfo(R.string.sold, LightGray)
    }
}

fun getBreedText(breed: Breed) = when (breed) {
    ZEBU ->R.string.zebu
    BRAHMAN -> R.string.brahman
    ROMO -> R.string.romo
    GYR -> R.string.gyr
    JERSEY -> R.string.jersey
    HOLSTEIN -> R.string.holstein
    BON -> R.string.bon
}