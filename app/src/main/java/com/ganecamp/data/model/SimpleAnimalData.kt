package com.ganecamp.data.model

import com.ganecamp.utilities.enums.Gender
import com.ganecamp.utilities.enums.State

data class SimpleAnimalData(
    val tag: String,
    val id: Int,
    val gender: Gender,
    val state: State,
    val lotId: Int
)

