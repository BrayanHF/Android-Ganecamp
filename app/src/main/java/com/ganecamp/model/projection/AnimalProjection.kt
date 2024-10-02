package com.ganecamp.model.projection

import com.ganecamp.utilities.enums.Gender
import com.ganecamp.utilities.enums.State

data class AnimalProjection(
    val tag: String,
    val gender: Gender,
    val lotId: String?,
    val state: State,
    val breed: String
)