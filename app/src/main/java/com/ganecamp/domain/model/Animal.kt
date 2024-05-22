package com.ganecamp.domain.model

import com.ganecamp.data.database.entities.AnimalEntity
import com.ganecamp.data.model.SimpleAnimalData
import com.ganecamp.utilities.enums.Gender
import com.ganecamp.utilities.enums.State

data class Animal(
    val tag: String,
    val id: String,
    val gender: Gender,
    val state: State,
    val lodId: String
)

fun SimpleAnimalData.toDomain() = Animal(tag, id, gender, state, lotId)


